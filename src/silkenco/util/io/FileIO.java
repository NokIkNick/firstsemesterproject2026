package silkenco.util.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import itumulator.world.Location;

public class FileIO {

    /**
     * Reads the world file with the given name from the data folder.
     * @param filename name of the file.
     * @return the world size and a list of everything to spawn, one entry per line.
     * @throws UncheckedIOException if the file could not be read.
     * @throws IllegalArgumentException if a line is not in a valid format.
     */
    public WorldInfo read(String filename){
        Path path = Path.of("data", filename);
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String firstLine = reader.readLine();
            if(firstLine == null){
                throw new IllegalArgumentException("World file is empty: " + path);
            }
            int size = Integer.parseInt(firstLine.trim());

            List<SpawnInfo> spawns = new ArrayList<>();
            String line;
            while((line = reader.readLine()) != null){
                line = line.trim();
                if(line.isEmpty()) continue;
                spawns.add(parseLine(line));
            }
            return new WorldInfo(size, spawns);
        } catch (IOException exception) {
            throw new UncheckedIOException("Could not read world file: " + path, exception);
        }
    }

    /**
     * Turns one line "grass 10-15" or "bear 1 (3,5)", into SpawnInfo.
     */
    private SpawnInfo parseLine(String line){
        String[] parts = line.split("\\s+");
        if(parts.length < 2 || parts.length > 3){
            throw new IllegalArgumentException("Invalid line in world file: \"" + line + "\"");
        }

        String type = parts[0];

        int min;
        int max;
        if(parts[1].contains("-")){
            String[] range = parts[1].split("-");
            min = Integer.parseInt(range[0]);
            max = Integer.parseInt(range[1]);
        }else{
            min = Integer.parseInt(parts[1]);
            max = min;
        }

        Location location = null;
        if(parts.length == 3){
            location = parseLocation(parts[2]);
        }

        return new SpawnInfo(type, min, max, location);
    }

    /**
     * Turns "(3,5)" into a Location.
     */
    private Location parseLocation(String text){
        String[] coordinates = text.replace("(", "").replace(")", "").split(",");
        int x = Integer.parseInt(coordinates[0].trim());
        int y = Integer.parseInt(coordinates[1].trim());
        return new Location(x, y);
    }
}
