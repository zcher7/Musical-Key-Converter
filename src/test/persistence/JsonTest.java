package persistence;

import model.Song;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Test structure borrowed from JsonSerializationDemo
public class JsonTest {
    protected void checkSong(String name, double rating, String noteString, String keyString, Song song) {
        assertEquals(name, song.getName());
        assertEquals(rating, song.getRating());
        assertEquals(noteString, song.getNoteString());
        assertEquals(keyString, song.getKeyString());
    }
}
