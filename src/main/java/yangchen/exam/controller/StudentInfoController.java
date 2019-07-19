package yangchen.exam.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import yangchen.exam.entity.StudentNew;
import yangchen.exam.model.JsonResult;
import yangchen.exam.model.ResultCode;
import yangchen.exam.service.excelservice.ExcelServiceImpl;
import yangchen.exam.service.student.studentService;
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
    private studentService studentService;

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentInfoController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ExcelServiceImpl excelServiceimpl;

    /**
     * @return 全部学生的信息
     */
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public JsonResult getAllStudent() {
        LOGGER.info("[{}] 查询全部学生", request.getHeader("userId"));
        return JsonResult.succResult(studentService.getAllStudent());
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public JsonResult getPagedStudent(Integer page, Integer pageLimit) {
        LOGGER.info("[{}]查询分页信息", request.getHeader("userId"));
        if (pageLimit == null) {
            pageLimit = 10;
        }
        return JsonResult.succResult(studentService.getPage(page, pageLimit));
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JsonResult AddStudent(@RequestBody StudentNew student) {
        LOGGER.info("[{}] add student", UserUtil.getUserId(request));
        return JsonResult.succResult(studentService.addStudent(student));
    }


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public JsonResult updateStudent(@RequestBody StudentNew student) {
        LOGGER.info("[{}] update student", UserUtil.getUserId(request));
        return JsonResult.succResult(studentService.changeStudentInfo(student));
    }

    @RequestMapping(value = "/password", method = RequestMethod.POST)
    public JsonResult updatePassword(@RequestParam Integer studentId, @RequestParam String oldpassword, @RequestParam String password) {
        LOGGER.info("[{}] change password", UserUtil.getUserId(request));
        StudentNew student = studentService.changePassword(studentId, oldpassword, password);
        if (student != null) {
            return JsonResult.succResult(student);
        } else {
            return JsonResult.errorResult(ResultCode.USER_NOT_EXIST, "用户不存在", null);
        }
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public JsonResult getStudentInfo(@RequestParam Integer studentId) {
        LOGGER.info("[{}] get [{}] studentInfo", UserUtil.getUserId(request), studentId);
        return JsonResult.succResult(studentService.getStudentByStudentId(studentId));
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public JsonResult uploadStudent(@RequestParam MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        JsonResult jsonResult = excelServiceimpl.huExcel(inputStream);
        return jsonResult;
    }


    @RequestMapping(value = "/csv")
    public String findByCSV(HttpServletResponse response) {
        List<Map<String, Object>> dataList = null;
        List<StudentNew> students = studentService.getAllStudent();
        String sTitle = "Id,学号,姓名,密码,专业，班级";
        String fName = "by_";
        String mapKey = "id,studentId,name,password,major,grade";
        dataList = new ArrayList<>();
        Map<String, Object> map = null;
        for (StudentNew student : students) {
            map = new HashMap<String, Object>();
            map.put("id", student.getId());
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
    public JsonResult getGrade() {
        List<String> strings = studentService.initGrade();
        return JsonResult.succResult(strings);
    }
}
