package com.msoroka.liu.managers;

import com.msoroka.liu.assets.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    private List<String> data;
    private int N;
    private HashMap<String, Integer> pi = new HashMap<String, Integer>();
    private HashMap<String, Integer> dj = new HashMap<String, Integer>();
    private HashMap<String, Integer> rj = new HashMap<String, Integer>();
    private HashMap<String, Integer> di = new HashMap<String, Integer>();
    private HashMap<String, List<String>> taskNext = new HashMap<String, List<String>>();
    private HashMap<String, List<String>> taskAllNexts = new HashMap<String, List<String>>();
    private HashMap<String, List<String>> taskPrevious = new HashMap<String, List<String>>();
    private Task[] tasks;

    public TaskManager() {
    }

    public TaskManager(List<String> data) {
        this.data = data;
    }

    public void generateTasks() {
        this.N = Integer.parseInt(this.data.get(0));
        this.tasks = new Task[N];
        this.designateTasks();

        System.out.println(this.taskNext);
        System.out.println(this.taskPrevious);
        System.out.println(this.taskAllNexts);
        System.out.println(this.pi);
        System.out.println(this.dj);
        System.out.println(this.rj);
        System.out.println(this.di);
    }

    private void designateTasks() {
        for (int i = 1; i <= this.N; i++) {
            String tempString = this.data.get(i);
            String[] parts = tempString.split(" ");

            if (parts.length == 1) {
                this.tasks[i - 1] = new Task(parts[0]);
                this.taskPrevious.put(parts[0], new ArrayList<String>());
            } else {
                List<String> parents = new ArrayList<>(Arrays.asList(parts).subList(1, parts.length));
                this.tasks[i - 1] = new Task(parts[0]);
                this.taskPrevious.put(parts[0], parents);
                this.tasks[i - 1].setPrevious(parents);
            }
        }

        for (Task task : this.tasks) {
            if (hasNext(task)) {
                taskNext.put(task.getName(), getNext(task));
                task.setNext(getNext(task));
            } else {
                taskNext.put(task.getName(), new ArrayList<String>());
            }
        }

        this.designateAllNexts();
        this.designatePI();
        this.designateDJ();
        this.designateRJ();
        this.designateDI();
    }

    private void designateAllNexts() {
        for (Task task: this.tasks) {
            String tempNext = null;
            List<String> tempNexts = new ArrayList<String>();

            if(!task.getNext().isEmpty()) {
                for(String s: task.getNext()) {
                    tempNext = s;
                    tempNexts.add(tempNext);
                }
            }

            while(tempNext != null) {
                if(!taskNext.get(tempNext).isEmpty()) {
                    for (String s: taskNext.get(tempNext)) {
                        tempNext = s;
                        tempNexts.add(tempNext);
                    }
                } else {
                    tempNext = null;
                }
            }

            taskAllNexts.put(task.getName(), tempNexts);
        }
    }

    private void designatePI() {
        for (int i = 0; i < this.tasks.length; i++) {
            String tempString = this.data.get(N + 1);
            String[] parts = tempString.split(" ");
            this.pi.put(this.tasks[i].getName(), Integer.valueOf(parts[i]));
        }
    }

    private void designateDJ() {
        for (int i = 0; i < this.tasks.length; i++) {
            String tempString = this.data.get(N + 2);
            String[] parts = tempString.split(" ");
            this.dj.put(this.tasks[i].getName(), Integer.valueOf(parts[i]));
        }
    }

    private void designateRJ() {
        for (int i = 0; i < this.tasks.length; i++) {
            String tempString = this.data.get(N + 3);
            String[] parts = tempString.split(" ");
            this.rj.put(this.tasks[i].getName(), Integer.valueOf(parts[i]));
        }
    }

    private void designateDI() {
        for (Task task: this.tasks) {
            int min = this.dj.get(task.getName());

            for (String s: this.taskAllNexts.get(task.getName())) {
                if(this.dj.get(s) < min ) {
                    min = this.dj.get(s);
                }
            }

            this.di.put(task.getName(), min);
        }
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
