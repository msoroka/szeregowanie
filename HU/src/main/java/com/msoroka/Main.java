package com.msoroka;

import com.msoroka.assets.Graph;
import com.msoroka.assets.ReadData;
import com.msoroka.assets.Task;
import com.msoroka.managers.TaskManager;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<String> data = new ReadData("data.txt").getResult();

        TaskManager taskManager = new TaskManager();

        Task[] tasks = taskManager.generateTasks(data);

        Graph graph = new Graph(tasks);
    }
}
