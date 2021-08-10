package ui;

import exceptions.InvalidKeyException;
import model.Key;
import model.Note;
import model.Song;
import model.SongList;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

// Music Note Converter application
public class ConverterApp {

    private static final String JSON_STORE = "./data/songList.json";
    private Scanner input;                     // user input
    private Key key;                           // original key
    private Key newKey;                        // desired key
    private Song song;                         // converted song
    private SongList songList;                 // list of songs
    private JsonWriter jsonWriter;             // jsonWriter
    private JsonReader jsonReader;             // jsonReader
    boolean keepGoing = true;                  // true if the program should convert another song, false otherwise
    boolean valid = false;                     // true if what the user inputted is valid, false otherwise

    // EFFECTS: runs the converter application
    public ConverterApp() {
        songList = new SongList("Next Biggest Hits");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runConverter();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runConverter() {
        this.input = new Scanner(System.in);
        String command;
        System.out.println("Welcome to mKey!");
        System.out.println();
        while (keepGoing) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }

        }
        System.out.println("Have a nice day!");
    }

    // EFFECTS: displays menu of options to user
    private void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\tc -> convert a song");
        System.out.println("\tp -> print song list");
        System.out.println("\ts -> save song list to file");
        System.out.println("\tl -> load song list from file");
        System.out.println("\tq -> quit");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void processCommand(String command) {
        switch (command) {
            case "c":
                inputKey();
                resetValid();

                inputNewKey();
                resetValid();

                inputSong();
                resetValid();

                inputName();
                break;
            case "p":
                printSongList();
                break;
            case "s":
                saveSongList();
                break;
            case "l":
                loadSongList();
                break;
            default:
                System.out.println("Invalid Selection! Please try again.");
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: stores user inputted key, loops if input is invalid
    private void inputKey() {
        String keyName;
        while (!valid) {
            this.valid = true;
            System.out.println("What key are you playing in? (ex. C, F#, Bb)");
            keyName = input.next();
            try {
                this.key = new Key(keyName);
            } catch (InvalidKeyException exception) {
                invalidInput();
                this.valid = false;
            }

        }
    }

    // MODIFIES: this
    // EFFECTS: stores user inputted key, loops if input is invalid
    private void inputNewKey() {
        String newKeyName;
        while (!valid) {
            this.valid = true;
            System.out.println("What key are you converting to?");
            newKeyName = input.next();
            try {
                this.newKey = new Key(newKeyName);
            } catch (InvalidKeyException exception) {
                invalidInput();
                this.valid = false;
            }

        }
    }

    // MODIFIES: this
    // EFFECTS: stores user inputted song, loops if input is invalid
    private void inputSong() {
        String noteList;
        while (!valid) {
            System.out.println("Please input your song (ex. FG#BbD)");
            noteList = input.next();
            this.valid = validSong(noteList);
            if (!valid) {
                invalidInput();
            } else {
                song = new Song(noteList, key);
                song.convertToNewKey(newKey);
                printSong();
            }
        }
    }

    // EFFECTS: print the song in the new key, as well as the randomized rating
    private void printSong() {
        System.out.print("\n");
        for (Note note : song.getNoteList()) {
            System.out.print(note + " ");
        }
        System.out.println("\n");
        System.out.format("Rating: %.1f / 10", song.getRating());
        System.out.println("\n");
    }

    // MODIFIES: this
    // EFFECTS: prompts user to name their song
    private void inputName() {
        String userInput;
        System.out.println("Please name your song:");
        userInput = input.next();
        song.setName(userInput);
        song.setNoteString(song.getNoteList().toString());
        song.setKeyString(song.getKey().getName());
        songList.addSong(song);
        System.out.println();
        System.out.println("Added to your collection!");
        System.out.println();
    }

    // EFFECTS: outputs true if the inputted string of "notes" is valid, false otherwise
    private boolean validSong(String noteList) {
        for (int i = 0; i < noteList.length(); i++) {
            char c = noteList.charAt(i);
            if (c == '#' || c == 'b') {
                if (i == 0) {                               // false if the first note is a sharp or flat
                    return false;
                }
                char current = noteList.charAt(i - 1);
                if (invalidNatural(current)) {              // false if there are two sharps or flats in a row
                    return false;
                }
            } else {
                if (invalidNatural(noteList.charAt(i))) {   // false if the current note is anything but a
                    return false;                           // capital letter from A to G
                }
            }
        }
        return true;
    }

    // EFFECTS: outputs true if the inputted char is not a natural note, false otherwise
    private static boolean invalidNatural(char c) {
        return c != 'C' && c != 'D' && c != 'E' && c != 'F' && c != 'G' && c != 'A' && c != 'B';
    }

    // EFFECTS: print all songs in song list to console
    private void printSongList() {
        List<Song> songs = songList.getSongList();
        for (Song s : songs) {
            System.out.println(s);
        }
    }

    // EFFECTS: saves the songList to file
    private void saveSongList() {
        try {
            jsonWriter.open();
            jsonWriter.write(songList);
            jsonWriter.close();
            System.out.println("Saved " + songList.getName() + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: load songList from file
    private void loadSongList() {
        try {
            songList = jsonReader.read();
            System.out.println("Loaded " + songList.getName() + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // EFFECTS: resets valid to false
    private void resetValid() {
        this.valid = false;
    }

    // EFFECTS: prints invalid input message
    private void invalidInput() {
        System.out.println("Invalid input! Please try again.");
    }
}
