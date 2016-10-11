import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by michaelschonbachler on 11.10.16.
 */
public class Reader {

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

        List<String> split = Arrays.asList(content.split(" "));
        // split.removeAll(Arrays.asList("\\n"));

        return split;
    }

}
