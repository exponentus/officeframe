package discussing.model.constants;

public enum TopicStatusType {
    UNKNOWN(0), OPEN(656), CLOSE(657), DRAFT(658);

    private int code;

    TopicStatusType(int code) {
        this.code = code;
    }

    public static TopicStatusType getType(int code) {
        for (TopicStatusType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        return UNKNOWN;
    }

    public int getCode() {
        return code;
    }
}
