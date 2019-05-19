package com.msoroka.liu;

import com.msoroka.liu.assets.Graph;
import com.msoroka.liu.assets.ReadData;
import com.msoroka.liu.assets.Task;
import com.msoroka.liu.managers.TaskManager;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<String> data = new ReadData("data.txt").getResult();
        TaskManager taskManager = new TaskManager(data);
        taskManager.generateTasks();
        Task[] tasks = taskManager.getTasks();
        Graph graph = new Graph(tasks);
    }
}
