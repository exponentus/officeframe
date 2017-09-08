package reference.model.constants;

public enum RealEstateObjStatusCode {
	UNKNOWN(0), OWNED(927), RENTED(826), ON_SALE(827), RENTAL(828);

	private int code;

	RealEstateObjStatusCode(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static RealEstateObjStatusCode getType(int code) {
		for (RealEstateObjStatusCode type : values()) {
			if (type.code == code) {
				return type;
			}
		}
		return UNKNOWN;
	}
}
