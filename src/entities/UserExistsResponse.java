package entities;

public class UserExistsResponse {
	private String status;
	private String id;
	
	public UserExistsResponse(String status, String id) {
		setStatus(status);
		setId(id);
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
}
