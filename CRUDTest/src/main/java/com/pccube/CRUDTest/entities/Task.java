package com.pccube.CRUDTest.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
		
	@Column
	private String description;
	
	@Column
	private String type;
	
	@OneToMany
	private List<Task> children = new ArrayList<>();
	
		
	public List<Task> getChildren() {
		return children;
	}

	public void setChildren(List<Task> children) {
		this.children = children;
	}

	//utente a cui viene assegnato il task
	@ManyToOne
	@JoinColumn(name = "username")
	private User user_id;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescrizione(String description) {
		this.description = description;
	}

	public User getUser_id() {
		return user_id;
	}

	public void setUser_id(User user_id) {
		this.user_id = user_id;
	}

}
