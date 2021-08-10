package model;

import exceptions.InvalidKeyException;

// Represents a musical key which has an arbitrary assigned number
public class Key {

    private final int number; // arbitrary number assigned to the key
    private String name;      // name of key

    // EFFECTS: number of the key is set to an integer [0,11] or -1 depending on what key it is;
    //          name is set to keyName;
    //          throws InvalidKeyException if keyName is not valid.
    public Key(String keyName) throws InvalidKeyException {
        this.number = keyToNumNatural(keyName);
        this.name = keyName;
    }

    // EFFECTS: converts the key to its equivalent integer value, throws InvalidKeyException if keyName is not valid
    private static int keyToNumNatural(String keyName) throws InvalidKeyException {
        switch (keyName) {
            case "C":
                return 0;
            case "D":
                return 2;
            case "E":
                return 4;
            case "F":
                return 5;
            case "G":
                return 7;
            case "A":
                return 9;
            case "B":
                return 11;
            default:
                return keyToNumSharpFlat(keyName);
        }
    }

    // EFFECTS: converts the key to its equivalent integer value, throws new InvalidKeyException if keyName is not valid
    private static int keyToNumSharpFlat(String keyName) throws InvalidKeyException {
        switch (keyName) {
            case "C#":
            case "Db":
                return 1;
            case "D#":
            case "Eb":
                return 3;
            case "F#":
            case "Gb":
                return 6;
            case "G#":
            case "Ab":
                return 8;
            case "A#":
            case "Bb":
                return 10;
            default:
                throw new InvalidKeyException();
        }
    }

    // EFFECTS: returns false if o is not of class Key,
    //          or if the number of o is not equal to this.number,
    //          otherwise true
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Key)) {
            return false;
        }
        Key k = (Key) o;
        return k.number == this.number;
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
