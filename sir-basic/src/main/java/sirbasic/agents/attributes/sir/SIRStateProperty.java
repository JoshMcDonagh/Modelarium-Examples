package sirbasic.agents.attributes.sir;

import modelarium.attributes.Property;
import sirbasic.config.ConfigLoader;

import java.util.concurrent.ThreadLocalRandom;

public class SIRStateProperty extends Property<SIRState> {
    private final double recoveryProbabilityPerTick;

    private SIRState state;

    public SIRStateProperty() {
        super("sir_state", true, SIRState.class);
        recoveryProbabilityPerTick =
                ConfigLoader.loadSIRConfig("sir-config.json")
                        .disease()
                        .recoveryProbabilityPerTick();
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
        if (state == SIRState.INFECTIOUS
                && ThreadLocalRandom.current().nextDouble(0.0, 1.0 + Double.MIN_VALUE)
                < recoveryProbabilityPerTick)
            set(SIRState.RECOVERED);
    }
}
