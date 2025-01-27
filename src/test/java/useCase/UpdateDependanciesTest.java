package useCase;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.CookieManager;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.List;


public class UpdateDependanciesTest {
    private long getIdFromTaskDesc(HttpClient client,String taskname ) throws IOException, InterruptedException {
        //get the id from the newly created task
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create(url+"/project/showProject?projectid=13"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();
        var response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        List<String> s = new java.util.ArrayList<>(List.of(response2.body().split("<tr>")));
        s.remove(0);
        s.remove(0);
        for (String td: s) {
            td = td.replace("</td>","");
            String[] string = td.split("<td>");
            if (string[2].trim().equals(taskname)) {
                return Long.parseLong(string[1].trim());
            }
        }
        return -1;
    }
    private final String url = "http://localhost:8080";

    /**
     * Log in a user with predefined projects and tasks
     * @throws IOException
     * @throws InterruptedException
     */
    public HttpClient LogInUser() throws IOException, InterruptedException {
        URI uri = URI.create(url+"/user/loginUser?username=user&password=t");
        HttpClient client = HttpClient.newBuilder()
                .cookieHandler(new CookieManager())
                .build();

        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(uri)
                .version(HttpClient.Version.HTTP_2)
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return client;
    }

    /**
     * Step 2: Test if the user is shown a list of projects with unfinished tasks.
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void viewProjectsTest() throws IOException, InterruptedException {
        HttpClient client = LogInUser();
        URI uri = URI.create(url+"/project/projectOverview");

        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(uri)
                .version(HttpClient.Version.HTTP_2)
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200,response.statusCode());
        Assertions.assertTrue(response.body().contains("AVAILABLE"));

    }

    /**
     * Step 3: Test if the user is shown the details of a project with available tasks.
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void requestMoreDetailsTest() throws IOException, InterruptedException {
        HttpClient client = LogInUser();

        URI uriViewProjects = URI.create(url+"/project/projectOverview");

        HttpRequest requestViewProjects = HttpRequest
                .newBuilder()
                .uri(uriViewProjects)
                .version(HttpClient.Version.HTTP_2)
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        var test = client.send(requestViewProjects, HttpResponse.BodyHandlers.ofString());

        URI uri = URI.create(url+"/project/showProject?projectid=13");
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(uri)
                .version(HttpClient.Version.HTTP_2)
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200,response.statusCode());

        Assertions.assertTrue(response.body().contains("AVAILABLE"));

    }

    /**
     * Step 6: Test if the user is shown the details with option to change dependancies of a task when requested.
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void requestMoreDetailsTaskTest() throws IOException, InterruptedException {
        HttpClient client = LogInUser();

        URI uriViewProjects = URI.create(url+"/project/projectOverview");

        HttpRequest requestViewProjects = HttpRequest
                .newBuilder()
                .uri(uriViewProjects)
                .version(HttpClient.Version.HTTP_2)
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        client.send(requestViewProjects, HttpResponse.BodyHandlers.ofString());

        URI uriGetProjects = URI.create(url+"/project/showProject?projectid=13");
        HttpRequest requestGetProject = HttpRequest
                .newBuilder()
                .uri(uriGetProjects)
                .version(HttpClient.Version.HTTP_2)
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        client.send(requestGetProject, HttpResponse.BodyHandlers.ofString());

        URI uri = URI.create(url+"/task/showTask?taskid=16&projectid=13");
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(uri)
                .version(HttpClient.Version.HTTP_2)
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200,response.statusCode());
        Assertions.assertTrue(response.body().contains("id: 16"));
        Assertions.assertTrue(response.body().contains("Depends on"));
    }

    /**
     * Step 7&8: Test if the user is shown the details with option to change dependancies of a task when requested.
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void submitNewDependancyTest() throws IOException, InterruptedException {
        HttpClient client = LogInUser();

        String taskname = "newTestTask" + Instant.now();
        HttpRequest creatTaskrequest = HttpRequest.newBuilder()
                .uri(URI.create(url+"/task/createTask"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString("project=13"+"&desc="+ taskname +"&estimated=1&deviation=1&rolesNecessary=Developer&dependsOn="))
                .build();
        var response1 = client.send(creatTaskrequest, HttpResponse.BodyHandlers.ofString());
        long id = getIdFromTaskDesc(client,taskname);
        // Send the HTTP request and get the response


        URI uri = URI.create(url+"/task/updateTaskDependancies?taskId="+id+"&projectId=13&newDependancies=17");

        HttpRequest requestViewProjects = HttpRequest
                .newBuilder()
                .uri(uri)
                .version(HttpClient.Version.HTTP_2)
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();

        var response = client.send(requestViewProjects, HttpResponse.BodyHandlers.ofString());

        uri = URI.create(url+"/task/showTask?taskid="+id+"&projectid=13");
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(uri)
                .version(HttpClient.Version.HTTP_2)
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200,response.statusCode());
        System.out.println(response.body());
        //Assertions.assertTrue(response.body().contains("Task: 17"));
        Assertions.assertTrue(response.body().contains("Depends on"));
    }

    /**
     * Step 7&8: Test remove a dependancy from a task.
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void removeDependancyTest() throws IOException, InterruptedException {
        HttpClient client = LogInUser();

        URI uri = URI.create(url+"/task/updateTaskDependancies?taskId=16&projectId=13&newDependancies=");

        HttpRequest requestViewProjects = HttpRequest
                .newBuilder()
                .uri(uri)
                .version(HttpClient.Version.HTTP_2)
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();

        var response = client.send(requestViewProjects, HttpResponse.BodyHandlers.ofString());

        uri = URI.create(url+"/task/showTask?taskid=16&projectid=13");
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(uri)
                .version(HttpClient.Version.HTTP_2)
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200,response.statusCode());

        Assertions.assertFalse(response.body().contains("Task: 17"));
        Assertions.assertTrue(response.body().contains("Depends on"));
    }

    /**
     * Step 7&8: Test loop in dependancy.
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void loopDependancyTest() throws IOException, InterruptedException {
        HttpClient client = LogInUser();

        String taskname = "newTestTask2" + Instant.now();
        HttpRequest creatTaskrequest = HttpRequest.newBuilder()
                .uri(URI.create(url+"/task/createTask"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString("project=13"+"&desc="+ taskname +"&estimated=1&deviation=1&rolesNecessary=Developer&dependsOn=[]"))
                .build();
        client.send(creatTaskrequest, HttpResponse.BodyHandlers.ofString());
        long id = getIdFromTaskDesc(client,taskname);

        String taskname2 = "newTestTask3" + Instant.now();
        HttpRequest creatTask2request = HttpRequest.newBuilder()
                .uri(URI.create(url+"/task/createTask"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString("project=13"+"&desc="+ taskname2 +"&estimated=1&deviation=1&rolesNecessary=Developer&dependsOn=[]"))
                .build();
        var response1 = client.send(creatTask2request, HttpResponse.BodyHandlers.ofString());
        long id2 = getIdFromTaskDesc(client,taskname2);
        //if (id2 == -1){id2 = Long.parseLong("");}

        URI uri = URI.create(url+"/task/updateTaskDependancies?taskId="+id+"&projectId=13&newDependancies="+id2);

        HttpRequest requestViewProjects = HttpRequest
                .newBuilder()
                .uri(uri)
                .version(HttpClient.Version.HTTP_2)
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();

        var response3 = client.send(requestViewProjects, HttpResponse.BodyHandlers.ofString());
        URI uri2 = URI.create(url+"/task/updateTaskDependancies?taskId="+id2+"&projectId=13&newDependancies="+id);

        HttpRequest requestView2Projects = HttpRequest
                .newBuilder()
                .uri(uri2)
                .version(HttpClient.Version.HTTP_2)
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();

        var response = client.send(requestView2Projects, HttpResponse.BodyHandlers.ofString());

        Assertions.assertTrue(response.body().contains("Loop in the dependencies"));
    }

}
