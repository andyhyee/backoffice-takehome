package com.fuze.takehome.mybatis;

import java.util.Collection;
import java.util.List;

import javax.inject.Named;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.mapping.FetchType;
import org.apache.ibatis.annotations.Many;

import com.fuze.takehome.model.User;

@Named
public interface UserMapper {
		
	@Insert("INSERT into takeHome.users "
			+ "(customer_id, username, first_name, last_name, email, telephone_number, mobile_number, fax_number, active) "
			+ "VALUES "
			+ "(#{in.customerId}, #{in.userName}, #{in.firstName}, #{in.lastName}, #{in.email}, #{in.telephoneNumber}, #{in.mobileNumber}, #{in.faxNumber}, #{in.active})")
	@Options(useGeneratedKeys=true, keyProperty="in.id")
	public int create(@Param("in") User in);

	@Update({
	  "<script>"
	    + "INSERT into takeHome.departments_users "
	    +    "(department_id, user_id) "
	    + "VALUES"   
        + "<foreach item='dept' collection='departments' open='' separator=',' close=''>" 
          + "(#{dept}, #{userId})"
        + "</foreach>"
    + "</script>"})
	public int createDepartmentRelationships(@Param("userId") Long userId, @Param("departments") List<Long> departments);
	
	@Select("SELECT "
			+ "id, customer_id, username, first_name, last_name, email, telephone_number, mobile_number, fax_number, active "
			+ "FROM takeHome.users "
			+ "WHERE id = #{id}" )
	@Results(value = { 
			@Result(property = "id", 			        column = "id"),
			@Result(property = "customerId",      column = "customer_id"), 
			@Result(property = "userName", 		    column = "username"),
			@Result(property = "firstName", 	    column = "first_name"),
			@Result(property = "lastName", 		    column = "last_name"),
			@Result(property = "email", 		      column = "email"),
			@Result(property = "telephoneNumber", column = "telephone_number"),
			@Result(property = "mobileNumber", 		column = "mobile_number"),
			@Result(property = "faxNumber", 		  column = "fax_number"),
			@Result(property = "active", 		      column = "active"),
			@Result(property = "departments", column = "id", javaType = List.class,
          many = @Many(fetchType = FetchType.DEFAULT, select = "getDepartmentsForUser")),
	})
	public User read(Long id);
	
	@Select("SELECT department_id FROM takeHome.departments_users WHERE user_id = #{userId}")
  public Collection<Long> getDepartmentsForUser(@Param("userId") Long userId);   
  
	
	@Select("SELECT id FROM takeHome.users")
	public Collection<Long> list(); 	
	
	@Delete("DELETE FROM takeHome.users WHERE id = #{id}")
	public int delete(Long id); 	
}

