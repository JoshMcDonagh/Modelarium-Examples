package sirbasic.agents.attributes.sir;

import modelarium.agents.Agent;
import modelarium.agents.AgentSet;
import modelarium.attributes.Event;
import sirbasic.agents.attributes.location.Coordinates;
import sirbasic.agents.attributes.location.LocationProperty;
import sirbasic.config.ConfigLoader;
import sirbasic.config.SIRConfig;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

public class InfectedEvent extends Event {
    private static double euclideanDistance(Coordinates coordinates1, Coordinates coordinates2) {
        double dx = coordinates2.getX() - coordinates1.getX();
        double dy = coordinates2.getY() - coordinates1.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    private final double infectionProbabilityPerContact;
    private final double contactDistance;

    public InfectedEvent() {
        super("infected", true);
        SIRConfig sirConfig = ConfigLoader.loadSIRConfig("sir-config.json");
        infectionProbabilityPerContact = sirConfig.disease().infectionProbabilityPerContact();
        contactDistance = sirConfig.movement().contactDistance();
    }

    @Override
    public boolean isTriggered() {
        SIRState sirState =
                ((SIRStateProperty)getAssociatedModelElement()
                        .getAttributeSetCollection()
                        .get("sir")
                        .getProperties()
                        .get("sir_state"))
                        .get();

        if (sirState != SIRState.SUSCEPTIBLE)
            return false;

        Coordinates coordinates =
                ((LocationProperty)getAssociatedModelElement()
                        .getAttributeSetCollection()
                        .get("location")
                        .getProperties()
                        .get("location"))
                        .get();

        Predicate<Agent> nearbyAndInfectiousOnly = agent -> {
            Coordinates otherCoordinates =
                    ((LocationProperty)agent
                            .getAttributeSetCollection()
                            .get("location")
                            .getProperties()
                            .get("location"))
                            .get();

            if (euclideanDistance(coordinates, otherCoordinates) > contactDistance)
                return false;

            SIRState otherSirState =
                    ((SIRStateProperty)agent
                            .getAttributeSetCollection()
                            .get("sir")
                            .getProperties()
                            .get("sir_state"))
                            .get();

            return otherSirState == SIRState.INFECTIOUS;
        };

        AgentSet otherAgentsNearby =
                getAssociatedModelElement()
                        .getModelElementAccessor().getFilteredAgents(nearbyAndInfectiousOnly);

        return otherAgentsNearby.size() > 0;
    }

    @Override
    public void run() {
        if (ThreadLocalRandom.current().nextDouble(0.0, 1.0)
                >= infectionProbabilityPerContact)
            return;

        ((SIRStateProperty)getAssociatedModelElement()
                .getAttributeSetCollection()
                .get("sir")
                .getProperties()
                .get("sir_state"))
                .set(SIRState.INFECTIOUS);
    }
}
