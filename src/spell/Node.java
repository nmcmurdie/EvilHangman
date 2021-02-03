package spell;

public class Node implements INode {
    private int count = 0;
    private Node[] children = new Node[26];

    public int getValue() {
        return this.count;
    }

    public void incrementValue() {
        ++count;
    }

    public Node[] getChildren() {
        return this.children;
    }

    public void addChild(Node node, int index) {
        children[index] = node;
    }

    @Override
    public String toString() {
        return String.format("[Node] Value: %d", count);
    }
}
