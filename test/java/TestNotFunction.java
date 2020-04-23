import com.google.common.io.Files;
import org.apache.nutch.parse.forum.splitter.GeneralSplitterFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.ls.LSInput;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TestNotFunction {


    @Test
    public void testNotFunction() throws IOException {
        try(BufferedReader br = new BufferedReader(new FileReader(new File("resources/test-html"))) ){
            Document doc = Jsoup.parse(br.lines().collect(Collectors.joining()));
            System.out.println(doc.select("div").size());
            Elements elemsRem = doc.select("p.story-body__introduction").remove();
            List<String> queries = new ArrayList<>();
            queries.add("div.story-body__inner");
//            Element element = doc.select("body").first().html();
            List<Element> elems = GeneralSplitterFactory.getContent(doc,new ArrayList<>(),queries);
//            System.out.println(elems.first().text());
//            System.out.println(doc.select("div").remove());
            for(Element elem : elems) {
                for(Element elem2 : elem.select("*")) {
                    for (TextNode tn : elem2.textNodes()) {
                        System.out.println(tn.text());
                        if (elem2.text().contains(Files.toString(new File("resources/test-text"), Charset.defaultCharset()))) {
//                        System.out.println(elem2.text());
                            Assert.fail("Text meant to be extracted still present.");
                        }
                    }
                }

            }

            System.out.println(elems.size());

        }
    }

}
