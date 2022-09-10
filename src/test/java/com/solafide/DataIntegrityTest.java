package com.solafide;

import static com.fasterxml.jackson.databind.MapperFeature.USE_ANNOTATIONS;
import static com.solafide.util.TestUtility.readLinesFor;
import static com.solafide.util.TestUtility.walkRegularFilesFor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solafide.domain.Catechism;
import com.solafide.domain.CatechismParts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.file.Paths;
import java.util.List;

import lombok.SneakyThrows;

class DataIntegrityTest {
    private ObjectMapper objectMapper;

    @SneakyThrows
    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(USE_ANNOTATIONS);
    }

    @SneakyThrows
    @ParameterizedTest
    @ValueSource(strings = {"catechisms", "pilot", "production"})
    void allCatechismsPresent(String folder) {
        List<Catechism> catechisms = objectMapper.readValue(Paths.get(".", folder, "catechisms.json").toFile(),
                new TypeReference<List<Catechism>>(){});
        assertEquals(2, catechisms.size());
        Catechism catechism0 = catechisms.get(0);
        assertEquals(0, catechism0.getId());
        assertEquals("Concordia Triglotta", catechism0.getTitle());
        CatechismParts catechismParts0 = objectMapper.readValue(Paths.get(".", folder, "catechism", "0", "catechism.json").toFile(),
                CatechismParts.class);
        assertEquals("The Ten Commandments", catechismParts0.get("TenCommandments").Category);

        Catechism catechism1 = catechisms.get(1);
        assertEquals(1, catechism1.getId());
        assertEquals("Dr. Martin Luther's Small Catechism", catechism1.getTitle());
        CatechismParts catechismParts1 = objectMapper.readValue(Paths.get(".", folder, "catechism", "1", "catechism.json").toFile(),
                CatechismParts.class);
        assertEquals("The Ten Commandments", catechismParts1.get("TenCommandments").Category);
    }

    @ParameterizedTest
    @ValueSource(strings = {"catechisms", "pilot", "production"})
    void allJsonIsMinified(String folder) {
        walkRegularFilesFor(Paths.get(".", folder)).stream()
                .parallel()
                .filter(p -> p.getFileName().toString().endsWith(".json"))
                .forEach(j -> assertEquals(1, readLinesFor(j).size(), j + " is not minified"));
    }
}
