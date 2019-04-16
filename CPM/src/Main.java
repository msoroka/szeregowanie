import java.io.*;
import java.util.*;
import java.util.stream.IntStream;

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

//        printResults();
//        System.out.println(criticalPath);
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

    

    private static void printResults() {
        for (Task task : tasks) {
            System.out.println(task.getName() + ": " + earliestStarts.get(task.getName()) + " " + earliestFinishes.get(task.getName()));
            System.out.println(task.getName() + ": " + latestStarts.get(task.getName()) + " " + latestFinishes.get(task.getName()));
            System.out.println();
        }

        System.out.println("cMax = " + cMax);
    }
}
