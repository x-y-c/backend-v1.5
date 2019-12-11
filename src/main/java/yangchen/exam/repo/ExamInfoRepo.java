package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import yangchen.exam.entity.ExamInfo;
import yangchen.exam.entity.ExamPaper;

import java.sql.Timestamp;
import java.util.List;

public interface ExamInfoRepo extends JpaRepository<ExamInfo, Integer> {

    List<ExamInfo> findByStudentNumber(Integer studentId);

    @Query(value = "select * from  exam_info where student_number = ? order by id desc ",nativeQuery = true)
    List<ExamInfo> getExamGroup(Integer studentId);

    ExamInfo findByExaminationId(Integer examinationId);

    List<ExamInfo> findByExamGroupId(Integer examGroupId);

    @Transactional
    @Modifying
    void deleteExamInfoByExamGroupId(Integer id);

    @Query(value = "select examination_id from exam_info where exam_group_id=?",nativeQuery = true)
    List<Integer> searchExamPaper(Integer id);

    ExamInfo findByStudentNumberAndExamGroupId(Integer studentId,Integer examGroupId);

}
