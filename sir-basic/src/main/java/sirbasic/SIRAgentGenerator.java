package sirbasic;

import modelarium.ModelSettings;
import modelarium.agents.Agent;
import modelarium.agents.AgentGenerator;
import modelarium.attributes.AttributeSetCollection;
import sirbasic.attributes.SIRState;
import sirbasic.attributes.SIRStateProperty;
import sirbasic.config.ConfigLoader;
import sirbasic.config.SIRConfig;

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
        AttributeSetCollection agentAttributeSetCollection = modelSettings.getBaseAgentAttributeSetCollection().deepCopy();
        if (susceptibleAgentCount < sirConfig.initialStates().S()) {
            ((SIRStateProperty)agentAttributeSetCollection
                    .get("sir_attribute_set")
                    .getProperties()
                    .get("SIR_state"))
                    .set(SIRState.SUSCEPTIBLE);
            susceptibleAgentCount++;
        } else if (infectiousAgentCount < sirConfig.initialStates().I()) {
            ((SIRStateProperty)agentAttributeSetCollection
                    .get("sir_attribute_set")
                    .getProperties()
                    .get("SIR_state"))
                    .set(SIRState.INFECTIOUS);
            infectiousAgentCount++;
        } else if (recoveredAgentCount < sirConfig.initialStates().R()) {
            ((SIRStateProperty)agentAttributeSetCollection
                    .get("sir_attribute_set")
                    .getProperties()
                    .get("SIR_state"))
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
