package com.example.json_to_sql;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class StructureJsonFormat {

    public static void main(String[] args) {
        String inputFilePath = "C:/Users/User/Desktop/MusicbrainzDatasets/mbdump/artist";
        String outputFilePath = "C:/Users/User/Desktop/MusicbrainzDatasets/mbdump/formatted1_artist.json";

        try {
            formatJson(inputFilePath, outputFilePath, 2000);  // Limit to 2000 root elements
            System.out.println("Formatted JSON has been saved to " + outputFilePath);
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

    public static void formatJson(String inputFilePath, String outputFilePath, int limit) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        // Read the input file as a single string
        String jsonContent = new String(Files.readAllBytes(Paths.get(inputFilePath)));

        // Parse the JSON string, correcting the structure if necessary
        JsonNode rootNode = objectMapper.readTree(jsonContent);

        // Create a new JSON node to hold the limited number of root elements
        ObjectMapper outputMapper = new ObjectMapper();
        outputMapper.enable(SerializationFeature.INDENT_OUTPUT);
        ArrayNode limitedNode = outputMapper.createArrayNode();

        int count = 0;
        if (rootNode.isArray()) {
            for (JsonNode node : rootNode) {
                if (count < limit) {
                    limitedNode.add(node);
                    count++;
                } else {
                    break;
                }
            }
        } else {
            limitedNode.add(rootNode);
        }

        // Write the formatted limited JSON to the output file
        outputMapper.writeValue(new File(outputFilePath), limitedNode);
    }
}
