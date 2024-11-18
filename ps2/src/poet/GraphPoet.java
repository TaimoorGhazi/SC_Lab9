package poet;

import graph.Graph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * A graph-based poetry generator.
 */
public class GraphPoet {
    private final Graph<String> affinityGraph;
    private final List<String> corpusWords;

    /**
     * Constructs a GraphPoet object from a corpus file.
     *
     * @param corpus the input corpus file
     * @throws IOException if the file cannot be read
     */
    public GraphPoet(File corpus) throws IOException {
        this.corpusWords = loadWordsFromCorpus(corpus);
        this.affinityGraph = buildAffinityGraph(corpusWords);
        checkRep();
    }

    private void checkRep() {
        assert affinityGraph != null : "Graph cannot be null";
    }

    private List<String> loadWordsFromCorpus(File corpus) throws IOException {
        List<String> words = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(corpus))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.replaceAll("[^a-zA-Z0-9\\s]", ""); // Remove punctuation
                words.addAll(Arrays.asList(line.toLowerCase().split("\\s+")));
            }
        }
        return words;
    }

    private Graph<String> buildAffinityGraph(List<String> words) {
        Graph<String> graph = Graph.empty();
        for (int i = 0; i < words.size() - 1; i++) {
            String source = words.get(i);
            String target = words.get(i + 1);
            graph.add(source);
            graph.add(target);
            int currentWeight = graph.set(source, target, 0);
            graph.set(source, target, currentWeight + 1);
        }
        return graph;
    }

    public String poem(String input) {
        String[] inputWords = input.split("\\s+");
        StringBuilder poemBuilder = new StringBuilder();

        for (int i = 0; i < inputWords.length - 1; i++) {
            String word1 = inputWords[i].toLowerCase();
            String word2 = inputWords[i + 1].toLowerCase();

            // Append current word
            poemBuilder.append(inputWords[i]).append(" ");

            // Find the best bridge word
            String bridgeWord = findBestBridge(affinityGraph.targets(word1), affinityGraph.sources(word2));
            if (bridgeWord != null) {
                poemBuilder.append(bridgeWord).append(" ");
            }
        }

        // Append the last word in the input
        poemBuilder.append(inputWords[inputWords.length - 1]);
        checkRep();
        return poemBuilder.toString();
    }

    private String findBestBridge(Map<String, Integer> targets, Map<String, Integer> sources) {
        String bestBridge = null;
        int highestWeight = 0;

        for (String bridge : targets.keySet()) {
            if (sources.containsKey(bridge)) {
                int weight = targets.get(bridge) + sources.get(bridge);
                if (weight > highestWeight) {
                    highestWeight = weight;
                    bestBridge = bridge;
                }
            }
        }

        return bestBridge;
    }

    @Override
    public String toString() {
        return affinityGraph.toString();
    }
}
