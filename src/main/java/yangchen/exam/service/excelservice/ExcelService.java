package yangchen.exam.service.excelservice;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.support.ExcelTypeEnum;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author YC
 * @date 2019/4/30 12:04
 * O(∩_∩)O)
 */
@Service
public class ExcelService {

    public void excelReader(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        try {
            AnalysisEventListener listener = new ExcelListener();
            ExcelReader excelReader = new ExcelReader(inputStream, ExcelTypeEnum.XLSX, null, listener);
            excelReader.read();
        } catch (Exception e) {

        } finally {
            {
                inputStream.close();

            }
        }


    }
}
