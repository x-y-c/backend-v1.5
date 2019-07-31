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

    @Query(value = "select * from  exam_info where studentNumber = ? order by id desc ",nativeQuery = true)
    List<ExamInfo> getExamGroup(Integer studentId);

    ExamInfo findByExaminationId(Integer examinationId);

    List<ExamInfo> findByExamGroupId(Integer examGroupId);


    List<ExamInfo> findByStudentNumberAndExamStartAfter(Integer studentId, Timestamp timestamp);

    List<ExamInfo> findByStudentNumberAndExamStartBeforeAndExamEndAfter(Integer studentId, Timestamp timestamp, Timestamp endTime);

    List<ExamInfo> findByStudentNumberAndExamEndBefore(Integer studentId, Timestamp timestamp);

    @Query(value = "select  * from exam_info join examation on exam_info.examination_id=examation.id\n" +
            "where examation.used=1 and  student_number=?", nativeQuery = true)
    List<ExamInfo> getFinishedExam(Integer studentId);

    List<ExamInfo> getByExamGroupId(Integer examGroupId);

    @Transactional
    @Modifying
    void deleteExamInfoByExamGroupId(Integer id);

    @Query(value = "select examination_id from exam_info where exam_group_id=?",nativeQuery = true)
    List<Integer> searchExamPaper(Integer id);

}
