package yangchen.exam.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "question_log")
public class QuestionLog{

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "question_bh")
    private String questionBh;

    @Column(name = "edit_custom_bh")
    private String editCustomBh;

    @Column(name = "option_do")
    private String optionDo;

    @Column(name = "edit_time")
    private Timestamp editTime;

    @PrePersist
    public void init() {
        this.editTime = new Timestamp(System.currentTimeMillis());
    }


}
