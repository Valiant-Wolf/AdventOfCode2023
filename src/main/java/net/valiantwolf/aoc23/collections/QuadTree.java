package net.valiantwolf.aoc23.collections;

import net.valiantwolf.aoc23.math.Rect;
import net.valiantwolf.aoc23.math.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class QuadTree<T> {

    private final int nodeCapacity;

    private Node root;

    public QuadTree(int nodeCapacity, int width) {
        this.nodeCapacity = nodeCapacity;
        root = new Node(new Rect(Vector.ORIGIN, Vector.IDENTITY.multiply(width)));
    }

    public void put(Vector key, T value) {
        boolean left, right, up, down;

        left = key.x() < root.rect.lowerBound().x();
        right = key.x() >= root.rect.upperBound().x();
        down = key.y() < root.rect.lowerBound().y();
        up = key.y() >= root.rect.upperBound().y();

        if (!(left || right || up || down)) {
            root.put(key, value);
        } else {
            embiggen(left, right, up, down);
            root.put(key, value);
        }
    }

    private void embiggen(boolean left, boolean right, boolean up, boolean down) {
        Vector old = root.centre;
        int radius = root.radius * 2;

        Vector upper = root.rect.upperBound();
        Vector lower = root.rect.lowerBound();
        Vector rightShift = new Vector(radius, 0);
        Vector upShift = new Vector(0, radius);

        Vector centre;
        if (right && !down) {
            upper = upper.add(rightShift).add(upShift);
        } else if (up && !right) {
            upper = upper.add(upShift);
            lower = lower.subtract(rightShift);
        } else if (left && !up) {
            lower = lower.subtract(rightShift).subtract(upShift);
        } else {
            upper = upper.add(rightShift);
            lower = lower.subtract(upShift);
        }

        Rect bounds = new Rect(lower, upper);

        if (root.size <= nodeCapacity) {
            root.setRect(bounds);
            return;
        }

        Node newRoot = new Node(bounds);
        newRoot.divide();
        newRoot.getChildReference(old).set(root);
        newRoot.size = root.size;
        root = newRoot;
    }

    public Stream<Entry<T>> search(Rect rect) {
        return doSearch(root, rect).map(o -> new Entry<>(o.getKey(), o.getValue()));
    }

    private Stream<Map.Entry<Vector, T>> doSearch(Node node, Rect rect) {
        if (node.isLeaf()) {
            return node.stream().filter(o -> rect.contains(o.getKey()));
        }

        if (rect.contains(node.rect)) {
            return node.stream();
        }

        return node.streamChildren()
                   .filter(o -> rect.intersects(o.rect))
                   .flatMap(o -> doSearch(o, rect));
    }

    private class Node {

        private final AtomicReference<Node> childTopLeft = new AtomicReference<>();
        private final AtomicReference<Node> childTopRight = new AtomicReference<>();
        private final AtomicReference<Node> childBotLeft = new AtomicReference<>();
        private final AtomicReference<Node> childBotRight = new AtomicReference<>();

        private Rect rect;
        private Vector centre;
        private int radius;

        private int size = 0;
        private Map<Vector, T> entries = new HashMap<>(nodeCapacity);

        private Node(Rect rect) {
            setRect(rect);
        }

        private void setRect(Rect rect) {
            this.rect = rect;
            radius = (rect.upperBound().x() - rect.lowerBound().x()) / 2;
            centre = rect.lowerBound().add(new Vector(radius, radius));
        }

        private boolean isLeaf() {
            return size <= nodeCapacity;
        }

        private boolean put(Vector key, T value) {
            boolean grow;

            if (isLeaf()) {
                grow = entries.put(key, value) == null;
                if (entries.size() > nodeCapacity) {
                    divide();
                }
            } else {
                grow = getChild(key).put(key, value);
            }

            if (grow) {
                size++;
            }

            return grow;
        }

        private void divide() {
            Vector shift = new Vector(radius, 0);

            childTopRight.setPlain(new Node(new Rect(centre, rect.upperBound())));
            childTopLeft.setPlain(new Node(new Rect(centre.subtract(shift), rect.upperBound().subtract(shift))));
            childBotLeft.setPlain(new Node(new Rect(rect.lowerBound(), centre)));
            childBotRight.setPlain(new Node(new Rect(rect.lowerBound().add(shift), centre.add(shift))));

            entries.forEach((k, v) -> getChild(k).put(k, v));
            entries = null;
        }

        private Node getChild(Vector target) {
            return getChildReference(target).getPlain();
        }

        private AtomicReference<Node> getChildReference(Vector target) {
            return target.x() < centre.x()
                   ? target.y() < centre.y()
                     ? childBotLeft
                     : childTopLeft
                   : target.y() < centre.y()
                     ? childBotRight
                     : childTopRight;
        }

        private Stream<Map.Entry<Vector, T>> stream() {
            if (isLeaf()) {
                return entries.entrySet().stream();
            }

            return streamChildren().flatMap(Node::stream);
        }

        private Stream<Node> streamChildren() {
            return Stream.of(childTopLeft, childTopRight, childBotLeft, childBotRight)
                         .map(AtomicReference::getPlain);
        }
    }

    public record Entry<T>(Vector key, T value) {

    }

}
