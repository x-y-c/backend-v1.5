package yangchen.exam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author YC
 * @date 2019/6/7 16:03
 * O(∩_∩)O)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamParam {
    /**
     * exam 考试元组  《阶段一  难》
     * beginTime 考试开始时间
     * endTime 考试结束时间
     * ttl 考试时长
     * examType 0 是练习，在时间段内都可以，1是考试，必须规定时间开始，开始时间+ttl
     *
     * {
     * 	"exam":[{"first":"阶段一","secord":"简单"}],
     * 	"title":"端午节考试",
     * 	"grades":["1503"],
     * 	"beginTime":"1557297827000",
     * 	"endTime":"1558334627000",
     * 	"ttl":7200,
     * 	"desc":"第一次考试",
     * 	"examType":0
     *        }
     *
     *
     *
     *        {
     *            exam:[],
     *            title:'',
     *            grades:[],
     *            beginTime:'',
     *            ttl:"",
     *            examType:1
     *            }
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     */
    private List<TwoTuple<String, String>> exam;
    private Timestamp beginTime;
    private Timestamp endTime;
    private Long ttl;
    private ExamType examType;
    private List<String> grades;
    private String title;
}
