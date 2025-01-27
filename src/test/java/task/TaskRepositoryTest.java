package task;

import com.taskmanager.model.Project;
import com.taskmanager.model.ProjectRepository;
import com.taskmanager.model.Task;
import com.taskmanager.model.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static com.taskmanager.model.Clock.getOriginalSystemTime;
import static com.taskmanager.model.Clock.getSystemTime;

public class TaskRepositoryTest {
    TaskRepository taskRepository = new TaskRepository();
    @Test
    public void addTaskTest(){
        Project project = new Project("test", "test", new Timestamp(System.currentTimeMillis() + 1000),getSystemTime(),getOriginalSystemTime());
        Task task = new Task("test", 2, 0.5f, project,getSystemTime(), List.of("JavaProgrammer"),new ArrayList<>());
        taskRepository.addTask(task);
        assert(taskRepository.exists(task.getId()));
    }

    @Test
    public void getTaskTest(){
        Project project = new Project("test", "test", new Timestamp(System.currentTimeMillis() + 1000),getSystemTime(),getOriginalSystemTime());
        Task t1 = new Task("test", 2, 0.5f, project,getSystemTime(), List.of("JavaProgrammer"),new ArrayList<>());
        Task t2 = new Task("other", 2, 0.5f, project,getSystemTime(), List.of("JavaProgrammer"), new ArrayList<>());
        taskRepository.addTask(t2);
        taskRepository.addTask(t1);
        assert(taskRepository.exists(t2.getId()));
    }


    @Test
    public void getTaskListTest(){
        Project project = new Project("test", "test", new Timestamp(System.currentTimeMillis() + 1000),getSystemTime(),getOriginalSystemTime());
        Task t1 = new Task("test", 2, 0.5f, project, getSystemTime(),List.of("JavaProgrammer"),new ArrayList<>());
        Task t2 = new Task("other", 2, 0.5f, project, getSystemTime(),List.of("JavaProgrammer"),new ArrayList<>());
        taskRepository.addTask(t2);
        taskRepository.addTask(t1);
        Assertions.assertTrue(taskRepository.getTasks().containsAll(List.of(t1, t2)));
    }

    @Test
    public void containsTaskTest(){
        Project project = new Project("test", "test", new Timestamp(System.currentTimeMillis() + 1000),getSystemTime(),getOriginalSystemTime());
        Task t1 = new Task("test", 2, 0.5f, project,getSystemTime(), List.of("JavaProgrammer"),new ArrayList<>());
        Task t2 = new Task("other", 2, 0.5f, project,getSystemTime(), List.of("JavaProgrammer"),new ArrayList<>());
        taskRepository.addTask(t1);

        Assertions.assertTrue(taskRepository.exists(t1.getId()));
        Assertions.assertFalse(taskRepository.exists(t2.getId()));
    }

    @Test
    public void deleteTaskTest(){
        Project project = new Project("test", "test", new Timestamp(System.currentTimeMillis() + 1000),getSystemTime(),getOriginalSystemTime());
        Task t1 = new Task("test", 2, 0.5f, project,getSystemTime(), List.of("JavaProgrammer"),new ArrayList<>());
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(t1);
        Task t2 = new Task("other", 2, 0.5f, project,getSystemTime(), List.of("JavaProgrammer"),tasks);
        taskRepository.addTask(t1);
        taskRepository.addTask(t2);
        Assertions.assertTrue(taskRepository.exists(t1.getId()));
        Assertions.assertTrue(project.getTasks().contains(t1));
        Assertions.assertTrue(t2.getDependsOn().contains(t1));
        taskRepository.deleteTask(t1.getId());
        Assertions.assertFalse(taskRepository.exists(t1.getId()));
        Assertions.assertFalse(project.getTasks().contains(t1));
        Assertions.assertFalse(t2.getDependsOn().contains(t1));
    }
}
