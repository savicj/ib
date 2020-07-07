package ib.project.rest;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ib.project.model.User;
import ib.project.service.UserService;

@RestController
@RequestMapping(value="api/users")
public class UserController {

	@Autowired
	public UserService userService;	
	
	
	@GetMapping(path="user/all")
	public ArrayList<User> getAll(){
		return userService.getAll();
	}
	
	//????????????
	@PutMapping(path="user/register")
	public ResponseEntity<User> addUser(@RequestParam String email, @RequestParam String password){
		User user = new User();
		
		User available = userService.findByEmail(email);
		if (available == null) {
			System.out.print("Email address is available.");
			user.setEmail(email);
			user.setPassword(password);
			user.setAuthority("REGULAR");
			user.setActive(false);
			//CERTIFICATE !!!!!
			
			
			return new ResponseEntity<User>(user,HttpStatus.CREATED);

		}else {
			System.out.println("The email is already registered.");
			return new ResponseEntity<>(HttpStatus.OK);
		}		
	}
	
	@PostMapping(path="user/approve")
	public ResponseEntity<User> approveUser(@RequestParam String email){
		userService.approveUser(email);
		return new ResponseEntity<User>(HttpStatus.CREATED);			
	}
	
	
	@GetMapping(path="user/findByEmail")
	public ResponseEntity<User> findByEmail(@RequestParam String email){
		userService.findByEmail(email);
		return new ResponseEntity<User>(HttpStatus.OK);
	}
}
