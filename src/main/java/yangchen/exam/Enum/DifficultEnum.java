package yangchen.exam.Enum;

public enum DifficultEnum {
    DIFFICULT_ONE("初级","1000401"),
    DIFFICULT_TWO("简单","1000402"),
    DIFFICULT_THREE("普通","1000403"),
    DIFFICULT_FOUR("困难","1000404"),
    DIFFICULT_FIVE("最难","1000405");


    private String difficultName;
    private String difficultCode;

    private DifficultEnum(String difficultName,String difficultCode){
        this.difficultCode=difficultCode;
        this.difficultName=difficultName;
    }


    //通过难度编码获取难度名称
    public static String getDifficultCode(String difficultName){
        for (DifficultEnum difficultEnum:values()){
            if (difficultEnum.getDifficultName().equals(difficultName)){
                return difficultEnum.getDifficultCode();
            }
        }
    return null;
    }

    public static  String getDifficultName(String difficultCode){
        for (DifficultEnum difficultEnum:values()){
            if(difficultEnum.getDifficultCode().equals(difficultCode)){
                return difficultEnum.getDifficultName();
            }
        }
        return null;
    }

    public String getDifficultName(){
        return difficultName;
    }

    public void setDifficultName(String difficultName){
        this.difficultName=difficultName;
    }

    public String getDifficultCode(){
        return difficultCode;
    }

    public void setDifficultCode(String difficultCode){
        this.difficultCode=difficultCode;
    }
}
