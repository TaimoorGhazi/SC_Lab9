package poet;

import graph.Graph;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class GraphPoetTest {

    private static final String TEST_CORPUS_PATH = "src/poet/test-corpus.txt"; // Replace with actual corpus file path

    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false;
    }

    @Test
    public void testEmptyInput() throws IOException {
        GraphPoet poet = new GraphPoet(new File(TEST_CORPUS_PATH));
        assertEquals("", poet.poem(""));
    }

    @Test
    public void testPoemWithoutBridgeWords() throws IOException {
        GraphPoet poet = new GraphPoet(new File(TEST_CORPUS_PATH));
        assertEquals("hello world", poet.poem("hello world"));
    }

    @Test
    public void testPoemWithBridgeWords() throws IOException {
        GraphPoet poet = new GraphPoet(new File(TEST_CORPUS_PATH));
        assertEquals("seek to explore the new and exciting synergies", poet.poem("seek to explore new and exciting synergies"));
    }

    @Test
    public void testEmptyCorpus() throws IOException {
        File emptyCorpus = new File("src/poet/empty-corpus.txt");
        GraphPoet poet = new GraphPoet(emptyCorpus);
        assertEquals("hello world", poet.poem("hello world"));
    }

    @Test
    public void testBridgeWordOrder() throws IOException {
        GraphPoet poet = new GraphPoet(new File(TEST_CORPUS_PATH));
        assertEquals("explore new horizons", poet.poem("explore horizons"));
    }
}
