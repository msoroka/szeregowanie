import java.util.ArrayList;
import java.util.List;

public class Task {
    private String name;
    private int time;
    private List<String> parents = new ArrayList<String>();
    private List<String> children = new ArrayList<String>();

    public Task(String task, int time) {
        this.name = task;
        this.time = time;
    }

    public Task(String task, int time, List<String> parents) {
        this.name = task;
        this.time = time;
        this.parents = parents;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public List<String> getParents() {
        return parents;
    }

    public void setParents(List<String> parents) {
        this.parents = parents;
    }

    public List<String> getChildren() {
        return children;
    }

    public void setChildren(List<String> children) {
        this.children = children;
    }
}
