package yangchen.exam.util;


import sun.misc.BASE64Decoder;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * base64工具类
 */
public class Base64Util {

    public static byte[] getImgByte(String imgBase64Code) throws IOException {
        //data:image/jpeg;base64,
        String imgBase64 = imgBase64Code.replaceAll("(data:image/).*(;base64,)", "");
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] bytes = decoder.decodeBuffer(imgBase64);
        return bytes;
    }

    public static void saveImgByte(String imgBase, String imgPath) throws IOException {
        byte[] imgByte = getImgByte(imgBase);
        FileOutputStream os = new FileOutputStream(imgPath);
        os.write(imgByte);
        os.close();
    }
}
