package yangchen.exam.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import yangchen.exam.model.QuestionUpdate;

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
    //是这行嘛？
//    @Column(name = "question_description")
//    private String questionDescription;
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

    @Column(name = "actived")
    private Boolean actived;

    @Column(name = "memo")
    private String memo;

    public QuestionNew(QuestionUpdate questionUpdate){
        this.questionBh = questionUpdate.getQuestionBh();
        this.questionDetails=questionUpdate.getQuestionDetails();
        this.questionName=questionUpdate.getQuestionName();
        this.questionType=questionUpdate.getQuestionType();
        this.memo = questionUpdate.getMemo();
        this.addTime = questionUpdate.getAddTime();
        this.isProgramBlank = questionUpdate.getIsProgramBlank();
        this.stage = questionUpdate.getStage();
        this.difficulty = questionUpdate.getDifficulty();
        this.actived = questionUpdate.getActived();
        this.answer = questionUpdate.getAnswer();
        this.customBh = questionUpdate.getCustomBh();
        this.sourceCode = questionUpdate.getSourceCode();
        this.startTag = questionUpdate.getStartTag();
        this.endTag = questionUpdate.getEndTag();
    }
}
