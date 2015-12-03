package entities;

public class Error {
	private Boolean hasError;
	private String message;
	
	public Error() {
		setHasError(false);
		setMessage("");
	}
	
	public Boolean getHasError() {
		return hasError;
	}
	public void setHasError(Boolean hasError) {
		this.hasError = hasError;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String errorMessage) {
		this.message = errorMessage;
	}
}
