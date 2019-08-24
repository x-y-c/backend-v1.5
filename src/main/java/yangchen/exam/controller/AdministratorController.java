package yangchen.exam.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import yangchen.exam.entity.Teacher;
import yangchen.exam.model.JsonResult;
import yangchen.exam.model.TeachClassInfoList;
import yangchen.exam.repo.TeachClassInfoRepo;
import yangchen.exam.service.adminManagement.AdminManagement;


@Api(value = "AdministratorController")
@RestController
@RequestMapping(value = "/admin", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdministratorController {


    @Autowired
    private TeachClassInfoRepo teachClassInfoRepo;


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
        return JsonResult.succResult(adminManagement.updateTeachClassInfo(teachClassInfoList));

    }

    @ApiOperation(value = "添加教师及管理班级信息")
    @ApiImplicitParam(name = "teachClassInfoList", value = "教师和对应班级类", required = true, dataType = "TeachClassInfoList")

    @RequestMapping(value = "/add/teachClassInfo", method = RequestMethod.POST)
    public JsonResult addTeachClassInfo(@RequestBody TeachClassInfoList teachClassInfoList) {
        TeachClassInfoList result = adminManagement.addTeacher(teachClassInfoList);
        return JsonResult.succResult(result);

    }


    @ApiOperation(value = "删除教师信息")
    @ApiImplicitParam(name = "teacherId", value = "教师编号", required = true, dataType = "Integer")

    @RequestMapping(value = "/delete/teacher", method = RequestMethod.POST)
    public JsonResult deteleTeacher(Integer teacherId) {
        Teacher teacher = adminManagement.deleteTeacher(teacherId);
        return JsonResult.succResult(teacher);
    }


    @ApiOperation(value = "获取班级信息")
    @RequestMapping(value = "/class/list", method = RequestMethod.GET)
    public JsonResult getClassList() {
        return JsonResult.succResult(adminManagement.getClassList());

    }
}
