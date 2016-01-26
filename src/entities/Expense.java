package entities;

import java.util.List;

public class Expense {

	private String creatorId;
	private String amount;
	private String name;
	private String id;
	private String type;
	private String eventId;
	private List<ShareSimple> shares;
	
	public Expense() {}
	public Expense(String creatorId, String amount,String name,
			String id,String type,String eventId, List<ShareSimple> shares){
	this.creatorId = creatorId;
	this.amount = amount;
	this.name = name;
	this.id = id;
	this.type = type;
	this.eventId = eventId;
	this.shares = shares;
	}
	

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<ShareSimple> getShares() {
		return shares;
	}

	public void setShares(List<ShareSimple> shares) {
		this.shares = shares;
	}


	public String getEventId() {
		return eventId;
	}


	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	
	
	
}
