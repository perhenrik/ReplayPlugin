package no.perhenrik.replayplugin;

public class ReturnValue {
	private String message;
	private boolean isOk;
	
	public ReturnValue() {
		this.setMessage("");
		this.setOk(true);
	}

	public String getMessage() {
		return message;
	}

	public ReturnValue setMessage(String message) {
		this.message = message;
		return this;
	}

	public boolean isOk() {
		return isOk;
	}
	
	public ReturnValue setOk(boolean isOk) {
		this.isOk = isOk;
		return this;
	}	
}
