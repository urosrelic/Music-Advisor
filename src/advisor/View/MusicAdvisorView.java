package advisor.View;

import advisor.Controller.MusicAdvisorController;

import java.util.Scanner;

public class MusicAdvisorView {
    public void startService() {
        MusicAdvisorController controller = new MusicAdvisorController();

        System.out.println("Please authorize the application by typing 'auth'.");
        Scanner scanner = new Scanner(System.in);

        boolean loopEnd = false;
        while(!loopEnd) {
            String choice = scanner.nextLine();
            String chosenPlaylist = null;

            if (choice.contains("playlists")) {
                chosenPlaylist = choice.replace("playlists", "").strip();
                choice = "playlists";
            }

            switch (choice) {
                case "auth":
                    controller.setAuthorization();
                    break;
                case "featured":
                    controller.getFeatured();
                    break;
                case "new":
                    controller.getNewReleases();
                    break;
                case "categories":
                    controller.getCategories();
                    break;
                case "playlists":
                    controller.getPlaylists(chosenPlaylist);
                    break;
                case "exit":
                    System.out.println("---GOODBYE!---");
                    loopEnd = true;
                    break;
                case "next":
                    controller.nextPage();
                    break;
                case "prev":
                    controller.previousPage();
                    break;
                default:
                    System.out.println("Unknown option.");
                    break;
            }
        }
    }
}
