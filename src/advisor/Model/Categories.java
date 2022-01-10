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

public class Categories extends MusicApi {
    private static final String CATEGORIES = "/v1/browse/categories";

    private final ArrayList<String> list;

    private int start, end, totalPages, currentPage;

    public Categories() {
        this.list = new ArrayList<>();
    }

    public void getCategories() {
        String path = API_LINK + CATEGORIES;
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + ServerAuthorization.ACCESS_TOKEN)
                .uri(URI.create(path))
                .GET()
                .build();
        HttpClient client = HttpClient.newBuilder().build();

        try {
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            JsonObject object = JsonParser.parseString(response.body()).
                    getAsJsonObject().
                    getAsJsonObject("categories");

            JsonArray array = object.getAsJsonArray("items");

            for (JsonElement item : array) {

                String name = item.getAsJsonObject().get("name").getAsString();
                String id = item.getAsJsonObject().get("id").getAsString();
                map.put(name, id);
                list.add(name);
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
        getCategories();
        printList();
    }

    @Override
    void printList() {
        for (int i = start; i < end; i++) {
            System.out.println(list.get(i));
        }
        System.out.printf("---PAGE %d OF %d---\n", currentPage, totalPages);
    }
}
