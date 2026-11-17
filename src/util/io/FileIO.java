package util.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class FileIO {
    
    /**
     * Reads the file from the given file name, found in the data folder.
     * @param filename name of the file.
     * @return a map in the form (String, String). The first entry is the size of the world. The rest is (typeofobject, amount) in Strings.
     */
    public Map<String, String> read(String filename){
        Map<String, String> content = new HashMap<>();
        String line = "";
        Path path = Path.of("data", filename);
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            boolean first = true;
            while((line = reader.readLine()) != null){
                if(first){
                    content.put("size", line);
                    first = false;
                }else {
                    content.put(line.split(" ")[0], line.split(" ")[1]);
                }
            }
        } catch (IOException exception) {
            System.out.println("Error whilst reading file: "+exception.getMessage());
        }
        return content;
    }


}
