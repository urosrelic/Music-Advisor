package advisor.Model;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class ServerAuthorization {
    public static String SERVER_PATH = "https://accounts.spotify.com";
    public static final String REDIRECT_URI = "Your redirect uri"; // For example http://localhost:8080
    public static final String CLIENT_ID = "Your client id";
    public static final String CLIENT_SECRET = "Your client secret";

    public static String ACCESS_CODE = "";
    public static String ACCESS_TOKEN = "";

    // Get Spotify access code
    public void getAccessCode() {
        System.out.println("use this link to request the access code: ");
        String uri =
                SERVER_PATH +
                "/authorize?client_id=" +
                CLIENT_ID +
                "&redirect_uri=" +
                REDIRECT_URI +
                "&response_type=code";
        System.out.println(uri);


        try {
            HttpServer server = HttpServer.create();
            server.bind(new InetSocketAddress(8080), 0);
            server.start();
            server.createContext("/",
                    new HttpHandler() {
                        @Override
                        public void handle(HttpExchange exchange) throws IOException {
                            String request;
                            String query = exchange.getRequestURI().getQuery();
                            if(query != null && query.contains("code")) {
                                ACCESS_CODE = query.substring(5);
                                System.out.println("code received");
                                System.out.println("CODE: " + ACCESS_CODE);
                                request = "Got the code. Return back to your program.";
                            } else {
                                request = "Authorization code not found. Try again.";
                            }
                            exchange.sendResponseHeaders(200, request.length());
                            exchange.getResponseBody().write(request.getBytes(StandardCharsets.UTF_8));
                            exchange.getResponseBody().close();
                        }
                    });
            System.out.println("waiting for code...");
            while (ACCESS_CODE.length() == 0) {
                Thread.sleep(100);
            }
            server.stop(1);
        } catch (IOException | InterruptedException e) {
            System.out.println("error");
        }

    }

    // Get Spotify access token
    public void getAccessToken() {
        System.out.println("making http request for access_token...");
        System.out.println("response:");

        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(SERVER_PATH + "/api/token"))
                .POST(HttpRequest.BodyPublishers.ofString(
                        "grant_type=authorization_code"
                                + "&code=" + ACCESS_CODE
                                + "&client_id=" + CLIENT_ID
                                + "&client_secret=" + CLIENT_SECRET
                                + "&redirect_uri=" + REDIRECT_URI))
                .build();


        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if(response != null) {
                ACCESS_TOKEN = getAccessToken(response.body());
                System.out.println(response.body());
                System.out.println("Token: " + ACCESS_TOKEN);
                System.out.println("---SUCCESS---");
            }
        } catch (InterruptedException | IOException e) {
            System.out.println("Error response");
        }
    }

    private String getAccessToken(String response) {
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        return jsonObject.get("access_token").getAsString();
    }



}
