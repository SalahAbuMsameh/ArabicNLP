package edu.nlp.arabic.util;

import edu.nlp.arabic.normalize.ArabicNormalizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.TreeSet;

/**
 * 
 * @author Salah Abu Msameh
 */
public class Utils {

    /**
     *
     * @return
     */
    public static Set<String> loadStopWords() {

        Set<String> stopWords = new TreeSet<String>();
        ArabicNormalizer normalizer = new ArabicNormalizer();

        BufferedReader br = new BufferedReader(new InputStreamReader(Utils.class.getResourceAsStream("/stop_words.txt")));
        String line = null;

        try {
            while ((line = br.readLine()) != null) {
                stopWords.add(normalizer.normalize(line).trim());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return stopWords;
    }
}
