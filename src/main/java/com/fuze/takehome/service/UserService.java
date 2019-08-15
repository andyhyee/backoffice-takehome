package com.fuze.takehome.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.fuze.takehome.model.User;
import com.fuze.takehome.mybatis.UserMapper;

//REVIEW: [SHOULD] There is some inconsistency in the code formatting.
//Should apply a code consistent formatter.
//Some blocks are started next line and some blocks are started on the same line. 
//I have generally not changed these. 

//REVIEW: [MUST] There is no unittest for this class.
public class UserService {
  private static final Logger log = LoggerFactory.getLogger(UserService.class);
  
	@Inject
	public UserMapper mapper;

	@Transactional
	public User create(User user) {
		mapper.create(user);
		return user;		
	}

	@Transactional
	public User read(Long id) {
		User user = mapper.read(id);
		if (user != null) { 
			return user;
		} else {
			throw new NotFoundException();
		}
	}
	
	@Transactional
	public List<User> list() {
		//REVIEW: [MAY] Can use diamond operators. Changed.
		LinkedList<User> userReturnList  = new LinkedList<>();
		
		//REVIEW: [SHOULD] Why not create a method to read the a List of full users instead of reading them one at a time. 
		//Can use dynamic sql in the Mybatis Mappers and share the method with the read(long) method. Not changed.
		ArrayList<Long> userIds = new ArrayList<>(mapper.list());
		
		//REVIEW: [SHOULD] Can use the cleaner 'for loop' available for Iterable. Changed.
		for(long id : userIds) {
			//REVIEW: [MUST] There is a bug here where the .get(0) is returning a hard coded element of the list. Fixed.
			userReturnList.add(mapper.read(id));
		}
		return userReturnList;
	}

	@Transactional
	public User delete(Long id) {
		User user = this.read(id);
		//REVIEW: Extra space to fix.
		if (user == null) {
			throw new NotFoundException();
		}
		int count = 0;
		try {
			count = mapper.delete(id);
		}
		catch(Exception e){
		  //REVIEW: [MUST] It is dangerous to eat exceptions. This can tell us if something is wrong like bad SQL. 
		  //Either log the exception or allow the RuntimeException through. Changed to log the exception to maintain previous behavior.
		  log.error(String.format("Unable to delete user id: %s", id), e);
		}
		if(count < 1)
		{
			throw new NotFoundException();	
		}
		return user;
	}	
}
