package com.senior.care.controller;

import com.senior.care.Dto.AuthRequest;
import com.senior.care.Entity.User;
import com.senior.care.service.JwtService;
import com.senior.care.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService service;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@GetMapping("/welcome")
	public String welcome() {
		return "Welcome this endpoint is not secure";
	}

	@GetMapping("/all")
//	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public String getAllTheUserDto() {
		return "Iam ALl";
//	        return service.getUserDto();
	}

	@PostMapping("/new")
	@CrossOrigin("http://localhost:3000/")
	public String addNewUser(@RequestBody User userInfo) {
		return service.addUser(userInfo);
	}

	@GetMapping("/usernew")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public String getUserDtoById() {
		return "Iam userr";
	}

	@PostMapping("/authenticate")
	@CrossOrigin("http://localhost:3000/")
	public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		System.out.println("hello");
		if (authentication.isAuthenticated()) {
			return jwtService.generateToken(authRequest.getUsername());
		} else {
			throw new UsernameNotFoundException("invalid user request !");
		}

	}
}
