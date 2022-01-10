package advisor.Model;

import java.util.ArrayList;

public class Album {
    private final String name;
    private final ArrayList<String> artists;
    private final String link;

    public Album(String name, ArrayList<String> artists, String link) {
        this.name = name;
        this.artists = artists;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getArtists() {
        return artists;
    }

    public String getLink() {
        return link;
    }
}
