package calendar.model.constants;

public enum ReminderType {
    UNKNOWN(0), SILENT(781), E_MAIL(782);

    private int code;

    ReminderType(int code) {
        this.code = code;
    }

    public static ReminderType getType(int code) {
        for (ReminderType type : values()) {
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
