package edu.nlp.arabic.util;

import java.io.BufferedReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.TreeSet;

import edu.nlp.arabic.normalize.ArabicNormalizer;

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

        URL url = Utils.class.getClassLoader().getResource("stop_words.txt");
        BufferedReader br = null;
        String line = null;

        try {
            br = Files.newBufferedReader(Paths.get(url.toURI()), Charset.forName("UTF-8"));

            while ((line = br.readLine()) != null) {
                stopWords.add(normalizer.normalize(line).trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stopWords;
    }
}
