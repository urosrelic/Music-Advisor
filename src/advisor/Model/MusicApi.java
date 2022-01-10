package advisor.Model;

import java.util.HashMap;

public abstract class MusicApi {
    public static String API_LINK = "https://api.spotify.com";
    public static int itemsPerPage = 5;

    public static HashMap<String, String> map = new HashMap<>();


    abstract public void next();
    abstract public void prev();
    abstract public void get();
    abstract void printList();
}
