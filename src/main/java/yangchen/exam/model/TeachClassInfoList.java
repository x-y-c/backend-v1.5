package yangchen.exam.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor


@ApiModel(value = "TeachClassInfoList", description = "教师对应班级信息class")
public class TeachClassInfoList {

    @ApiModelProperty(value = "教师编号", name = "teacherId", example = "1")
    private Integer teacherId;

    @ApiModelProperty(value = "教师姓名", name = "teacherName", example = "徐洋")
    private String teacherName;

    @ApiModelProperty(value = "前端用于切换状态字段", name = "show", example = "false")
    private Boolean show;

    @ApiModelProperty(value = "班级列表", dataType = "List<String>")
    private List<String> teachClassList;
}
