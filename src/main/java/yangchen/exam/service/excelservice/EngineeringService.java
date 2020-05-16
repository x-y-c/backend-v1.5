package yangchen.exam.service.excelservice;

import yangchen.exam.entity.Engineering;
import yangchen.exam.model.JsonResult;

import java.util.List;

public interface EngineeringService {

    JsonResult uploadStudentList(String teacherName, List<Engineering> engineeringList);

    Engineering getStudentByStudentId(Integer studentId);
}
