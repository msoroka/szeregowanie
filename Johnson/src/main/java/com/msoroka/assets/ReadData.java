package com.msoroka.assets;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReadData {
    private Task[] tasks;
    private List<String> results = new ArrayList<>();

    public ReadData(String fileName) {
        File file = new File(fileName);

        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader((new FileReader(file)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (true) {
            String tempString = "";

            try {
                assert bufferedReader != null;
                if ((tempString = bufferedReader.readLine()) == null) break;
            } catch (IOException e) {
                e.printStackTrace();
            }

            this.results.add(tempString);
        }

        this.tasks = new Task[this.results.size() / 4];

        for (int i = 0; i < this.results.size()-1; i += 4) {
            int tempM1 = Integer.parseInt(this.results.get(i + 1));
            int tempM2 = Integer.parseInt(this.results.get(i + 2));
            int tempM3 = Integer.parseInt(this.results.get(i + 3));

            this.tasks[i / 4] = new Task(this.results.get(i), tempM1, tempM2, tempM3);
        }
    }

    public Task[] getTasks() {
        return tasks;
    }

    public void setTasks(Task[] tasks) {
        this.tasks = tasks;
    }
}
