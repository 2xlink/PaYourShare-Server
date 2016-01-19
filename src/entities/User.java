package entities;

public class User {
	private Integer id;
	private String email;

	public User() {
	}
	
	public User(Integer id, String email) {
		setId(id);
		setEmail(email);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
