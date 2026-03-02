package sirbasic.agents.attributes.sir;

import modelarium.attributes.AttributeSet;

public class SIRAttributeSet extends AttributeSet {
    public SIRAttributeSet() {
        super("sir");
        getPreEvents().add(new InfectedEvent());
        getProperties().add(new SIRStateProperty());
    }
}
