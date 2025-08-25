package com.ican.cortex.platform.utilities.file.utils;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

@Component
public class FileUtils {

    public static String readFile(String filePath) throws IOException {
        return Files.readString(Path.of(filePath));
    }

    public static List<String> readFileLines(String filePath) throws IOException {
        return Files.readAllLines(Path.of(filePath));
    }

    public static void writeFile(String filePath, String content) throws IOException {
        Files.writeString(Path.of(filePath), content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public static void appendToFile(String filePath, String content) throws IOException {
        Files.writeString(Path.of(filePath), content, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    public static boolean deleteFile(String filePath) throws IOException {
        return Files.deleteIfExists(Path.of(filePath));
    }

    public static boolean fileExists(String filePath) {
        return Files.exists(Path.of(filePath));
    }
}