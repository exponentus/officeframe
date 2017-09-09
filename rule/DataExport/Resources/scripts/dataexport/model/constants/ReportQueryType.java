package dataexport.model.constants;

public enum ReportQueryType {
	UNKNOWN(0), JPQL(788), ENTITY_REQUEST(789), CUSTOM_CLASS(790);

	private int code;

	ReportQueryType(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static ReportQueryType getType(int code) {
		for (ReportQueryType type : values()) {
			if (type.code == code) {
				return type;
			}
		}
		return UNKNOWN;
	}
}
