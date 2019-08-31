### CompileController

url：/compile/ 

参数的请求类型：post  form-data

| 参数          | 类型    | 说明   | 是否必填 |
| ------------- | ------- | ------ | -------- |
| code          | String  | 源代码 | 是       |
| examinationId | Integer | 试卷id | 是       |
| index | Integer | 试卷中的试题编号 | 是 |
| studentId | Integer | 学号 | 是 |

返回值：

| 参数        | 类型    | 说明                         |
| ----------- | ------- | ---------------------------- |
| compileSucc | Boolean | 是否编译成功                 |
| compileMsg  | String  | 编译信息(如果成功，就是null) |
| score       | Double  | 本题得分，百分制             |

