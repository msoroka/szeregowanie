package com.msoroka.liu.managers;

import com.msoroka.liu.assets.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    private List<String> data;
    private int N;
    private int t;
    private int Lmax;
    private HashMap<String, Integer> pi = new HashMap<String, Integer>();
    private HashMap<String, Integer> dj = new HashMap<String, Integer>();
    private HashMap<String, Integer> rj = new HashMap<String, Integer>();
    private HashMap<String, Integer> di = new HashMap<String, Integer>();
    private HashMap<String, Integer> li = new HashMap<String, Integer>();
    private HashMap<Integer, String> taskStart = new HashMap<Integer, String>();
    private HashMap<Integer, String> taskFinish = new HashMap<Integer, String>();
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
        this.generateSchedule();
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
        for (Task task : this.tasks) {
            String tempNext = null;
            List<String> tempNexts = new ArrayList<String>();

            if (!task.getNext().isEmpty()) {
                for (String s : task.getNext()) {
                    tempNext = s;
                    tempNexts.add(tempNext);
                }
            }

            while (tempNext != null) {
                if (!taskNext.get(tempNext).isEmpty()) {
                    for (String s : taskNext.get(tempNext)) {
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
        for (Task task : this.tasks) {
            int min = this.dj.get(task.getName());

            for (String s : this.taskAllNexts.get(task.getName())) {
                if (this.dj.get(s) < min) {
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

    private void generateSchedule() {
        int time = 0;

        while (!this.tasks[N - 1].isCompleted()) {
            List<Task> inSystem = new ArrayList<Task>();

            for (Task t : this.tasks) {
                if (rj.get(t.getName()) <= time && !t.isCompleted()) {
                    inSystem.add(t);
                }
            }

            if (inSystem.size() > 0) {
                int minDi = this.di.get(inSystem.get(0).getName());
                Task taskWithMinDi = inSystem.get(0);

                for (Task in : inSystem) {
                    if (this.di.get(in.getName()) < minDi) {
                        minDi = this.di.get((in.getName()));
                        taskWithMinDi = in;
                    }
                }

                if (taskWithMinDi.getPrevious().isEmpty()) {
                    putTaskIntoSchedule(time, taskWithMinDi);
                }
                else {
                    boolean isBreak = false;
                    for (String prev : taskWithMinDi.getPrevious()) {
                        for (Task tt : this.tasks) {
                            if (prev.equals(tt.getName())) {
                                if (!tt.isCompleted()) {
                                    taskStart.put(time, "break");
                                    taskFinish.put(time + 1, "break");
                                    isBreak = true;
                                }
                            }
                        }
                    }

                    if (!isBreak) {
                        putTaskIntoSchedule(time, taskWithMinDi);
                    }
                }
            }

            time++;
        }

        this.t = time;
        this.designateLmax();
    }

    private void putTaskIntoSchedule(int time, Task taskWithMinDi) {
        taskStart.put(time, taskWithMinDi.getName());
        taskFinish.put(time + 1, taskWithMinDi.getName());
        pi.replace(taskWithMinDi.getName(), pi.get(taskWithMinDi.getName()) - 1);

        if (pi.get(taskWithMinDi.getName()) == 0) {
            taskWithMinDi.setCompleted(true);
            li.put(taskWithMinDi.getName(), (time + 1) - dj.get(taskWithMinDi.getName()));
        }
    }

    private void designateLmax() {
        int max = li.get(this.tasks[0].getName());
        for (Task task: this.tasks) {
            if(li.get(task.getName()) > max ){
                max = li.get(task.getName());
            }
        }

        this.Lmax = max;
    }
}
