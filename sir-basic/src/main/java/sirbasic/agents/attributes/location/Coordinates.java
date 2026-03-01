package sirbasic.agents.attributes.location;

import java.util.concurrent.ThreadLocalRandom;

public class Coordinates {
    private int xCoordinate;
    private int yCoordinate;

    public Coordinates(int xCoordinate, int yCoordinate) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    public void setXCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public void setYCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public int getXCoordinate() {
        return xCoordinate;
    }

    public int getYCoordinate() {
        return yCoordinate;
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

        xCoordinate = Math.max(0, Math.min(xCoordinate + dx, maxXCoordinate));
        yCoordinate = Math.max(0, Math.min(yCoordinate + dy, maxYCoordinate));
    }
}
