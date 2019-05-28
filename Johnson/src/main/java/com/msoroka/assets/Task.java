package com.msoroka.assets;

import java.util.ArrayList;
import java.util.List;

public class Task {
    private String name;
    private List<Integer> startTime = new ArrayList<>();
    private List<Integer> finishTime = new ArrayList<>();
    private int firstMachineTime;
    private int secondMachineTime;
    private int thirdMachineTime;
    private int firstModifiedTime;
    private int secondModifiedTime;
    private int chosenTime;

    public Task() {
    }

    public Task(String name, int firstMachineTime, int secondMachineTime, int thirdMachineTime) {
        this.name = name;
        this.firstMachineTime = firstMachineTime;
        this.secondMachineTime = secondMachineTime;
        this.thirdMachineTime = thirdMachineTime;

        this.firstModifiedTime = this.firstMachineTime + this.secondMachineTime;
        this.secondModifiedTime = this.secondMachineTime + this.thirdMachineTime;

        if(this.firstModifiedTime > this.secondModifiedTime) {
            this.chosenTime = this.secondModifiedTime;
        } else {
            this.chosenTime = this.firstModifiedTime;
        }
    }

    public String toString() {
        return "\nNazwa zadania: " + this.name + "\n" +
                "Czas na M1: " + this.firstMachineTime + "\n" +
                "Czas na M2: " + this.secondMachineTime + "\n" +
                "Czas na M3: " + this.thirdMachineTime + "\n" +
                "Zmodyfikowany 1: " + this.firstModifiedTime + "\n" +
                "Zmodyfikowany 2: " + this.secondModifiedTime + "\n" +
                "Wybrany czas: " + this.chosenTime + "\n" +
                "Początek: " + this.startTime + "\n" +
                "Koniec: " + this.finishTime + "\n";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getStartTime() {
        return startTime;
    }

    public void setStartTime(List<Integer> startTime) {
        this.startTime = startTime;
    }

    public List<Integer> getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(List<Integer> finishTime) {
        this.finishTime = finishTime;
    }

    public int getFirstMachineTime() {
        return firstMachineTime;
    }

    public void setFirstMachineTime(int firstMachineTime) {
        this.firstMachineTime = firstMachineTime;
    }

    public int getSecondMachineTime() {
        return secondMachineTime;
    }

    public void setSecondMachineTime(int secondMachineTime) {
        this.secondMachineTime = secondMachineTime;
    }

    public int getThirdMachineTime() {
        return thirdMachineTime;
    }

    public void setThirdMachineTime(int thirdMachineTime) {
        this.thirdMachineTime = thirdMachineTime;
    }

    public int getFirstModifiedTime() {
        return firstModifiedTime;
    }

    public void setFirstModifiedTime(int firstModifiedTime) {
        this.firstModifiedTime = firstModifiedTime;
    }

    public int getSecondModifiedTime() {
        return secondModifiedTime;
    }

    public void setSecondModifiedTime(int secondModifiedTime) {
        this.secondModifiedTime = secondModifiedTime;
    }

    public int getChosenTime() {
        return chosenTime;
    }

    public void setChosenTime(int chosenTime) {
        this.chosenTime = chosenTime;
    }
}
