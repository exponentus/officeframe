package reference.model.constants;

public enum ApprovalSchemaType {
	UNKNOWN(0), REJECT_IF_NO(456), IN_ANY_CASE_DECIDE_SIGNER(457);

	private int code;

	ApprovalSchemaType(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static ApprovalSchemaType getType(int code) {
		for (ApprovalSchemaType type : values()) {
			if (type.code == code) {
				return type;
			}
		}
		return UNKNOWN;
	}

}
