package edu.nlp.arabic.normalize;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Arabic word normalizer.
 * The normalizer search for non Arabic char's and remove them from the text.
 *
 * @author Salah Abu Msameh
 */
public class ArabicNormalizer {
    
    private static final String EMOJI_RANGE_REGEX =
            "[\uD83C\uDF00-\uD83D\uDDFF]|[\uD83D\uDE00-\uD83D\uDE4F]|[\uD83D\uDE80-\uD83D\uDEFF]|[\u2600-\u26FF]|[\u2700-\u27BF]";
    private static final Pattern PATTERN = Pattern.compile(EMOJI_RANGE_REGEX);

	/**
	 * normalize given arabic text.
     *
	 * @param text
	 * @return
	 */
	public String normalize(String text) {
		
		//Remove honorific sign
        text = text.replaceAll("\u0610", "");//ARABIC SIGN SALLALLAHOU ALAYHE WA SALLAM
        text = text.replaceAll("\u0611", "");//ARABIC SIGN ALAYHE ASSALLAM
        text = text.replaceAll("\u0612", "");//ARABIC SIGN RAHMATULLAH ALAYHE
        text = text.replaceAll("\u0613", "");//ARABIC SIGN RADI ALLAHOU ANHU
        text = text.replaceAll("\u0614", "");//ARABIC SIGN TAKHALLUS
        
        //Remove koranic anotation
        text = text.replaceAll("\u0615", "");//ARABIC SMALL HIGH TAH
        text = text.replaceAll("\u0616", "");//ARABIC SMALL HIGH LIGATURE ALEF WITH LAM WITH YEH
        text = text.replaceAll("\u0617", "");//ARABIC SMALL HIGH ZAIN
        text = text.replaceAll("\u0618", "");//ARABIC SMALL FATHA
        text = text.replaceAll("\u0619", "");//ARABIC SMALL DAMMA
        text = text.replaceAll("\u061A", "");//ARABIC SMALL KASRA
        text = text.replaceAll("\u06D6", "");//ARABIC SMALL HIGH LIGATURE SAD WITH LAM WITH ALEF MAKSURA
        text = text.replaceAll("\u06D7", "");//ARABIC SMALL HIGH LIGATURE QAF WITH LAM WITH ALEF MAKSURA
        text = text.replaceAll("\u06D8", "");//ARABIC SMALL HIGH MEEM INITIAL FORM
        text = text.replaceAll("\u06D9", "");//ARABIC SMALL HIGH LAM ALEF
        text = text.replaceAll("\u06DA", "");//ARABIC SMALL HIGH JEEM
        text = text.replaceAll("\u06DB", "");//ARABIC SMALL HIGH THREE DOTS
        text = text.replaceAll("\u06DC", "");//ARABIC SMALL HIGH SEEN
        text = text.replaceAll("\u06DD", "");//ARABIC END OF AYAH
        text = text.replaceAll("\u06DE", "");//ARABIC START OF RUB EL HIZB
        text = text.replaceAll("\u06DF", "");//ARABIC SMALL HIGH ROUNDED ZERO
        text = text.replaceAll("\u06E0", "");//ARABIC SMALL HIGH UPRIGHT RECTANGULAR ZERO
        text = text.replaceAll("\u06E1", "");//ARABIC SMALL HIGH DOTLESS HEAD OF KHAH
        text = text.replaceAll("\u06E2", "");//ARABIC SMALL HIGH MEEM ISOLATED FORM
        text = text.replaceAll("\u06E3", "");//ARABIC SMALL LOW SEEN
        text = text.replaceAll("\u06E4", "");//ARABIC SMALL HIGH MADDA
        text = text.replaceAll("\u06E5", "");//ARABIC SMALL WAW
        text = text.replaceAll("\u06E6", "");//ARABIC SMALL YEH
        text = text.replaceAll("\u06E7", "");//ARABIC SMALL HIGH YEH
        text = text.replaceAll("\u06E8", "");//ARABIC SMALL HIGH NOON
        text = text.replaceAll("\u06E9", "");//ARABIC PLACE OF SAJDAH
        text = text.replaceAll("\u06EA", "");//ARABIC EMPTY CENTRE LOW STOP
        text = text.replaceAll("\u06EB", "");//ARABIC EMPTY CENTRE HIGH STOP
        text = text.replaceAll("\u06EC", "");//ARABIC ROUNDED HIGH STOP WITH FILLED CENTRE
        text = text.replaceAll("\u06ED", "");//ARABIC SMALL LOW MEEM
        
        //Remove tatweel
        text = text.replaceAll("\u0640", "");
        
        //Remove tashkeel
        text = text.replaceAll("\u064B", "");//ARABIC FATHATAN
        text = text.replaceAll("\u064C", "");//ARABIC DAMMATAN
        text = text.replaceAll("\u064D", "");//ARABIC KASRATAN
        text = text.replaceAll("\u064E", "");//ARABIC FATHA
        text = text.replaceAll("\u064F", "");//ARABIC DAMMA
        text = text.replaceAll("\u0650", "");//ARABIC KASRA
        text = text.replaceAll("\u0651", "");//ARABIC SHADDA
        text = text.replaceAll("\u0652", "");//ARABIC SUKUN
        text = text.replaceAll("\u0653", "");//ARABIC MADDAH ABOVE
        text = text.replaceAll("\u0654", "");//ARABIC HAMZA ABOVE
        text = text.replaceAll("\u0655", "");//ARABIC HAMZA BELOW
        text = text.replaceAll("\u0656", "");//ARABIC SUBSCRIPT ALEF
        text = text.replaceAll("\u0657", "");//ARABIC INVERTED DAMMA
        text = text.replaceAll("\u0658", "");//ARABIC MARK NOON GHUNNA
        text = text.replaceAll("\u0659", "");//ARABIC ZWARAKAY
        text = text.replaceAll("\u065A", "");//ARABIC VOWEL SIGN SMALL V ABOVE
        text = text.replaceAll("\u065B", "");//ARABIC VOWEL SIGN INVERTED SMALL V ABOVE
        text = text.replaceAll("\u065C", "");//ARABIC VOWEL SIGN DOT BELOW
        text = text.replaceAll("\u065D", "");//ARABIC REVERSED DAMMA
        text = text.replaceAll("\u065E", "");//ARABIC FATHA WITH TWO DOTS
        text = text.replaceAll("\u065F", "");//ARABIC WAVY HAMZA BELOW
        text = text.replaceAll("\u0670", "");//ARABIC LETTER SUPERSCRIPT ALEF
        
        //Replace Waw Hamza Above by Waw
        text = text.replaceAll("\u0624", "\u0648");
        
        //Replace Ta Marbuta by Ha
        text = text.replaceAll("\u0629", "\u0647");
        
        //Replace Ya
        // and Ya Hamza Above by Alif Maksura
        //text = text.replaceAll("\u064A", "\u0649");
        text = text.replaceAll("\u0626", "\u0649");
        
        // Replace Alifs with Hamza Above/Below
        // and with Madda Above by Alif
        text = text.replaceAll("\u0622", "\u0627");
        text = text.replaceAll("\u0623", "\u0627");
        text = text.replaceAll("\u0625", "\u0627");
        
        text = text.replaceAll("[a-zA-Z]", " ");
        
        text = text.replaceAll("⭐|…|▪|�|\\!|\\@|\\#|\\$|\\%|\\^|\\&|\\*|\\)|\\(|\\_|\\+|\\~}"
        		+ "|\\{|�|�|�|,|\\.|\\\\|\\>|\\<|�|�|�|�|�|/|\\*|\\-|\\+|!|@|#|$|%|^|&|\\*|\\(|\\)|-|_|=|\\}|\\{|\\'|\\\\|\\;|\\:|\"", " ");
        text = text.replaceAll("\"|\u060C|\u061F", " ");
    	text = text.replaceAll("[0-9]", " ");
    	text = text.replaceAll("[\u0661-\u0669]", " ");
    	text = text.replaceAll("\u0660|\u2022|\u02da|\u00b0|\u10e6|\ue20c|\ud83e\udd14|\ud83e\udd19|\ud83e\udd23|\ud83e\udd26"
    			+ "|\ud83c\uddf8\ud83c\udde6|\u00ab|\u00bb|\uffe6|\u00a3|\u00d7|\u066a\u061c|\u300a|\u201c|\u2019|\u2013"
    			+ "|\u061b", " ");
    	text = text.replaceAll("\\]|\\||\\[|\\?", " ");
        //remove emojies and return results
        return eraseEmojis(text);
    }
        
     /**
      * Remove emojis for the given text.
      *
      * @param text
      * @return 
      */
    public String eraseEmojis(String text) {
        
        Matcher matcher = PATTERN.matcher(text);
        StringBuffer sb = new StringBuffer();
        
        while (matcher.find()) {
            matcher.appendReplacement(sb, " ");
        }
        
        matcher.appendTail(sb);
        return sb.toString();
    }
}
