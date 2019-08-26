package yangchen.exam.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "question_new")
public class QuestionNew {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String customBh;
    private String questionType;
    private String stage;
    private String difficulty;
    @Column(name = "question_name")
    private String questionName;
    @Column(name = "question_description")
    private String questionDescription;
    private String sourceCode;
    private String startTag;
    private String endTag;
    private String answer;
    @Column(name = "add_time")
    private Timestamp addTime;
    private String questionBh;

    @Column(name = "question_details")
    private String questionDetails;

    @Column(name = "IsProgramBlank")
    private String isProgramBlank;

    @Column(name = "pre_question_details")
    private String preQuestionDetails;

    @Column(name = "actived")
    private Boolean actived;

    @Column(name = "memo")
    private String memo;
}
