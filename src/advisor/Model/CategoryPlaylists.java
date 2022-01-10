package advisor.Model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class CategoryPlaylists extends MusicApi {
    private static final String PLAYLIST = "/v1/browse/categories/";

    private final String playlistName;
    private static boolean obtained = false;

    private final ArrayList<Playlist> list;

    public CategoryPlaylists(String playlistName) {
        this.list = new ArrayList<>();
        this.playlistName = playlistName;
    }

    private int start, end, totalPages, currentPage;

    public boolean getPlaylists() {
        if(!obtained) {
            new Categories().getCategories();
        }

        if (!map.containsKey(playlistName)) {
            System.out.println("Specified id doesn't exist");
            return false;
        }

        String id = map.get(playlistName);
        String path = API_LINK + PLAYLIST + id + "/playlists";
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + ServerAuthorization.ACCESS_TOKEN)
                .uri(URI.create(path))
                .GET()
                .build();
        HttpClient client = HttpClient.newBuilder().build();

        try {
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();

            if (response.statusCode() != 200) {
                JsonObject error = jsonObject.getAsJsonObject("error");
                System.out.println(error.get("message").getAsString());
                return false;
            }

            try {
                JsonObject playlists = jsonObject.getAsJsonObject("playlists");

                for (JsonElement element : playlists.getAsJsonArray("items")) {
                    if (element.isJsonObject()) {
                        String name = element.getAsJsonObject().
                                get("name").toString().replaceAll("\"", "");
                        String link = element.getAsJsonObject().get("external_urls")
                                .getAsJsonObject().get("spotify")
                                .toString().replaceAll("\"", "");
                        list.add(new Playlist(name, link));
                    }
                }
            } catch (NullPointerException e) {
                JsonObject error = jsonObject.getAsJsonObject("error");
                System.out.println(error.get("message").getAsString());
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
        return true;
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

        obtained = getPlaylists();
        if(obtained) {
            printList();
        }
    }

    @Override
    void printList() {
        for (int i = start; i < end; i++) {
            Playlist current = list.get(i);
            System.out.println(current.getName());
            System.out.println(current.getLink());
            System.out.println();
        }
        System.out.printf("---PAGE %d OF %d---\n", currentPage, totalPages);
    }

}
