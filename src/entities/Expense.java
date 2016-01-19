package entities;

public class Expense {

	String idcreator;
	String amount;
	String name;
	String idexpense;
	String idevent;
	
	public Expense() {}
	
	public Expense(String idcreator, String amount, String name, String idexpense, String idevent){
		this.idcreator = idcreator;
		this.amount = amount;
		this.name = name;
		this.idexpense = idexpense;
		this.idevent = idevent;
	}

	public String getIdcreator() {
		return idcreator;
	}

	public void setIdcreator(String idcreator) {
		this.idcreator = idcreator;
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

	public String getIdexpense() {
		return idexpense;
	}

	public void setIdexpense(String idexpense) {
		this.idexpense = idexpense;
	}

	public String getIdevent() {
		return idevent;
	}

	public void setIdevent(String idevent) {
		this.idevent = idevent;
	}

	
	
	
	
}
