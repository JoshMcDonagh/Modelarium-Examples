package sirbasic.agents.attributes;

import modelarium.attributes.Property;

public class SIRStateProperty extends Property<SIRState> {
    SIRState state;

    public SIRStateProperty() {
        super("SIR_state", true, SIRState.class);
    }

    @Override
    public void set(SIRState sirState) {
        state = sirState;
    }

    @Override
    public SIRState get() {
        return state;
    }

    @Override
    public void run() {
        // TODO: Implement state transitions
    }
}
