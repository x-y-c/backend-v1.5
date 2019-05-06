package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import yangchen.exam.entity.ExamGroup;

/**
 * @author YC
 * @date 2019/5/6 16:09
 * O(∩_∩)O)
 */
public interface examGroupRepo extends JpaRepository<ExamGroup,Integer> {
}
