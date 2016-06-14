package com.tellem.microservices.api.user.model;

import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

@Table
public class User {
	@PrimaryKey
	private int user_id;

	private String fname;
	private String lname;

	public User(int user_id, String fname, String lname) {
		this.user_id = user_id;
		this.fname = fname;
		this.lname = lname;
	}

	public int getUser_id() {
		return user_id;
	}

	public String getFname() {
		return fname;
	}

	public String getLname() {
		return lname;
	}

	@Override
	public String toString() {
		return "User [user_id=" + user_id + ", fname=" + fname + ", lname=" + lname
				+ "]";
	}
}
