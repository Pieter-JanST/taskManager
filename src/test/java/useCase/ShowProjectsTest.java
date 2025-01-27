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

/**
 * Test if the user input is handled correctly for use case 3.
 */

public class ShowProjectsTest {
    private final String url = "http://localhost:8080";

    /**
     * Log in a user with predefined projects
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
     * Step1: Test if the user is shown a list of projects when requested (if be logged in).
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
        Assertions.assertTrue(response.body().contains("<td>Write CSS For WebPage</td>"));

    }

    /**
     * Step4: Test if the user is shownn the details of a project when requested.
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

        Assertions.assertTrue(response.body().contains("id: 13"));

    }

    /**
     * Step 6: Test if the user is shwon the details of a task when requested.
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
    }
}
