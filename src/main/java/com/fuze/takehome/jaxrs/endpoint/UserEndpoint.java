package com.fuze.takehome.jaxrs.endpoint;

import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.fuze.takehome.model.User;
import com.fuze.takehome.service.UserService;

@Path("/users")
public class UserEndpoint {
	
	@Inject
	UserService service;

	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })	
	public User create(@Valid @NotNull User entity) {
		//REVIEW: Fixed indentation formatting.
		return service.create(entity);
	}
	
	@GET
	@Path("/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	public User read(@NotNull @PathParam("id") Long id) {
		return service.read(id);
	}
	
	@GET
	@Path("/")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<User> list() {
		return service.list();
	}	
	
	@DELETE
	@Path("/delete/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	public User delete(@NotNull @PathParam("id") Long id) {
		return service.delete(id);
	}
}
