package advisor.Model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class NewReleases extends MusicApi {
    private static final String NEW = "/v1/browse/new-releases";

    private final ArrayList<Album> list;

    private int start, end, totalPages, currentPage;

    public NewReleases() {
        this.list = new ArrayList<>();
    }

    public void getNewReleases() {
        String path = API_LINK + NEW;
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + ServerAuthorization.ACCESS_TOKEN)
                .uri(URI.create(path))
                .GET()
                .build();

        HttpClient client = HttpClient.newBuilder().build();

        try {
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonObject albums = jsonObject.getAsJsonObject("albums");
            JsonArray array = albums.getAsJsonArray("items");

            for(JsonElement element : array) {
                String name = element.getAsJsonObject().get("name").toString().replaceAll("\"", "");
                String link = element.getAsJsonObject().get("external_urls").
                        getAsJsonObject().get("spotify").toString().replaceAll("\"", "");

                JsonArray elementArray = element.getAsJsonObject().getAsJsonArray("artists");

                ArrayList<String> artists = new ArrayList<>();

                for(JsonElement el : elementArray) {
                    artists.add(el.getAsJsonObject().get("name").toString().replaceAll("\"", ""));
                }

                list.add(new Album(name, artists, link));
            }

            start = 0;
            end = Math.min(start + itemsPerPage, list.size());
            totalPages = list.size() % itemsPerPage == 0 ?
                    list.size() / itemsPerPage :
                    list.size() / itemsPerPage + 1;
            currentPage = 1;

        } catch (IOException | InterruptedException e) {
            System.out.println("Response error");
        }
    }

    @Override
    public void next() {

        if (end == list.size()) {
            System.out.println("No more pages.");
            return;
        }

        start = end;
        end = Math.min(start + itemsPerPage, list.size());
        currentPage++;
        printList();
    }

    @Override
    public void prev() {

        if (start == 0) {
            System.out.println("No more pages.");
            return;
        }

        end = start;
        start = end - itemsPerPage;
        currentPage--;
        printList();
    }

    @Override
    public void get() {
        getNewReleases();
        printList();
    }

    @Override
    void printList() {
        for (int i = start; i < end; i++) {

            Album current = list.get(i);

            System.out.println(current.getName());
            System.out.println(current.getArtists());
            System.out.println(current.getLink());
            System.out.println();
        }
        System.out.printf("---PAGE %d OF %d---\n", currentPage, totalPages);
    }
}
