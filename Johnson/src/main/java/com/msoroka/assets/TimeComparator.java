package com.msoroka.assets;

import java.util.Comparator;

public class TimeComparator implements Comparator<Task> {
    @Override
    public int compare(Task t1, Task t2) {
        int t1Time = t1.getChosenTime();
        int t2Time = t2.getChosenTime();

        return Integer.compare(t1Time, t2Time);
    }
}