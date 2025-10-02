package com.example.json_to_sql;


import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class App 
{
	public static void main(String[] args) {
        // Path to your JSON file
        String filePath = "C:\\Users\\User\\Desktop\\MusicbrainzDatasets\\mbdump\\artist";

        // Create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Read JSON file as tree
            JsonNode rootNode = objectMapper.readTree(new File(filePath));

            if (rootNode.isArray() && rootNode.size() > 0) {
                // Get the first node
                JsonNode firstNode = rootNode.get(0);

                // Print all attributes in the first node
                Iterator<String> fieldNames = firstNode.fieldNames();
                while (fieldNames.hasNext()) {
                    String fieldName = fieldNames.next();
                    System.out.println("Attribute: " + fieldName);
                }
            } else {
                System.out.println("The JSON file does not contain an array or is empty.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
