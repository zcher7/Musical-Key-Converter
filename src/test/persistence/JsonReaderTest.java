package persistence;

import model.Song;
import model.SongList;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Test structure borrowed from JsonSerializationDemo
public class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            SongList sl = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptySongList() {
        JsonReader reader = new JsonReader("./data/testReaderEmptySongList.json");
        try {
            SongList sl = reader.read();
            assertEquals("My song list", sl.getName());
            assertEquals(0, sl.numSong());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralSongList() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralSongList.json");
        try {
            SongList sl = reader.read();
            assertEquals("My song list", sl.getName());
            List<Song> songList = sl.getSongList();
            assertEquals(2, songList.size());
            checkSong("first", 8.8, "[D, E, F#, B]", "D", songList.get(0));
            checkSong("second", 4.5, "[E, E, E, E, E, E, E, E, E, E, E]", "E",
                    songList.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
