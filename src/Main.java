import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Created by michael-home on 17.10.2016.
 */
public class Main {

    private static Integer amountOfHamEmails;
    private static Integer amountOfSpamEmails;
    private static HashSet<String> words = new HashSet<>();
    private static HashMap<String, Integer> wordCounterHam;
    private static HashMap<String, Integer> wordCounterSpam;
    private static HashMap<String, Word> spamProbability = new HashMap<>();

    public static void main(String[] args) {

        // ***********************************************************************************************************//
        // Learn
        // ***********************************************************************************************************//
        amountOfHamEmails = 100;
        amountOfSpamEmails = 100;
        wordCounterHam = Reader.readAndCountWordsOfEmails("ham-anlern", 100);
        wordCounterSpam = Reader.readAndCountWordsOfEmails("spam-anlern", 100);
//        wordCounterHam = Reader.readAndCountWordsOfEmails("custom/ham", 100);
//        wordCounterSpam = Reader.readAndCountWordsOfEmails("custom/spam", 100);

        // Put learned words into a single list (without probability value)
        for (Object o : wordCounterHam.entrySet()) {
            HashMap.Entry pair = (HashMap.Entry) o;
            words.add((String) pair.getKey());
        }

        for (Object o : wordCounterSpam.entrySet()) {
            HashMap.Entry pair = (HashMap.Entry) o;
            words.add((String) pair.getKey());
        }

        // loop through all words and store them in a HashMap with spam and ham appearance value
        // (amount of appearance / ammount of Emails)
        // if word  does not exist in ham or spam -> set a default value
        for (String word : words) {

            Word w = new Word(word);
            double countSpam;
            if(wordCounterSpam.get(word) == null){
                w.pS = 0.01;
            } else {
                countSpam = wordCounterSpam.get(word);
                // P( word | spam )
                w.pS = countSpam / amountOfSpamEmails;
            }

            double countHam;
            if(wordCounterHam.get(word) == null){
                w.pH = 0.01;
            } else {
                countHam = wordCounterHam.get(word);
                // P( word | ham )
                w.pH = countHam / amountOfHamEmails;
            }

            spamProbability.put(word, w);
        }

        // ***********************************************************************************************************//
        // Calibration
        // ***********************************************************************************************************//

        File[] files = new File("resources/spam-kallibrierung").listFiles();
        // File[] files = new File("resources/custom").listFiles();

        int spamCounter = 0;
        int hamCounter = 0;

        if(files != null) {

            // loop through every file in the specified folder above
            for (File file : files) {

                if (!file.isDirectory()) {

                    List<String> email = null;
                    try {
                        email = Reader.getContent("spam-kallibrierung", file.getName());
                        //email = Reader.getContent("custom", file.getName());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if(calculate(email) > 0.3){
                        spamCounter++;
                    } else {
                        hamCounter++;
                    }
                }

            }

        }

        System.out.println("Spam: " + spamCounter);
        System.out.println("Ham: " + hamCounter);

    }

    /**
     * Bayes formula
     * @param email
     * @return
     */
    private static Double calculate (List<String> email) {

        double AnB_S = 1;
        double AnB = 1;

        for(String word : email){
            Word w = spamProbability.get(word);
            if(w != null) {
                AnB_S = AnB_S * w.pS;
                AnB = AnB * w.pH;
            }
        }

        return (AnB_S / (AnB_S + AnB));
    }

    /**
     * Inner class word
     */
    private static class Word {
        /**
         * analyzed word
         */
        String word;
        /**
         * Probability of ham
         */
        Double pH;
        /**
         * Probability of spam
         */
        Double pS;

        public Word(String word){
            this.word = word;
        }
    }
}
