package yangchen.exam.util;


import org.apache.commons.codec.binary.Base64;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * base64工具类
 */
public class Base64Util {

    public static byte[] getImgByte(String imgBase64Code) throws IOException {
        //data:image/jpeg;base64,
        String imgBase64 = imgBase64Code.replaceAll("(data:image/).*(;base64,)", "");
        Base64 base64=new Base64();
        //byte[] bytes = base64.decode(imgBase64);
        //return bytes;
        return null;
    }

    public static void saveImgByte(String imgBase, String imgPath) throws IOException {
        byte[] imgByte = getImgByte(imgBase);
        FileOutputStream os = new FileOutputStream(imgPath);
        os.write(imgByte);
        os.close();
    }
}
