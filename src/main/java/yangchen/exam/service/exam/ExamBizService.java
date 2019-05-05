package yangchen.exam.service.exam;

import yangchen.exam.entity.Examination;
import yangchen.exam.model.Category;

import java.sql.Timestamp;
import java.util.List;

public interface ExamBizService {

    //自动编写试卷(阶段,数量)
    Boolean createExam(Category category, Integer count);

    //阶段，年级，考试时长
    Boolean createExam(Category category, String grade, Integer timeTtl);

    //根据专业，班级，时间出卷
    Boolean createExam(String grade, Timestamp timestamp);

    //通过时间查询试卷
    List<Examination> findExamByTime(Timestamp timestamp);

    //通过年级和专业查询试卷
    List<Examination> findExamByGradeAndMajor(String grade);

    //编辑试卷
    Examination updateExam(Examination examination);

    //查看试卷
    Examination getExamById(Integer examination);





}
