package yangchen.exam.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="second_indicator")
@Data

public class Second_indicator{
        @Id
        private Float firstid;
        private Integer parentid;
        private String content;

}
