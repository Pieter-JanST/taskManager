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

public class CreateProjectTest {

    private final String url = "http://localhost:8080";

    /**
     * Log in a user with predefined projects
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
     * Step 2: Test if a user is shown the form to create a project.
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void createProjectFormTest() throws IOException, InterruptedException {
        HttpClient client = logInUser();

        URI uri = URI.create(url+"/project/projectForm");
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
        Assertions.assertTrue(response.body().contains("Create a new Project"));
    }

    /**
     * Step 3&4: Test if the entered details create a project.
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void enterDetailsTest() throws IOException, InterruptedException {
        HttpClient client = logInUser();

        URI uri = URI.create(url+"/project/createProject");

        String testName = "NewProject";
        String testDesc = "NewDesc";

        // Build the HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString("name="+testName+"&description="+testDesc+"&duetime=2029-01-01T12:00"))
                .build();


        // Send the HTTP request and get the response
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        URI test = URI.create(url+"/project/projectOverview");
        HttpRequest testRequest = HttpRequest
                .newBuilder()
                .uri(test)
                .header("Content-Type", "application/json")
                .GET()
                .build();
        var testResponse = client.send(testRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(302,response.statusCode());

        Assertions.assertTrue(response.headers().toString().contains("succesmessage=project%20has%20been%20added"));
        Assertions.assertTrue(testResponse.body().contains("NewProject"));
    }

    /**
     * Step 3a: Test if the user can cancel creating a project.
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void stopFormTest() throws IOException, InterruptedException {
        HttpClient client = logInUser();

        URI uri = URI.create(url+"/project/projectForm");

        String testName = "TestName";
        // Build the HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .GET()
                .build();


        // Send the HTTP request and get the response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        URI test = URI.create(url+"/project/projectOverview");
        HttpRequest testRequest = HttpRequest
                .newBuilder()
                .uri(test)
                .version(HttpClient.Version.HTTP_2)
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        var testResponse = client.send(testRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200,response.statusCode());
        //check if the project is created
        Assertions.assertFalse(response.body().contains(testName));
    }

    /**
     * Step 4a: Test if the user can enter wrong details.
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void enterWrongDetailsTest() throws IOException, InterruptedException {
        HttpClient client = logInUser();

        URI uri = URI.create(url+"/project/createProject");

        String testName = "FalseProject";
        String testDesc = "";

        // Build the HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .version(HttpClient.Version.HTTP_2)
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString("name="+testName+"&description="+testDesc+"&duetime=2029-01-01T12:00"))
                .build();


        // Send the HTTP request and get the response
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        URI test = URI.create(url+"/project/projectOverview");
        HttpRequest testRequest = HttpRequest
                .newBuilder()
                .uri(test)
                .version(HttpClient.Version.HTTP_2)
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        var testResponse = client.send(testRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertNotEquals(302,response.statusCode());
        Assertions.assertTrue(response.body().contains("description cant be empty"));

        Assertions.assertFalse(testResponse.body().contains("FalseProject"));

    }

}
