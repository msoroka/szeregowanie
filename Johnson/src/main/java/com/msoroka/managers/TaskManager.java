package com.msoroka.managers;

import com.msoroka.assets.Task;
import com.msoroka.assets.TimeComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskManager {
    private Task[] tasks;
    private List<Task> N1 = new ArrayList<>();
    private List<Task> N2 = new ArrayList<>();

    public TaskManager(Task[] tasks) {
        this.tasks = tasks;

        if(!this.validate()) {
            System.out.println("Maszyna 2 nie jest zdominowana.");
            System.exit(1);
        } else {
            this.designateN();
            this.sortN();
            this.designateSchedule();
            this.printTasks();
        }
    }

    public Task[] getTasks() {
        return tasks;
    }

    public void setTasks(Task[] tasks) {
        this.tasks = tasks;
    }

    private boolean validate() {
        int minM1 = this.tasks[0].getFirstMachineTime();
        int maxM2 = this.tasks[0].getSecondMachineTime();
        int minM3 = this.tasks[0].getThirdMachineTime();

        for(Task task: this.tasks) {
            if(task.getFirstMachineTime() < minM1) {
                minM1 = task.getFirstMachineTime();
            }
            if(task.getSecondMachineTime() < maxM2) {
                maxM2 = task.getSecondMachineTime();
            }
            if(task.getThirdMachineTime() < minM3) {
                minM3 = task.getThirdMachineTime();
            }
        }

        return maxM2 <= minM1 || maxM2 <= minM3;
    }

    private void designateN() {
        for(Task task: this.tasks) {
            if(task.getFirstModifiedTime() < task.getSecondModifiedTime()) {
                this.N1.add(task);
            } else {
                this.N2.add(task);
            }
        }
    }

    private void sortN() {
        this.N1.sort(new TimeComparator());
        this.N2.sort(new TimeComparator());
        Collections.reverse(this.N2);
    }

    private void designateSchedule() {
        int actualM1 = 0;

        for (Task tempTask : this.N1) {
            actualM1 = designateTimes(actualM1, tempTask);
        }

        for (Task tempTask : this.N2) {
            actualM1 = designateTimes(actualM1, tempTask);
        }
    }

    private int designateTimes(int actualM1, Task tempTask) {
        int actualM2;
        int actualM3;
        tempTask.getStartTime().add(actualM1);
        tempTask.getFinishTime().add(actualM1 + tempTask.getFirstMachineTime());
        actualM1 += tempTask.getFirstMachineTime();
        actualM2 = actualM1;

        tempTask.getStartTime().add(actualM2);
        tempTask.getFinishTime().add(actualM2 + tempTask.getSecondMachineTime());
        actualM2 += tempTask.getSecondMachineTime();
        actualM3 = actualM2;

        tempTask.getStartTime().add(actualM3);
        tempTask.getFinishTime().add(actualM3 + tempTask.getThirdMachineTime());
        actualM3 += tempTask.getThirdMachineTime();
        return actualM1;
    }

    private void printTasks() {
        for (Task tempTask : this.N1) {
            System.out.println(tempTask);
        }

        for (Task tempTask : this.N2) {
            System.out.println(tempTask);
        }
    }
}
