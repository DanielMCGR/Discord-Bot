package codeWords;

import genshin.TextFileHandler;
import utils.Calculator;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class gets the words to be used on a
 * CodeWords game from a text file.
 *
 * @author Daniel Rocha
 * @version 1.0
 * @see TextFileHandler
 * @see CodeWords
 */
public class WordHandler {
    static final File DATA = new File("../Words.txt");
    static ArrayList<Integer> indexes = new ArrayList<>();

    /**
     * Gets a list of 25 words to be used on a CodeWords game
     *
     * @return the world list
     */
    public static ArrayList<String> getWords() {
        try {
            ArrayList<String> words = new ArrayList<>();
            ArrayList<String> wordList = new ArrayList<>();
            wordList.addAll(Arrays.asList(TextFileHandler.GetFileAsArray(DATA)));
            int count=0;
            while(count<25) {
                int random = Calculator.GetRandom(0,wordList.size());
                if(!indexes.contains(random)) {
                    indexes.add(random);
                    words.add(wordList.get(random));
                    count++;
                }
            }
            return words;
        } catch (Exception e) {
            return null;
        }
    }
}
