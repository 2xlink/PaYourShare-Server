package entities;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LoginResponse {
	private String status;
	private Error error;
	private String id;
	private String token;
	
	public LoginResponse() {}
	public LoginResponse(Error er, String status, String id) {
		setStatus(status);
		setError(er);
		setId(id);
	}

	public Error getError() {
		return error;
	}

	public void setError(Error error) {
		this.error = error;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}
