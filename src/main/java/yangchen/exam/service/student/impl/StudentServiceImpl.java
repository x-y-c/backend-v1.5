package yangchen.exam.service.student.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import yangchen.exam.Enum.UserTypeEnum;
import yangchen.exam.entity.Administrator;
import yangchen.exam.entity.StudentNew;
import yangchen.exam.entity.TeachClassInfo;
import yangchen.exam.entity.Teacher;
import yangchen.exam.model.JsonResult;
import yangchen.exam.model.ResultCode;
import yangchen.exam.model.StudentInfo;
import yangchen.exam.model.StudentModifyModel;
import yangchen.exam.repo.AdministratorRepo;
import yangchen.exam.repo.StudentRepo;
import yangchen.exam.repo.TeachClassInfoRepo;
import yangchen.exam.repo.TeacherRepo;
import yangchen.exam.service.student.StudentService;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yc
 */
@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private TeacherRepo teacherRepo;

    @Autowired
    private AdministratorRepo administratorRepo;

    @Autowired
    private TeachClassInfoRepo teachClassInfoRepo;


    @Cacheable(value = "student")
    @Override
    public StudentNew getStudentByStudentId(Integer studentId) {
        return studentRepo.findByStudentId(studentId);
    }

    @Override
    public List<StudentNew> getStudentListByGrade(String grade) {
        return studentRepo.findByStudentGrade(grade);
    }

    @Override
    public StudentNew changeStudentInfo(StudentNew student) {
        return studentRepo.save(student);
    }

    public JsonResult changePassword(String type,String userName,String studentId, String oldPassword, String password) {
        if(type.equals(UserTypeEnum.getUserTypeCode("学生"))){
            StudentNew student = studentRepo.findByStudentId(Integer.valueOf(studentId));
            if (student != null) {
                if (student.getPassword().equals(oldPassword)) {
                    student.setPassword(password);
                    StudentNew save = studentRepo.save(student);
                    return JsonResult.succResult(save);
                } else {
                    return JsonResult.errorResult(ResultCode.WRONG_PASSWORD, "旧密码错误", null);
                }

            } else {
                return JsonResult.errorResult(ResultCode.USER_NOT_EXIST, "用户不存在", null);
            }
        }
        else if(type.equals(UserTypeEnum.getUserTypeCode("教师"))){
            Teacher teacher = teacherRepo.findByTeacherName(userName);
            if(teacher!=null){
                if (teacher.getPassword().equals(oldPassword)) {
                    teacher.setPassword(password);
                    Teacher save = teacherRepo.save(teacher);
                    return JsonResult.succResult(save);
                } else {
                    return JsonResult.errorResult(ResultCode.WRONG_PASSWORD, "旧密码错误", null);
                }
            }else{
                return JsonResult.errorResult(ResultCode.USER_NOT_EXIST, "用户不存在", null);
            }
        }
        else if(type.equals(UserTypeEnum.getUserTypeCode("管理员"))){
            Administrator administrator = administratorRepo.findByAdminNameAndActived(userName,Boolean.TRUE);
            if(administrator!=null){
                if (administrator.getAdminPassword().equals(oldPassword)) {
                    administrator.setAdminPassword(password);
                    Administrator save = administratorRepo.save(administrator);
                    return JsonResult.succResult(save);
                } else {
                    return JsonResult.errorResult(ResultCode.WRONG_PASSWORD, "旧密码错误", null);
                }
            }else{
                return JsonResult.errorResult(ResultCode.USER_NOT_EXIST, "用户不存在", null);
            }
        }
        else {
            return JsonResult.errorResult(ResultCode.USER_NOT_EXIST,"没有此类用户","");
        }


    }


    @Override
    public void deleteStudentInfo(Integer id) {
        studentRepo.deleteStudentNewById(id);
    }


    @Override
    public JsonResult updateStudent(StudentModifyModel student) {

        StudentNew studentNew = studentRepo.findByStudentId(student.getStudentId());

        if (student.getPassword() == null || student.getPassword().length() <= 0) {
            student.setPassword("123456");
        }

        if (studentNew == null) {
            return JsonResult.errorResult(ResultCode.USER_NOT_EXIST, "用户不存在", "");
        } else {
            studentNew.setStudentName(student.getStudentName());
            studentNew.setPassword(student.getPassword());
            studentNew.setStudentGrade(student.getStudentGrade());
            studentNew.setStudentId(student.getStudentId());
            return JsonResult.succResult(studentRepo.save(studentNew));
        }

    }


    public JsonResult insertStudent(StudentModifyModel studentModifyModel) {
        StudentNew result = studentRepo.findByStudentId(studentModifyModel.getStudentId());
        Teacher teacher = teacherRepo.findByTeacherName(studentModifyModel.getTeacherName());
        if (result != null) {
            return JsonResult.errorResult(ResultCode.USER_EXIST, "用户已存在", null);
        } else {
            StudentNew studentNew = new StudentNew();
            studentNew.setTeacherId(teacher.getId());
            studentNew.setPassword("123456");
            studentNew.setStudentGrade(studentModifyModel.getStudentGrade());
            studentNew.setStudentId(studentModifyModel.getStudentId());
            studentNew.setStudentName(studentModifyModel.getStudentName());
            StudentNew save = studentRepo.save(studentNew);
            return JsonResult.succResult(save);
        }
    }

    @Override
    public JsonResult addStudent(StudentModifyModel student) {
        StudentNew studentNew1 = studentRepo.findByStudentId(student.getStudentId());
        if (student.getPassword() == null || student.getPassword().length() <= 0) {
            student.setPassword("123456");
        }
        StudentNew studentNew = new StudentNew();
        if (studentNew1 != null) {
            return JsonResult.errorResult(ResultCode.USER_EXIST, "用户已存在", "");
        } else {
            String grade = student.getStudentGrade();
            Teacher teacher = teacherRepo.findByTeacherName(student.getTeacherName());
            List<String> classNameList = teachClassInfoRepo.getClassNameByTeacherId(teacher.getId());
            for (int i = 0; i < classNameList.size(); i++) {
                if (grade.equals(classNameList.get(i))) {
                    break;
                } else {
                    if (i == classNameList.size() - 1) {
                        TeachClassInfo teachClassInfo = new TeachClassInfo();
                        teachClassInfo.setClassName(grade);
                        teachClassInfo.setTeacherId(teacher.getId());
                        teachClassInfoRepo.save(teachClassInfo);
                    }
                }
            }
            studentNew.setStudentName(student.getStudentName());
            studentNew.setPassword(student.getPassword());
            studentNew.setStudentGrade(student.getStudentGrade());
            studentNew.setStudentId(student.getStudentId());
            return JsonResult.succResult(studentRepo.save(studentNew));
        }
    }


    /**
     * 用在excel处理的部分，excel读取的是 studentInfo
     *
     * @param studentInfo
     * @return
     */
    @Override
    public StudentNew addStudent(StudentInfo studentInfo) {
        StudentNew student = new StudentNew();
        student.setStudentName(studentInfo.getName());
        student.setStudentId(studentInfo.getStudentId());
        student.setPassword("123456");
        student.setStudentGrade(studentInfo.getGrade());
        return studentRepo.save(student);
    }


    @Override
    public List<StudentNew> getAllStudent() {
        return studentRepo.findAll();
    }


    @Override
    public Page<StudentNew> getPage(String teacherId, Integer pageNum, Integer pageLimit) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNum - 1, pageLimit, sort);
        if (StringUtils.isEmpty(teacherId)) {
            return studentRepo.findAll(pageable);
        } else {
            Integer id = teacherRepo.findByTeacherName(teacherId).getId();
            List<String> grades = teachClassInfoRepo.getClassNameByTeacherId(id);
            return studentRepo.findByStudentGradeIn(grades, pageable);
        }

    }

    //获取全部学生的分页；
    @Override
    public Page<StudentNew> getStudentPage(String teacherId, Integer pageNum, Integer pageLimit) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNum - 1, pageLimit, sort);
        if (StringUtils.isEmpty(teacherId)) {
            return studentRepo.findAll(pageable);
        } else {
            Integer id = teacherRepo.findByTeacherName(teacherId).getId();
            return studentRepo.findByTeacherId(id, pageable);
        }
    }

    @Override
    //查询grade的分页；
    public Page<StudentNew> getGradePage(String teacherId, String grade, Integer pageNum, Integer pageLimit) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNum - 1, pageLimit, sort);
        if (StringUtils.isEmpty(teacherId)) {
            return studentRepo.findByStudentGrade(grade, pageable);
        } else {
            Integer id = teacherRepo.findByTeacherName(teacherId).getId();
            List<String> grades = teachClassInfoRepo.getClassNameByTeacherId(id);
            if (grades.contains(grade)) {
                return studentRepo.findByStudentGrade(grade, pageable);
            } else {
                return studentRepo.findByStudentGrade(null, pageable);
            }
        }

    }

    //0918 在学生表中添加学生到教师的对应关系，省去教师->班级->学生的查询，优化查询速度，使教师对学生的管理更为准确直接
    @Override
    public Page<StudentNew> getGradePageList(String teacherId, String grade, Integer pageNum, Integer pageLimit) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNum - 1, pageLimit, sort);
        Teacher teacher = teacherRepo.findByTeacherName(teacherId);
        if (StringUtils.isEmpty(teacherId)) {
            return studentRepo.findByStudentGrade(grade, pageable);
        } else {
            return studentRepo.findByTeacherIdAndStudentGrade(teacher.getId(), grade, pageable);
        }
    }

    @Override
    public List<String> initGrade() {
        return studentRepo.getGrade();
    }


    public JsonResult uploadStudentList(String teacherName, List<StudentNew> studentNewList) {
        Teacher teacher = teacherRepo.findByTeacherName(teacherName);
        for (StudentNew studentNew : studentNewList) {
            if (studentRepo.findByStudentId(studentNew.getStudentId()) != null) {
                return JsonResult.errorResult(ResultCode.USER_EXIST, "Excel中的学号已存在,请检查后再导入", studentNew.getStudentId());
            } else {
                studentNewList.parallelStream().forEach(studentNew1 -> {
                    studentNew1.setTeacherId(teacher.getId());
                });
                List<StudentNew> studentNews = studentRepo.saveAll(studentNewList);
                return JsonResult.succResult("添加成功", studentNews.size());
            }
        }

        return null;
    }

    @Override
    public JsonResult uploadStudents(String teacherName, List<StudentNew> studentNewList) {

        TeachClassInfo teachClassInfo = new TeachClassInfo();
        List<TeachClassInfo> teachClassInfos = new ArrayList<>();


        for (StudentNew studentNew : studentNewList) {
            if (studentRepo.findByStudentId(studentNew.getStudentId()) != null) {
                return JsonResult.errorResult(ResultCode.USER_EXIST, "excel中的学号已存在，请检查后导入", studentNew.getStudentId());
            } else {
                String grade = studentNew.getStudentGrade();
                Teacher teacher = teacherRepo.findByTeacherName(teacherName);

                //遍历teachClassInfo表中的同一老师所带的班级
                List<TeachClassInfo> teachClassInfoList = teachClassInfoRepo.findByTeacherId(teacher.getId());

                int flag = -1;
                int teachClassInfoListSize = teachClassInfoList.size();
                if (teachClassInfoListSize == 0) {
                    if (teachClassInfos.size() == 0) {
                        TeachClassInfo t = new TeachClassInfo();
                        t.setTeacherId(teacher.getId());
                        t.setClassName(grade);
                        teachClassInfos.add(t);
                    } else {
                        for (int j = 0; j < teachClassInfos.size(); j++) {
                            if (grade.equals(teachClassInfos.get(j).getClassName())) {
                                break;
                            } else {
                                if (j == teachClassInfos.size() - 1) {
                                    TeachClassInfo t = new TeachClassInfo();
                                    t.setTeacherId(teacher.getId());
                                    t.setClassName(grade);
                                    teachClassInfos.add(t);
                                }
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < teachClassInfoListSize; i++) {
                        //1 teachClassInfoList (1,2)
                        //2 upload (2,3);
                        //upload-->teachClassInfoList(1,2,3)
                        if (grade.equals(teachClassInfoList.get(i).getClassName())) {
                            break;
                        } else {
                            if (i == teachClassInfoList.size() - 1) {
                                teachClassInfo.setTeacherId(teacher.getId());
                                teachClassInfo.setClassName(grade);
                                teachClassInfos.add(teachClassInfo);
                            }
                        }
                    }
//                teachClassInfoList.remove(flag);
                }
            }
        }

        List<StudentNew> studentNews = studentRepo.saveAll(studentNewList);
        teachClassInfoRepo.saveAll(teachClassInfos);

        return JsonResult.succResult("添加成功", studentNews.size());

    }

    @Override
    public void downloadStudents(HttpServletResponse response, String grade) throws IOException {
        List<StudentNew> studentNew = new ArrayList<StudentNew>();
        if (StringUtils.isEmpty(grade)) {
            studentNew = studentRepo.findAll();
        } else {
            studentNew = studentRepo.findByStudentGrade(grade);
        }
        List<StudentNew> rows = CollUtil.newArrayList(studentNew);

        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("studentId", "学号");
        writer.addHeaderAlias("studentName", "姓名");
        writer.addHeaderAlias("password", "密码");
        writer.addHeaderAlias("studentGrade", "班级");

        writer.write(rows, true);

        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        String value = "attachment;filename=" + URLEncoder.encode("学生名单" + ".xls", "UTF-8");
        response.setHeader("Content-Disposition", value);
        ServletOutputStream outputStream = response.getOutputStream();

        writer.flush(outputStream, true);
        writer.close();
        IoUtil.close(outputStream);

    }

    @Override
    public List<String> getGrades(String teacherId) {
        Teacher teacher = teacherRepo.findByTeacherName(teacherId);
        List<String> className = teachClassInfoRepo.getClassNameByTeacherId(teacher.getId());
        return className;
    }


    public List<String> getGradesByTeacherId(String teacherId) {
        Teacher teacher = teacherRepo.findByTeacherName(teacherId);
        List<String> grade = studentRepo.getGradeByTeacherId(teacher.getId());
        return grade;
    }
}
