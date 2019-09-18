package yangchen.exam.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import yangchen.exam.entity.TeachClassInfo;
import yangchen.exam.entity.Teacher;
import yangchen.exam.model.JsonResult;
import yangchen.exam.model.ResultCode;
import yangchen.exam.model.TeachClassInfoList;
import yangchen.exam.repo.TeachClassInfoRepo;
import yangchen.exam.repo.TeacherRepo;
import yangchen.exam.service.adminManagement.AdminManagement;


@Api(value = "AdministratorController")
@RestController
@RequestMapping(value = "/admin", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdministratorController {
    public static Logger LOGGER = LoggerFactory.getLogger(AdministratorController.class);

    @Autowired
    private TeachClassInfoRepo teachClassInfoRepo;


    @Autowired
    private TeacherRepo teacherRepo;

    @Autowired
    private AdminManagement adminManagement;

    @ApiOperation(value = "获取教师信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "teacherId", value = "教师编号", required = true, dataType = "Integer")})

    @RequestMapping(value = "/teachClassInfo", method = RequestMethod.GET)
    public JsonResult getTeachClassInfo(@RequestParam Integer teacherId) {
        TeachClassInfoList teachClassInfo = adminManagement.getTeachClassInfo(teacherId);
        return JsonResult.succResult(teachClassInfo);
    }


    @ApiOperation(value = "获取全部教师信息")
    @RequestMapping(value = "/teachClassInfo/all", method = RequestMethod.GET)
    public JsonResult getAllTeachClassInfo() {
        return JsonResult.succResult(adminManagement.getAllTeachClassInfo());
    }


    @ApiOperation(value = "更新教师管理的班级")
    @ApiImplicitParam(name = "teachClassInfoList", value = "教师和对应班级类", required = true, dataType = "TeachClassInfoList")

    @RequestMapping(value = "/update/teachClassInfo", method = RequestMethod.POST)
    public JsonResult updateTeachClassInfo(@RequestBody TeachClassInfoList teachClassInfoList) {
        LOGGER.info(teachClassInfoList.toString());
        return JsonResult.succResult(adminManagement.updateTeachClassInfo(teachClassInfoList));
//        return JsonResult.succResult(null);
    }

    @ApiOperation(value = "添加教师")
    @RequestMapping(value = "/update/teacher", method = RequestMethod.POST)
    public JsonResult updateTeacher(@RequestBody Teacher teacher) {
        Teacher byTeacherName = teacherRepo.findByTeacherName(teacher.getTeacherName());
//        LOGGER.info(teacher.toString());
        if (teacher.getId() != null) {
            teacher.setActive(Boolean.TRUE);
            teacher.setPassword("123456");
            Teacher save = teacherRepo.save(teacher);
            return JsonResult.succResult(save);
        }
        if (byTeacherName != null) {
            return JsonResult.errorResult(ResultCode.USER_EXIST, "用户名已存在", null);
        }
        teacher.setActive(Boolean.TRUE);
        teacher.setPassword("123456");
        Teacher save = teacherRepo.save(teacher);
        return JsonResult.succResult(null);
    }

    @ApiOperation(value = "添加教师")
    @ApiImplicitParam(name = "teachClassInfoList", value = "教师和对应班级类", required = true, dataType = "TeachClassInfoList")

    @RequestMapping(value = "/add/teachClassInfo", method = RequestMethod.POST)
    public JsonResult addTeachClassInfo(@RequestBody TeachClassInfoList teachClassInfoList) {
        TeachClassInfoList result = adminManagement.addTeacher(teachClassInfoList);
        return JsonResult.succResult(result);

    }


    @ApiOperation(value = "删除教师信息")
    @ApiImplicitParam(name = "teacherId", value = "教师编号", required = true, dataType = "Integer")

    @RequestMapping(value = "/delete/teacher", method = RequestMethod.GET)
    public JsonResult deleteTeacher(Integer teacherId) {
        Teacher teacherClassInfo = adminManagement.deleteTeacher(teacherId);
        Teacher teacher = teacherRepo.findById(teacherId).get();
        teacher.setActive(Boolean.FALSE);
        Teacher save = teacherRepo.save(teacher);
        return JsonResult.succResult(save);
    }


    @ApiOperation(value = "获取班级信息")
    @RequestMapping(value = "/class/list", method = RequestMethod.GET)
    public JsonResult getClassList() {
        return JsonResult.succResult(adminManagement.getClassList());

    }
}
