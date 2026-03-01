package sirbasic.agents.attributes.location;

import modelarium.attributes.AttributeSet;

public class LocationAttributeSet extends AttributeSet {
    public LocationAttributeSet() {
        super("location");
        getProperties().add(new LocationProperty());
    }
}
