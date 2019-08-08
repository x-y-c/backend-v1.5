package yangchen.exam.Enum;

public enum QuestionTypeEnum {

    QUESTION_TIANKONG("填空题","1000204"),
    QUESTION_CODING("编程题","1000206"),
    QUESTION_PANDUAN("判断题","1000203"),
    QUESTION_XUANZE("选择题","100020101"),
    QUESTION_CESHI("测试","1000205");


    private String questionTypeName;
    private String questionTypeCode;

    private QuestionTypeEnum(String questionTypeName, String questionTypeCode){
        this.questionTypeCode = questionTypeCode;
        this.questionTypeName = questionTypeName;
    }


    //通过难度编码获取难度名称
    public static String getQuestionTypeCode(String questionTypeName){
        for (QuestionTypeEnum questionTypeEnum:values()){
            if (questionTypeEnum.getQuestionTypeName().equals(questionTypeName)){
                return questionTypeEnum.getQuestionTypeCode();
            }
        }
        return null;
    }

    public static  String getQuestionTypeName(String questionTypeCode){
        for (QuestionTypeEnum questionTypeEnum:values()){
            if(questionTypeEnum.getQuestionTypeCode().equals(questionTypeCode)){
                return questionTypeEnum.getQuestionTypeName();
            }
        }
        return null;
    }

    public String getQuestionTypeName(){
        return questionTypeName;
    }

    public void setQuestionTypeName(String questionTypeName){
        this.questionTypeName = questionTypeName;
    }

    public String getQuestionTypeCode(){
        return questionTypeCode;
    }

    public void setQuestionTypeCode(String questionTypeCode){
        this.questionTypeCode = questionTypeCode;
    }



}
