package com.msoroka;

import com.msoroka.assets.ReadData;
import com.msoroka.assets.Task;
import com.msoroka.managers.TaskManager;

public class Main {
    public static void main(String[] args) {
        ReadData readData = new ReadData("dane.txt");
        Task[] tasks = readData.getTasks();
        TaskManager taskManager = new TaskManager(tasks);

        for (Task task: tasks) {
            System.out.println(task);
        }
    }
}
