package entities;

import java.util.List;

public class Event {
	private String name;
	private String id;
	private List<User> users;
	private String description;
	private String creatorId;
	private String version;
	private List<Expense> expenses;
	
	public Event() {}
	public Event(String name, String Id, List<User> users, String description, String creatorId, String version, List<Expense> expenses) {
		setName(name);
		setId(Id);
		setUsers(users);
		setDescription(description);
		setCreatorId(creatorId);
		setVersion(version);
		setExpenses(expenses);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
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

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
	public List<Expense> getExpenses() {
		return expenses;
	}
	public void setExpenses(List<Expense> expenses) {
		this.expenses = expenses;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
}
