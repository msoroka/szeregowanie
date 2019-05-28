package com.msoroka.liu.managers;

import com.msoroka.liu.assets.Task;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class TaskManager {
    private List<String> data;
    private int N;
    private int t;
    private int Lmax;
    private HashMap<String, Integer> pi = new HashMap<>();
    private HashMap<String, Integer> piClone = new HashMap<>();
    private HashMap<String, Integer> dj = new HashMap<>();
    private HashMap<String, Integer> rj = new HashMap<>();
    private HashMap<String, Integer> di = new HashMap<>();
    private HashMap<String, Integer> li = new HashMap<>();
    private HashMap<Integer, String> taskStart = new HashMap<>();
    private HashMap<Integer, String> taskFinish = new HashMap<>();
    private HashMap<String, List<String>> taskNext = new HashMap<>();
    private HashMap<String, List<String>> taskAllNexts = new HashMap<>();
    private HashMap<String, List<String>> taskPrevious = new HashMap<>();
    private Task[] tasks;

    public TaskManager() {
    }

    public TaskManager(List<String> data) {
        this.data = data;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public int getN() {
        return N;
    }

    public void setN(int n) {
        N = n;
    }

    public int getT() {
        return t;
    }

    public void setT(int t) {
        this.t = t;
    }

    public int getLmax() {
        return Lmax;
    }

    public void setLmax(int lmax) {
        Lmax = lmax;
    }

    public HashMap<String, Integer> getPi() {
        return pi;
    }

    public void setPi(HashMap<String, Integer> pi) {
        this.pi = pi;
    }

    public HashMap<String, Integer> getDj() {
        return dj;
    }

    public void setDj(HashMap<String, Integer> dj) {
        this.dj = dj;
    }

    public HashMap<String, Integer> getRj() {
        return rj;
    }

    public void setRj(HashMap<String, Integer> rj) {
        this.rj = rj;
    }

    public HashMap<String, Integer> getDi() {
        return di;
    }

    public void setDi(HashMap<String, Integer> di) {
        this.di = di;
    }

    public HashMap<String, Integer> getLi() {
        return li;
    }

    public void setLi(HashMap<String, Integer> li) {
        this.li = li;
    }

    public HashMap<Integer, String> getTaskStart() {
        return taskStart;
    }

    public void setTaskStart(HashMap<Integer, String> taskStart) {
        this.taskStart = taskStart;
    }

    public HashMap<Integer, String> getTaskFinish() {
        return taskFinish;
    }

    public void setTaskFinish(HashMap<Integer, String> taskFinish) {
        this.taskFinish = taskFinish;
    }

    public HashMap<String, List<String>> getTaskNext() {
        return taskNext;
    }

    public void setTaskNext(HashMap<String, List<String>> taskNext) {
        this.taskNext = taskNext;
    }

    public HashMap<String, List<String>> getTaskAllNexts() {
        return taskAllNexts;
    }

    public void setTaskAllNexts(HashMap<String, List<String>> taskAllNexts) {
        this.taskAllNexts = taskAllNexts;
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

    public void generateTasks() {
        this.N = Integer.parseInt(this.data.get(0));
        this.tasks = new Task[N];
        this.designateTasks();
        this.generateSchedule();
        this.writeResults();
    }

    private void designateTasks() {
        for (int i = 1; i <= this.N; i++) {
            String tempString = this.data.get(i);
            String[] parts = tempString.split(" ");

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


        for (Task task : this.tasks) {
            if (hasNext(task)) {
                taskNext.put(task.getName(), getNext(task));
                task.setNext(getNext(task));
            } else {
                taskNext.put(task.getName(), new ArrayList<>());
            }
        }


        if(this.checkIfCycled()) {
            System.out.println("Graf cykliczny!");
            System.exit(1);
        } else {
            this.designateAllNexts();
            this.designatePI();
            this.designateDJ();
            this.designateRJ();
            this.designateDI();
        }
    }

    private void designateAllNexts() {
        for (Task task : this.tasks) {
            String tempNext = null;
            List<String> tempNexts = new ArrayList<>();

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
        this.piClone = new HashMap<String, Integer>(this.pi);

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
                } else {
                    boolean isBreak = false;
                    for (String prev : taskWithMinDi.getPrevious()) {
                        for (Task tt : this.tasks) {
                            if (prev.equals(tt.getName())) {
                                if (!tt.isCompleted()) {
                                    taskStart.put(time, "przerwa");
                                    taskFinish.put(time + 1, "przerwa");
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
        for (Task task : this.tasks) {
            if (li.get(task.getName()) > max) {
                max = li.get(task.getName());
            }
        }

        this.Lmax = max;
    }

    private boolean checkIfCycled() {
        for (Task task: this.tasks) {
            task.setVisited(true);
            for (String s: task.getNext()) {
                for (Task t : this.tasks) {
                    if(s.equals(t.getName())) {
                        if(t.isVisited()){
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private void writeResults() {
        System.out.println("Pi:");
        System.out.println(this.piClone);
        System.out.println();

        System.out.println("Dj:");
        System.out.println(this.dj);
        System.out.println();

        System.out.println("Rj:");
        System.out.println(this.rj);
        System.out.println();

        System.out.println("Di*:");
        System.out.println(this.di);
        System.out.println();

        System.out.println("Li:");
        System.out.println(this.li);
        System.out.println();

        System.out.println("t: " + this.t);
        System.out.println("Lmax: " + this.Lmax);
        System.out.println();

        System.out.println("Harmonogram:");
        for (Map.Entry<Integer, String> task : this.taskStart.entrySet()) {
            Integer key = task.getKey();
            String value = task.getValue();

            System.out.println("[" + key + ", " + (key + 1) + "] => " + value);
        }

    }
}
