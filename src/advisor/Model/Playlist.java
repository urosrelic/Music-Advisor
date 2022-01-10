package advisor.Model;

public class Playlist {
    private final String name;
    private final String link;

    public Playlist(String name, String link) {
        this.name = name;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }
}
