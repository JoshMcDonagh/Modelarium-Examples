package sirbasic.agents;

import modelarium.ModelSettings;
import modelarium.agents.Agent;
import modelarium.agents.AgentGenerator;
import modelarium.attributes.AttributeSetCollection;
import sirbasic.agents.attributes.location.Coordinates;
import sirbasic.agents.attributes.location.LocationProperty;
import sirbasic.agents.attributes.sir.SIRState;
import sirbasic.agents.attributes.sir.SIRStateProperty;
import sirbasic.config.ConfigLoader;
import sirbasic.config.SIRConfig;

import java.util.concurrent.ThreadLocalRandom;

public class SIRAgentGenerator extends AgentGenerator {
    private static int agentCount = 0;
    private static int susceptibleAgentCount = 0;
    private static int infectiousAgentCount = 0;
    private static int recoveredAgentCount = 0;

    private final SIRConfig sirConfig;

    public SIRAgentGenerator() {
        sirConfig = ConfigLoader.loadSIRConfig("sir-config.json");
    }

    @Override
    protected Agent generateAgent(ModelSettings modelSettings) {
        AttributeSetCollection agentAttributeSetCollection = modelSettings
                .getBaseAgentAttributeSetCollection()
                .deepCopy();

        // Randomly set agent's location
        int xCoordinate = ThreadLocalRandom.current().nextInt(0, sirConfig.environment().width());
        int yCoordinate = ThreadLocalRandom.current().nextInt(0, sirConfig.environment().height());
        ((LocationProperty)agentAttributeSetCollection
                .get("location")
                .getProperties()
                .get("location"))
                .set(new Coordinates(xCoordinate, yCoordinate));

        // Set agent's SIR state
        if (susceptibleAgentCount < sirConfig.initialStates().S()) {
            ((SIRStateProperty)agentAttributeSetCollection
                    .get("sir")
                    .getProperties()
                    .get("sir_state"))
                    .set(SIRState.SUSCEPTIBLE);
            susceptibleAgentCount++;
        } else if (infectiousAgentCount < sirConfig.initialStates().I()) {
            ((SIRStateProperty)agentAttributeSetCollection
                    .get("sir")
                    .getProperties()
                    .get("sir_state"))
                    .set(SIRState.INFECTIOUS);
            infectiousAgentCount++;
        } else if (recoveredAgentCount < sirConfig.initialStates().R()) {
            ((SIRStateProperty)agentAttributeSetCollection
                    .get("sir")
                    .getProperties()
                    .get("sir_state"))
                    .set(SIRState.RECOVERED);
            recoveredAgentCount++;
        } else {
            throw new IllegalStateException("Agent cannot be generated - all initial SIR states have already been assigned.");
        }

        Agent newAgent = new Agent("Agent_" + agentCount, agentAttributeSetCollection);
        agentCount++;

        return newAgent;
    }
}
