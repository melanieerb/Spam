import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Created by michael-home on 17.10.2016.
 */
public class Main {

    private static HashSet<String> words = new HashSet<>();
    private static HashMap<String, Word> spamProbability = new HashMap<>();
    private static final Boolean CUSTOM = true;
    private static final Double THRESHOLD_VALUE = 0.3;
    private static final Double DEFAULT_ALPHA = 0.00001;
    private static final Integer AMOUNT_OF_EMAILS = 1000;

    public static void main(String[] args) throws IOException {

        // ***********************************************************************************************************//
        // Learn
        // ***********************************************************************************************************//
        Integer amountOfHamEmails = Reader.countEmail("ham-anlern");
        Integer amountOfSpamEmails = Reader.countEmail("spam-anlern");
        HashMap<String, Integer> wordCounterHam = Reader.readAndCountWordsOfEmails("ham-anlern", amountOfHamEmails);
        HashMap<String, Integer> wordCounterSpam = Reader.readAndCountWordsOfEmails("spam-anlern", amountOfSpamEmails);

        if(CUSTOM){
            System.out.println("Custom Mode");
            wordCounterHam = Reader.readAndCountWordsOfEmails("custom/ham", 100);
            wordCounterSpam = Reader.readAndCountWordsOfEmails("custom/spam", 100);
        }

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
                w.pS = DEFAULT_ALPHA;
            } else {
                countSpam = wordCounterSpam.get(word);
                // P( word | spam )
                w.pS = countSpam / amountOfSpamEmails;
            }

            double countHam;
            if(wordCounterHam.get(word) == null){
                w.pH = DEFAULT_ALPHA;
            } else {
                countHam = wordCounterHam.get(word);
                // P( word | ham )
                w.pH = countHam / amountOfHamEmails;
            }

            spamProbability.put(word, w);
        }

        System.out.println("Learn");
        System.out.println("===================================================");
        System.out.println("Words: " + words.size());
        System.out.println("Amount of ham emails: " + amountOfHamEmails);
        System.out.println("Amount of spam emails: " + amountOfSpamEmails);
        System.out.println("");


        if(CUSTOM){

            List<String> email = null;
            try {
                email = Reader.getContent("custom", "testMail");
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("TEST Custom");
            System.out.println("===================================================");
            System.out.println("Probability: " + calculate(email) + "38%");
            System.out.println("");

        } else {

            // ***********************************************************************************************************//
            // Calibration
            // ***********************************************************************************************************//

            Integer[] calibrationResultHAM = testEmails("ham-kallibrierung");
            Integer[] calibrationResultSPAM = testEmails("spam-kallibrierung");

            System.out.println("Calibration");
            System.out.println("===================================================");
            System.out.println("ham-calibration");
            System.out.println("Spam: " + calibrationResultHAM[0]);
            System.out.println("Ham: " + calibrationResultHAM[1]);
            System.out.println("spam-calibration");
            System.out.println("Spam: " + calibrationResultSPAM[0]);
            System.out.println("Ham: " + calibrationResultSPAM[1]);
            System.out.println("");

            // ***********************************************************************************************************//
            // Test
            // ***********************************************************************************************************//

            Integer[] testResultHAM = testEmails("ham-test");
            Integer[] testResultSPAM = testEmails("spam-test");

            double percentHAM = (double) testResultHAM[1] / (testResultHAM[1] + testResultHAM[0]) * 100;
            double percentSPAM = (double) testResultSPAM[0] / (testResultSPAM[1] + testResultSPAM[0]) * 100;

            System.out.println("Test");
            System.out.println("===================================================");
            System.out.println("ham-test");
            System.out.println("Marked as ham: " + percentHAM + "%");
            System.out.println("spam-test");
            System.out.println("Marked as spam: " + percentSPAM + "%");
            System.out.println("");

            // ***********************************************************************************************************//
            // Test custom email
            // ***********************************************************************************************************//

            System.out.println("Read mail from disk");
            System.out.println("===================================================");
            File file = new File("resources/mail/mail");

            List<String> email = null;
            try {
                email = Reader.getContent("mail", file.getName());
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Spam probability: " + calculate(email) + "%");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Would you like to move this email to the spam folder? [J/N]");
            String s = br.readLine();
            if (s.equals("J")) {
                copyFile("resources/mail/mail", "resources/spam-anlern/mail");
                System.out.print("Mail has been added to the spam-anlern folder");
            } else {
                copyFile("resources/mail/mail", "resources/ham-anlern/mail");
                System.out.print("Mail has been added to the ham-anlern folder");
            }
        }
    }

    /**
     * Bayes formula
     * @param email
     * @return
     */
    private static Double calculate (List<String> email) {

        double AnB_S = 1;
        double AnB = 1;

        /** Die Klassifizierungswerte ihres Programms sind noch ziemlich schlecht.
            Das liegt daran, dass Sie in der calculate Funktion oft die Situation haben,
            dass sowohl AnB als auch AnB_S Null ist. Anstelle also a1*a2*…*an/( a1*a2*…*an+ b1*b2*…*bn)
            so wie Sie zu berechnen (also Zähler und Nenner separat), wäre es besser,
            das zu 1/(1+b1/a1*b2/a2*…*bn/an) umzuformen und die Ausdrücke b_i/a_i direkt zu berechnen.

                for(String word : email){
                    Word w = spamProbability.get(word);
                    if(w != null) {
                    AnB_S = AnB_S * w.pS;
                    AnB = AnB * w.pH;
                    }
                }
                return (AnB_S / (AnB_S + AnB));
         */


        for(String word : email){
            Word w = spamProbability.get(word);
            if(w != null) {
                AnB = AnB * (w.pH / w.pS);
            }
        }

        System.out.println(1 / (AnB_S + AnB));
        return (1 / (AnB_S + AnB));
    }

    private static Integer[] testEmails(String folder){

        File[] files = new File("resources/" + folder).listFiles();
        Integer[] counter = new Integer[2];

        // spam counter
        counter[0] = 0;
        // ham counter
        counter[1] = 0;

        if(files != null) {
            // loop through every file in the specified folder above
            for (File file : files) {
                if (!file.isDirectory()) {

                    List<String> email = null;
                    try {
                        email = Reader.getContent(folder, file.getName());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if(calculate(email) > THRESHOLD_VALUE){
                        counter[0]++;
                    } else {
                        counter[1]++;
                    }
                }
            }
        }
        return counter;
    }

    public static void copyFile (String source, String dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            is.close();
            os.close();
        }
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
