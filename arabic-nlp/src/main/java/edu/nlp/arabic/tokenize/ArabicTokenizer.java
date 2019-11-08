package edu.nlp.arabic.tokenize;

import edu.nlp.arabic.normalize.ArabicNormalizer;
import edu.nlp.arabic.util.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Arabic text tokenizer.
 *
 * @author Salah Abu Msameh
 */
public class ArabicTokenizer {
    
    private static final Set<String> STOP_WORDS;
    
    static {
        STOP_WORDS = Utils.loadStopWords();
    }

    private ArabicNormalizer normalizer = new ArabicNormalizer();
    
    /**
     * tokenize given text with no duplicate.
     *
     * @param text to be tokenized
     * @return return set of tokens
     */
    public Set<String> tokenizeRemoveDuplicate(String text) {
        
        Set<String> normalizedTokens = new TreeSet<String>();
        tokenize(text, normalizedTokens);
        return normalizedTokens;
    }
    
    /**
     * tokenize given text with keeping the duplicate words.
     *
     * @param text to be tokenized
     * @return return list of tokens
     */
    public List<String> tokenize(String text) {

        List<String> normalizedTokens = new ArrayList<String>();
        tokenize(text, normalizedTokens);
        return normalizedTokens;
    }

    /**
     * tokenize given text and populate the token in the given collection.
     *
     * @param text to be tokenized
     * @param tokensCollection collection to populate tokens into
     */
    public void tokenize(String text, Collection<String> tokensCollection) {

        text = normalizer.normalize(text);
        String[] tokens = text.split("\\s+");

        for(int i = 0; i < tokens.length; i++) {

            String token = tokens[i];

            if(token == null || token.length() == 0 || token.length() == 1) {
                continue;
            }

            if(STOP_WORDS.contains(token)) {}
            else {
                tokensCollection.add(token);
            }
        }
    }
}
