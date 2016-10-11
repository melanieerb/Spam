import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Melanie Erb on 11.10.2016.
 */
public class Anlern {

    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    private String[][] spamlist = new String[MAX_ARRAY_SIZE][MAX_ARRAY_SIZE];
    private String[][] hamlist = new String[MAX_ARRAY_SIZE][MAX_ARRAY_SIZE];

    public static void main(String[] args) {

        List<String> str = new ArrayList<>();

        try {
            str = Reader.getContent("ham-anlern", "0126.d002ec3f8a9aff31258bf03d62abdafa");
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(String s: str) {
            System.out.println(s);
        }

        //Spam test = new Spam();
        //test.get("Test");
    }


    public void addWordAsSpam(String string){

    }

    public void addWordAsHam(String string){

    }


}
