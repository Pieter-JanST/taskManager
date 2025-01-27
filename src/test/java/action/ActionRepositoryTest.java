package action;

import com.taskmanager.controller.UserService;
import com.taskmanager.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static com.taskmanager.model.Clock.getOriginalSystemTime;
import static com.taskmanager.model.Clock.getSystemTime;
import static org.junit.jupiter.api.Assertions.*;

public class ActionRepositoryTest {
    /*
    HashMap<Long, Project> projectRepo =new HashMap<>();
    HashMap<Long, Task> taskRepo =new HashMap<>();
    HashMap<Long, User> userRepo =new HashMap<>();

    private ProjectRepository createProjectRepository() {
        ProjectRepository p = new ProjectRepository();
        Timestamp t = Timestamp.from(Instant.now().plus(60, ChronoUnit.MINUTES));

        p.addProject(new Project("name","e",t,getSystemTime(),getOriginalSystemTime()));
        return p;
    }
    private TaskRepository createTaskRepository(Project p) {
        TaskRepository t = new TaskRepository();
        t.addTask(new Task("f",1,1.0F,p, List.of("JavaProgrammer")));
        return t;
    }
    private UserService ceateUserService() {
        UserService userService = new UserService();
        userService.addUser(new User("t","t"));
        return userService;
    }

    @Test
    public void addActionTest(){
        ActionRepository actionRepository  = new ActionRepository();
        User user = new User("bert","t");
        userRepo.put(user.getId(),user);
        Action a = new Action("new action", user.getId(),projectRepo, taskRepo, userRepo);
        actionRepository.addAction(a);
        assertEquals(a,actionRepository.getLastAction());
    }
    @Test
    public void addNullActionTest(){
        ActionRepository actionRepository  = new ActionRepository();
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    actionRepository.addAction(null);
        });
        Assertions.assertEquals("action cant be null",exception.getMessage());
    }


    @Test
    public void undoActionTest(){
        ActionRepository actionRepository  = new ActionRepository();

        ProjectRepository p = createProjectRepository();
        TaskRepository t = createTaskRepository(p.getProjectsList().stream().findFirst().get());
        UserService userService = ceateUserService();

        Action a1 = new Action("new action", userService.getUsers().get(0).getId(),new HashMap<>(), new HashMap<>(), userService.getUsersAsMap());
        actionRepository.addAction(a1);

        Action a2 = new Action("new action", userService.getUsers().get(0).getId(),projectRepo, taskRepo, userService.getUsersAsMap());
        actionRepository.addAction(a2);

        ProjectRepository pp = new ProjectRepository();
        TaskRepository tt = new TaskRepository();
        UserService uu = new UserService();
        actionRepository.undoAction(userService.getUsers().get(0),pp,tt,uu);

        assertEquals(pp.getProjectAsMap().keySet(),projectRepo.keySet());
        assertEquals(tt.getProjectAsMap().keySet(),taskRepo.keySet());
    }



    @Test
    public void undoActionWhithNoAvailableActionTest(){
        ActionRepository actionRepository  = new ActionRepository();
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    actionRepository.undoAction(new User("u","u"),new ProjectRepository(),new TaskRepository(),new UserService());
                });
        Assertions.assertEquals("no action to undo",exception.getMessage());
    }
    @Test
    public void redoActionTest(){
        ActionRepository actionRepository  = new ActionRepository();

        ProjectRepository p = createProjectRepository();
        TaskRepository t = createTaskRepository(p.getProjectsList().stream().findFirst().get());
        UserService userService = ceateUserService();

        Action a1 = new Action("new action", userService.getUsers().get(0).getId(),new HashMap<>(), new HashMap<>(), userService.getUsersAsMap());
        actionRepository.addAction(a1);

        Action a2 = new Action("new action", userService.getUsers().get(0).getId(),projectRepo, taskRepo, userService.getUsersAsMap());
        actionRepository.addAction(a2);

        ProjectRepository pp = new ProjectRepository();
        TaskRepository tt = new TaskRepository();
        UserService uu = new UserService();
        actionRepository.undoAction(userService.getUsers().get(0),new ProjectRepository(),new TaskRepository(),new UserService());

        actionRepository.redoAction(userService.getUsers().get(0),pp,tt,uu);

        assertEquals(pp.getProjectAsMap().keySet(),projectRepo.keySet());
        assertEquals(tt.getProjectAsMap().keySet(),taskRepo.keySet());
    }
    @Test
    public void redoActionWhithNoAvailableActionTest(){
        ActionRepository actionRepository  = new ActionRepository();
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    actionRepository.redoAction(new User("u","u"),new ProjectRepository(),new TaskRepository(),new UserService());
                });
        Assertions.assertEquals("no action to redo",exception.getMessage());
    }
    @Test
    public void clearListTest(){
        ActionRepository actionRepository  = new ActionRepository();
        User user = new User("bert","t");
        userRepo.put(user.getId(),user);
        Action a = new Action("new action", user.getId(),projectRepo, taskRepo, userRepo);
        actionRepository.addAction(a);
        assertEquals(a,actionRepository.getLastAction());
        actionRepository.clearList();
        assertNull(actionRepository.getLastAction());
    }

    @Test
    public void getActionUndoListTest(){
        ActionRepository actionRepository  = new ActionRepository();

        ProjectRepository p = createProjectRepository();
        TaskRepository t = createTaskRepository(p.getProjectsList().stream().findFirst().get());
        UserService userService = ceateUserService();

        Action a1 = new Action("new action", userService.getUsers().get(0).getId(),new HashMap<>(), new HashMap<>(), userService.getUsersAsMap());
        actionRepository.addAction(a1);

        Action a2 = new Action("new action", userService.getUsers().get(0).getId(),projectRepo, taskRepo, userService.getUsersAsMap());
        actionRepository.addAction(a2);

        ProjectRepository pp = new ProjectRepository();
        TaskRepository tt = new TaskRepository();
        UserService uu = new UserService();
        actionRepository.undoAction(userService.getUsers().get(0),pp,tt,uu);

        assertTrue(actionRepository.getActionUndoList(userService.getUsers().get(0)).contains(a1));
        assertFalse(actionRepository.getActionUndoList(userService.getUsers().get(0)).contains(a2));

    }
    @Test
    public void getActionRedoListTest(){
        ActionRepository actionRepository  = new ActionRepository();

        ProjectRepository p = createProjectRepository();
        TaskRepository t = createTaskRepository(p.getProjectsList().stream().findFirst().get());
        UserService userService = ceateUserService();

        Action a1 = new Action("new action", userService.getUsers().get(0).getId(),new HashMap<>(), new HashMap<>(), userService.getUsersAsMap());
        actionRepository.addAction(a1);

        Action a2 = new Action("new action", userService.getUsers().get(0).getId(),projectRepo, taskRepo, userService.getUsersAsMap());
        actionRepository.addAction(a2);

        ProjectRepository pp = new ProjectRepository();
        TaskRepository tt = new TaskRepository();
        UserService uu = new UserService();
        actionRepository.undoAction(userService.getUsers().get(0),pp,tt,uu);

        assertTrue(actionRepository.getActionRedoList(userService.getUsers().get(0)).contains(a2));
        assertFalse(actionRepository.getActionRedoList(userService.getUsers().get(0)).contains(a1));
    }*/

}
