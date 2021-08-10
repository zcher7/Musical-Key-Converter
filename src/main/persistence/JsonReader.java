package persistence;


import model.Song;
import model.SongList;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Represents a reader that reads songList from JSON data stored in file
// Code structure borrowed from JsonSerializationDemo
public class JsonReader {

    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads songList from file and returns it;
    // throws IOException if an error occurs reading data from file
    public SongList read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseSongList(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses songList from JSON object and returns it
    private SongList parseSongList(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        SongList sl = new SongList(name);
        addSongs(sl, jsonObject);
        return sl;
    }

    // MODIFIES: sl
    // EFFECTS: parses songs from JSON object and adds them to songList
    private void addSongs(SongList sl, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("songList");
        for (Object json : jsonArray) {
            JSONObject nextSong = (JSONObject) json;
            addSong(sl, nextSong);
        }
    }

    // MODIFIES: sl
    // EFFECTS: parses song from JSON object and adds it to songList
    private void addSong(SongList sl, JSONObject jsonObject) {
        String noteString = jsonObject.getString("noteList");
        String keyString = jsonObject.getString("key");
        String name = jsonObject.getString("name");
        Double rating = jsonObject.getDouble("rating");
        Song song = new Song(noteString, keyString, name, rating);
        sl.addSong(song);
    }
}


