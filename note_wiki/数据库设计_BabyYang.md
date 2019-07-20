# 数据库设计
## 创建考试需要的表
### exam_group_new
| 字段名 | 类型 | 说明 |
| :----: | :----: | :----: |
| id | int | 主键自增 |
| exam_description | varchar | 考试名称 |
| class_name | list\<string\> | 参与考试的班级 |
| exam_teacher | varchar | 考试出卷的老师 | 
| begin_date | timeStamp | 考试开始的日期 |
| begin_time | timeStamp | 考试开始的时间 | 
| exam_time | int | 考试时长(以分为单位) |

### exam_paper_new
| 字段名 | 类型 | 说明 |
| :----: | :----: | :----: |
| id | int | 主键自增 |
| title_id | list\<Integer\> | 试卷题目id（对应question表） |
  
### exam_info_new
| 字段名 | 类型 | 说明 |
| :----: | :----: | :----: |
| id | int | 主键自增 |
| student_id | int | 学生学号 |
| exam_group_id | int | 考试组号（标记是哪一场考试） |
| exam_paper_id | int | 考试卷号（标记这个学生的卷子id） |

### score_new
| 字段名 | 类型 | 说明 |
| :----: | :----: | :----: |
| id | int | 主键自增 |
| student_id | int | 学生学号 |
| exam_paper_id | int | 考试卷号 |
| question_id | int | 试卷上的第几题 |
| score | int | 单题成绩 |

## 考试提交相关的表
### submit_new
| 字段名 | 类型 | 说明 |
| :----: | :----: | :----: |
| id | int | 主键自增 |

### last_submit_new
| 字段名 | 类型 | 说明 |
| :----: | :----: | :----: |
| id | int | 主键自增 |

## 创建练习需要的表
### practice_group_new
### practice_info_new
### practice_paper_new

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
| Question | int | 题型 |
| Stage | int | 阶段(9种) |
| Difficulty | int | 难度（5种） |
| name | varchar | 题目缩略描述 |
| Description | varchar | 题目具体描述 |
| SourceCode | varchar | 源代码 |
| startTag | varchar | 代码填空题起始位置 |
| endTag | varchar | 代码填空题结束位置 |
| Answer | varchar |选择题，判断题答案 |
| Add_time | timestamp | 添加题目的时间 |
//先说这么多  有些字段还是觉得怪怪的


### teat_case_new
| 字段名 | 类型 | 说明 |
| :----: | :----: | :----: |
| id | int | 主键自增 |

## 用户多点登陆ip表
### ip_addr_new
| 字段名 | 类型 | 说明 |
| :----: | :----: | :----: |
| id | int | 主键自增 |
