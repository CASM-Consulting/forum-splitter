import com.google.common.io.Files;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.stream.Collectors;

public class TestNotFunction {


    @Test
    public void testNotFunction() throws IOException {
        try(BufferedReader br = new BufferedReader(new FileReader(new File("resources/test-html"))) ){
            Document doc = Jsoup.parse(br.lines().collect(Collectors.joining()));
            System.out.println(doc.select("div").size());
            Elements elems = doc.select("p.story-body__introduction").remove();
            elems = doc.select("*");
//            System.out.println(doc.select("div").remove());
            for(Element elem : elems) {
                if(elem.ownText().contains(Files.toString(new File("resources/test-text"), Charset.defaultCharset()))){
                    System.out.println(elem.ownText());
                    Assert.fail("Text meant to be extracted still present.");
                }
            }

            System.out.println(elems.size());

        }
    }

}
