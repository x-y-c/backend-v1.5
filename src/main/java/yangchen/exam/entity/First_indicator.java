package yangchen.exam.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="first_indicator")
@Data
public class First_indicator {
    @Id
   // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer firstid;
    private String content;
   // private List<Second_indicator> children;
}
