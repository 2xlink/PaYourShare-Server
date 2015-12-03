package entities;

public class Error {
	private Boolean hasError;
	private String errorMessage;
	
	private Error() {
		setHasError(false);
		setErrorMessage("");
	}
	
	public Boolean getHasError() {
		return hasError;
	}
	public void setHasError(Boolean hasError) {
		this.hasError = hasError;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
