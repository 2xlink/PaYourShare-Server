package entities;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserTemp {
	
	private String email;
	private String password;

	public UserTemp() {
	}
	
	public UserTemp(String email, String password) {
		setEmail(email);
		setPassword(password);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
