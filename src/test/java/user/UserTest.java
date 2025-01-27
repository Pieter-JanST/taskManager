package user;

import com.taskmanager.model.roles.*;
import com.taskmanager.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class UserTest {
    @Test
    public void validUserTest() {
        User u = new User("user","t");
        Assertions.assertEquals("user",u.getUsername());
        Assertions.assertEquals("t",u.getPassword());
        Assertions.assertNotEquals(0,u.getId());
        Assertions.assertTrue(u.getRoles().isEmpty());
        List<String> roles = List.of("Developer");
        User u1 = new User("user1","t1", roles);
        Assertions.assertEquals(1, u1.getRoles().size());
        Assertions.assertTrue(u1.getRoles().contains("Developer"));
    }
    @Test
    public void emptyUserNameTest() {
        Assertions.assertThrowsExactly(IllegalArgumentException.class,
                () -> {
                    User u = new User("","t");
                },"username cant be empty");
    }
    @Test
    public void emptyUserPassWordTest() {
        Assertions.assertThrowsExactly(IllegalArgumentException.class,
                () -> {
                    User u = new User("","t");
                },"password cant be empty");
    }
    @Test
    public void emptyUserRoleTest() {
        Assertions.assertThrowsExactly(IllegalArgumentException.class,
                () -> {
                    User u = new User("","t",null);
                },"roles cant be empty");
    }
    @Test
    public void addValidRole() {
        User u = new User("user","t");
        u.addRole("Developer");
        u.addRole("ProjectManager");
        Assertions.assertEquals(2, u.getRoles().size());
        Assertions.assertTrue(u.getRoles().contains("Developer"));
        Assertions.assertTrue(u.getRoles().contains("ProjectManager"));
    }
    @Test
    public void addNullRole() {
        Assertions.assertThrowsExactly(IllegalArgumentException.class,
                () -> {
                    User u = new User("user","t");
                    u.addRole(null);
                },"role cant be empty");
    }
    @Test
    public void addInValidRole() {
        Exception e = Assertions.assertThrowsExactly(IllegalArgumentException.class,
                () -> {
                    User u = new User("user","t");
                    u.addRole("Invaild");
                });
        Assertions.assertEquals("Invaild is an invalid role",e.getMessage());
    }
}

