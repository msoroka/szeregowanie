package com.msoroka;

import com.msoroka.assets.ReadData;
import com.msoroka.assets.Task;
import com.msoroka.managers.TaskManager;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<String> data = new ReadData("data.txt").getResult();

        Task[] tasks = new Task[data.size() - 1];
        TaskManager taskManager = new TaskManager();
        taskManager.setData(data);
    }
}
