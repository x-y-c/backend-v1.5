package yangchen.exam.util;

import org.apache.commons.lang3.text.StrTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import yangchen.exam.controller.ExamController;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

/**
 * @author YC
 * @date 2019/4/11 16:34
 * O(∩_∩)O)
 */

public class IpUtil {
    public static final Logger LOGGER = LoggerFactory.getLogger(IpUtil.class);

    public  static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        String info = request.getHeader("X-Real-IP");
//        LOGGER.info("ip全部信息[{}]",ip);
//        LOGGER.info("info全部信息[{}]",info);
//        LOGGER.info("request全部信息[{}]",request);
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.equals("127.0.0.1")) {
            InetAddress inet = null;
            try {
                inet = InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            ip = inet.getHostAddress();
        }
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }

        return ip;
    }
}
//public class IpUtil {
//    public static final Logger LOGGER = LoggerFactory.getLogger(IpUtil.class);
//
//    public static final String _255 = "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
//    public static final Pattern pattern = Pattern.compile("^(?:" + _255 + "\\.){3}" + _255 + "$");
//
//    public static String longToIpV4(long longIp) {
//        int octet3 = (int) ((longIp >> 24) % 256);
//        int octet2 = (int) ((longIp >> 16) % 256);
//        int octet1 = (int) ((longIp >> 8) % 256);
//        int octet0 = (int) ((longIp) % 256);
//        return octet3 + "." + octet2 + "." + octet1 + "." + octet0;
//    }
//
//    public static long ipV4ToLong(String ip) {
//        String[] octets = ip.split("\\.");
//        return (Long.parseLong(octets[0]) << 24) + (Integer.parseInt(octets[1]) << 16)
//                + (Integer.parseInt(octets[2]) << 8) + Integer.parseInt(octets[3]);
//    }
//
//    public static boolean isIPv4Private(String ip) {
//        long longIp = ipV4ToLong(ip);
//        return (longIp >= ipV4ToLong("10.0.0.0") && longIp <= ipV4ToLong("10.255.255.255"))
//                || (longIp >= ipV4ToLong("172.16.0.0") && longIp <= ipV4ToLong("172.31.255.255"))
//                || longIp >= ipV4ToLong("192.168.0.0") && longIp <= ipV4ToLong("192.168.255.255");
//    }
//
//    public static boolean isIPv4Valid(String ip) {
//        return pattern.matcher(ip).matches();
//    }
//
//    public static String getIpAddr(HttpServletRequest request) {
//        String ip;
//        boolean found = false;
//        if ((ip = request.getHeader("x-forwarded-for")) != null) {
//            StrTokenizer tokenizer = new StrTokenizer(ip, ",");
//            while (tokenizer.hasNext()) {
//                ip = tokenizer.nextToken().trim();
//                if (isIPv4Valid(ip) && !isIPv4Private(ip)) {
//                    found = true;
//                    break;
//                }
//            }
//        }
//        if (!found) {
//            ip = request.getRemoteAddr();
//        }
//        LOGGER.info("ipUtil：[{}]",ip);
//        return ip;
//    }
//}