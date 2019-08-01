package yangchen.exam.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class UrlImageUrl {

    public static String setImagesDomain(String htmlStr) {
        Document document = Jsoup.parse(htmlStr);
        Elements srcs = document.select("img[src]");
        String domainStr = "http://119.3.217.233:2048";
        for (Element element : srcs) {
            String imgUrl = element.attr("src");
            if (imgUrl.trim().startsWith("/")) {
                imgUrl = domainStr + imgUrl;
                element.attr("src", imgUrl);
            }
        }
        return document.toString();
    }
}
