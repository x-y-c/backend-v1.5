package yangchen.exam.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {
    public static ByteArrayOutputStream zip(HashMap<String, ByteArrayOutputStream> files, File tar) {
        try {
            ByteArrayOutputStream fileOutputStream = new ByteArrayOutputStream();
            ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
            Iterator maplist = files.entrySet().iterator();
            while (maplist.hasNext()) {
                Map.Entry<String, ByteArrayOutputStream> entry = (Map.Entry<String, ByteArrayOutputStream>) maplist.next();
                ZipEntry zipEntry = new ZipEntry(entry.getKey());
                zipOutputStream.putNextEntry(zipEntry);
                zipOutputStream.write(entry.getValue().toByteArray());

            }
            zipOutputStream.close();
            fileOutputStream.close();
            return fileOutputStream;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
