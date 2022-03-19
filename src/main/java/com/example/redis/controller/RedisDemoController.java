package com.example.redis.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.redis.entity.User;
import com.example.redis.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class RedisDemoController {
	
	@Autowired
	private UserRepository userRepo;
	
	@GetMapping
	public String home() {
		return "This is an example of Redis Integartion with Spring Boot as a Cache";
	}
	
	@GetMapping("/user/find/{id}")
	@Cacheable(value = "user", key = "#id")
	public User fetchUser(@PathVariable Long id) {
		log.info("fetching from DB...");
		return userRepo.findById(id).get();
	}
	
	@GetMapping("/user/find/all")
	public List<User> fetchAllUsers() {
		log.info("fetching from DB...");
		List<User> users = new ArrayList<>();
		userRepo.findAll().iterator().forEachRemaining(users::add);
		return users;
	}
	
	@PostMapping("/user/add")
	public User addUser(@RequestBody User user) {
		return userRepo.save(user);
	}
	
	@PutMapping("/user/update")
	public String updateUser(@RequestBody User user) {
		Optional<User> usr = userRepo.findById(user.getId());
		if(usr.isPresent()) {
			userRepo.save(user);
			return "Updated successfully!";
		} else {
			return "User is not present" + user.toString();
		}
	}
	
	@DeleteMapping("/user/delete/{id}")
	@CacheEvict(value = "user", key = "#id")
	public String deleteUser(@PathVariable Long id) {
		userRepo.deleteById(id);
		return "Deleted successfully!";
	}
	
}
