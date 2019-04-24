package com.msoroka.managers;

import com.msoroka.assets.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

public class TaskManager {
    private List<String> data;
    private HashMap<String, List<String>> taskNext = new HashMap<String, List<String>>();
    private HashMap<String, List<String>> taskPrevious = new HashMap<String, List<String>>();
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
            if(i == 0) {
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

        for(Task task: this.tasks) {
            if (hasNext(task)) {
                taskNext.put(task.getName(), getNext(task));
                task.setNext(getNext(task));
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
}
