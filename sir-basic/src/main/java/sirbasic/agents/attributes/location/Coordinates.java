package sirbasic.agents.attributes.location;

import java.util.concurrent.ThreadLocalRandom;

public class Coordinates {
    private int x;
    private int y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setX(int xCoordinate) {
        this.x = xCoordinate;
    }

    public void setY(int yCoordinate) {
        this.y = yCoordinate;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    void moveRandomlyBy(int maxDistance, int maxXCoordinate, int maxYCoordinate) {
        if (maxDistance < 1)
            throw new IllegalArgumentException("maxDistance must be >= 1");

        int dx;
        int dy;

        do {
            dx = ThreadLocalRandom.current().nextInt(-maxDistance, maxDistance + 1);
            dy = ThreadLocalRandom.current().nextInt(-maxDistance, maxDistance + 1);
        } while (dx == 0 && dy == 0);

        x = Math.max(0, Math.min(x + dx, maxXCoordinate));
        y = Math.max(0, Math.min(y + dy, maxYCoordinate));
    }
}
