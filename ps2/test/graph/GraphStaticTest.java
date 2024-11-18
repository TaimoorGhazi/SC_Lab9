/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import java.util.Collections;

import org.junit.Test;


public class GraphStaticTest {

    // Testing strategy for static method `empty()`
    // - The graph should be empty when created.
    // - The graph should return an empty set of vertices.

    // This method is used to ensure assertions are enabled
    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    // Test that `Graph.empty()` creates an empty graph
    @Test
    public void testEmptyVerticesEmpty() {
        assertEquals("Expected empty() graph to have no vertices",
                Collections.emptySet(), Graph.empty().vertices());
    }

    // Additional test: Ensure the graph created by empty() is of the correct type
    @Test
    public void testEmptyGraphType() {
        Graph<String> graph = Graph.empty();
        assertTrue("Graph.empty() should return an instance of Graph", graph instanceof Graph);
    }

    // Additional test: Ensure that multiple calls to empty() return different instances
    @Test
    public void testMultipleEmptyGraphs() {
        Graph<String> graph1 = Graph.empty();
        Graph<String> graph2 = Graph.empty();

        assertNotSame("Each call to Graph.empty() should return a new instance", graph1, graph2);
        assertTrue(graph1.vertices().isEmpty());
        assertTrue(graph2.vertices().isEmpty());
    }
}
