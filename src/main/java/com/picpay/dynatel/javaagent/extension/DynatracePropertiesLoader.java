package com.picpay.dynatel.javaagent.extension;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class DynatracePropertiesLoader {

    private static final Logger LOGGER = Logger.getLogger(DynatracePropertiesLoader.class.getSimpleName());

    public static Properties readDynatraceMetadataFrom(List<String> metadataFilePaths) {
        Properties props = new Properties();
        for (String name : metadataFilePaths) {
            try (FileInputStream fileInputStream = name.startsWith("/var")
                    ? new FileInputStream(name)
                    : new FileInputStream(Files.readAllLines(Paths.get(name)).get(0))) {

                props.load(fileInputStream);
                LOGGER.info("Successfully read properties from " + name);
            } catch (IOException ignore) {
            }
        }

        return props;
    }
}
