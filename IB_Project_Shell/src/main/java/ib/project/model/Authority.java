package ib.project.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;


@SuppressWarnings("serial")
@Entity
@Table(name="Authority")
public class Authority implements GrantedAuthority{

	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="name")
	private String name;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Authority(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	@Override
	public String getAuthority() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
