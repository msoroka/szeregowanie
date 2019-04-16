import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        ReadData readData = new ReadData("data.txt");
        readData.readFile();
        List<String> data = readData.getResult();
        Task[] tasks = new Task[data.size()];
        HashMap<String, List<String>> taskPrevious = new HashMap<String, List<String>>();
        HashMap<String, Integer> taskTime = new HashMap<String, Integer>();

        for (int i = 0; i < data.size(); i++) {
            String tempString = data.get(i);
            String[] parts = tempString.split(" ");

            if (parts.length == 2) {
                tasks[i] = new Task(parts[0], Integer.parseInt(parts[1]));

                taskPrevious.put(parts[0], new ArrayList<String>());

            } else {
                List<String> previous = new ArrayList<String>(Arrays.asList(parts).subList(2, parts.length));

                tasks[i] = new Task(parts[0], Integer.parseInt(parts[1]), previous);

                taskPrevious.put(parts[0], previous);
            }

            taskTime.put(parts[0], Integer.parseInt(parts[1]));
        }

        count(tasks, taskTime);
    }

    public static void count(Task[] tasks, HashMap<String, Integer> taskTime) {
        HashMap<String, Integer> earliestStarts = new HashMap<String, Integer>();
        HashMap<String, Integer> earliestFinishes = new HashMap<String, Integer>();
        HashMap<String, Integer> latestStarts = new HashMap<String, Integer>();
        HashMap<String, Integer> latestFinishes = new HashMap<String, Integer>();

        for (int i = 0; i < tasks.length; i++) {
            if (tasks[i].getPrevious().isEmpty()) {
                earliestStarts.put(tasks[i].getTask(), 0);
                earliestFinishes.put(tasks[i].getTask(), tasks[i].getTime());
            } else {
                int maxFromPrevious = 0;
                for (int j = 0; j < tasks[i].getPrevious().size(); j++) {
                    int temp = earliestFinishes.get(tasks[i].getPrevious().get(j));

                    if (temp > maxFromPrevious) maxFromPrevious = temp;
                }

                earliestStarts.put(tasks[i].getTask(), maxFromPrevious);
                earliestFinishes.put(tasks[i].getTask(), maxFromPrevious + tasks[i].getTime());
            }
        }

        System.out.println("A " + earliestStarts.get("A") + " " + earliestFinishes.get("A"));
        System.out.println("A " + latestStarts.get("A") + " " + latestFinishes.get("A"));
        System.out.println();
        System.out.println("B " + earliestStarts.get("B") + " " + earliestFinishes.get("B"));
        System.out.println("B " + latestStarts.get("B") + " " + latestFinishes.get("B"));
        System.out.println();
        System.out.println("C " + earliestStarts.get("C") + " " + earliestFinishes.get("C"));
        System.out.println("C " + latestStarts.get("C") + " " + latestFinishes.get("C"));
        System.out.println();
        System.out.println("D " + earliestStarts.get("D") + " " + earliestFinishes.get("D"));
        System.out.println("D " + latestStarts.get("D") + " " + latestFinishes.get("D"));
        System.out.println();
        System.out.println("E " + earliestStarts.get("E") + " " + earliestFinishes.get("E"));
        System.out.println("E " + latestStarts.get("E") + " " + latestFinishes.get("E"));
        System.out.println();
        System.out.println("F " + earliestStarts.get("F") + " " + earliestFinishes.get("F"));
        System.out.println("F " + latestStarts.get("F") + " " + latestFinishes.get("F"));
        System.out.println();
        System.out.println("G " + earliestStarts.get("G") + " " + earliestFinishes.get("G"));
        System.out.println("G " + latestStarts.get("G") + " " + latestFinishes.get("G"));
        System.out.println();
        System.out.println("H " + earliestStarts.get("H") + " " + earliestFinishes.get("H"));
        System.out.println("H " + latestStarts.get("H") + " " + latestFinishes.get("H"));
        System.out.println();
        System.out.println("I " + earliestStarts.get("I") + " " + earliestFinishes.get("I"));
        System.out.println("I " + latestStarts.get("I") + " " + latestFinishes.get("I"));
        System.out.println();
        System.out.println("J " + earliestStarts.get("J") + " " + earliestFinishes.get("J"));
        System.out.println("J " + latestStarts.get("J") + " " + latestFinishes.get("J"));
    }
}
