package yangchen.exam.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import yangchen.exam.entity.Student;
import yangchen.exam.model.JsonResult;
import yangchen.exam.service.base.studentService;
import yangchen.exam.service.excelservice.ExcelService;
import yangchen.exam.service.excelservice.ExcelServiceImpl;
import yangchen.exam.util.UserUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

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
    private ExcelService excelService;

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
        if(pageLimit==null){
            pageLimit=10;
        }
        return JsonResult.succResult(studentService.getPage(page, pageLimit));
    }


    @RequestMapping(value = "/major", method = RequestMethod.GET)
    public JsonResult getStudentByMajor(@RequestParam String major) {
        LOGGER.info("[{}] get student By Major", UserUtil.getUserId(request));
        return JsonResult.succResult(studentService.getStudentByMajor(major));
    }


    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JsonResult AddStudent(@RequestBody Student student) {
        LOGGER.info("[{}] add student", UserUtil.getUserId(request));
        return JsonResult.succResult(studentService.addStudent(student));
    }


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public JsonResult updateStudent(@RequestBody Student student) {
        LOGGER.info("[{}] update student", UserUtil.getUserId(request));
        return JsonResult.succResult(studentService.changeStudentInfo(student));
    }


    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public JsonResult uploadStudent(@RequestParam MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        JsonResult jsonResult = excelServiceimpl.huExcel(inputStream);
        return jsonResult;
    }
}
