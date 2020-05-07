package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import yangchen.exam.entity.First_indicator;

import java.util.List;

public interface First_indicatorRepo extends JpaRepository<First_indicator,Integer> {
  // @Query(value="select * from first_indicator f join second_indicator s on f.firstid=s.first_id",nativeQuery = true)
   //List<Object> getByfirstid(Integer first_id);

}
