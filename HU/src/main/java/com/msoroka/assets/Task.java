package com.msoroka.assets;

import java.util.ArrayList;
import java.util.List;

public class Task {
    private String name;
    private List<String> next = new ArrayList<>();
    private List<String> previous = new ArrayList<>();

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
}
