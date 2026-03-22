package sirbasic.results;

import modelarium.results.Results;
import sirbasic.agents.attributes.sir.SIRState;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SIRResults extends Results {
    @Override
    protected List<?> accumulateAgentPropertyResults(
            String attributeSetName,
            String propertyName,
            List<?> accumulatedValues,
            List<?> valuesToBeProcessed) {
        if (Objects.equals(attributeSetName, "sir")) {
            if (Objects.equals(propertyName, "sir_state")) {
                List<Integer> result;
                if (accumulatedValues != null)
                    result = (List<Integer>) accumulatedValues;
                else {
                    result = new ArrayList<>();
                    for (int i = 0; i < valuesToBeProcessed.size(); i++)
                        result.add(0);
                }

                for (int i = 0; i < valuesToBeProcessed.size(); i++) {
                    SIRState state = (SIRState) valuesToBeProcessed.get(i);
                    if (state == SIRState.INFECTIOUS)
                        result.set(i, result.get(i) + 1);
                }

                return result;
            }
        }
        return List.of();
    }

    @Override
    protected List<?> accumulateAgentPreEventResults(
            String attributeSetName,
            String preEventName,
            List<?> accumulatedValues,
            List<Boolean> valuesToBeProcessed) {
        if (Objects.equals(attributeSetName, "sir")) {
            if (Objects.equals(preEventName, "infected")) {
                List<Integer> result;
                if (accumulatedValues != null)
                    result = (List<Integer>) accumulatedValues;
                else {
                    result = new ArrayList<>();
                    for (int i = 0; i < valuesToBeProcessed.size(); i++)
                        result.add(0);
                }

                for (int i = 0; i < valuesToBeProcessed.size(); i++) {
                    boolean isTriggered = valuesToBeProcessed.get(i);
                    if (isTriggered)
                        result.set(i, result.get(i) + 1);
                }

                return result;
            }
        }
        return List.of();
    }

    @Override
    protected List<?> accumulateAgentPostEventResults(
            String attributeSetName,
            String postEventName,
            List<?> accumulatedValues,
            List<Boolean> valuesToBeProcessed) {
        return List.of();
    }
}
