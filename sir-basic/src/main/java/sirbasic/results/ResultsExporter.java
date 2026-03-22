package sirbasic.results;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import modelarium.results.Results;
import sirbasic.agents.attributes.location.Coordinates;
import sirbasic.agents.attributes.sir.SIRState;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ResultsExporter {
    private static final ObjectMapper OBJECT_MAPPER =
            new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    private ResultsExporter() {}

    public static void export(Results results, String outputDir) {
        Path outputPath = Path.of(outputDir);

        try {
            Files.createDirectories(outputPath);

            int agentCount = results.getAgentNames().size();
            int tickCount = determineTickCount(results);

            writeMetadata(results, outputPath.resolve("metadata.json"), agentCount, tickCount);
            writeAggregateCountsCsv(results, outputPath.resolve("aggregate_counts.csv"), tickCount);
            writeAgentStatesCsv(results, outputPath.resolve("agent_states.csv"), tickCount);

        } catch (IOException e) {
            throw new RuntimeException("Failed to export results to: " + outputDir, e);
        }
    }

    private static int determineTickCount(Results results) {
        List<String> agentNames = results.getAgentNames();

        if (agentNames.isEmpty())
            return 0;

        String firstAgentName = agentNames.getFirst();
        List<Object> sirStates = results.getAgentPropertyValues(firstAgentName, "sir", "sir_state");
        return sirStates.size();
    }

    private static void writeMetadata(
            Results results,
            Path file,
            int agentCount,
            int tickCount
    ) throws IOException {
        Map<String, Object> metadata = new LinkedHashMap<>();
        metadata.put("exportedAt", OffsetDateTime.now().toString());
        metadata.put("resultsClass", results.getClass().getName());
        metadata.put("agentCount", agentCount);
        metadata.put("tickCount", tickCount);
        metadata.put("files", List.of(
                "aggregate_counts.csv",
                "agent_states.csv"
        ));

        OBJECT_MAPPER.writeValue(file.toFile(), metadata);
    }

    private static void writeAggregateCountsCsv(
            Results results,
            Path file,
            int tickCount
    ) throws IOException {
        int[] susceptibleCounts = new int[tickCount];
        int[] infectiousCounts = new int[tickCount];
        int[] recoveredCounts = new int[tickCount];
        int[] newInfectionsCounts = new int[tickCount];

        for (String agentName : results.getAgentNames()) {
            List<Object> sirStates = results.getAgentPropertyValues(agentName, "sir", "sir_state");
            List<Boolean> infectedEvents = results.getAgentPreEventValues(agentName, "sir", "infected");

            if (sirStates.size() != tickCount) {
                throw new IllegalStateException(
                        "Agent " + agentName + " has " + sirStates.size()
                                + " sir_state values, expected " + tickCount + "."
                );
            }

            if (infectedEvents.size() != tickCount) {
                throw new IllegalStateException(
                        "Agent " + agentName + " has " + infectedEvents.size()
                                + " infected pre-event values, expected " + tickCount + "."
                );
            }

            for (int tick = 0; tick < tickCount; tick++) {
                SIRState state = (SIRState) sirStates.get(tick);

                switch (state) {
                    case SUSCEPTIBLE -> susceptibleCounts[tick]++;
                    case INFECTIOUS -> infectiousCounts[tick]++;
                    case RECOVERED -> recoveredCounts[tick]++;
                    default -> throw new IllegalStateException(
                            "Unexpected SIR state at tick " + tick + " for agent " + agentName + ": " + state
                    );
                }

                if (infectedEvents.get(tick))
                    newInfectionsCounts[tick]++;
            }
        }

        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            writer.write("tick,susceptible,infectious,recovered,new_infections");
            writer.newLine();

            for (int tick = 0; tick < tickCount; tick++) {
                writer.write(
                        tick + ","
                                + susceptibleCounts[tick] + ","
                                + infectiousCounts[tick] + ","
                                + recoveredCounts[tick] + ","
                                + newInfectionsCounts[tick]
                );
                writer.newLine();
            }
        }
    }

    private static void writeAgentStatesCsv(
            Results results,
            Path file,
            int tickCount
    ) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            writer.write("tick,agent_name,sir_state,x,y");
            writer.newLine();

            for (String agentName : results.getAgentNames()) {
                List<Object> sirStates = results.getAgentPropertyValues(agentName, "sir", "sir_state");
                List<Object> locations = results.getAgentPropertyValues(agentName, "location", "location");

                if (sirStates.size() != tickCount) {
                    throw new IllegalStateException(
                            "Agent " + agentName + " has " + sirStates.size()
                                    + " sir_state values, expected " + tickCount + "."
                    );
                }

                if (locations.size() != tickCount) {
                    throw new IllegalStateException(
                            "Agent " + agentName + " has " + locations.size()
                                    + " location values, expected " + tickCount + "."
                    );
                }

                for (int tick = 0; tick < tickCount; tick++) {
                    SIRState state = (SIRState) sirStates.get(tick);
                    Coordinates coordinates = (Coordinates) locations.get(tick);

                    writer.write(
                            tick + ","
                                    + escapeCsv(agentName) + ","
                                    + state.name() + ","
                                    + coordinates.getX() + ","
                                    + coordinates.getY()
                    );
                    writer.newLine();
                }
            }
        }
    }

    private static String escapeCsv(String value) {
        if (value == null)
            return "";

        boolean needsQuotes =
                value.contains(",")
                        || value.contains("\"")
                        || value.contains("\n")
                        || value.contains("\r");

        if (!needsQuotes)
            return value;

        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}