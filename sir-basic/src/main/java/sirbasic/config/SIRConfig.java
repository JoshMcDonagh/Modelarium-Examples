package sirbasic.config;

public record SIRConfig(
        InitialStates initialStates,
        Environment environment,
        Movement movement,
        Disease disease
) {
    public record InitialStates(int S, int I, int R) {}
    public record Environment(int width, int height) {}
    public record Movement(double probabilityPerTick, double contactDistance) {}
    public record Disease(double infectionProbabilityPerContact, double recoveryProbabilityPerTick) {}
}
