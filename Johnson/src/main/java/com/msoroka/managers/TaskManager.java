package com.msoroka.managers;

import com.msoroka.assets.Task;

public class TaskManager {
    private Task[] tasks;

    public TaskManager(Task[] tasks) {
        this.tasks = tasks;
    }

    public Task[] getTasks() {
        return tasks;
    }

    public void setTasks(Task[] tasks) {
        this.tasks = tasks;
    }
}
