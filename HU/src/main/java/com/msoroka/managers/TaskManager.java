package com.msoroka.managers;

import java.util.HashMap;
import java.util.List;

public class TaskManager {
    private List<String> data;
    private HashMap<String, List<String>> taskNext = new HashMap<String, List<String>>();
    private HashMap<String, List<String>> taskChildren = new HashMap<String, List<String>>();

    public TaskManager() {
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public HashMap<String, List<String>> getTaskNext() {
        return taskNext;
    }

    public void setTaskNext(HashMap<String, List<String>> taskNext) {
        this.taskNext = taskNext;
    }

    public HashMap<String, List<String>> getTaskChildren() {
        return taskChildren;
    }

    public void setTaskChildren(HashMap<String, List<String>> taskChildren) {
        this.taskChildren = taskChildren;
    }
}
