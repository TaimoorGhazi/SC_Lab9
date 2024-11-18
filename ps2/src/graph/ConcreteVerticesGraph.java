/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * An implementation of Graph.
 * 
 * <p>PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteVerticesGraph<L> implements Graph<L> {
    private final List<Vertex<L>> vertices = new ArrayList<>();
    
    public ConcreteVerticesGraph() {
    }

    private void checkRep() {        
        assert vertices().size() == vertices.size();
    }
    
    private int indexInVertices(L label) {
        for (int i = 0; i < vertices.size(); i++) {
            if (vertices.get(i).getLabel().equals(label)) {
                return i;
            }
        }
        return -1;
    }

    @Override 
    public boolean add(L vertex) {        
        if (vertices().contains(vertex)) {
            return false;
        }
        Vertex<L> vertexObj = new Vertex<>(vertex);    
        boolean vertexAdded = vertices.add(vertexObj);
        checkRep();
        return vertexAdded;
    }

    @Override 
    public int set(L source, L target, int weight) {
        assert source != target;
        assert weight >= 0;

        final Vertex<L> sourceVertex;
        final Vertex<L> targetVertex;

        Set<L> verticeLabels = vertices();
        if (verticeLabels.contains(source)) {
            int sourceIndex = indexInVertices(source);
            sourceVertex = vertices.get(sourceIndex);
        } else {
            sourceVertex = new Vertex<>(source);
            vertices.add(sourceVertex);
        }

        if (verticeLabels.contains(target)) {
            int targetIndex = indexInVertices(target);
            targetVertex = vertices.get(targetIndex);
        } else {
            targetVertex = new Vertex<>(target);
            vertices.add(targetVertex);
        }

        int sourcePrevWeight = sourceVertex.setTarget(target, weight);
        int targetPrevWeight = targetVertex.setSource(source, weight);
        assert sourcePrevWeight == targetPrevWeight;

        checkRep();
        return sourcePrevWeight;
    }

    @Override 
    public boolean remove(L vertex) {
        if (!vertices().contains(vertex)) {
            return false;
        }
        int vertexIndex = indexInVertices(vertex);
        assert vertexIndex != -1;
        final Vertex<L> removedVertex = vertices.remove(vertexIndex);
        assert removedVertex.getLabel() == vertex;

        for (Vertex<L> v : vertices) {
            v.remove(vertex);
        }
        return removedVertex != null;
    }

    @Override 
    public Set<L> vertices() {
        return vertices.stream()
                .map(Vertex::getLabel)
                .collect(Collectors.toSet());
    }

    @Override 
    public Map<L, Integer> sources(L target) {
        int targetIndex = indexInVertices(target);
        if (targetIndex < 0) {
            return Collections.emptyMap();
        }
        Vertex<L> targetVertex = vertices.get(targetIndex);
        return Collections.unmodifiableMap(targetVertex.getSources());
    }

    @Override 
    public Map<L, Integer> targets(L source) {
        int sourceIndex = indexInVertices(source);
        if (sourceIndex < 0) {
            return Collections.emptyMap();
        }
        Vertex<L> sourceVertex = vertices.get(sourceIndex);
        return Collections.unmodifiableMap(sourceVertex.getTargets());
    }

    @Override 
    public String toString() {
        return vertices.stream()
                .filter(vertex -> vertex.getTargets().size() > 0)
                .map(vertex -> vertex.getLabel().toString() + " -> " + vertex.getTargets())
                .collect(Collectors.joining("\n"));
    }
}

class Vertex<L> {
    private final L label;
    private final Map<L, Integer> sources = new HashMap<>();
    private final Map<L, Integer> targets = new HashMap<>();
    
    public Vertex(final L label) {
        this.label = label;        
    }

    private void checkRep() {
        final Set<L> sourceLabels = sources.keySet();
        final Set<L> targetLabels = targets.keySet();
        
        assert !sourceLabels.contains(this.label);
        assert !targetLabels.contains(this.label);
    }

    private void checkInputLabel(final L inputLabel) {
        assert inputLabel != null;
        assert inputLabel != this.label;
    }

    public L getLabel() {
        return this.label;
    }

    public boolean addSource(final L source, final int weight) {
        checkInputLabel(source);
        assert weight > 0;

        if (sources.putIfAbsent(source, weight) == null) {
            checkRep();
            return true;
        }
        return false;
    }

    public boolean addTarget(final L target, final int weight) {
        checkInputLabel(target);
        assert weight > 0;

        if (targets.putIfAbsent(target, weight) == null) {
            checkRep();
            return true;
        }
        return false;
    }

    public int remove(final L vertex) {
        checkInputLabel(vertex);
        int sourcePrevWeight = removeSource(vertex);
        int targetPrevWeight = removeTarget(vertex);

        if (sourcePrevWeight > 0 && targetPrevWeight > 0) {
            assert sourcePrevWeight == targetPrevWeight;
        }
        return sourcePrevWeight == 0 ? targetPrevWeight : sourcePrevWeight;
    }

    public int removeSource(final L source) {
        checkInputLabel(source);
        Integer previousWeight = sources.remove(source);
        checkRep();
        return previousWeight == null ? 0 : previousWeight;
    }

    public int removeTarget(final L target) {
        checkInputLabel(target);
        Integer previousWeight = targets.remove(target);
        checkRep();
        return previousWeight == null ? 0 : previousWeight;
    }

    public int setSource(final L source, final int weight) {
        checkInputLabel(source);
        assert weight >= 0;
        final int previousWeight;

        if (weight == 0) {
            previousWeight = removeSource(source); 
        } else if (addSource(source, weight) || sources.get(source) == (Integer) weight) {
            previousWeight = 0;
        } else {
            previousWeight = sources.replace(source, weight);
        }
        checkRep();
        return previousWeight;
    }

    public int setTarget(final L target, final int weight) {
        checkInputLabel(target);
        assert weight >= 0;
        final int previousWeight;

        if (weight == 0) {
            previousWeight = removeTarget(target);
        } else if (addTarget(target, weight) || targets.get(target) == (Integer) weight) {
            previousWeight = 0;
        } else {
            previousWeight = targets.replace(target, weight);
        }
        checkRep();
        return previousWeight;
    }

    public Map<L, Integer> getSources() {
        return Collections.unmodifiableMap(sources);
    }

    public Map<L, Integer> getTargets() {
        return Collections.unmodifiableMap(targets);
    }

    public boolean isTarget(final L vertex) {
        return targets.containsKey(vertex);
    }

    public boolean isSource(final L vertex) {
        return sources.containsKey(vertex);
    }

    @Override 
    public String toString() {
        return String.format(
                "%s -> %s \n" +
                "%s <- %s",
                this.label.toString(), this.targets,
                this.label.toString(), this.sources);
    }
}
