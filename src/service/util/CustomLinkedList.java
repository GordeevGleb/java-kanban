package service.util;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class CustomLinkedList {
    private HashMap<Integer, Node<Task>> nodeMap;
    private Node firstNode;
    private Node lastNode;

    public CustomLinkedList() {
        this.nodeMap = new HashMap<>();
    }


    public void linkLast(Task task) {
        Node<Task> node = new Node<>(task);
        if (isExist(task)) {
            removeNode(task);
        }
        if (nodeMap.isEmpty()) {
            firstNode = node;
            lastNode = node;
        } else {
            lastNode.next = node;
            node.prev = lastNode;
            lastNode = node;
        }
        nodeMap.put(node.data.getId(), node);
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> resultList = new ArrayList<>();
        Node<Task> currentNode = firstNode;
        while (currentNode != null) {
            resultList.add(currentNode.getData());
            currentNode = currentNode.next;
        }


        return resultList;
    }

    public boolean isExist(Task task) {
        return nodeMap.containsKey(task.getId());
    }


    public void removeNode(Task task) {
        Node<Task> oldNode = nodeMap.get(task.getId());
        if (oldNode != null) {
            if (oldNode.prev != null) {
                oldNode.prev.next = oldNode.next;
            } else {
                firstNode = oldNode.next;
            }
            if (oldNode.next != null) {
                oldNode.next.prev = oldNode.prev;
            } else {
                lastNode = oldNode.prev;
            }
            nodeMap.remove(oldNode);
        }
    }
    public void removeAllTasks() {
        Node<Task> node = firstNode;
        while (node != null) {
            if (node.data.getClass().equals(Task.class)) {
                removeNode(node.data);
            }
            node = node.next;
        }
    }
    public void removeAllSubTasks() {
        Node<Task> node = firstNode;
        while (node != null) {
            if (node.data.getClass().equals(SubTask.class)) {
                removeNode(node.data);
            }
            node = node.next;
        }
    }
    public void removeAllEpic() {
        Node<Task> node = firstNode;
        while (node != null) {
            if (node.data.getClass().equals(Epic.class)) {
                removeNode(node.data);
            }
            node = node.next;
        }
    }

    public class Node<Task> {
        private Task data;
        private Node prev;
        private Node next;


        public Node(Task data) {
            this.data = data;
        }

        public Task getData() {
            return data;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Node)) return false;
            Node<?> node = (Node<?>) o;
            return Objects.equals(data, node.data);
        }

        @Override
        public int hashCode() {
            return Objects.hash(data);
        }
    }
}
