package sirbasic.environment.attributes.geography;

import modelarium.attributes.AttributeSet;

public class GeographyAttributeSet extends AttributeSet {
    public GeographyAttributeSet() {
        super("geography");
        getProperties().add(new WidthProperty());
        getProperties().add(new HeightProperty());
    }
}
