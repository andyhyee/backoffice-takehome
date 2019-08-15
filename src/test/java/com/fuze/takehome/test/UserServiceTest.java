package com.fuze.takehome.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;

import org.junit.Test;

import com.fuze.takehome.model.User;
import com.fuze.takehome.service.UserService;

/**
 * Test the User Entity and User Service.
 * 
 * Assert that entity validation is working correctly and the service methods are doing what they are supposed to be
 * doing.
 * 
 * Before this test executes, AbstractEntityTest takes care of initializing Spring and starting an in-memory DB
 * instance. DummyDataGenerator is invoked to insert a couple dummy entities into the database.
 * 
 */

/**
 * Normally for a test class like this I would use Mockito to mock the UserMapper in UserService. This allows this 
 * test class to be self contained. I am not adding Mockito to the project. The original tests in this test class are 
 * more integration tests than unittests. I will continue this test class as an integration test.
 * My adopted method naming convention is <method_name>_<condition>_<result>.
 */
public class UserServiceTest extends AbstractEntityTest {

  private static final long EXISTING_USER_ID = 0;
  private static final long NON_EXISTENT_USER_ID = 9999999;
  private static final long DELETE_EXISTING_USER_ID = 1;

  @Inject
  private UserService service;

  @Test
  public void create_userWithMinimalFields_userWithNewIdAndOtherFieldsEqual() {
    User newUser = new User().withActive(true).withCustomerId(0L).withUserName("username")
        .withDepartments(Arrays.asList(new Long[] {0L}));

    User createdUser = service.create(newUser);

    assertTrue(createdUser.getId() > 0);
    assertEquals(newUser.isActive(), createdUser.isActive());
    assertEquals(newUser.getCustomerId(), createdUser.getCustomerId());
    assertEquals(newUser.getUserName(), createdUser.getUserName());
    assertEquals(newUser.getDepartments(), createdUser.getDepartments());
  }
  
  @Test
  public void read_existingUser_user() {
    assertNotNull(service.read(EXISTING_USER_ID));
  }
  
  @Test (expected=NotFoundException.class)
  public void read_nonExistentUser_notFoundException() {
    service.read(NON_EXISTENT_USER_ID);
  }
  
  /**
   * There is a problem with this test case as each instance of this test case is created more entities are added to the 
   * in memory database. This causes this test case to have an unpredictable number of users being returned. It is a 
   * function how many test cases are run before this one. Using Mockito to mock the UserMapper would make this test 
   * have a predictable result.
   */
  @Test
  public void list_nonEmptyList() {
    assertFalse(service.list().isEmpty());
  }
  
  @Test
  public void delete_existingUser_user() {
    assertNotNull(service.delete(DELETE_EXISTING_USER_ID));
  }
    
  @Test (expected=NotFoundException.class)
  public void delete_nonExistentUser_notFoundException() {
    service.delete(NON_EXISTENT_USER_ID);
  }
}
