package entities;

public class UserInfoResponse {
	private String status;
	private User user;
	
	public UserInfoResponse () {}
	public UserInfoResponse(String status, User user) {
		setStatus(status);
		setUser(user);
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
