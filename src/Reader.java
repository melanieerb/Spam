import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by michaelschonbachler on 11.10.16.
 */
public class Reader {

    /**
     * Get
     * @param folder
     * @param filename
     * @return
     * @throws IOException
     */
    public static List<String> getContent(String folder, String filename) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader("resources/" + folder + "/" + filename));
        String content = "";

        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }

            content = sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            br.close();
        }

        List<String> split = Arrays.asList(content.split("\\s+"));

        return split;
    }

    public static Integer countEmail(String folder){
        File[] files = new File("resources/" + folder).listFiles();
        return files.length;
    }

    /**
     * read all textfiles in a folder and return a list of words and their appearance
     * @param folder
     * @param max
     * @return
     */
    public static HashMap<String, Integer> readAndCountWordsOfEmails(String folder, int max) {
        // HAM
        int count = 0;
        List<String> str = new ArrayList<>();
        HashMap<String, Integer> hashmap = new HashMap<>();
        File[] files = new File("resources/" + folder).listFiles();

        if(files == null) {
            return hashmap;
        }

        for (File file : files) {
            count++;
            if(count <= max) {
                if (!file.isDirectory()) {

                    try {
                        str = Reader.getContent(folder, file.getName());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    for (String s : str) {
                        Integer counter = hashmap.get(s);
                        if (counter == null) {
                            counter = 0;
                        }
                        hashmap.put(s, ++counter);
                    }
                }
            }
        }

        return hashmap;
    }

}
