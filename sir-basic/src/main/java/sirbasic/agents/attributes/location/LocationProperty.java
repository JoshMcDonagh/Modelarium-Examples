package sirbasic.agents.attributes.location;

import modelarium.attributes.Property;
import sirbasic.config.ConfigLoader;
import sirbasic.environment.attributes.geography.HeightProperty;
import sirbasic.environment.attributes.geography.WidthProperty;

import java.util.concurrent.ThreadLocalRandom;

public class LocationProperty extends Property<Coordinates> {
    private final double movementProbabilityPerTick;

    private Coordinates coordinates;

    public LocationProperty() {
        super("location", true, Coordinates.class);
        movementProbabilityPerTick =
                ConfigLoader.loadSIRConfig("sir-config.json")
                        .movement()
                        .probabilityPerTick();
    }

    @Override
    public void set(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public Coordinates get() {
        return coordinates;
    }

    @Override
    public void run() {
        if (ThreadLocalRandom.current().nextDouble(0.0, 1.0 + Double.MIN_VALUE)
                < movementProbabilityPerTick) {
            int geographicalAreaWidth =
                    ((WidthProperty)getAssociatedModelElement()
                            .getModelElementAccessor()
                            .getEnvironment()
                            .getAttributeSetCollection()
                            .get("geography")
                            .getProperties()
                            .get("width"))
                            .get();

            int geographicalAreaHeight =
                    ((HeightProperty)getAssociatedModelElement()
                            .getModelElementAccessor()
                            .getEnvironment()
                            .getAttributeSetCollection()
                            .get("geography")
                            .getProperties()
                            .get("height"))
                            .get();

            coordinates.moveRandomlyBy(1, geographicalAreaWidth, geographicalAreaHeight);
        }
    }
}
