# 数据库设计
## 创建考试需要的表
### exam_group_xy
| 字段名 | 类型 | 说明 |
| ---- | ---- | ---- |
| id | int | 主键自增 |
| exam_description | varchar | 考试名称 |
| class_name | list<string> | 参与考试的班级 |
| exam_teacher | varchar | 考试出卷的老师 | 
| begin_date | date | 考试开始的日期 |
| begin_time | date | 考试开始的时间 | 
| exam_time | int | 考试时长(以分为单位) |
### exam_info_xy
### exam_paper_xy

## 创建练习需要的表
### practice_group_xy
### practice_info_xy
### practice_paper_xy

## 用户信息管理
### student
### teacher

## 试题及测试用例
### question
### teat_case
