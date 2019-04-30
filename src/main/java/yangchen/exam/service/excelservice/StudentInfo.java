package yangchen.exam.service.excelservice;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author YC
 * @date 2019/4/30 11:54
 * O(∩_∩)O)
 */

/**
 * excel和学生数据库映射的实体类
 * studentId:学号；
 * name：姓名：
 * grade 班级
 * major: 专业
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentInfo extends BaseRowModel {


    @ExcelProperty(index = 0)
    private Long studentId;

    @ExcelProperty(index = 1)
    private String name;

    @ExcelProperty(index = 2)
    private String grade;

    @ExcelProperty(index = 3)
    private String major;
}
