package advisor.Controller;

import advisor.Model.ServerAuthorization;
import advisor.Model.*;

public class MusicAdvisorController {
    MusicApi api = null;
    private boolean authorized = false;

    public void previousPage() {
        if(authorized) {
            api.prev();
        } else {
            System.out.println("Please, provide access for application.");
        }
    }

    public void nextPage() {
        if(authorized) {
            api.next();
        } else {
            System.out.println("Please, provide access for application.");
        }
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public void setAuthorization() {
        ServerAuthorization serverAuthorization = new ServerAuthorization();
        serverAuthorization.getAccessCode();
        serverAuthorization.getAccessToken();
        this.authorized = true;
    }

    public void getNewReleases() {
        if(authorized) {
            api = new NewReleases();
            api.get();
        } else {
            System.out.println("Please, provide access for application.");
        }
    }

    public void getFeatured() {
        if(authorized) {
            api = new Featured();
            api.get();
        } else {
            System.out.println("Please, provide access for application.");
        }
    }

    public void getCategories() {
        if(authorized) {
            api = new Categories();
            api.get();
        } else {
            System.out.println("Please, provide access for application.");
        }
    }

    public void getPlaylists(String playlistName) {
        if(authorized) {
            api = new CategoryPlaylists(playlistName);
            api.get();
        } else {
            System.out.println("Please, provide access for application.");
        }
    }
}
