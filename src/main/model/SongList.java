package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Represents a song list with a name and list of Song
// Code structure borrowed from JsonSerializationDemo
public class SongList implements Writable {

    private String name;              // name of a song
    private List<Song> songList;      // list of song

    // EFFECTS: constructs song list with a name and empty list of songs
    public SongList(String name) {
        this.name = name;
        songList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    // MODIFIES: this
    // EFFECTS: adds song to this song list
    public void addSong(Song song) {
        songList.add(song);
    }

    public void removeSong(int index) {
        songList.remove(index);
    }

    // EFFECTS: returns an unmodifiable list of songs in this song list
    public List<Song> getSongList() {
        return Collections.unmodifiableList(songList);
    }

    // EFFECTS: returns number of songs in this song list
    public int numSong() {
        return songList.size();
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("songList", songsToJson());
        return json;
    }

    // EFFECTS: returns songs in song list as a JSON array
    private JSONArray songsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Song s : songList) {
            jsonArray.put(s.toJson());
        }

        return jsonArray;
    }
}
