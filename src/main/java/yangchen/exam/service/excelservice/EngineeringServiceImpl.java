package yangchen.exam.service.excelservice;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yangchen.exam.entity.Engineering;
import yangchen.exam.entity.Teacher;
import yangchen.exam.model.JsonResult;
import yangchen.exam.repo.EngineeringRepo;
import yangchen.exam.repo.StudentRepo;
import yangchen.exam.repo.TeacherRepo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
@Service
public class EngineeringServiceImpl implements EngineeringService{

    @Autowired
    private TeacherRepo teacherRepo;
    @Autowired
    private EngineeringRepo engineeringRepo;
    @Autowired
    private EngineeringService engineeringService;


    public JsonResult huExcel(String teacherName,InputStream inputStream) {
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        List<Engineering> engineeringList = new ArrayList<>();
        List<List<Object>> all = reader.read();
        for (int i = 1; i < all.size(); i++) {
            List<Object> objects = all.get(i);
            Engineering engineering = new Engineering();
            engineering.setStudentId(Integer.valueOf(objects.get(0).toString()));
            engineering.setStudentYear(Integer.valueOf(objects.get(1).toString()));
            engineering.setStudentGrade(objects.get(3).toString());
            engineering.setTeacherId(Integer.valueOf(objects.get(4).toString()));
            engineering.setScore(Integer.valueOf(objects.get(5).toString()));
            engineeringList.add(engineering);
        }
        //0918 更新学生教师对应关系
        return engineeringService.uploadStudentList(teacherName,engineeringList);
    }

    public JsonResult uploadStudentList(String teacherName, List<Engineering> engineeringList) {
        Teacher teacher = teacherRepo.findByTeacherName(teacherName);
        List<Engineering> engineerings = new ArrayList<>(engineeringList.size());
        for (Engineering engineering : engineeringList) {
            Engineering student = engineeringRepo.findByStudentId(engineering.getStudentId());
            if (student != null) {
                //return JsonResult.errorResult(ResultCode.USER_EXIST, "Excel中的学号已存在,请检查后再导入", studentNew.getStudentId());
                student.setStudentYear(engineering.getStudentYear());
                student.setStudentGrade(engineering.getStudentGrade());
                student.setTeacherId(teacher.getId());
                student.setScore(engineering.getScore());
                engineerings.add(student);

            } else {

                engineering.setTeacherId(teacher.getId());
                engineerings.add(engineering);
            }
        }
        List<Engineering> engineeringList1 = engineeringRepo.saveAll(engineerings);
        return JsonResult.succResult("添加成功", engineeringList1.size());

    }

    @Override
    public Engineering getStudentByStudentId(Integer studentId) {
        return engineeringRepo.findByStudentId(studentId);
    }
}

