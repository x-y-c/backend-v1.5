package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import yangchen.exam.entity.QuestionLog;

public interface QuestionLogRepo extends JpaRepository<QuestionLog, Integer> {

}
