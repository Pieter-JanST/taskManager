package project;

import com.taskmanager.model.Project;
import com.taskmanager.model.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.sql.Timestamp;
import java.util.List;

import static com.taskmanager.model.Clock.getOriginalSystemTime;
import static com.taskmanager.model.Clock.getSystemTime;

public class ProjectRepositoryTest {
    ProjectRepository projectRepository = new ProjectRepository();

    @Test
    public void addProjectTest(){
        Project project = new Project("test", "test", new Timestamp(System.currentTimeMillis() + 1000),getSystemTime(),getOriginalSystemTime());
        projectRepository.addProject(project);
        assert(projectRepository.exists(project.getId()));
    }

    @Test
    public void getProjectTest(){
        Project p1 = new Project("test", "test", new Timestamp(System.currentTimeMillis() + 1000),getSystemTime(),getOriginalSystemTime());
        projectRepository.addProject(p1);
        Project p2 = new Project("other", "other", new Timestamp(System.currentTimeMillis() + 2000),getSystemTime(),getOriginalSystemTime());
        projectRepository.addProject(p2);

        assert(projectRepository.exists(p2.getId()));
    }


    @Test
    public void getProjectsListTest(){
        Project p1 = new Project("test", "test", new Timestamp(System.currentTimeMillis() + 1000),getSystemTime(),getOriginalSystemTime());
        projectRepository.addProject(p1);
        Project p2 = new Project("other", "other", new Timestamp(System.currentTimeMillis() + 2000),getSystemTime(),getOriginalSystemTime());
        projectRepository.addProject(p2);

        Assertions.assertTrue(projectRepository.getProjectsList().containsAll(List.of(p1, p2)));
    }
}
