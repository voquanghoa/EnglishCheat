package com.quanghoa.englishcheat;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        TranslateController translateController = new TranslateController();
        assertTrue(translateController.isWord("acv"));
        assertFalse(translateController.isWord("acs."));
        assertFalse(Character.isLetter('.'));
    }
}