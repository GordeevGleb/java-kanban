package server;


import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private String apiToken;
    private HttpClient httpClient;
    private URL url;

    public KVTaskClient(URL url) {
        this.url = url;
        this.httpClient = HttpClient.newHttpClient();
        this.apiToken = register();
    }

     public String register() {
        String result = null;
        URI uri = URI.create(url + "/register/");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        try {
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (httpResponse.statusCode() == 200) {
                result = httpResponse.body();
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
     public void put(String key, String json) {
        URI uri = URI.create(url + "/save/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
             httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
     public String load(String key) {
        String result = null;
        URI uri = URI.create(url + "/load/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        try {
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (httpResponse.statusCode() == 200) {
                result = httpResponse.body();
            }
        }
        catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}
