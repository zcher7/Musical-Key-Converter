package model;

// Represents a music note having a number [0,11] or -1, and a name
public class Note {

    private int number;   // arbitrary integer assigned to each note
    private String name;  // name of the note

    // EFFECTS: number of the note is set to an arbitrary assigned integer,
    //          name is set to the name of the note
    public Note(char inputChar, String noteName) {
        this.number = noteToNum(inputChar);
        this.name = noteName;
    }

    // EFFECTS: number of the note is set to the given number,
    //          name is set to the number to name equivalent
    public Note(int number) {
        this.number = number;
        this.name = numToNote(number);
    }

    // EFFECTS: overrides the toString method so that when we print the note later,
    //          it will print the name instead
    @Override
    public String toString() {
        return name;
    }

    // REQUIRES: initialNote is a capital letter from A to G
    // EFFECTS: converts the note to its corresponding number,
    //          starting from 0, up the octave from C to B
    private int noteToNum(char initialNote) {
        switch (initialNote) {
            case 'C':
                return 0;
            case 'D':
                return 2;
            case 'E':
                return 4;
            case 'F':
                return 5;
            case 'G':
                return 7;
            case 'A':
                return 9;
            case 'B':
                return 11;
            default:
                return -1;
        }
    }

    // REQUIRES: num is an integer [0, 11]
    // EFFECTS: converts a number to its corresponding note
    private String numToNote(int num) {
        String[] note = new String[] {
                "C", "C#", "D", "Eb", "E", "F", "F#", "G", "Ab", "A", "Bb", "B"
        };
        return note[num];
    }

    // MODIFIES: this
    // EFFECTS: adds 1 to a notes number and appends a "#" sign to the note name
    public void addSharp() {
        this.number++;
        this.name += "#";
    }

    // MODIFIES: this
    // EFFECTS: subtracts 1 to a notes number and appends a "b" sign to the note name
    public void addFlat() {
        this.number--;
        this.name += "b";
    }

    // EFFECTS: checks equality between two notes
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Note)) {
            return false;
        }
        Note n = (Note) o;
        return n.number == this.number;
    }

    // EFFECTS: returns the number of the key instead of its hashcode;
    //
    //          not actually utilized in this project, but is customary to implement when overriding equals
    @Override
    public int hashCode() {
        return number;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

}
