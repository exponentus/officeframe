package monitoring.model.constants;

public enum ActivityType {
    UNKNOWN(0), LOGIN(580), LOGOUT(581), SENT_EMAIL(582), COMPOSE(583), SENT_SLACK_MESSAGE(584);

    private int code;

    ActivityType(int code) {
        this.code = code;
    }

    public static ActivityType getType(int code) {
        for (ActivityType type : values()) {
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
