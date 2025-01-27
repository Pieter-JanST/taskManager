package useCase;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.net.CookieManager;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class StartTaskTest {

    private final String url = "http://localhost:8080";

    /**
     * Log in a user with predefined projects and tasks
     * @throws IOException
     * @throws InterruptedException
     */
    public HttpClient logInUser() throws IOException, InterruptedException {
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
     * Step 2: test if the system shows all task with status.
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void taskOverviewTest() throws IOException, InterruptedException {
        HttpClient client = logInUser();

        URI uri = URI.create(url+"/task/taskOverview");
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
     * Step 3&4&5: test if the system shows all task with status.
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void startTaskPageTest() throws IOException, InterruptedException {
        HttpClient client = logInUser();

        URI uri = URI.create(url+"/task/startTaskPage?taskid=16&projectid=13");
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
        Assertions.assertTrue(response.body().contains("id: 16"));
        Assertions.assertTrue(response.body().contains("startTime"));
        //TODO current time
    }

    /**
     * Step 3: test if the system shows all task with status.
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void startTaskTest() throws IOException, InterruptedException {
        HttpClient client = logInUser();
        //Add a user to the task and start the task
        URI uri = URI.create(url+"/task/addUserToTask");
        // Build the HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString("taskId=16"+"&projectId=13"))
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        uri = URI.create(url+"/task/startTaskPage?taskid=16&projectid=13");
        request = HttpRequest
                .newBuilder()
                .uri(uri)
                .version(HttpClient.Version.HTTP_2)
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200,response.statusCode());
        Assertions.assertTrue(response.body().contains("user"));
        Assertions.assertTrue(response.body().contains("startTime: 2"));
    }

    /**
     * Extension 2a: test if user is already working on another task.
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void alreadyWorkingTest() throws IOException, InterruptedException {
        HttpClient client = logInUser();
        //Add a user to the task and start the task
        URI uri = URI.create(url+"/task/addUserToTask");
        // Build the HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString("taskId=16"+"&projectId=13"+"&roleId=Developer0"))
                .build();

        //TODO: finish after start is fixed
    }
    //TODO: rest of extensions
}
