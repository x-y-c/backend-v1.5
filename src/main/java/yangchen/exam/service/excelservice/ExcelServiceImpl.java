package yangchen.exam.service.excelservice;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yangchen.exam.model.JsonResult;
import yangchen.exam.model.StudentInfo;
import yangchen.exam.service.base.studentService;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author YC
 * @date 2019/5/1 21:23
 * O(∩_∩)O)
 */

@Service
public class ExcelServiceImpl {

    @Autowired
    private studentService studentService;


    private static Logger LOGGER = LoggerFactory.getLogger(ExcelServiceImpl.class);

    public JsonResult saveExcelToDataBase(InputStream inputStream) throws IOException {

        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        POIFSFileSystem fileSystem = new POIFSFileSystem(bufferedInputStream);
        HSSFWorkbook workbook = new HSSFWorkbook(fileSystem);
        HSSFSheet sheet = workbook.getSheetAt(0);

        int lastRowNum = sheet.getLastRowNum();
        for (int i = 1; i <= lastRowNum; i++) {
            HSSFRow row = sheet.getRow(i);
            row.getCell(0).setCellType(CellType.STRING);
            String studentId = row.getCell(0).getStringCellValue();
            String name = row.getCell(1).getStringCellValue();
            row.getCell(2).setCellType(CellType.STRING);
            String grade = row.getCell(2).getStringCellValue();
            String major = row.getCell(3).getStringCellValue();
            LOGGER.info(String.valueOf(lastRowNum));
            LOGGER.info("[{}][{}][{}][{}]", studentId, name, grade, major);
        }

        return JsonResult.succResult("");


    }


    public JsonResult huExcel(InputStream inputStream) {
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        List<List<Object>> all = reader.read();
        for (int i = 1; i < all.size(); i++) {
            List<Object> objects = all.get(i);
            StudentInfo studentInfo = new StudentInfo();
            studentInfo.setStudentId(Long.valueOf(objects.get(0).toString()));
            studentInfo.setName(objects.get(1).toString());
            studentInfo.setGrade(objects.get(2).toString());
            studentInfo.setMajor(objects.get(3).toString());

            studentService.addStudent(studentInfo);
        }
        return JsonResult.succResult("添加成功", all.size()-1d);
    }
}
