package model;

import exceptions.InvalidKeyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SongTest {

    private Song song;
    private Song song2;
    private Key keyFSharp;
    private Key keyE;
    private Key keyB;
    private Key keyC;

    @BeforeEach
    void runBefore() {
        try {
            song = new Song("AC#BbD", new Key("F#"));
            keyFSharp = new Key("F#");
            keyE = new Key("E");
            keyB = new Key("B");
            keyC = new Key("C");
        } catch (InvalidKeyException exception) {
            fail("InvalidKeyException should not have been thrown.");
        }

        song2 = new Song("[C, D, E, A]", "A", "first", 5.5);
    }

    @Test
    void testConstructorNoteList() {
        List<Note> noteList = new ArrayList<>();
        Note noteA = new Note('A', "A");
        Note noteCSharp = new Note('C', "C");
        Note noteBFlat = new Note('B', "B");
        Note noteD = new Note('D', "D");

        noteCSharp.addSharp();
        noteBFlat.addFlat();

        noteList.add(noteA);
        noteList.add(noteCSharp);
        noteList.add(noteBFlat);
        noteList.add(noteD);

        assertEquals(noteList, song.getNoteList());

    }

    @Test
    void testConstructorKey() {
        assertEquals(keyFSharp, song.getKey());
    }

    @Test
    void testConvertToNewKeyOver() {
        Song song1 = new Song("GBAbC", keyE);
        song1.convertToNewKey(keyFSharp);
        assertEquals(song1, song);
    }

    @Test
    void testConvertToNewKeyUnder() {
        Song song2 = new Song("DF#EbG", keyB);
        song2.convertToNewKey(keyFSharp);
        assertEquals(song2, song);
    }

    @Test
    void testEquals() {
        Object o = new Object();
        assertFalse(song.equals(o));
    }

    @Test
    void testEqualsNoteList() {
        Song song3 = new Song("ABEb", keyC);

        assertFalse(song.equals(song3));
    }

    @Test
    void testHashCode() {
        assertEquals(1408443512, song.hashCode());

        Song song4 = new Song("", keyB);
        assertEquals(122, song4.hashCode());
    }



    // tests here are for coverage
    @Test
    void testGetRating() {
        assertTrue(song.getRating() <= 10);
        assertTrue(song.getRating() >= 0);
    }

    @Test
    void testGetName() {
        song.setName("test");
        assertEquals("test", song.getName());
    }

    @Test
    void testSetName() {
        song.setName("first");
        assertEquals("first", song.getName());
    }

    @Test
    void testSetNoteString() {
        song.setNoteString("ADD");
        assertEquals("ADD", song.getNoteString());
    }

    @Test
    void testSetKeyString() {
        song.setKeyString("A");
        assertEquals("A", song.getKeyString());
    }

    @Test
    void testToString() {
        assertEquals("first (5.5) - [C, D, E, A] [Key: A]", song2.toString());
    }
}
