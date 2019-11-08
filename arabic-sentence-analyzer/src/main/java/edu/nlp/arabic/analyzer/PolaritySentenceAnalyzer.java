package edu.nlp.arabic.analyzer;

import edu.nlp.arabic.tokenize.ArabicTokenizer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * This analyzer analyzes an arabic sentence and finds the sentence polarity (good, bad or neutral).
 *
 * @author Salah Abu Msameh
 */
public class PolaritySentenceAnalyzer {

    private static final String POSITIVE = "Pos";
    private static final String NEGATIVE = "Neg";
    private static final String NEUTRAL = "Neut";

    private Map<String, String> oneLexiconTerms;
    private Map<String, String> twoLexiconTerms;
    private Map<String, String> threeLexiconTerms;
    private Map<String, String> fourLexiconTerms;

    private StringBuilder unListedTerms = new StringBuilder();
    private ArabicTokenizer tokenizer = new ArabicTokenizer();

    /**
     *
     * @param oneLexiconTerms
     * @param twoLexiconTerms
     * @param threeLexiconTerms
     * @param fourLexiconTerms
     */
    public PolaritySentenceAnalyzer(Map<String, String> oneLexiconTerms,
                                    Map<String, String> twoLexiconTerms,
                                    Map<String, String> threeLexiconTerms,
                                    Map<String, String> fourLexiconTerms) {
        this.oneLexiconTerms = oneLexiconTerms;
        this.twoLexiconTerms = twoLexiconTerms;
        this.threeLexiconTerms = threeLexiconTerms;
        this.fourLexiconTerms = fourLexiconTerms;
    }

    /**
     * analyze the given sentence and finds the polarity.
     *
     * @param sentence
     */
    public String analyze(String sentence) {

        Map<String, Integer> polarities = new HashMap<String, Integer>();

        //1. remove 4th terms
        sentence = calculatePolarities(sentence, fourLexiconTerms, polarities);
        sentence = sentence.length() > 0 ?
                calculatePolarities(sentence, threeLexiconTerms, polarities) : "";
        sentence = sentence.length() > 0 ?
                calculatePolarities(sentence, twoLexiconTerms, polarities) : "";

        if(sentence.length() > 0) {
            tokenizer.tokenize(sentence).forEach(token -> {

                if(oneLexiconTerms.containsKey(token)) {
                    increment(polarities, oneLexiconTerms.get(token));
                } else {
                    unListedTerms.append(token).append("\t\r");
                }
            });
        }

        int positiveCount = Optional.ofNullable(polarities.get(POSITIVE)).orElse(0);
        int negativeCount = Optional.ofNullable(polarities.get(NEGATIVE)).orElse(0);

        if(positiveCount > negativeCount) {
            return POSITIVE;
        } else if(negativeCount > positiveCount) {
            return NEGATIVE;
        } else {
            return NEUTRAL;
        }
    }

    /**
     * calculate polarity by removing lexicon terms from the sentence.
     *
     * @param sentence
     * @param lexiconTerms
     * @param polarities
     * @return
     */
    private String calculatePolarities(String sentence, Map<String, String> lexiconTerms, Map<String, Integer> polarities) {

        if(sentence.length() == 0) {
            return sentence;
        }

        for(String term : lexiconTerms.keySet()) {
            if(sentence.contains(term)) {
                sentence = sentence.replace(term, "");
                increment(polarities, lexiconTerms.get(term));
            }
        }

        return sentence;
    }

    /**
     * increment given polarity number
     *
     * @param polarities
     * @param polarity
     */
    private void increment(final Map<String, Integer> polarities, final String polarity) {

        if(polarities.containsKey(polarity)) {
            polarities.put(polarity, polarities.get(polarity) + 1);
        } else {
            polarities.put(polarity, 1);
        }
    }

    public StringBuilder getUnListedTerms() {
        return unListedTerms;
    }
}
