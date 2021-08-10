package model;

import exceptions.InvalidKeyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class KeyTest {

    private Key keyAFlat;

    @BeforeEach
    void runBefore() {
        try {
            keyAFlat = new Key("Ab");
        } catch (InvalidKeyException exception) {
            fail("InvalidKeyException should not have been thrown.");
        }
    }

    @Test
    void testConstructorValidKey() {
        try {
            Key keyC = new Key("C");
            Key keyCSharp = new Key("C#");
            Key keyD = new Key("D");
            Key keyEFlat = new Key("Eb");
            Key keyE = new Key("E");
            Key keyF = new Key("F");
            Key keyFSharp = new Key("F#");
            Key keyG = new Key("G");
            Key keyA = new Key("A");
            Key keyBFlat = new Key("Bb");
            Key keyB = new Key("B");



            assertEquals(0, keyC.getNumber());
            assertEquals(1, keyCSharp.getNumber());
            assertEquals(2, keyD.getNumber());
            assertEquals(3, keyEFlat.getNumber());
            assertEquals(4, keyE.getNumber());
            assertEquals(5, keyF.getNumber());
            assertEquals(6, keyFSharp.getNumber());
            assertEquals(7, keyG.getNumber());
            assertEquals(8, keyAFlat.getNumber());
            assertEquals(9, keyA.getNumber());
            assertEquals(10, keyBFlat.getNumber());
            assertEquals(11, keyB.getNumber());
        } catch (InvalidKeyException exception) {
            fail("InvalidKeyException should not have been thrown.");
        }

    }

    @Test
    void testConstructorInvalidKey() {
        try {
            Key invalidKey = new Key("Z");
            fail("InvalidKeyException should have been thrown.");
        } catch (InvalidKeyException exception) {
            // expected
        }
    }

    @Test
    void testEquals() {
        Object o = new Object();
        assertFalse(keyAFlat.equals(o));
    }

    @Test
    void testEqualsKeysEqual() {
        try {
            Key keyAb = new Key("Ab");
            assertEquals(keyAb, keyAFlat);
        } catch (InvalidKeyException exception) {
            fail("InvalidKeyException should not have been thrown.");
        }
    }

    @Test
    void testEqualsKeysNotEqual() {
        try {
            Key keyC = new Key("C");
            assertNotEquals(keyC, keyAFlat);
        } catch (InvalidKeyException exception) {
            fail("InvalidKeyException should not have been thrown.");
        }


    }

    @Test
    void testHashCode() {
        assertEquals(8, keyAFlat.hashCode());
    }

    @Test
    void testGetName() {
        assertEquals("Ab", keyAFlat.getName());
    }

}
