package yangchen.exam.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author YC
 * @date 2019/6/4 13:32
 * O(∩_∩)O)
 */
public class ExportUtil {
    private static final Logger logger = LoggerFactory.getLogger(ExportUtil.class);
    private static final String CSV_COLUMN_SEPARATOR = ",";
    private static final String CSV_RN = "\r\n";

    public static boolean doExport(List<Map<String, Object>> dataList, String colNames, String mapKey, OutputStream os) {
        try {
            StringBuffer buf = new StringBuffer();
            String[] colNamesArr = null;
            String[] mapKeyArr = null;
            colNamesArr = colNames.split(",");
            mapKeyArr = mapKey.split(",");

            for (int i = 0; i < colNamesArr.length; i++) {
                buf.append(colNamesArr[i]).append(CSV_COLUMN_SEPARATOR);

            }
            buf.append(CSV_RN);
            if (null != dataList) {
                for (int i = 0; i < dataList.size(); i++) {
                    for (int j = 0; j < mapKeyArr.length; j++) {
                        buf.append(dataList.get(i).get(mapKeyArr[j])).append(CSV_COLUMN_SEPARATOR);

                    }
                    buf.append(CSV_RN);
                }
            }

            os.write(buf.toString().getBytes("GBK"));
            os.flush();
            return true;


        } catch (Exception e) {
            logger.error("doExport错误.....", e);
        }
        return false;
    }


    public static void responseSetProperties(String fielName, HttpServletResponse response) throws UnsupportedEncodingException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String fn = fielName + sdf.format(new Date()).toString() + ".csv";
        String utf ="UTF-8";

        response.setContentType("application/ms-txt.numberformat:@");
        response.setCharacterEncoding(utf);
        response.setHeader("Pragma", "public");
        response.setHeader("Cache-Control", "max-age=30");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fn, utf));
    }

}
