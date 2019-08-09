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
import yangchen.exam.model.ExcelScoreModel;
import yangchen.exam.model.JsonResult;
import yangchen.exam.model.ResultCode;
import yangchen.exam.model.StudentInfo;
import yangchen.exam.repo.StudentRepo;
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
    public StudentNew addStudent(StudentNew student) {
        if (student.getPassword() == null || student.getPassword().length() <= 0) {
            student.setPassword("123456");
        }

        StudentNew byStudentId = studentRepo.findByStudentId(student.getStudentId());
        if (byStudentId != null) {
            byStudentId.setStudentGrade((student.getStudentGrade()));
            return studentRepo.save(byStudentId);
        }
        return studentRepo.save(student);
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
    public Page<StudentNew> getPage(Integer pageNum, Integer pageLimit) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNum - 1, pageLimit, sort);
        return studentRepo.findAll(pageable);
    }

    @Override
    public Page<StudentNew> getGradePage(String grade, Integer pageNum, Integer pageLimit) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNum - 1, pageLimit,sort);
        return studentRepo.findByStudentGrade(grade, pageable);
    }

    @Override
    public List<String> initGrade() {
        return studentRepo.getGrade();
    }

    @Override
    public JsonResult uploadStudents(List<StudentNew> studentNewList) {

        for (StudentNew studentNew : studentNewList) {
            if (studentRepo.findByStudentId(studentNew.getStudentId()) != null) {
                return JsonResult.errorResult(ResultCode.USER_EXIST, "excel中的学号已存在，请检查后导入", null);
            }
        }

        List<StudentNew> studentNews = studentRepo.saveAll(studentNewList);
        return JsonResult.succResult("添加成功", studentNews.size());
    }

    @Override
    public void downloadStudents(HttpServletResponse response, String grade) throws IOException {
        List<StudentNew> studentNew = new ArrayList<StudentNew>();
        if(StringUtils.isEmpty(grade)){
            studentNew = studentRepo.findAll();
        }else {
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
}
