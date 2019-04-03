package yangchen.exam.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

//ExamRecord
//考试信息，考试id，考试时间


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExamRecord {

    private String examDesc;

    private Integer examId;

    private Timestamp examTime;
}
