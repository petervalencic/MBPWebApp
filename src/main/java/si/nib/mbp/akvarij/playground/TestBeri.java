package si.nib.mbp.akvarij.playground;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestBeri {

    public static void main(String[] args) {
        try {
            System.out.println(preberiPodatkeURL("http://google1121ffff.com"));
        } catch (Exception ex) {
           System.out.println(ex.toString());
        }
    }
    
   static String preberiPodatkeURL(String reqURL) throws IOException {
        try (Scanner scanner = new Scanner(new URL(reqURL).openStream(),
                StandardCharsets.UTF_8.toString())) {
            scanner.useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        }
   }
    

}
