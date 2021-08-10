package model;

import org.json.JSONObject;
import persistence.Writable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import java.util.Random;

// Represents a song having a string of inputted notes, a key, and a name
public class Song implements Writable {

    private List<Note> noteList;           // list of notes parsed from the inputted string
    private Key key;                       // key that the song is in
    private String name;                   // name of song
    private String noteString;             // list of notes of song in string form
    private String keyString;              // key of song in string form
    private double rating;                 // rating for a song
    private Random random;                 // random object


    // EFFECTS: noteList is set to a list of notes parsed from the string inputNotes;
    //          key is set to given key;
    //          name is set to "";
    //          random is set to the random object;
    //          rating is set to a random number between 0 and 1, multiplied by 10.
    public Song(String inputNotes, Key key) {
        this.noteList = parseString(inputNotes);
        this.key = key;
        this.random = new Random();
        this.rating = random.nextDouble() * 10;
    }

    // EFFECTS: this.noteString is set to noteString;
    //          this.keyString is set to keyString;
    //          this.name is set to name.
    //          this.rating is set to rating
    public Song(String noteString, String keyString, String name, Double rating) {
        this.noteString = noteString;
        this.keyString = keyString;
        this.name = name;
        this.rating = round(rating);
    }

    // REQUIRES: inputNotes is a string of capital letters ranging from A to G,
    //           as well as potentially a sharp (#) or flat (b) sign
    // EFFECTS: converts the string input of "notes" into an actual list of notes
    private static List<Note> parseString(String inputNotes) {
        int numNotes = 0;
        List<Note> noteList = new ArrayList<>();
        for (int i = 0; i < inputNotes.length(); i++) {         // NOTE: sharps and flats by themselves are not notes
            char c = inputNotes.charAt(i);
            if (c == '#') {                                     // if c is a sharp, increase the previous notes number
                Note current = noteList.get(numNotes - 1);      // by 1
                current.addSharp();
            } else if (c == 'b') {                              // if c is a flat, decrease the previous notes number
                Note current = noteList.get(numNotes - 1);      // by 1
                current.addFlat();
            } else {
                Note next = new Note(c, String.valueOf(c));
                noteList.add(next);
                numNotes++;
            }
        }
        return noteList;
    }

    // MODIFIES: this
    // EFFECTS: converts list of notes into a list of notes in the new key
    public void convertToNewKey(Key newKey) {
        int uniqueNotes = 12;
        Note next;
        List<Note> newNoteList = new ArrayList<>();
        int diff = newKey.getNumber() - this.key.getNumber();
        for (Note note : noteList) {
            if (note.getNumber() + diff > 11) {                                   // if the addition goes over 11,
                next = new Note(note.getNumber() + diff - uniqueNotes);    // subtract 12 from the total.
            } else if (note.getNumber() + diff < 0) {                             // if the addition goes under 0,
                next = new Note(note.getNumber() + diff + uniqueNotes);    // add 12 to the total.
            } else {
                next = new Note(note.getNumber() + diff);
            }
            newNoteList.add(next);
        }
        this.noteList = newNoteList;
        this.key = newKey;
        this.rating = random.nextDouble() * 10;
    }

    // EFFECTS: returns false if o is not of class Song, if the noteList of o is not
    //          equal to this.noteList, or if the key of o is not equal to this.key,
    //          otherwise true
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Song)) {
            return false;
        }
        Song s = (Song) o;
        if (!s.noteList.equals(this.noteList)) {
            return false;
        }
        return s.key.equals(this.key);
    }

    // EFFECTS: a formula that converts the hashcode of an object into an integer,
    //          the Szudzik Pairing is used since there are two variables (noteList and key)
    //
    //          not actually utilized in this project, but is customary to implement when overriding equals
    @Override
    public int hashCode() {
        return noteList.hashCode() < key.hashCode() ? key.hashCode() * key.hashCode() + noteList.hashCode() :
                noteList.hashCode() * noteList.hashCode() + noteList.hashCode() + key.hashCode();   // Szudzik Pairing
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNoteString(String noteList) {
        this.noteString = noteList;
    }

    public void setKeyString(String keyString) {
        this.keyString = keyString;
    }

    public List<Note> getNoteList() {
        return noteList;
    }

    public Key getKey() {
        return key;
    }

    public double getRating() {
        return rating;
    }

    public String getName() {
        return name;
    }

    public String getNoteString() {
        return noteString;
    }

    public String getKeyString() {
        return keyString;
    }

    // EFFECTS: returns string representation of this song
    @Override
    public String toString() {
        return name + " (" + round(rating) + ") - " + noteString + " [Key: " + keyString + "]";
    }

    // EFFECTS: rounds rating to nearest decimal place
    private static double round(double value) {
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(1, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("noteList", noteString);
        json.put("key", keyString);
        json.put("name", name);
        json.put("rating", round(rating));
        return json;
    }
}
