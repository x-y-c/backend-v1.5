package yangchen.exam.service.adminManagement.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yangchen.exam.entity.Administrator;
import yangchen.exam.entity.TeachClassInfo;
import yangchen.exam.entity.Teacher;
import yangchen.exam.model.ClassModel;
import yangchen.exam.model.TeachClassInfoList;
import yangchen.exam.repo.AdministratorRepo;
import yangchen.exam.repo.StudentRepo;
import yangchen.exam.repo.TeachClassInfoRepo;
import yangchen.exam.repo.TeacherRepo;
import yangchen.exam.service.adminManagement.AdminManagement;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminManagementImpl implements AdminManagement {

    @Autowired
    private TeachClassInfoRepo teachClassInfoRepo;

    @Autowired
    private AdministratorRepo administratorRepo;

    @Autowired
    private TeacherRepo teacherRepo;

    @Autowired
    private StudentRepo studentRepo;

    @Override
    public List<String> updateTeachClassInfo(TeachClassInfoList teachClassInfoList) {
        List<String> teachClassList = teachClassInfoList.getTeachClassList();
        teachClassInfoRepo.deleteByTeacherId(teachClassInfoList.getTeacherId());
        teachClassList.parallelStream().forEach(className -> {
                    TeachClassInfo teachClassInfo = new TeachClassInfo();
                    teachClassInfo.setClassName(className);
                    teachClassInfo.setTeacherId(teachClassInfoList.getTeacherId());
                    teachClassInfoRepo.saveAndFlush(teachClassInfo);
                }
        );
        return teachClassList;
    }

    @Override
    public TeachClassInfoList addTeacher(TeachClassInfoList teachClassInfoList) {
        List<String> teachClassList = teachClassInfoList.getTeachClassList();
        Teacher teacher = new Teacher();
        teachClassList.forEach(teachClass -> {
            teacher.setTeacherName(teachClassInfoList.getTeacherName());
            teacher.setPassword("123456");
            Teacher teacherSaved = teacherRepo.save(teacher);
            TeachClassInfo teachClassInfo = new TeachClassInfo();
            teachClassInfo.setClassName(teachClass);
            teachClassInfo.setTeacherId(teacherSaved.getId());
            teachClassInfoRepo.save(teachClassInfo);
        });

        return teachClassInfoList;
    }


    @Override
    public Teacher deleteTeacher(Integer teacherId) {
        Teacher teacher = teacherRepo.findById(teacherId).get();
        teacher.setActive(Boolean.FALSE);
        Teacher teacherUpdated = teacherRepo.save(teacher);
        teachClassInfoRepo.deleteByTeacherId(teacherId);
        return teacherUpdated;

    }

    @Override
    public TeachClassInfoList getTeachClassInfo(Integer teacherId) {
        List<String> classNameByTeacherId = teachClassInfoRepo.getClassNameByTeacherId(teacherId);
        TeachClassInfoList teachClassInfoList = new TeachClassInfoList();
        Teacher teacher = teacherRepo.findById(teacherId).get();
        teachClassInfoList.setTeacherName(teacher.getTeacherName());
        teachClassInfoList.setTeacherId(teacherId);
        teachClassInfoList.setTeachClassList(classNameByTeacherId);
        return teachClassInfoList;
    }

    @Override
    public List<TeachClassInfoList> getAllTeachClassInfo() {
        List<Integer> teacherIdList = teacherRepo.getTeacherId();

        List<TeachClassInfoList> result = new ArrayList<>(teacherIdList.size());
        teacherIdList.forEach(teacherId -> {
            //teacherIdList.parallelStream().forEach(teacherId -> {
            //List<String> classNameList = teachClassInfoRepo.getClassNameByTeacherId(teacherId);
            Teacher teacher = teacherRepo.findById(teacherId).get();
            TeachClassInfoList teachClassInfoList = new TeachClassInfoList();
            teachClassInfoList.setTeacherId(teacherId);
            List<String> classNameList = studentRepo.getGradeByTeacherId(teacher.getId());
            teachClassInfoList.setTeachClassList(classNameList);
            teachClassInfoList.setTeacherName(teacher.getTeacherName());
            teachClassInfoList.setShow(Boolean.FALSE);
            result.add(teachClassInfoList);

        });
        return result;
    }

    @Override
    public List<ClassModel> getClassList() {
        List<String> grade = studentRepo.getGrade();
        List<ClassModel> result = new ArrayList<>(grade.size());
        grade.forEach(s -> {
            ClassModel classModel = new ClassModel();
            classModel.setLabel(s);
            classModel.setValue(s);
            result.add(classModel);
        });
        return result;
    }

    @Override
    public Administrator findByAdminName(String name) {
        Administrator administrator = administratorRepo.findByAdminNameAndActived(name,Boolean.TRUE);
        return administrator;
    }

}



