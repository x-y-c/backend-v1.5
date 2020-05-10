package yangchen.exam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectParam {

    private Timestamp beginTime;
    private Integer ttl;
    private List<String> grades;
    private String homeworkName;
    private String teacherName;
    private List<Integer> homework;

}
