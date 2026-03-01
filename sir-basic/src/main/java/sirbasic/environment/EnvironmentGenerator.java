package sirbasic.environment;

import modelarium.ModelSettings;
import modelarium.attributes.AttributeSetCollection;
import modelarium.environments.Environment;
import sirbasic.config.ConfigLoader;
import sirbasic.config.SIRConfig;
import sirbasic.environment.attributes.geography.HeightProperty;
import sirbasic.environment.attributes.geography.WidthProperty;

public class EnvironmentGenerator extends modelarium.environments.EnvironmentGenerator {
    private final SIRConfig sirConfig;

    public EnvironmentGenerator() {
        sirConfig = ConfigLoader.loadSIRConfig("sir-config.json");
    }

    @Override
    public Environment generateEnvironment(ModelSettings modelSettings) {
        AttributeSetCollection environmentAttributeSetCollection = modelSettings
                .getBaseEnvironmentAttributeSetCollection()
                .deepCopy();

        ((WidthProperty)environmentAttributeSetCollection
                .get("geography")
                .getProperties()
                .get("width"))
                .set(sirConfig.environment().width());

        ((HeightProperty)environmentAttributeSetCollection
                .get("geography")
                .getProperties()
                .get("height"))
                .set(sirConfig.environment().height());

        return new Environment("Environment", environmentAttributeSetCollection);
    }
}
