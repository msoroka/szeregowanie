package com.msoroka.assets;

import java.util.ArrayList;
import java.util.List;

public class Task {
    private String name;
    private List<String> next = new ArrayList<>();
    private List<String> previous = new ArrayList<>();
    private int level;
    private int machine;
    private int start;
    private int finish;

    public Task() {
    }

    public Task(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getNext() {
        return next;
    }

    public void setNext(List<String> next) {
        this.next = next;
    }

    public List<String> getPrevious() {
        return previous;
    }

    public void setPrevious(List<String> previous) {
        this.previous = previous;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getMachine() {
        return machine;
    }

    public void setMachine(int machine) {
        this.machine = machine;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getFinish() {
        return finish;
    }

    public void setFinish(int finish) {
        this.finish = finish;
    }
}
