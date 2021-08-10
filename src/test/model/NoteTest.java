package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NoteTest {

    private Note note;
    private Note noteInt;

    @BeforeEach
    void runBefore() {
        note = new Note('A', "A");
        noteInt = new Note(3);
    }

    @Test
    void testConstructor() {
        assertEquals(9, note.getNumber());
        assertEquals("A", note.getName());
    }

    @Test
    void testConstructorInvalid() {
        Note invalidNote = new Note('J', "J");
        assertEquals(-1, invalidNote.getNumber());
    }

    @Test
    void testConstructorInt() {
        assertEquals(3, noteInt.getNumber());
        assertEquals("Eb", noteInt.getName());
    }

    @Test
    void testToString() {
        assertTrue(note.toString().contains("A"));
        assertTrue(noteInt.toString().contains("Eb"));
    }

    @Test
    void testAddSharp() {
        note.addSharp();
        assertEquals(10, note.getNumber());
        assertEquals("A#", note.getName());
    }

    @Test
    void testAddFlat() {
        note.addFlat();
        assertEquals(8, note.getNumber());
        assertEquals("Ab", note.getName());
    }

    @Test
    void testEquals() {
        Object o = new Object();
        assertFalse(note.equals(o));
    }

    @Test
    void testHashCode() {
        assertEquals(9, note.hashCode());
    }
}
