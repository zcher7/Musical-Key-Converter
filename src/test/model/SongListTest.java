package model;

import exceptions.InvalidKeyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SongListTest {

    private SongList songList;
    private Key keyFSharp;

    @BeforeEach
    void runBefore() {
        songList = new SongList("Next Biggest Hits");
        try {
            keyFSharp = new Key("F#");
        } catch (InvalidKeyException exception) {
            fail("InvalidKeyException should not have been thrown.");
        }
    }

    @Test
    void testConstructor() {
        assertEquals("Next Biggest Hits", songList.getName());
        assertEquals(0, songList.getSongList().size());
    }

    @Test
    void testAddSong() {
        Song song = new Song("AC#BbD", keyFSharp);
        songList.addSong(song);
        assertEquals(1, songList.getSongList().size());
        assertEquals(song, songList.getSongList().get(0));
    }

    @Test
    void testRemoveSong() {
        Song song = new Song("AC#BbD", keyFSharp);
        songList.addSong(song);
        assertEquals(1, songList.getSongList().size());
        assertEquals(song, songList.getSongList().get(0));

        songList.removeSong(0);
        assertEquals(0, songList.getSongList().size());
    }
}
