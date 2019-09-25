package yangchen.exam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionUpdate {

        private Integer id;
        private String customBh;
        private String questionType;
        private String stage;
        private String difficulty;
        private String questionName;
        private String sourceCode;
        private String startTag;
        private String endTag;
        private String answer;
        private Timestamp addTime;
        private String questionBh;
        private String questionDetails;
        private String isProgramBlank;
        private String preQuestionDetails;
        private Boolean actived;
        private String memo;


}



