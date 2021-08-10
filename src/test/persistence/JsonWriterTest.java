package persistence;

import model.Song;
import model.SongList;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Test structure borrowed from JsonSerializationDemo
public class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            SongList sl = new SongList("My song list");
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptySongList() {
        try {
            SongList sl = new SongList("My song list");
            JsonWriter writer = new JsonWriter("./data/testWriterEmptySongList.json");
            writer.open();
            writer.write(sl);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptySongList.json");
            sl = reader.read();
            assertEquals("My song list", sl.getName());
            assertEquals(0, sl.numSong());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralSongList() {
        try {
            SongList sl = new SongList("My song list");
            sl.addSong(new Song("[F#, B, A, C, D]", "A", "old", 1.2));
            sl.addSong(new Song("[A, E, G]", "F#", "new", 3.5));
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralSongList.json");
            writer.open();
            writer.write(sl);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralSongList.json");
            sl = reader.read();
            assertEquals("My song list", sl.getName());
            List<Song> songList = sl.getSongList();
            assertEquals(2, songList.size());
            checkSong("old", 1.2, "[F#, B, A, C, D]", "A", songList.get(0));
            checkSong("new", 3.5, "[A, E, G]", "F#", songList.get(1));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
