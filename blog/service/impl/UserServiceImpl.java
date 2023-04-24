package com.shyam.blog.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shyam.blog.entity.User;
import com.shyam.blog.exception.ResourceNotFoundException;
import com.shyam.blog.playload.UserDto;
import com.shyam.blog.repositories.UserRepo;
import com.shyam.blog.service.UserService;


@Service
public class UserServiceImpl implements UserService{

	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	//@Autowired
	//private RoleRepo roleRepo;

	
	@Override
	public UserDto createUser(UserDto userDto) {
		User user = this.dtoToUser(userDto);
		User savedUser = this.userRepo.save(user);
		return this.userToDto(savedUser);
	}
    
	@Override
	public UserDto updateUser(UserDto userDto, Integer userId) {
		User user= this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", " Id ", userId));

		
		user.setName(userDto.getName());
		user.setEmail(userDto.getEmail());
		user.setPassword(userDto.getPassword());
		user.setCity(userDto.getCity());
		
		user.setAbout(userDto.getAbout());

		User updatedUser = this.userRepo.save(user);
		UserDto userDto1 = this.userToDto(updatedUser);
		return userDto1;
	}
	

	@Override
	public UserDto getUserById(Integer userId) {
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", " Id ", userId));

		return this.userToDto(user);
	}

	@Override
	public List<UserDto> getAllUsers() {
		List<User> users =  (List<User>) this.userRepo.findAll();
		List<UserDto> userDtos = users.stream().map(user -> this.userToDto(user)).collect(Collectors.toList());

		return userDtos;
	}

	@Override
	public void deleteUser(Integer userId) {
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
		this.userRepo.delete(user);
	}
	
	    
	public User dtoToUser(UserDto userDto) {
		User user = this.modelMapper.map(userDto, User.class);//userdto ko convert krna he User class me..
		                             // source:kis object ko pass krna he ,kis class ke object me pass krna he... 
			// user.setId(userDto.getId());
			 //user.setName(userDto.getName());
			// user.setCity(userDto.getCity());
			// user.setAbout(userDto.getAbout());
			return user;
	}
			public UserDto userToDto(User user) {
				UserDto userDto = this.modelMapper.map(user, UserDto.class);
				return userDto;
		
		}
		//public UserDto userToDto(User user) {
			//User user = this.modelMapper.map(userDto, User.class);
			//UserDto userdto = new UserDto();
			//userdto.setId(user.getId());
			//userdto.setName(user.getName());
			//userdto.setCity(user.getCity());
			//userdto.setAbout(user.getAbout());
			//return userdto;
		}
		
	


