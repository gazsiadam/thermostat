package com.sh.thermostat.actuator.repository;

import lombok.extern.log4j.Log4j2;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Log4j2
public abstract class AbstractFileRepository<T> {

    private final String filePath;

    public AbstractFileRepository(String filePath, String fileName) {
        if (StringUtils.isEmpty(filePath)) {
            this.filePath = System.getProperty("user.home") + fileName;
        } else {
            this.filePath = filePath;
        }

        Path path = Path.of(this.filePath);
        if (!Files.exists(path)) {
            log.warn("Repository file does not exists ({})", path);
            try {
                Files.createFile(path);
                writeFile(getDefaultValue().toString());
            } catch (IOException e) {
                log.error("Cannot create repository file", e);
                throw new RuntimeException("Cannot create repository file");
            } catch (Exception e) {
                log.error("Cannot write default value of repository", e);
                throw new RuntimeException("Cannot write default value of repository");
            }
        }
    }

    protected void writeFile(String state) throws Exception {
        Files.writeString(Path.of(filePath), state);
    }

    protected void appendFile(String value) throws IOException {
        Files.writeString(Path.of(filePath), value, StandardOpenOption.APPEND);
    }

    protected String readFile() {
        try {
            return Files.readString(Path.of(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return getDefaultValue().toString(); // TODO: return correct string
    }

    protected abstract T getDefaultValue();

}
