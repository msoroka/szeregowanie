import java.util.ArrayList;
import java.util.List;

public class Task {
    private String task;
    private int time;
    private List<String> previous = new ArrayList<String>();

    public Task(String task, int time) {
        this.task = task;
        this.time = time;
    }

    public Task(String task, int time, List<String> previous) {
        this.task = task;
        this.time = time;
        this.previous = previous;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public List<String> getPrevious() {
        return previous;
    }

    public void setPrevious(List<String> previous) {
        this.previous = previous;
    }
}
