package de.nevini.core.services;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Service
public class VersionService {

    @Getter
    private final String version;

    public VersionService() throws IOException {
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/version.txt"), StandardCharsets.UTF_8)
        )) {
            version = Objects.requireNonNull(in.readLine());
        }
    }

}
