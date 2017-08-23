package dataexport.model.constants;

public enum RequestType {
	UNKNOWN(0), JPQL(788),PARAMETERS_FORM(789);

	private int code;

	RequestType(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static RequestType getType(int code) {
		for (RequestType type : values()) {
			if (type.code == code) {
				return type;
			}
		}
		return UNKNOWN;
	}
}
