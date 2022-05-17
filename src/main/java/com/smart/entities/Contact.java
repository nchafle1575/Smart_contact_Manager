package com.smart.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "CONTACT")
public class Contact {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int cid;
	
	@NotBlank(message = "Name can not be empty")
	private String name;
	private String secondName;
	private String work;

	@Column(unique = true)
	@Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")
	private String email;
	
	private String image;
	
	private String phone;
	
	@Column(length = 5000)
	private String description;

	@ManyToOne
	private User user;

	public Contact(int cid, String name, String secondName, String work, String email, String image, String phone,
			String description) {
		super();
		this.cid = cid;
		this.name = name;
		this.secondName = secondName;
		this.work = work;
		this.email = email;
		this.image = image;
		this.phone = phone;
		this.description = description;
	}

	public Contact() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
//@Override
//public String toString() {
//	return "Contact [cid=" + cid + ", name=" + name + ", nickName=" + nickName + ", work=" + work + ", email=" + email
//			+ ", image=" + image + ", phone=" + phone + ", description=" + description + ", user=" + user + "]";
//}

}
