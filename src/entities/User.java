package entities;

public class User {
	private String name;
	private Integer id;
	private String email;

	public User() {
	}
	
	public User(String name, Integer id, String email) {
		setName(name);
		setId(id);
		setEmail(email);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
