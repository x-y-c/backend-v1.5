package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import yangchen.exam.entity.Score;

import java.util.List;

/**
 * @author YC
 * @date 2019/6/1 15:53
 * O(∩_∩)O)
 */
public interface ScoreRepo extends JpaRepository<Score, Integer> {
    /**
     * 通过学号和考试标号查成绩；
     *
     * @param studentId
     * @param examinationId
     * @return
     */
    List<Score> findByStudentIdAndExaminationId(Long studentId, Integer examinationId);

    Score findByStudentIdAndExaminationIdAndIndex(Long studentId, Integer examinationId, Integer index);

}
