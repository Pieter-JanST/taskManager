package user;

import com.taskmanager.controller.UserService;
import com.taskmanager.model.roles.*;
import com.taskmanager.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.Arrays;
import java.util.List;

public class UserServiceTest {


    @Test
    public void AddUserTest() {
        User u = new User("bob","t");
        UserService userService = new UserService();
        userService.addUser(u);
        Assertions.assertTrue(userService.getUsers().contains(u));
        long id = userService.addUser("ernie","e");

        Assertions.assertEquals("ernie", userService.getUserById(id).getUsername());
    }
    @Test
    public void AddInvalidUserTest() {
        UserService userService = new UserService();
        Assertions.assertThrowsExactly(IllegalArgumentException.class,()->{
            userService.addUser(null);
        },"user cant be null");
        Assertions.assertThrowsExactly(IllegalArgumentException.class,()->{
            userService.addUser("","t");
        },"username cant be empty");
        Assertions.assertThrowsExactly(IllegalArgumentException.class,()->{
            userService.addUser("ernie","");
        },"password cant be empty");
    }
    @Test
    public void SuccesLoginTest() {
        User u = new User("user1","t");
        UserService userService = new UserService();
        userService.addUser(u);
        Assertions.assertNotEquals(-1L,userService.logInUser("user1","t"));
    }
    @Test
    public void InvalidPasswordLoginTest() {
        User u = new User("user2","t");
        UserService userService = new UserService();
        userService.addUser(u);
        Assertions.assertEquals(-1L,userService.logInUser("user2","teee"));
    }
    @Test
    public void InvalidUserNameLoginTest() {
        User u = new User("user3","t");
        UserService userService = new UserService();
        userService.addUser(u);
        Assertions.assertEquals(-1L,userService.logInUser("esssssssss","t"));

    }
    @Test
    public void getRolesFromUserLoginTest() {
        List<String> roles = Arrays.asList("Developer", "ProjectManager");
        User u = new User("user3","t", roles);
        UserService userService = new UserService();
        long id = userService.addUser(u);
        List l = userService.getRolesFromUserId(id);
        Assertions.assertTrue(l.contains("Developer"));
        Assertions.assertTrue(l.contains("ProjectManager"));
    }

}


