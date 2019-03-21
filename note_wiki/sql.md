### 学生表 student
字段名 | 类型 | 说明
-|-|-
 enabled | Boolean | 表明该学生信息是否有效
id | Integer | 主键自增id
student_id | Long | 学号
name | String |  姓名 
major |  String | 专业
grade | String |  年级班级信息
createdTime | Timestamp | 创建时间



### 题目表 question
字段名 | 类型 | 说明
-|-|-
id | Integer | 主键自增id
qid | Integer | 题目id
name | String |  题目名称
description | String | 题目描述
category |Category |  阶段信息（该题目是哪个阶段的题目，如 数组） 

### 权限表(用户权限)
字段名 | 类型 | 说明
-|-|-
 id|Integer | 主键id
email  |String | 邮箱信息
password  | String | 密码
name  | String | 姓名 
enabled | Boolean | 是否有效
 role   | String |  管理员，学生


### 教师表：
字段| 类型|说明
-|-|-
id|Integer|(主键自增)
teacher_name | String | 教师姓名
teach_class | String | 教师所教班级
password | String | 密码，用于登录（考虑加密）
email | String  | 用于登录 



### 试卷表：
字段 | 类型 | 说明 
-|-|-
id  |Integer  | 主键自增ID
active | Boolean | 是否生效
title_id | String |  题目列表 (题号,题号,)
title_type| String| 题目类型 
used | Boolean  | 是否分配

### 考试信息:
字段 | 类型 | 说明
-|-|-
student_number | Long | 学生学号
id | Integer  | 试卷id
created_at | Timestamp |创建时间
teacher_name | String |操作教师


### 测试用例表：(test case )
字段 |  类型 | 说明
-|-|-
id | Integer | 主键自增id
qid | Integer |  题目id
input | String | 输入信息
output | String | 输出信息
score | Integer | 测试用例占用的分值


###  testInfo(每次用户提交的数据)：
字段 | 类型 | 说明
-|-|-
id| Integer | 提交Id
student_id | Integer  | 学号
question_id | Integer  | 题目id
submitedTime | TimeStamp | 提交时间
code  | String | 代码 
correctRate  | Double |  正确率
TestCaseId  | String  | 1,2,3




private List<测试用例信息表和该测试用例的通过率>

单独的model记录信息；








