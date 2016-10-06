package com.company.redacted;

import java.util.HashMap;

/**
 * There are two types of problems the interviewer could have been asking, and I don't think I was able to
 * adequately explain to him why I was confused. Here are the problems:
 *
 * Problem 1:
 * Given a word (x), what is the probability, out of all the occurrences of x in the input strings,
 * that the word (y) is the next word.
 *
 * Problem 2:
 * Given a string (x), what is the probability of the next word being (y).
 *
 * What was causing confusion was I thought the interviewer was asking for 2, rather than 1.
 *
 * In any case, below is the solution to problem 1, as I have described. The time to build the graph is
 * O(n), and the time for lookup is O(1).
 *
 * The following will explain the solution to problem 2:
 *
 * The solution to problem 2 would be implemented as a simple trie, where each "node" is a word.
 * However, the lookup for a specific word would have to be O(log n), where n is the number of letters in
 * the word. The way to make this O(1), you would need  a hash of strings of every length, to
 * Node with word y. This would take an amazing amount of space though. You could make the space smaller by storing
 * a hash of the string rather than the actually string. The space complexity of this would be O(n), where
 * n is the number of words(if you use the hash methodology).
 */
public class WordMapper {

    HashMap<String, Node> map = new HashMap<>();

    public WordMapper(String[] input) {
        createMap(input);
    }

    private void createMap(String[] input) {
        for (String s : input) {
            Node previousNode = null;
            String[] words = splitStringIntoWords(s); //I like A
            for(int i = 0; i < words.length; i++) {
                String currentWord = words[i];
                Node currentNode = map.get(currentWord);
                if (currentNode == null) { // If this word doesn't exist in our graph, create it
                    currentNode = new Node(currentWord);
                    map.put(currentWord, currentNode);
                }

                addMappingFromPreviousToCurrentNode(previousNode, currentNode);

                previousNode = map.get(currentWord);
            }
        }
    }

    /**
     * Given a word (x), what is the probability, out of all the occurrences of x in the input strings,
     * that the word (y) is the next word.
     *
     */
    public double getProbability(String x, String y) {
        // If the first word doesn't exist in our graph, return 0
        Node nodeX = map.get(x);
        if (nodeX == null) {
            return 0;
        }

        // If there are no adjacent words, return 0
        int totalNumberOfStrings = nodeX.totalAdjacentCount;
        if (totalNumberOfStrings == 0) {
            return 0;
        }
        // If the specific adjacent word we are looking for doesn't exist, return 0
        Integer countOfAdjacentString = nodeX.adjacentWordsToCount.get(y);
        if (countOfAdjacentString == null) {
            return 0;
        }
        return (double) countOfAdjacentString / (double) totalNumberOfStrings;

    }

    /**
     * Add the current node's name to the previous node's name-map. If the name already exists in
     * the previous node name-map, increment the number of occurrences by 1. Otherwise, add it to the map
     * and initialize to 1.
     *
     * Also, increase the previous node's total seen word count.
     */
    private void addMappingFromPreviousToCurrentNode(Node previousNode, Node currentNode) {
        if (previousNode != null) {
            if (previousNode.adjacentWordsToCount.get(currentNode.name) != null) {
                int nextValueCount = previousNode.adjacentWordsToCount.get(currentNode.name) + 1;
                previousNode.adjacentWordsToCount.put(currentNode.name, nextValueCount);
            } else {
                previousNode.adjacentWordsToCount.put(currentNode.name, 1);
            }
            previousNode.incrementTotalCount();
        }
    }


    private String[] splitStringIntoWords(String words) {
        return words.split(" ");
    }

    private class Node {
        private String name;
        private HashMap<String, Integer> adjacentWordsToCount = new HashMap<>();
        private int totalAdjacentCount;

        private Node(String name) {
            this.name = name;
            totalAdjacentCount = 0;
        }

        private void incrementTotalCount() {
            totalAdjacentCount++;
        }
    }
}
