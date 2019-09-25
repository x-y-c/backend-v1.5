package yangchen.exam.controller;

import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import yangchen.exam.Enum.UserTypeEnum;
import yangchen.exam.entity.Administrator;
import yangchen.exam.entity.StudentNew;
import yangchen.exam.entity.Teacher;
import yangchen.exam.model.JsonResult;
import yangchen.exam.model.ResultCode;
import yangchen.exam.model.StudentModifyModel;
import yangchen.exam.repo.StudentRepo;
import yangchen.exam.repo.TeacherRepo;
import yangchen.exam.service.adminManagement.AdminManagement;
import yangchen.exam.service.excelservice.ExcelServiceImpl;
import yangchen.exam.service.student.StudentService;
import yangchen.exam.service.teacher.TeacherService;
import yangchen.exam.util.ExportUtil;
import yangchen.exam.util.UserUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author YC
 * @date 2019/4/17 17:03
 * O(∩_∩)O)
 */

@RestController
@RequestMapping(value = "/student", produces = MediaType.APPLICATION_JSON_VALUE)
public class StudentInfoController {
    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private AdminManagement adminManagement;

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentInfoController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ExcelServiceImpl excelServiceimpl;

    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private TeacherRepo teacherRepo;


    /**
     * @return 全部学生的信息
     */
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public JsonResult getAllStudent() {
        LOGGER.info("[{}] 查询全部学生", request.getHeader("userId"));
        return JsonResult.succResult(studentService.getAllStudent());
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public JsonResult getPagedStudent(@RequestParam(required = false) String teacherId, @RequestParam(required = false) String grade, Integer page, Integer pageLimit) {
        LOGGER.info("查询分页信息");
        LOGGER.error("[{}],[{}],[{}],[{}]", teacherId, grade, page, pageLimit);
        if (StringUtils.isEmpty(grade)) {
            return JsonResult.succResult(studentService.getStudentPage(teacherId, page, pageLimit));
        } else {
            return JsonResult.succResult(studentService.getGradePageList(teacherId, grade, page, pageLimit));
        }

    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JsonResult addStudent(@RequestBody StudentModifyModel student) {
        LOGGER.info("[{}] add student", UserUtil.getUserId(request));
        if (student.getType().equals(0)) {
            return studentService.insertStudent(student);
        } else {
            return studentService.updateStudent(student);
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public JsonResult deleteUserByStudentId(@RequestParam Integer studentId) {
        studentService.deleteStudentInfo(studentId);
        return JsonResult.succResult(null);
    }


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public JsonResult updateStudent(@RequestBody StudentNew student) {
        LOGGER.info("[{}] update student", UserUtil.getUserId(request));
        return JsonResult.succResult(studentService.changeStudentInfo(student));
    }

    @RequestMapping(value = "/password", method = RequestMethod.POST)
    public JsonResult updatePassword(@RequestParam(required = false) String studentId,
                                     @RequestParam String oldPassword,
                                     @RequestParam String password,
                                     @RequestParam String type,
                                     @RequestParam String userName) {
        //LOGGER.info("[{}] change password", UserUtil.getUserId(request));

        return studentService.changePassword(type,userName,studentId, oldPassword, password);

    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public JsonResult getStudentInfo(@RequestParam(required = false) Integer studentId,@RequestParam String userName,String type) {
        if(type.equals(UserTypeEnum.getUserTypeCode("学生"))){
            LOGGER.info("学生用户：[{}] get [{}] studentInfo", UserUtil.getUserId(request), studentId);
            return JsonResult.succResult(studentService.getStudentByStudentId(studentId));
        }
        else if(type.equals(UserTypeEnum.getUserTypeCode("教师"))){
            LOGGER.info("教师用户：[{}] get [{}] studentInfo", UserUtil.getUserId(request), userName );
            Teacher teacher = teacherService.findTeacherByName(userName);
            return JsonResult.succResult(teacher);
        }
        else{
            LOGGER.info("管理员用户：[{}] get [{}] studentInfo", UserUtil.getUserId(request), userName );
            Administrator administrator = adminManagement.findByAdminName(userName);
            return JsonResult.succResult(administrator);
        }
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public JsonResult uploadStudent(@RequestParam MultipartFile file, @RequestParam String teacherName) throws IOException {
        InputStream inputStream = file.getInputStream();
        return excelServiceimpl.huExcel(teacherName, inputStream);
    }


    @RequestMapping(value = "/excel")
    public void findByExcel(HttpServletResponse response, @RequestParam(required = false) String grade) {
        try {
            studentService.downloadStudents(response, grade);
        } catch (IOException e) {
            LOGGER.error("导出学生失败[{}]", e.getMessage());
        }
    }

    @RequestMapping(value = "/csv")
    public String findByCSV(HttpServletResponse response) {
        List<Map<String, Object>> dataList = null;
        List<StudentNew> students = studentService.getAllStudent();
        String sTitle = "学号,姓名,密码,班级";
        String fName = "学生信息";
        String mapKey = "studentId,name,password,grade";
        dataList = new ArrayList<>();
        Map<String, Object> map = null;
        for (StudentNew student : students) {
            map = new HashMap<String, Object>();
            map.put("studentId", student.getStudentId());
            map.put("name", student.getStudentName());
            map.put("password", student.getPassword());
            map.put("grade", student.getStudentGrade());
            dataList.add(map);
        }
        try (final OutputStream os = response.getOutputStream()) {
            ExportUtil.responseSetProperties(fName, response);
            ExportUtil.doExport(dataList, sTitle, mapKey, os);
            return null;
        } catch (Exception e) {
            System.out.println(e);
        }
        return "数据导出错误";
    }


    @RequestMapping(value = "/grade", method = RequestMethod.GET)
    public JsonResult getGrade(@RequestParam(required = false) String teacherId) {
        if (StringUtils.isEmpty(teacherId)) {
            List<String> strings = studentService.initGrade();
            return JsonResult.succResult(strings);
        } else {
            Teacher teacher = teacherRepo.findByTeacherName(teacherId);
            List<String> grades = studentRepo.getGradeByTeacherId(teacher.getId());
            return JsonResult.succResult(grades);
        }

    }
}
