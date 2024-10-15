package memo.common;

public enum PreferenceField {

	FONT("Font"),
	FONT_COLOR("Font Color"),
	
	DATABASE("Database"),
	DRIVER_PATH("Driver Path"),
	DRIVER_CLASS("Driver Class"),
	CONNECTION_URL("Connection URL"),
	USERNAME("Username"),
	PASSWORD("Password");
	
	private final String text;
	

	private PreferenceField(String text) {
		this.text = text;
	}

	public String text() {
		return text;
	}
	
}
