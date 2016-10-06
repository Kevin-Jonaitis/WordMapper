package com.company.redacted;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by kevin.jonaitis on 10/5/16.
 */
public class WordMapperTest {

    @Test
    public void testBasicString() {
        String[] input = new String[3];
        input[0] = "I like A";
        input[1] = "I like B";
        input[2] = "I like C";
        WordMapper mapper = new WordMapper(input);
        assertEquals(1.0, mapper.getProbability("I", "like"));
        assertEquals(0.333, mapper.getProbability("like", "A"), 0.001);
        assertEquals(0.333, mapper.getProbability("like", "B"), 0.001);
        assertEquals(0.333, mapper.getProbability("like", "C"), 0.001);
        assertEquals(0, mapper.getProbability("I", "C"), 0);
    }

    /**
     * In this case, even though there are two likes in different places in the string,
     * we're creating a graph of previous to next words.
     *
     * So like maps to A, B, B, and C. So the probability it maps to B is 50%.
     */
    @Test
    public void testWordInMultiplePositionsInString() {
        String[] input = new String[3];
        input[0] = "I like A like B";
        input[1] = "I like B";
        input[2] = "I like C";
        WordMapper mapper = new WordMapper(input);
        assertEquals(0.25, mapper.getProbability("like", "A"), 0.001);
        assertEquals(0.50, mapper.getProbability("like", "B"), 0.001);
        assertEquals(0.25, mapper.getProbability("like", "C"), 0.001);

    }

    @Test
    public void emptyInput() {
        String[] input = new String[1];
        input[0] = "";
        WordMapper mapper = new WordMapper(input);
        assertEquals(0, mapper.getProbability("anything", "something"), 0);
    }

    @Test
    public void isolatedWord() {
        String[] input = new String[3];
        input[0] = "A";
        input[1] = "B";
        input[2] = "C";
        WordMapper mapper = new WordMapper(input);
        assertEquals(0, mapper.getProbability("A", "B"), 0);
        assertEquals(0, mapper.getProbability("A", "C"), 0);
    }
}
