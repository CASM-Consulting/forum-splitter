import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class GetDomainTest {

        @Test
        public void getDomainTest() throws MalformedURLException {
            String urlString = "https://javarevisited.blogspot.com/2015/01/how-to-use-lambda-expression-in-place-anonymous-class-java8.html";
            URL url = new URL(urlString);
            String domain = (url.getHost().startsWith("www")) ? url.getHost().substring(4) : url.getHost();
            System.out.println(domain);
        }

}
