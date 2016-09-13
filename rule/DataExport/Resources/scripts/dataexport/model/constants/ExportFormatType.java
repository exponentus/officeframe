package dataexport.model.constants;

public enum ExportFormatType {
	UNKNOWN(0), XML(690), CSV(691);

	private int code;

	ExportFormatType(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static ExportFormatType getType(int code) {
		for (ExportFormatType type : values()) {
			if (type.code == code) {
				return type;
			}
		}
		return UNKNOWN;
	}
}
