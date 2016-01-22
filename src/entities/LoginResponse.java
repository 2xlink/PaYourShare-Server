package entities;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LoginResponse {
	private String status;
	private String id; //TODO: is this needed?
	private String token;
	
	public LoginResponse() {}
	public LoginResponse(String status, String id, String token) {
		setStatus(status);
		setId(id);
		setToken(token);
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
