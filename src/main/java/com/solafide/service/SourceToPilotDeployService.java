package com.solafide.service;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import lombok.SneakyThrows;

public class SourceToPilotDeployService {
    @SneakyThrows
    public static void main(String[] args) {
        String projectDirectory = args[0];
        FileUtils.copyDirectory(
                Paths.get(projectDirectory, "catechisms").toFile(),
                Paths.get(projectDirectory, "pilot").toFile());
    }
}