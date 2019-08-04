package yangchen.exam.Enum;

public enum QuestionTypeEnum {

    DIFFICULT_ONE("初级","1000401"),
    DIFFICULT_TWO("简单","1000402"),
    DIFFICULT_THREE("普通","1000403"),
    DIFFICULT_FOUR("困难","1000404"),
    DIFFICULT_FIVE("最难","1000405");


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
