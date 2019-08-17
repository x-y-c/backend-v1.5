# 数据库设计
## 创建考试需要的表
### exam_group_new
| 字段名 | 类型 | 说明 |
| :----: | :----: | :----: |
| id | int | 主键自增 |
| exam_description | varchar(50) | 考试名称 |
| class_name | varchar(50) | 参与考试的班级 |
| begin_date | timeStamp | 考试开始的日期 |
| begin_time | timeStamp | 考试开始的时间 | 
| exam_time | int(11) | 考试时长(以分为单位) |
| exam_teacher | varchar(50) | 考试出卷的老师 |
其中exam_teacher 字段暂留 意义待商榷

### exam_paper_new
| 字段名 | 类型 | 说明 |
| :----: | :----: | :----: |
| id | int | 主键自增 |
| title_id | varchar(50) | 试卷题目id（对应question表）,list形式以逗号分隔 |
| finished | tinyint(4) | default=0 判断此张试卷是否做过， 0未做，1已做 |
  
### exam_info
| 字段名 | 类型 | 说明 |
| :----: | :----: | :----: |
| id | int | 主键自增 |
| student_number | int(11) | 学生学号 |
| created_at | timestamp | 试卷创建时间 |
| teacher_name | varchar(40) | 教师姓名 |
| examination_id | int(11) | 考试卷号（标记这个学生的卷子id）| 
| exam_group_id | int(11) | 考试组号（标记是哪一场考试） |
| examination_score | int(11) | 学生考试成绩 |
| student_name | varchar(30) | 学生姓名 |

> 以上三张表，在创建考试时生成相应数据。  
> exam_group记录考试组相关信息，exam_paper记录试卷相关信息。exam_info记录试卷与考生与考试组的联系。  
> 其中exam_group 和 exam_paper之间没有关联，只对应着考试信息和试卷，exam_info中的student_number字段（对应学生表的student_id），examination_id字段(对应exam_paper表中的id)，exam_group_id字段（对应exam_group表中的id)）将学生，试卷和考试组联系起来。

### score
| 字段名 | 类型 | 说明 |
| :----: | :----: | :----: |
| id | int | 主键自增 |
| student_id | int(11) | 学生学号 |
| examination_id | int(11) | 试卷id |
| score | int(11) | 当前题目的得分 |
| number | int(11) | 试卷中的第几题 |
| question_id | varchar(50) | 题目对应的questionId |

> 当学生登陆进入考试页面时，页面一共五道题，examination_id对应exam_paper表中的id字段，number对应试卷页面的index序号，即是当前页面的第几题，然后可以通过exam_paper中的title_id定位到是question表中的第几题。question_id对应question表中的questionBh字段。score为当前题的百分制得分。student_id对应student表的student_id字段。

## 考试提交相关的表
### submit
| 字段名 | 类型 | 说明 |
| :----: | :----: | :----: |
| id | int | 主键自增 |
| submit_time | timestamp | 提交时间 | 
| student_id | int(11) | 学生学号 |
| question_id | varchar(50) | 试题id |
| examination_id | int(11) | 试卷id |
| src | text | 提交的源代码 |

> submit表记录学生提交试题的记录，stduent_id对应sudent表的student_id字段，question_id对应Question表的questionBh字段，examination_id对应exam_paper中的id字段。


## 用户信息管理
### student_new
| 字段名 | 类型 | 说明 |
| :----: | :----: | :----: |
| id | int | 主键自增 |
| student_id | int | 学生学号 |
| student_name | varchar | 学生姓名 |
| student_grade | varchar | 学生班级 |
| student_password | int | 账号密码 |

### teacher_new
| 字段名 | 类型 | 说明 |
| :----: | :----: | :----: |
| id | int | 主键自增 |
| teacher_id | int | 教师工号 |
| teacher_name | varchar | 教师姓名 |
| teacher_grade | varcher | 教师所属院系 |
| teacher_password | int | 账号密码 |

## 试题及测试用例
### question_new
| 字段名 | 类型 | 说明 |
| :----: | :----: | :----: |
| id | int | 主键自增 |
| CustomBh | varchar | 出题人编号 |
| QuestionType | int | 题型 |
| Stage | int | 阶段(9种) |
| Difficulty | int | 难度（5种） |
| question_name | varchar | 题目缩略描述 |
| question_description | varchar | 题目具体描述 |
| SourceCode | varchar | 源代码 |
| startTag | varchar | 代码填空题起始位置 |
| endTag | varchar | 代码填空题结束位置 |
| answer | varchar |选择题，判断题答案 |
| add_time | timestamp | 添加题目的时间 |
| questionBh | varchar(50) | 题目编号 |
| question_details | text | 题目描述（new） |
| IsProgramBlank | varchar(50) | 是否为代码填空题 |
| pre_question_details | longtext | base64格式的问题描述 |
| actived | tinyint(4) | 题目是否被删 default=1 |


### teat_case_new
| 字段名 | 类型 | 说明 |
| :----: | :----: | :----: |
| TestcaseBh | varchar(32) | 自生成UUID |
| ScoreWeight | decimal(10,2) | 测试用例权重 |
| TestCaseInput | longtext | 测试用例输入 |
| TestcaseOutput | longtext | 测试用例输出 |
| TextCaseTips | longtext | （未知） |
| QuestionId | varchar(32) | 试题编号 |
| Memo | varchar(500) | （未知） |

> test_case表记录试题的测试用例，QuestionId对应question表的QuestionBh的字段。

## 用户多点登陆ip表
### ip_addr
| 字段名 | 类型 | 说明 |
| :----: | :----: | :----: |
| id | int | 主键自增 |
| browser | varchar(255) | 登陆的浏览器信息 |
| ip_addr | varchar(255) | ip地址 |
| login_time | timestamp | 登陆此ip地址的时间 |
| student_id | int(11) | 学生学号 |
| student_name | varchar(50) | 学生姓名 |
| exam_group_id | int(11) | 学生考试组id |
| exam_group_desc | varchar(255) | 学生考试组名称 |

> 用户多点登陆记录异常IP的表，student_id对应student表中的student_id字段，exam_group_id对应exam_group表中的id字段。


这是目前数据库中有效的十张表，存在字段冗余情况，表与表之间的联系用逻辑限定，没有设相关外键约束，增加了索引。

