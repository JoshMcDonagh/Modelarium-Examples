package sirbasic.attributes;

import modelarium.attributes.AttributeSet;

public class SIRAttributeSet extends AttributeSet {
    public SIRAttributeSet() {
        super("sir_attribute_set");
        getProperties().add(new SIRStateProperty());
    }
}
