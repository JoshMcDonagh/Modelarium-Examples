package sirbasic;

import modelarium.Model;
import modelarium.ModelSettings;
import modelarium.attributes.AttributeSetCollection;
import modelarium.results.Results;
import modelarium.scheduler.RandomOrderScheduler;
import sirbasic.agents.SIRAgentGenerator;
import sirbasic.agents.attributes.location.LocationAttributeSet;
import sirbasic.agents.attributes.sir.SIRAttributeSet;
import sirbasic.config.ConfigLoader;
import sirbasic.config.SIRConfig;
import sirbasic.environment.SIREnvironmentGenerator;
import sirbasic.environment.attributes.geography.GeographyAttributeSet;
import sirbasic.results.ResultsExporter;
import sirbasic.results.SIRResults;

import java.lang.reflect.InvocationTargetException;

public class SIRBasicMain {
    private static AttributeSetCollection getBaseAgentAttributeSetCollection() {
        AttributeSetCollection attributeSetCollection = new AttributeSetCollection();
        attributeSetCollection.add(new LocationAttributeSet());
        attributeSetCollection.add(new SIRAttributeSet());
        return attributeSetCollection;
    }

    private static AttributeSetCollection getBaseEnvironmentAttributeSetCollection() {
        AttributeSetCollection attributeSetCollection = new AttributeSetCollection();
        attributeSetCollection.add(new GeographyAttributeSet());
        return attributeSetCollection;
    }

    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        SIRConfig sirConfig = ConfigLoader.loadSIRConfig("sir-config.json");

        ModelSettings settings = new ModelSettings();

        settings.setNumOfAgents(
                sirConfig.initialStates().S()
                + sirConfig.initialStates().I()
                + sirConfig.initialStates().R());

        settings.setNumOfCores(sirConfig.modelSettings().numOfCores());
        settings.setNumOfTicksToRun(sirConfig.modelSettings().numOfTicks());
        settings.setNumOfWarmUpTicks(sirConfig.modelSettings().numOfWarmUpTicks());

        settings.setBaseAgentAttributeSetCollection(getBaseAgentAttributeSetCollection());
        settings.setBaseEnvironmentAttributeSetCollection(getBaseEnvironmentAttributeSetCollection());

        settings.setAreProcessesSynced(true);
        settings.setIsCacheUsed(true);

        settings.setResultsClass(SIRResults.class);
        settings.setResults(new SIRResults());

        settings.setAgentGenerator(new SIRAgentGenerator());
        settings.setEnvironmentGenerator(new SIREnvironmentGenerator());
        settings.setModelScheduler(new RandomOrderScheduler());

        Results results = new Model(settings).run();

        String timestamp = java.time.LocalDateTime
                .now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));

        ResultsExporter.export(results, "results/run-" + timestamp);
    }
}
