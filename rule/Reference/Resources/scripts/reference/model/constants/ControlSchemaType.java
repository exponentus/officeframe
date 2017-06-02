package reference.model.constants;

public enum ControlSchemaType {
	UNKNOWN(0), RESET_ALL_MANULALLY(566), ALLOW_RESET_ON_BASIS_REPORT(567), ALLOW_RESET_ON_BASIS_COORDINATOR_REPORT(
			568);

	private int code;

	ControlSchemaType(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static ControlSchemaType getType(int code) {
		for (ControlSchemaType type : values()) {
			if (type.code == code) {
				return type;
			}
		}
		return UNKNOWN;
	}

}