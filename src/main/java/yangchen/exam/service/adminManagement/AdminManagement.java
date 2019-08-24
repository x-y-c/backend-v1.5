package yangchen.exam.service.adminManagement;

import yangchen.exam.entity.Teacher;
import yangchen.exam.model.ClassModel;
import yangchen.exam.model.TeachClassInfoList;

import java.util.List;

public interface AdminManagement {

    //管理员编辑教师信息
    List<String> updateTeachClassInfo(TeachClassInfoList teachClassInfoList);

    //添加教师信息
    TeachClassInfoList addTeacher(TeachClassInfoList teachClassInfoList);


    //删除教师信息
    Teacher deleteTeacher(Integer teacherId);

    TeachClassInfoList getTeachClassInfo(Integer teacherId);

    List<TeachClassInfoList> getAllTeachClassInfo();

    List<ClassModel> getClassList();

}
