package yangchen.exam.util;

import com.google.gson.Gson;
import org.apache.commons.text.StringEscapeUtils;
import yangchen.exam.model.SourceCode;

/**
 * 去转义sourceCode，
 */
public class DecodeSourceCode {

    public static String getCode(String sourceCode) {
        Gson gson = new Gson();
        SourceCode sourceCodeResult = gson.fromJson(sourceCode, SourceCode.class);
        return sourceCodeResult.getKey().get(0).getCode();
    }
}
