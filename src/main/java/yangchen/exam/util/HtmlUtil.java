package yangchen.exam.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlUtil {
    public static String stripHtml(String content) {
        // <p>段落替换为换行
        content = content.replaceAll("<p .*?>", "\r\n");
        // <br><br/>替换为换行
        content = content.replaceAll("<br\\s*/?>", "\r\n");
        // 去掉其它的<>之间的东西
        content = content.replaceAll("\\<.*?>", "");
        // 还原HTML
        // content = HTMLDecoder.decode(content);
        return content;
    }


    public static String delHtmlTag(String htmlStr) {
        String regExScript = "<script[^>]*?>[\\s\\S]*?<\\/script>";
        String regExStyle = "<style[^>]*?>[\\s\\S]*?<\\/style>";
        String regExHtml = "<[^>]+>";
        Pattern p_script = Pattern.compile(regExScript, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll("");

        Pattern p_style = Pattern.compile(regExStyle, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll("");

        Pattern p_html = Pattern.compile(regExHtml, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll("");
        String s = StringEscapeUtils.unescapeHtml4(htmlStr);

        return s.trim();

    }

}
