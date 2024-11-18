/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.Map;
import java.util.Set;

/**
 * A mutable weighted directed graph with labeled vertices.
 * Vertices have distinct labels of an immutable type {@code L} when compared
 * using the {@link Object#equals(Object) equals} method.
 * Edges are directed and have a positive weight of type {@code int}.
 * 
 * <p>PS2 instructions: this is a required ADT interface.
 * You MUST NOT change the specifications or add additional methods.
 * 
 * @param <L> type of vertex labels in this graph, must be immutable
 */
public interface Graph<L> {
    
    public static <L> Graph<L> empty() {
        return new ConcreteEdgesGraph<>();
    }
    
    public boolean add(L vertex);
    
    public int set(L source, L target, int weight);
    
    public boolean remove(L vertex);
    
    public Set<L> vertices();
    
    public Map<L, Integer> sources(L target);
    
    public Map<L, Integer> targets(L source);
}
