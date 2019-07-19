# 数据库设计
## 创建考试需要的表
### exam_group_new
| 字段名 | 类型 | 说明 |
| :----: | :----: | :----: |
| id | int | 主键自增 |
| exam_description | varchar | 考试名称 |
| class_name | list<string> | 参与考试的班级 |
| exam_teacher | varchar | 考试出卷的老师 | 
| begin_date | date | 考试开始的日期 |
| begin_time | date | 考试开始的时间 | 
| exam_time | int | 考试时长(以分为单位) |

### exam_paper_new
| 字段名 | 类型 | 说明 |
| :----: | :----: | :----: |
| id | int | 主键自增 |
| title_id | list<Integer> | 试卷题目id（对应question表） |
  
### exam_info_new
| 字段名 | 类型 | 说明 |
| :----: | :----: | :----: |
| id | int | 主键自增 |
| student_id | int | 学生学号 |
| exam_group_id | int | 考试组号（标记是哪一场考试） |
| exam_paper_id | int | 考试卷号（标记这个学生的卷子id） |
| exam_score | int | 考试成绩 |

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

### teat_case_new
| 字段名 | 类型 | 说明 |
| :----: | :----: | :----: |
| id | int | 主键自增 |
