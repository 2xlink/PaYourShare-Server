package entities;

import java.util.List;

public class Event {
	private String name;
	private Integer id;
	private List<User> users;
	private String description;
	private Integer creatorId;
	
	public String getName() {
		return name;
	}
	
	public Event() {}
	public Event(String name, Integer Id, List<User> users, String description, Integer creatorId) {
		setName(name);
		setId(Id);
		setUsers(users);
		setDescription(description);
		setCreatorId(creatorId);
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
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
	}
}
