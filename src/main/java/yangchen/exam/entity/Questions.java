package yangchen.exam.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Questions")
@Data
public class Questions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String QuestionBh;

    private String CustomBh;
    private Integer IsProgramming;
    private Double score;
    private Integer difficulty;
    private Integer chapter;
    private Integer stage;
    private String description;
    private String questionType;
    private String name;
    private String sourceCode;
    private String startTag;
    private String endTag;
    private String answerDescript;
    private String answer;
    private String knowledgeBh;
    private String memo;
    private String isProgramBlank;
    private String checked;
    private Timestamp addTime;
}
