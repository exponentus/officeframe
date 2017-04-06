package reference.model.constants;

public enum ApprovalType {
	UNKNOWN(0), SERIAL(45), PARALLEL(46), SIGNING(47);

	private int code;

	ApprovalType(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static ApprovalType getType(int code) {
		for (ApprovalType type : values()) {
			if (type.code == code) {
				return type;
			}
		}
		return UNKNOWN;
	}

}
