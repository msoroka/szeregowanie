package com.msoroka.managers;

import com.msoroka.assets.Task;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;
import org.json.*;

public class TaskManager {
    private List<String> data;
    private HashMap<String, List<String>> taskNext = new HashMap<String, List<String>>();
    private HashMap<String, List<String>> taskPrevious = new HashMap<String, List<String>>();
    private HashMap<String, Integer> taskMachine = new HashMap<String, Integer>();
    private HashMap<String, Integer> taskStart = new HashMap<String, Integer>();
    private HashMap<String, Integer> taskFinish = new HashMap<String, Integer>();
    private HashMap<String, Integer> taskLevel = new HashMap<String, Integer>();
    private Task[] tasks;
    private int machines;

    public TaskManager() {
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public HashMap<String, List<String>> getTaskNext() {
        return taskNext;
    }

    public void setTaskNext(HashMap<String, List<String>> taskNext) {
        this.taskNext = taskNext;
    }

    public HashMap<String, List<String>> getTaskPrevious() {
        return taskPrevious;
    }

    public void setTaskPrevious(HashMap<String, List<String>> taskPrevious) {
        this.taskPrevious = taskPrevious;
    }

    public Task[] getTasks() {
        return tasks;
    }

    public void setTasks(Task[] tasks) {
        this.tasks = tasks;
    }

    public int getMachines() {
        return machines;
    }

    public void setMachines(int machines) {
        this.machines = machines;
    }

    public Task[] generateTasks(List<String> data) {
        this.tasks = new Task[data.size() - 1];

        IntStream.range(0, data.size()).forEach(i -> {
            String tempString = data.get(i);
            String[] parts = tempString.split(" ");
            if (i == 0) {
                this.machines = Integer.parseInt(parts[0]);
            } else {
                if (parts.length == 1) {
                    this.tasks[i - 1] = new Task(parts[0]);
                    this.taskPrevious.put(parts[0], new ArrayList<>());
                } else {
                    List<String> parents = new ArrayList<>(Arrays.asList(parts).subList(1, parts.length));
                    this.tasks[i - 1] = new Task(parts[0]);
                    this.taskPrevious.put(parts[0], parents);
                    this.tasks[i - 1].setPrevious(parents);
                }
            }
        });

        for (Task task : this.tasks) {
            if (hasNext(task)) {
                taskNext.put(task.getName(), getNext(task));
                task.setNext(getNext(task));
            }
        }

        int level = 1;

        for (int i = this.tasks.length - 1; i >= 0; i--) {
            if (this.tasks[i].getNext().isEmpty() && this.tasks[i].getLevel() == 0) {
                this.tasks[i].setLevel(level);
                this.taskLevel.put(this.tasks[i].getName(), level);
            } else if (this.taskLevel.containsKey(this.tasks[i].getNext().get(0))) {
                this.taskLevel.put(this.tasks[i].getName(), this.taskLevel.get(this.tasks[i].getNext().get(0)) + 1);
                this.tasks[i].setLevel(this.taskLevel.get(this.tasks[i].getNext().get(0)) + 1);
            }
        }

        return this.tasks;
    }

    private boolean hasNext(Task task) {
        for (Task t : tasks) {
            if (t.getPrevious().contains(task.getName())) {
                return true;
            }
        }

        return false;
    }

    private List<String> getNext(Task task) {
        List<String> next = new ArrayList<>();

        for (Task t : tasks) {
            if (t.getPrevious().contains(task.getName())) {
                next.add(t.getName());
            }
        }

        return next;
    }

    public void generateSchedule() {
        int machine = 1;
        int start = 0;
        int finish = 1;

        for (Task task : this.tasks) {
                task.setMachine(machine);
                task.setStart(start);
                task.setFinish(finish);
                taskMachine.put(task.getName(), task.getMachine());
                taskStart.put(task.getName(), task.getStart());
                taskFinish.put(task.getName(), task.getFinish());

            machine++;

            if(machine > this.machines) {
                machine = 1;
                start += 1;
                finish += 1;
            }
        }

        for(Task task: this.tasks) {
            if(!task.getPrevious().isEmpty()) {
                List<String> previousTasks = task.getPrevious();

                for(String t: previousTasks) {
                    int s = taskStart.get(t);
                    int f = taskFinish.get(t);

                    if(s == task.getStart() && f == task.getFinish()) {
                        task.setStart(task.getStart() + 1);
                        task.setFinish(task.getFinish() + 1);

                        taskStart.replace(task.getName(), task.getStart() + 1);
                        taskFinish.replace(task.getName(), task.getFinish() + 1);
                    }

                    if(s > task.getStart() && f > task.getFinish()) {
                        task.setStart(task.getStart() + Math.abs(s - task.getStart()));
                        task.setFinish(task.getFinish() + Math.abs(f - task.getFinish()));

                        taskStart.replace(task.getName(), task.getStart() + Math.abs(s - task.getStart()));
                        taskFinish.replace(task.getName(), task.getFinish() + Math.abs(f - task.getFinish()));
                    }
                }
            }
        }

        for (Task task: this.tasks) {
            System.out.println(task.getName() + ", start: " + task.getStart() + " finish: " + task.getFinish() + " previous: " + task.getPrevious());
        }

        createTimetableJson();
    }

    private void createTimetableJson() {
        String jsonString = null;
        JSONObject jsonObject = new JSONObject();
        try {
            for (Task task : this.tasks) {
                jsonObject.put(task.getName(), new JSONObject()
                        .put("level", task.getLevel())
                        .put("machine", task.getMachine())
                        .put("values", new JSONObject()
                                .put("earliestStarts", task.getStart())
                                .put("earliestFinishes", task.getFinish())));
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
}
