package action;

import com.taskmanager.controller.UserService;
import com.taskmanager.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.taskmanager.model.Clock.getOriginalSystemTime;
import static com.taskmanager.model.Clock.getSystemTime;
import static org.junit.jupiter.api.Assertions.*;

public class ActionTest {
/*
    HashMap<Long, Project> projectRepo =new HashMap<>();
    HashMap<Long, Task> taskRepo =new HashMap<>();
    HashMap<Long, User> userRepo =new HashMap<>();
    @Test
    public void createValidAction() {
        Timestamp t = Timestamp.from(Instant.now().plus(60, ChronoUnit.MINUTES));

        User user = new User("bert","t");
        userRepo.put(user.getId(),user);

        Action action = new Action("new action",user.getId(),projectRepo, taskRepo, userRepo);

        assertEquals(user.getId(),action.getUserId());
        assertEquals("new action",action.getDescription());

    }
    @Test
    public void createActionWithInValidprojectRepo() {
        User user = new User("bert","t");
        userRepo.put(user.getId(),user);
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    Action action = new Action("new action",user.getId(),null, taskRepo, userRepo);
                });
        Assertions.assertEquals("projectRepository cant be null",exception.getMessage());
    }
    @Test
    public void createActionWithInValidTaskRepo() {
        User user = new User("bert","t");
        userRepo.put(user.getId(),user);
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    Action action = new Action("new action",user.getId(),projectRepo,null, userRepo);
                });
        Assertions.assertEquals("taskRepository cant be null",exception.getMessage());

    }
    @Test
    public void createActionWithInValidUserRepo() {
        User user = new User("bert","t");
        userRepo.put(user.getId(),user);
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    Action action = new Action("new action",user.getId(),projectRepo, taskRepo,null);
                });
        Assertions.assertEquals("userRepository cant be null",exception.getMessage());
    }
    @Test
    public void createActionWithInValidDesc() {
        User user = new User("bert","t");
        userRepo.put(user.getId(),user);
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    Action action = new Action("",user.getId(),projectRepo,taskRepo, userRepo);
                });
        Assertions.assertEquals("description cant be empty",exception.getMessage());
    }
    @Test
    public void createActionWithNullDesc() {
        User user = new User("bert","t");
        userRepo.put(user.getId(),user);
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    Action action = new Action("",user.getId(),projectRepo,taskRepo, userRepo);
                });
        Assertions.assertEquals("description cant be empty",exception.getMessage());
    }
    @Test
    public void createActionWithInvalidUserId() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    Action action = new Action("",-1,projectRepo,taskRepo, userRepo);
                });
        Assertions.assertEquals("userId does not exists",exception.getMessage());
    }
    @Test
    public void rollBackAction() {
        User user = new User("bert","t");
        userRepo.put(user.getId(),user);
        Timestamp t = Timestamp.from(Instant.now().plus(60, ChronoUnit.MINUTES));
        Project project = new Project("name","dec",t,getSystemTime(),getOriginalSystemTime());
        projectRepo.put(project.getId(),project);
        Task task = new Task("desc",1, 1.0F,project,getSystemTime(), List.of("Developer"),new ArrayList<>());
        taskRepo.put(task.getId(),task);

        Action action = new Action("new action",user.getId(),projectRepo, taskRepo, userRepo);

        ProjectRepository projectRepository = new ProjectRepository();
        TaskRepository taskRepository = new TaskRepository();
        UserService userService = new UserService();

        action.rolleBack(projectRepository,taskRepository,userService);

        assertEquals(projectRepository.getProjectAsMap().keySet(),projectRepo.keySet());
        assertEquals(taskRepository.getProjectAsMap().keySet(),taskRepo.keySet());
        assertEquals(userService.getUsersAsMap().keySet(),userRepo.keySet());
    }

    @Test
    public void rollBackActionWithNullProjectRepo() {
        User user = new User("bert","t");
        userRepo.put(user.getId(),user);
        Action action = new Action("new action",user.getId(),projectRepo, taskRepo, userRepo);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    action.rolleBack( null, new TaskRepository(), new UserService());
                });
        Assertions.assertEquals("projectRepository cant be null",exception.getMessage());
    }
    @Test
    public void rollBackActionWithNullTaskRepo() {
        User user = new User("bert","t");
        userRepo.put(user.getId(),user);
        Action action = new Action("new action",user.getId(),projectRepo, taskRepo, userRepo);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    action.rolleBack( new ProjectRepository(), null, new UserService());
                });
        Assertions.assertEquals("taskRepository cant be null",exception.getMessage());
    }
    @Test
    public void rollBackActionWithNullUserRepo() {
        User user = new User("bert","t");
        userRepo.put(user.getId(),user);
        Action action = new Action("new action",user.getId(),projectRepo, taskRepo, userRepo);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    action.rolleBack( new ProjectRepository(), new TaskRepository(), null);
                });
        Assertions.assertEquals("userService cant be null",exception.getMessage());

    }*/
}
