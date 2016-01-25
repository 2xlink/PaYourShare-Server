package entities;

public class User {
	private String id;
	private String email;
	private String name;

	public User() {
	}
	
	public User(String id, String email) {
		setId(id);
		setEmail(email);
		setName(email);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
