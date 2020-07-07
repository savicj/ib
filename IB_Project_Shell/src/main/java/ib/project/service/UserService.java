package ib.project.service;

import ib.project.model.User;
import ib.project.repository.UserRepository;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	
	public User findByEmail(String email) {
		User u = userRepository.findByEmail(email);
		if(u != null)
			return u;
		return null;
	}
	
	public ArrayList<User> getAll(){
		return (ArrayList<User>) userRepository.findAll();
	}
	
	public User addUser(User user) {
    	User user1 = new User();
    	user1.setId(user.getId());
    	user1.setPassword(user.getPassword());
    	user1.setCertificate(user.getCertificate());
    	user1.setEmail(user.getEmail());
    	user1.setActive(false);
    	//user1.setAuthority("Regular");
    	userRepository.save(user);
		return user1;
	}
	
	public User approveUser(String email) {
		User u = userRepository.findByEmail(email);
		if(u != null) {
			u.setActive(true);
			userRepository.save(u);
			return u;
		}
		return null;
	}
	
	
    
	
	
	
	
}
