package monitoring.model.constants;

public enum ActivityType {
	UNKNOWN(0), LOGIN(580), LOGOUT(581);

	private int code;

	ActivityType(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static ActivityType getType(int code) {
		for (ActivityType type : values()) {
			if (type.code == code) {
				return type;
			}
		}
		return UNKNOWN;
	}
}
