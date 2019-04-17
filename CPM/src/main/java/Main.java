import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxConstants;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.*;
import org.json.*;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Main {
    private static HashMap<String, Integer> earliestStarts = new HashMap<String, Integer>();
    private static HashMap<String, Integer> earliestFinishes = new HashMap<String, Integer>();
    private static HashMap<String, Integer> latestStarts = new HashMap<String, Integer>();
    private static HashMap<String, Integer> latestFinishes = new HashMap<String, Integer>();
    private static HashMap<String, List<String>> taskParents = new HashMap<String, List<String>>();
    private static HashMap<String, List<String>> taskChildren = new HashMap<String, List<String>>();
    private static HashMap<String, Integer> taskTime = new HashMap<String, Integer>();
    private static HashMap<String, Boolean> criticalPath = new HashMap<String, Boolean>();
    private static HashMap<String, Integer> timetable = new HashMap<String, Integer>();
    private static ReadData readData = new ReadData("data.txt");
    private static List<String> data;
    private static Task[] tasks;
    private static int cMax = 0;

    public static void main(String[] args) {
        initializeTasks();
        count();
        getCriticalPath();
        generateTimetable();

        createGraphJson();
        createTimetableJson();

        createGraph();

        printResults();
    }

    private static void initializeTasks() {
        try {
            readData.readFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        data = readData.getResult();
        tasks = new Task[data.size()];

        IntStream.range(0, data.size()).forEach(i -> {
            String tempString = data.get(i);
            String[] parts = tempString.split(" ");
            if (parts.length == 2) {
                tasks[i] = new Task(parts[0], Integer.parseInt(parts[1]));
                taskParents.put(parts[0], new ArrayList<>());
            } else {
                List<String> parents = new ArrayList<>(Arrays.asList(parts).subList(2, parts.length));
                tasks[i] = new Task(parts[0], Integer.parseInt(parts[1]), parents);
                taskParents.put(parts[0], parents);
            }

            taskTime.put(parts[0], Integer.parseInt(parts[1]));
        });

        for (Task task : tasks) {
            if (hasChildren(task)) {
                taskChildren.put(task.getName(), getChildren(task));
                task.setChildren(getChildren(task));
            }
        }
    }

    private static void count() {
        for (Task task : tasks) {
            if (task.getParents().isEmpty()) {
                earliestStarts.put(task.getName(), 0);
                earliestFinishes.put(task.getName(), task.getTime());
            } else {
                int maxFromPrevious = 0;
                for (int j = 0; j < task.getParents().size(); j++) {
                    int temp = earliestFinishes.get(task.getParents().get(j));

                    if (temp > maxFromPrevious) maxFromPrevious = temp;
                }

                if (maxFromPrevious + task.getTime() > cMax) cMax = maxFromPrevious + task.getTime();

                earliestStarts.put(task.getName(), maxFromPrevious);
                earliestFinishes.put(task.getName(), maxFromPrevious + task.getTime());
            }
        }

        for (Task task : tasks) {
            if (!hasChildren(task)) {
                latestStarts.put(task.getName(), cMax - task.getTime());
                latestFinishes.put(task.getName(), cMax);
            }
        }

        for (int i = tasks.length - 1; i >= 0; i--) {
            if (hasChildren(tasks[i])) {
                int childMinLatestStart = cMax;

                for (String child : tasks[i].getChildren()) {
                    if (latestStarts.get(child) < childMinLatestStart) childMinLatestStart = latestStarts.get(child);
                }

                latestStarts.put(tasks[i].getName(), childMinLatestStart - tasks[i].getTime());
                latestFinishes.put(tasks[i].getName(), childMinLatestStart);
            }
        }
    }

    private static boolean hasChildren(Task task) {
        for (Task t : tasks) {
            if (t.getParents().contains(task.getName())) {
                return true;
            }
        }

        return false;
    }

    private static List<String> getChildren(Task task) {
        List<String> children = new ArrayList<>();

        for (Task t : tasks) {
            if (t.getParents().contains(task.getName())) {
                children.add(t.getName());
            }
        }

        return children;
    }

    private static void getCriticalPath() {
        for (Task task : tasks) {
            String name = task.getName();
            if (earliestStarts.get(name).equals(latestStarts.get(name)) && earliestFinishes.get(name).equals(latestFinishes.get(name))) {
                criticalPath.put(name, true);
            } else {
                criticalPath.put(name, false);
            }
        }
    }

    private static void generateTimetable() {
        HashMap<String, Integer> taskTimeClone = (HashMap<String, Integer>) taskTime.clone();

        for (Task task : tasks) {
            if (criticalPath.get(task.getName())) {
                timetable.put(task.getName(), 1);
                taskTimeClone.remove(task.getName());
            }
        }

        int machine = 2;

        while (!taskTimeClone.isEmpty()) {
            int max = 0;
            for (Task task : tasks) {
                if (taskTimeClone.containsKey(task.getName())) {
                    if (!timetable.containsValue(machine)) {
                        timetable.put(task.getName(), machine);
                        max = earliestFinishes.get(task.getName());
                        taskTimeClone.remove(task.getName());
                    } else {
                        if (earliestStarts.get(task.getName()) >= max && earliestFinishes.get(task.getName()) < cMax) {
                            timetable.put(task.getName(), machine);
                            max = earliestFinishes.get(task.getName());
                            taskTimeClone.remove(task.getName());
                        }
                    }
                }
            }
            machine++;
        }
    }

    private static void createGraphJson() {
        String jsonString = null;
        JSONObject jsonObject = new JSONObject();
        try {
            for (Task task : tasks) {
                jsonObject.put(task.getName(), new JSONObject()
                                .put("time", task.getTime())
                                .put("parents", task.getParents())
                                .put("critical", criticalPath.get(task.getName()))
                                .put("values", new JSONObject()
                                        .put("earliestStarts", earliestStarts.get(task.getName()))
                                        .put("earliestFinishes", earliestFinishes.get(task.getName()))
                                        .put("latestStarts", latestStarts.get(task.getName()))
                                        .put("latestFinishes", latestFinishes.get(task.getName()))));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsonString = jsonObject.toString();

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter("graph.json"));
            writer.write(jsonString);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createTimetableJson() {
        String jsonString = null;
        JSONObject jsonObject = new JSONObject();
        try {
            for (Task task : tasks) {
                jsonObject.put(task.getName(), new JSONObject()
                        .put("time", task.getTime())
                        .put("machine", timetable.get(task.getName()))
                        .put("values", new JSONObject()
                                .put("earliestStarts", earliestStarts.get(task.getName()))
                                .put("earliestFinishes", earliestFinishes.get(task.getName()))));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsonString = jsonObject.toString();

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter("timetable.json"));
            writer.write(jsonString);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createGraph() {
        File imgFile = new File("graph.png");

        try {
            imgFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        DefaultDirectedGraph<String, DefaultEdge> g =
                new DefaultDirectedGraph<String, DefaultEdge>(DefaultEdge.class);

        for (Task task: tasks) {
            g.addVertex(task.getName() + ", " + task.getTime());
        }

        for (Task task: tasks) {
            if(!task.getParents().isEmpty()) {
                for(String parent: task.getParents()) {
                    g.addEdge(parent + ", " + taskTime.get(parent), task.getName() + ", " + task.getTime());
                }
            }
        }

        try {
            graphToFile(g);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void graphToFile(DefaultDirectedGraph<String, DefaultEdge> g) throws IOException {

        JGraphXAdapter<String, DefaultEdge> graphAdapter =
                new JGraphXAdapter<String, DefaultEdge>(g);
        mxHierarchicalLayout layout = new mxHierarchicalLayout(graphAdapter);

        graphAdapter.getStylesheet().getDefaultEdgeStyle().put(mxConstants.STYLE_NOLABEL, "1");

        layout.setIntraCellSpacing(50);
        layout.setInterRankCellSpacing(40);
        layout.setOrientation(SwingConstants.WEST);

        layout.execute(graphAdapter.getDefaultParent());


        BufferedImage image =
                mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);
        File imgFile = new File("graph.png");
        ImageIO.write(image, "PNG", imgFile);
    }

    private static void printResults() {
        for (Task task : tasks) {
            System.out.println(task.getName() + ": " + earliestStarts.get(task.getName()) + " " + earliestFinishes.get(task.getName()));
            System.out.println(task.getName() + ": " + latestStarts.get(task.getName()) + " " + latestFinishes.get(task.getName()));
            System.out.println();
        }

        System.out.println("cMax = " + cMax);
    }
}
