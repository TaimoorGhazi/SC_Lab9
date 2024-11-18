/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import org.junit.Test;

import org.junit.Before;

import java.util.Map;

public class ConcreteEdgesGraphTest {
    
    private Graph<String> graph;

    // Set up a new empty graph before each test
    @Before
    public void setUp() {
        graph = new ConcreteEdgesGraph<>();
    }

    // Test that a new graph has no vertices
    @Test
    public void testInitialVerticesEmpty() {
        assertTrue("New graph should have no vertices", graph.vertices().isEmpty());
    }

    // Test adding a vertex to the graph
    @Test
    public void testAddVertex() {
        assertTrue("Vertex 'A' should be added successfully", graph.add("A"));
        assertTrue("Graph should contain vertex 'A'", graph.vertices().contains("A"));
    }

    // Test adding a duplicate vertex to the graph
    @Test
    public void testAddDuplicateVertex() {
        graph.add("A");
        assertFalse("Adding duplicate vertex 'A' should return false", graph.add("A"));
    }

    // Test adding an edge and ensuring it is added with the correct weight
    @Test
    public void testSetEdge() {
        graph.add("A");
        graph.add("B");
        int previousWeight = graph.set("A", "B", 10);
        
        assertEquals("Expected previous edge weight to be 0", 0, previousWeight);
        Map<String, Integer> targets = graph.targets("A");
        assertTrue("Graph should have a target 'B' from 'A'", targets.containsKey("B"));
        assertEquals("Edge weight should be 10", Integer.valueOf(10), targets.get("B"));
    }

    // Test removing a vertex and ensuring it no longer exists
    @Test
    public void testRemoveVertex() {
        graph.add("A");
        graph.add("B");
        graph.set("A", "B", 5);
        
        assertTrue("Removing vertex 'A' should return true", graph.remove("A"));
        assertFalse("Graph should no longer contain vertex 'A'", graph.vertices().contains("A"));
        
        Map<String, Integer> targets = graph.targets("A");
        assertTrue("Vertex 'A' should have no outgoing edges", targets.isEmpty());
    }

    // Test sources and targets for a directed edge
    @Test
    public void testSourcesAndTargets() {
        graph.add("A");
        graph.add("B");
        graph.set("A", "B", 5);
        
        Map<String, Integer> sources = graph.sources("B");
        assertTrue("Graph should have 'A' as a source for 'B'", sources.containsKey("A"));
        assertEquals("Edge weight from 'A' to 'B' should be 5", Integer.valueOf(5), sources.get("A"));
        
        Map<String, Integer> targets = graph.targets("A");
        assertTrue("Graph should have 'B' as a target for 'A'", targets.containsKey("B"));
        assertEquals("Edge weight from 'A' to 'B' should be 5", Integer.valueOf(5), targets.get("B"));
    }
}
