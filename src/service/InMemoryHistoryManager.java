package service;

import model.Node;
import model.Task;

import java.util.*;


public class InMemoryHistoryManager implements HistoryManager {
    private CustomLinkedList historyList = new CustomLinkedList();

    @Override
    public void add(Task task) {
        Node<Task> newNode = new Node<>(task);
        if (historyList.checkDublicates(newNode)) {
            Node<Task> oldNode = historyList.nodeMap.get(task.getId());
            historyList.removeNode(oldNode);
        }
            historyList.linkLast(newNode);

    }

    @Override
    public void remove(int id) {
        if (historyList.nodeMap.containsKey(id))
            historyList.removeNode(historyList.nodeMap.get(id));
    }


    @Override
    public List<Task> getHistory() {
        return historyList.getTasks();
    }

    public class CustomLinkedList {
        private HashMap<Integer, Node> nodeMap;
        private Node firstNode;
        private Node lastNode;

        public CustomLinkedList() {
            this.nodeMap = new HashMap<>();
        }


        public void linkLast(Node<Task> node) {

            if (nodeMap.isEmpty()) {
                firstNode = node;
                lastNode = node;
            }
             else
                lastNode.setNext(node);

             node.setPrev(lastNode);
             lastNode = node;

            nodeMap.put(node.getData().getId(), node);
        }

        public ArrayList<Task> getTasks() {
            ArrayList<Task> resultList = new ArrayList<>();
            Node<Task> currentNode = firstNode;
            while (currentNode != null) {
                resultList.add(currentNode.getData());
                currentNode = currentNode.getNext();
            }


            return resultList;
        }

        public boolean checkDublicates(Node<Task> node) {
            return nodeMap.containsKey(node.getData().getId());
        }


        public void removeNode(Node<Task> node) {
            Node<Task> oldNode = nodeMap.get(node.getData().getId());
            if (!(oldNode.equals(firstNode)))
                oldNode.getPrev().setNext(oldNode.getNext());
            else
                firstNode = firstNode.getNext();
            if (!(oldNode.equals(lastNode)))
                oldNode.getNext().setPrev(oldNode.getPrev());
            else
                lastNode = lastNode.getPrev();

            oldNode.setPrev(null);
            oldNode.setNext(null);

            nodeMap.remove(node.getData().getId());
        }
    }
}
