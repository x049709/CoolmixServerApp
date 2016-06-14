package com.tellem.microservices.api.user.model;

import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

@Table
public class AxonUser {
	@PrimaryKey
	private int user_id;
	private String fname;
	private String lname;
	private String email;
	private String dob;
	private String ssn;
	private String emplid;
	
	public AxonUser() {};

	public AxonUser(int user_id, String fname, String lname, String email, String dob, String ssn, String emplid) {
		this.user_id = user_id;
		this.fname = fname;
		this.lname = lname;
		this.email = email;
		this.dob = dob;
		this.ssn = ssn;
		this.emplid = emplid;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getEmplid() {
		return emplid;
	}

	public void setEmplid(String emplid) {
		this.emplid = emplid;
	}

	@Override
	public String toString() {
		return "AxonUser [user_id=" + user_id + ", fname=" + fname + ", lname=" + lname + ", email=" + email
				 + ", dob=" + dob  + ", ssn=" + ssn  + ", emplid=" + emplid + "]";
	}
}
