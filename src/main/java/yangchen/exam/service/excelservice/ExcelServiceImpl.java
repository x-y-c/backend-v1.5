package yangchen.exam.service.excelservice;

import cn.hutool.core.io.FileUtil;
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
import yangchen.exam.entity.QuestionNew;
import yangchen.exam.entity.StudentNew;
import yangchen.exam.model.JsonResult;
import yangchen.exam.service.question.QuestionService;
import yangchen.exam.service.student.StudentService;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author YC
 * @date 2019/5/1 21:23
 * O(∩_∩)O)
 */

@Service
public class ExcelServiceImpl {

    @Autowired
    private StudentService studentService;


    @Autowired
    private QuestionService questionService;

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


    public JsonResult huExcel(String teacherName,InputStream inputStream) {
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        List<StudentNew> studentList = new ArrayList<>();
        List<List<Object>> all = reader.read();
        for (int i = 1; i < all.size(); i++) {
            List<Object> objects = all.get(i);
            StudentNew studentNew = new StudentNew();
            studentNew.setStudentId(Integer.valueOf(objects.get(0).toString()));
            studentNew.setStudentName(objects.get(1).toString());
            studentNew.setStudentGrade(objects.get(3).toString());
            studentNew.setPassword("123456");
            studentList.add(studentNew);
        }
        //0918 更新学生教师对应关系
        return studentService.uploadStudentList(teacherName,studentList);
    }


    //这个也用不到了
    //关于取questionDescription的所有操作应该都没用了
    //因为咱们新加的题都没有存过这个字段，系统也没问题
    //这个方法是读Excel的题目，然后导入数据库的
    // ok
    public JsonResult uploadQuestion(InputStream inputStream) {
        ExcelReader excelReader = ExcelUtil.getReader(inputStream);
        List<List<Object>> all = excelReader.read();
        for (int i = 1; i < all.size(); i++) {
            List<Object> objects = all.get(i);
            QuestionNew question = new QuestionNew();
            //2019/07/24 在 question_new 表中，取消了question_title
            //题目
            question.setQuestionName(objects.get(1).toString());
            //阶段
            //todo  阶段前端处理
            /**
             * 1000301  --》阶段一
             * 1000302  --》阶段二
             * 1000303  --》阶段三
             * 1000304  --》阶段四
             * 1000305  --》阶段5
             * 1000306  --》阶段6
             * 1000307  --》阶段7
             * 1000308  --》阶段8
             * 1000309  --》阶段9
             *
             *
             */
            question.setStage((objects.get(2).toString()));
            //question_new 表中取消了 knowledge参数
            //难度

            //todo 难度的关系对应交给前端处理
//            question.setDifficulty(objects.get(4).toString());
            //题目描述
            question.setQuestionDetails(objects.get(5).toString());

            questionService.createQuestion(question);
        }
        return JsonResult.succResult("添加成功", all.size() - 1);
    }
}
