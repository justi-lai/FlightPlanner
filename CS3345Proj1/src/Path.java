import java.util.LinkedList;

public class Path {
    public int currentNode;
    public LinkedList<Integer> path;
    public LinkedList<String> pathNames;
    public double time;
    public double cost;

    public Path(int cn, LinkedList<Integer> currentPath, LinkedList<String> pathNames, double time, double cost) {
        currentNode = cn;
        path = currentPath;
        this.pathNames = pathNames;
        this.time = time;
        this.cost = cost;
    }
}
