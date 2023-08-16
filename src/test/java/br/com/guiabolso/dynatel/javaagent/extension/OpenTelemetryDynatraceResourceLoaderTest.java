package br.com.guiabolso.dynatel.javaagent.extension;

import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.sdk.autoconfigure.spi.internal.DefaultConfigProperties;
import io.opentelemetry.sdk.resources.Resource;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OpenTelemetryDynatraceResourceLoaderTest {
    OpenTelemetryDynatraceResourceLoader loader = new OpenTelemetryDynatraceResourceLoader();

    @Test
    void should_create_resources_with_all_properties_metadata() {
        Map<String, String> config = Map.of("dynatrace.metadata.file.paths", "src/test/resources/dt_metadata_e617c525669e072eebe3d0f08212e8f2.properties");

        Resource resource = loader.createResource(DefaultConfigProperties.createForTest(config));

        String host = resource.getAttribute(AttributeKey.stringKey("dt-host"));
        String podName = resource.getAttribute(AttributeKey.stringKey("pod.name"));

        assertEquals("ABC-123", host);
        assertEquals(podName, "MyPodName");
    }
}
