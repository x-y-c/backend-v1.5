package yangchen.exam.Enum;

public enum UserTypeEnum {
    USER_TYPE_ONE("学生", "0"),
    USER_TYPE_TWO("教师", "1"),
    USER_TYPE_THREE("管理员", "2");

    private String userTypeName;
    private String userTypeCode;

    private UserTypeEnum(String userTypeName, String userTypeCode) {
        this.userTypeName = userTypeName;
        this.userTypeCode = userTypeCode;
    }

    public static String getUserTypeCode(String userTypeName) {
        for (UserTypeEnum userTypeEnum : values()) {
            if (userTypeEnum.getUserTypeName().equals(userTypeName.trim())) {
                return userTypeEnum.getUserTypeCode();
            }
        }
        return null;
    }


    public static String getUserTypeName(String userTypeCode) {
        for (UserTypeEnum userTypeEnum : values()) {
            if (userTypeEnum.getUserTypeCode().equals(userTypeCode)) {
                return userTypeEnum.getUserTypeName();
            }
        }
        return null;
    }


    public String getUserTypeName() {
        return userTypeName;
    }

    public void setUserTypeName(String userTypeName) {
        this.userTypeName = userTypeName;
    }

    public String getUserTypeCode() {
        return userTypeCode;
    }

    public void setUserTypeCode(String code) {
        this.userTypeCode = code;
    }


}
