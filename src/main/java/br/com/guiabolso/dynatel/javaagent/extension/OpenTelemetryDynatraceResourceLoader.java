package br.com.guiabolso.dynatel.javaagent.extension;

import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.sdk.autoconfigure.spi.ConfigProperties;
import io.opentelemetry.sdk.autoconfigure.spi.ResourceProvider;
import io.opentelemetry.sdk.resources.Resource;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class OpenTelemetryDynatraceResourceLoader implements ResourceProvider {

    private static final Logger LOGGER = Logger.getLogger(OpenTelemetryDynatraceResourceLoader.class.getSimpleName());
    private static final List<String> PROPERTY_FILES = List.of(
            "dt_metadata_e617c525669e072eebe3d0f08212e8f2.properties",
            "/var/lib/dynatrace/enrichment/dt_metadata.properties"
    );

    private Resource resource;

    @Override
    public Resource createResource(ConfigProperties config) {
        List<String> metadataFilePaths = config.getList("dynatrace.metadata.file.paths", PROPERTY_FILES);
        return createResource(metadataFilePaths);
    }

    private static Resource createResource(List<String> metadataFilePaths) {
        Attributes dynatraceEnvironmentMetadata = readDynatraceMetadataFrom(metadataFilePaths);

        if (dynatraceEnvironmentMetadata.isEmpty()) return Resource.empty();

        log(dynatraceEnvironmentMetadata.asMap());
        return Resource.create(dynatraceEnvironmentMetadata);
    }

    private static Attributes readDynatraceMetadataFrom(List<String> metadataFilePaths) {
        return DynatracePropertiesLoader
                .readDynatraceMetadataFrom(metadataFilePaths)
                .entrySet()
                .stream()
                .reduce(
                        Attributes.builder(),
                        (builder, prop) -> builder.put(prop.getKey().toString(), prop.getValue().toString()),
                        (acc, builder) -> acc.putAll(builder.build()))
                .build();
    }

    private static void log(Map<AttributeKey<?>, Object> attributes) {
        List<String> attrsPairs =
                attributes.entrySet()
                        .stream()
                        .map(entry -> String.format("(%s=%s)", entry.getKey().getKey(), entry.getValue().toString()))
                        .collect(Collectors.toList());
        if (!attrsPairs.isEmpty()) LOGGER.info("Loaded resource attributes " + attrsPairs);
    }
}
