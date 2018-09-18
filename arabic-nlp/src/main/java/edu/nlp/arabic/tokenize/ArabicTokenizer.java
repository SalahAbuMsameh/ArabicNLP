package edu.nlp.arabic.tokenize;

import java.util.Set;
import java.util.TreeSet;

import edu.nlp.arabic.normalize.ArabicNormalizer;
import edu.nlp.arabic.util.Utils;

/**
 * 
 * @author Salah Abu Msameh
 */
public class ArabicTokenizer {
    
    private static final Set<String> STOP_WORDS;
    
    static {
        STOP_WORDS = Utils.loadStopWords();
    }
    
    /**
     * 
     * @param text
     * @return
     */
    public Set<String> tokenize(String text) {
        
        text = new ArabicNormalizer().normalize(text);
        Set<String> normalizedTokens = new TreeSet<String>();
        
        String[] tokens = text.split("\\s+");
        
        for(int i = 0; i < tokens.length; i++) {
            
            String token = tokens[i];
            
            if(token == null || token.length() == 0) {
                continue;
            }
            
            if(STOP_WORDS.contains(token)) {}
            else {
                normalizedTokens.add(token);
            }
        }
        
        return normalizedTokens;
    }
}
