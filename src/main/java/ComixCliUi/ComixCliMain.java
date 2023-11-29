package ComixCliUi;

import java.io.IOException;

public class ComixCliMain {
    
    public static void main(String[] args) throws IOException {
        ComixCliUtils.heading();
        ComixCliUtils.divider();
        ComixCliUtils.printMessage("Welcome to COMIX, the best way to manage your comic collection!");
        ComixCliClient comix = new ComixCliClient();
        comix.mainLoop();
    }
}