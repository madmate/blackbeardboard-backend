package backend.blackbeardboard;

import jakarta.inject.Singleton;

@Singleton
public class BoardController {

    private int counter;

    public int getNumber() {
        return 4;
    }

    public int count() {
        return ++counter;
    }
}
