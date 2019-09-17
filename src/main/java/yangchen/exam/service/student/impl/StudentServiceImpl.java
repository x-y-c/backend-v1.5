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
import yangchen.exam.entity.StudentNew;
import yangchen.exam.entity.TeachClassInfo;
import yangchen.exam.entity.Teacher;
import yangchen.exam.model.JsonResult;
import yangchen.exam.model.ResultCode;
import yangchen.exam.model.StudentInfo;
import yangchen.exam.model.StudentModifyModel;
import yangchen.exam.repo.StudentRepo;
import yangchen.exam.repo.TeachClassInfoRepo;
import yangchen.exam.repo.TeacherRepo;
import yangchen.exam.service.student.studentService;

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
public class StudentServiceImpl implements studentService {
    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private TeacherRepo teacherRepo;

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

    public JsonResult changePassword(Integer studentId, String oldpassword, String password) {
        StudentNew byStudentId = studentRepo.findByStudentId(studentId);
        if (byStudentId != null) {
            if (byStudentId.getPassword().equals(oldpassword)) {
                byStudentId.setPassword(password);
                StudentNew save = studentRepo.save(byStudentId);
                return JsonResult.succResult(null);
            } else {
                return JsonResult.errorResult(ResultCode.WRONG_PASSWORD, "旧密码错误", null);
            }

        } else {
            return JsonResult.errorResult(ResultCode.USER_NOT_EXIST, "用户不存在", null);
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

        if(studentNew==null){
            return JsonResult.errorResult(ResultCode.USER_NOT_EXIST,"用户不存在","");
        }
        else {
            studentNew.setStudentName(student.getStudentName());
            studentNew.setPassword(student.getPassword());
            studentNew.setStudentGrade(student.getStudentGrade());
            studentNew.setStudentId(student.getStudentId());
            return JsonResult.succResult(studentRepo.save(studentNew));
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
            return JsonResult.errorResult(ResultCode.USER_EXIST,"用户已存在","");
        } else {
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

    @Override
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

    @Override
    public List<String> initGrade() {
        return studentRepo.getGrade();
    }

    @Override
    public JsonResult uploadStudents(String teacherName, List<StudentNew> studentNewList) {

        TeachClassInfo teachClassInfo = new TeachClassInfo();
        List<TeachClassInfo> teachClassInfos = new ArrayList<>();

        for (StudentNew studentNew : studentNewList) {
            if (studentRepo.findByStudentId(studentNew.getStudentId()) != null) {
                return JsonResult.errorResult(ResultCode.USER_EXIST, "excel中的学号已存在，请检查后导入", studentNew.getStudentId());
            }
            else {
                String grade = studentNew.getStudentGrade();
                Teacher teacher = teacherRepo.findByTeacherName(teacherName);

                //遍历teachClassInfo表中的同一老师所带的班级
                List<TeachClassInfo> teachClassInfoList = teachClassInfoRepo.findByTeacherId(teacher.getId());
                int flag = -1;
                int teachClassInfoListSize = teachClassInfoList.size();
                if(teachClassInfoListSize==0){
                    teachClassInfo.setTeacherId(teacher.getId());
                    teachClassInfo.setClassName(grade);
                    teachClassInfos.add(teachClassInfo);
                }
                else{
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
//                            teachClassInfoRepo.save(teachClassInfo);
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
}
