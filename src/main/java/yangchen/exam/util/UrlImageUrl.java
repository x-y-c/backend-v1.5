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
//todo 地址放在配置文件
    public static String updateImageDomain(String htmlStr, String updateKey) {
        Document document = Jsoup.parse(htmlStr);
        Elements srcs = document.select("img[src]");
        String domainStr = "http://119.3.217.233:2048/";
        for (Element element : srcs) {
            String imgUrl = element.attr("src");
            imgUrl = domainStr + updateKey;
            element.attr("src", imgUrl);
        }
        return document.toString();
    }


    public static String getImgLabel(String htmlStr) {
        Document document = Jsoup.parse(htmlStr);
        Elements srcs = document.select("img[src]");
        String imgStr = null;
        for (Element element : srcs) {
            imgStr = element.attr("src");
        }
        return imgStr;

    }
}
