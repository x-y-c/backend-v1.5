package yangchen.exam.Enum;

public enum StageEnum {
    STAGE_ONE("阶段一", "1000301"),
    STAGE_TWO("阶段二", "1000302"),
    STAGE_THREE("阶段三", "1000303"),
    STAGE_FOUR("阶段四", "1000304"),
    STAGE_FIVE("阶段五", "1000305"),
    STAGE_SIX("阶段六", "1000306"),
    STAGE_SEVER("阶段七", "1000307"),
    STAGE_EIGHT("阶段八", "1000308"),
    STAGE_NIGHT("阶段九", "1000309");

    private String stageName;
    private String stageCode;

    private StageEnum(String stageName, String stageCode) {
        this.stageName = stageName;
        this.stageCode = stageCode;
    }

    public static String getStageCode(String stageName) {
        for (StageEnum stageEnum : values()) {
            if (stageEnum.getStageName().equals(stageName.trim())) {
                return stageEnum.getStageCode();
            }
        }
        return null;
    }


    public static String getStageName(String stageCode) {
        for (StageEnum stageEnum : values()) {
            if (stageEnum.getStageCode().equals(stageCode)) {
                return stageEnum.getStageName();
            }
        }
        return null;
    }


    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public String getStageCode() {
        return stageCode;
    }

    public void setStageCode(String code) {
        this.stageCode = code;
    }


}
