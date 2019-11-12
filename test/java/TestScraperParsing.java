import org.apache.nutch.parse.forum.splitter.GeneralSplitterFactory;
import org.apache.nutch.splitter.utils.POJOHTMLMatcherDefinition;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class TestScraperParsing {

    @Test
    public void TestScraperParsing() throws IOException {

        String scraperLoc = "resources/scraper.json";
        List<POJOHTMLMatcherDefinition> parser = GeneralSplitterFactory.getTagSetFromJson(Paths.get(scraperLoc));
        for(POJOHTMLMatcherDefinition matcher : parser) {
            System.out.println(matcher.field);
            for (Map<String, String> match : matcher.tags) {
                for (Map.Entry<String, String> tag : match.entrySet()) {
                    System.out.println(tag.getKey() + " " + tag.getValue());
                }
            }
        }
    }

}
