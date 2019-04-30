package yangchen.exam.service.biz.impl;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import yangchen.exam.service.biz.ExcelService;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author YC
 * @date 2019/4/29 22:48
 * O(∩_∩)O)
 */
public class ExcelServiceImpl implements ExcelService {
    @Override
    public void saveExcel(MultipartFile multipartFile) {

    }

    public List getBankListByExcel(InputStream in, String fileName) throws Exception {
        List list = new ArrayList<>();
        Workbook workbook = this.getWorkBook(in, fileName);

        if (workbook == null) {
            throw new Exception("创建的Excel工作簿为空!");
        }
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;

        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            sheet = workbook.getSheetAt(i);
            if (sheet == null) {
                continue;
            }
            for (int j = sheet.getFirstRowNum(); j <= sheet.getLastRowNum(); j++) {

                row = sheet.getRow(j);
                if (row == null || row.getFirstCellNum() == j) {
                    continue;
                }

                List<Object> li = new ArrayList<>();
                for (int k = row.getFirstCellNum(); k < row.getLastCellNum(); k++) {

                    cell = row.getCell(k);
                    li.add(cell);
                }
                list.add(li);


            }
        }
        workbook.close();
        return list;
    }

    private Workbook getWorkBook(InputStream in, String fileName) throws Exception {

        Workbook workbook = null;
        //获取文件后缀；
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if (".xls".equals(fileType)) {
            workbook = new HSSFWorkbook(in);
        } else if (".xlsx".equals(fileType)) {
            workbook = new XSSFWorkbook(in);
        } else {
            throw new Exception("请上传excel文件！");
        }
        return workbook;

    }
}
