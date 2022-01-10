package advisor;

import advisor.Model.MusicApi;
import advisor.Model.ServerAuthorization;
import advisor.View.MusicAdvisorView;

public class Main {

    public static void main(String[] args) {
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "-access":
                        i++;
                        ServerAuthorization.SERVER_PATH = args[i];
                        break;
                    case "-resource":
                        i++;
                        MusicApi.API_LINK = args[i];
                        break;
                    case "-page":
                        i++;
                        MusicApi.itemsPerPage = Integer.parseInt(args[i]);
                        break;
                }
            }
        }

        new MusicAdvisorView().startService();
    }
}
