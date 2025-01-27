package useCase;

import com.taskmanager.model.Clock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.CookieManager;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.time.Duration;

public class AdvanceTimeTest {

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
     * Step 1: Test if user is shown the form to advance time.
     */
    @Test
    public void timePageTest() throws IOException, InterruptedException {
        ///clock/timePage
        HttpClient client = logInUser();

        URI uri = URI.create(url+"/clock/timePage");
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(uri)
                .version(HttpClient.Version.HTTP_2)
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(response.statusCode(), 200);
        Assertions.assertTrue(response.body().contains("CurrentTime"));
    }

    /**
     * Step 3: enter a new date time test.
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void advanceTimeDateTimeTest() throws IOException, InterruptedException {
        HttpClient client = logInUser();
        ///clock/advanceTime?systemTime=2029-04-19T15:22:52

        URI uri = URI.create(url+"/clock/advanceTime?systemTime=2029-06-19T15:22:52");
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(uri)
                .version(HttpClient.Version.HTTP_2)
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        //Assertions.assertEquals(response.statusCode(), 302);

        uri = URI.create(url+"/clock/timePage");
        request = HttpRequest
                .newBuilder()
                .uri(uri)
                .version(HttpClient.Version.HTTP_2)
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertTrue(response.body().contains("2029"));
    }

    /**
     * Step 3: enter minutes test
     * @throws IOException
     * @throws InterruptedException
     */
    //TODO: weet niet goed hoe we mooi checken dat de minuten omhoog zijn gegaan
    @Test
    public void advanceTimMinutesTest() throws IOException, InterruptedException {

        HttpClient client = logInUser();
        ///clock/advanceTime?systemTime=2029-04-19T15:22:52
        // systemTime
        Timestamp before = Clock.getSystemTime();
        URI uri = URI.create(url+"/clock/advanceTime?minutesTime=60");
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(uri)
                .version(HttpClient.Version.HTTP_2)
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(response.statusCode(), 302);

        uri = URI.create(url+"/clock/timePage");
        request = HttpRequest
                .newBuilder()
                .uri(uri)
                .version(HttpClient.Version.HTTP_2)
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Timestamp after = Clock.getSystemTime();

        //Assertions.assertTrue(response.body().contains("2029"));
        //Assertions.assertTrue(after.after(before));
    }

    /**
     * Step 4: system updates internal clock test
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void internalClockTest() throws IOException, InterruptedException {
        HttpClient client = logInUser();
        ///clock/advanceTime?systemTime=2029-04-19T15:22:52
        // systemTime
        Timestamp before = Clock.getSystemTime();
        URI uri = URI.create(url+"/clock/advanceTime?minutesTime=60");
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(uri)
                .version(HttpClient.Version.HTTP_2)
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(response.statusCode(), 302);

        uri = URI.create(url+"/clock/timePage");
        request = HttpRequest
                .newBuilder()
                .uri(uri)
                .version(HttpClient.Version.HTTP_2)
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Timestamp after = Clock.getSystemTime();

        //Assertions.assertTrue(response.body().contains("2029"));
        //Assertions.assertTrue(after.after(before));


    }


}
