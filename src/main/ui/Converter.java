package ui;

import exceptions.InvalidKeyException;
import model.Key;
import model.Song;
import model.SongList;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

// Music Note Converter
public class Converter extends JFrame {

    public static final int WIDTH = 550;
    public static final int HEIGHT = 300;

    private static final String convertString = "Convert!";
    private static final String saveString = "Save";
    private static final String loadString = "Load";
    private static final String deleteString = "Delete";

    private static final String JSON_STORE = "./data/songList.json";

    private final JsonWriter jsonWriter;
    private final JsonReader jsonReader;

    private JList<?> list;
    private DefaultListModel<String> listModel;

    private JButton convertButton;
    private JButton deleteButton;

    private ConvertListener convertListener;

    private JComboBox<?> keyList;
    private JComboBox<?> newKeyList;

    private Key key;
    private Key newKey;

    private JTextField inputNotesText;
    private JTextField songName;

    private SongList songList;

    // EFFECTS: runs the converter
    public Converter() {
        super("mKey");
        this.songList = new SongList("Next Biggest Hits");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        initializeGraphics();
    }

    // MODIFIES: this
    // EFFECTS: draws the JFrame window where this Converter will operate,
    //          and creates the areas where menus and buttons operate
    private void initializeGraphics() {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(WIDTH, HEIGHT));

        createSongList();
        createConvertBar();
        createKeyBar();
        createSidebar();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: constructs the list display that displays songs in the user's song list
    private void createSongList() {
        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.setVisibleRowCount(10);
        list.setFont(new Font("DIALOG", Font.PLAIN, 14));
        JScrollPane listScrollPane = new JScrollPane(list);

        add(listScrollPane, BorderLayout.CENTER);
    }

    // MODIFIES: this
    // EFFECTS: constructs the text fields for input notes and song name,
    //          as well as the convert button
    private void createConvertBar() {
        convertButton = new JButton(convertString);
        convertListener = new ConvertListener(convertButton);
        convertButton.setActionCommand(convertString);
        convertButton.addActionListener(convertListener);
        convertButton.setEnabled(false);

        inputNotesText = new JTextField(10);
        inputNotesText.addActionListener(convertListener);
        inputNotesText.getDocument().addDocumentListener(convertListener);

        songName = new JTextField(10);
        songName.addActionListener(convertListener);
        songName.getDocument().addDocumentListener(convertListener);

        setupConvertBarArea();
    }

    // MODIFIES: this
    // EFFECTS: a helper method that adds the text fields and convert button to the display
    private void setupConvertBarArea() {
        JPanel convertBarArea = new JPanel();
        convertBarArea.setLayout(new BoxLayout(convertBarArea, BoxLayout.X_AXIS));

        JLabel songLabel = new JLabel("Song:");
        JLabel nameLabel = new JLabel("Name:");

        Dimension dimension = new Dimension(10, 0);
        convertBarArea.add(songLabel);
        convertBarArea.add(Box.createRigidArea(dimension));
        convertBarArea.add(inputNotesText);
        convertBarArea.add(Box.createRigidArea(dimension));
        convertBarArea.add(nameLabel);
        convertBarArea.add(Box.createRigidArea(dimension));
        convertBarArea.add(songName);
        convertBarArea.add(Box.createRigidArea(dimension));
        convertBarArea.add(convertButton);
        convertBarArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(convertBarArea, BorderLayout.SOUTH);
    }

    // MODIFIES: this
    // EFFECTS: constructs two combo boxes that represent the keys the user is working with,
    //          and adds them to the display
    private void createKeyBar() {
        JPanel keyBarArea = new JPanel();
        keyBarArea.setLayout(new BoxLayout(keyBarArea, BoxLayout.Y_AXIS));

        String[] keyStrings = { "-", "C", "C#", "D", "Eb", "E", "F", "F#", "G", "Ab", "A", "Bb", "B" };

        JLabel keyLabel = new JLabel("Initial Key");
        JLabel newKeyLabel = new JLabel("New Key");

        keyList = new JComboBox<>(keyStrings);
        newKeyList = new JComboBox<>(keyStrings);

        KeyListener keyListener = new KeyListener();
        keyList.setSelectedIndex(0);
        keyList.addActionListener(keyListener);
        keyList.addItemListener(convertListener);
        NewKeyListener newKeyListener = new NewKeyListener();
        newKeyList.setSelectedIndex(0);
        newKeyList.addActionListener(newKeyListener);
        newKeyList.addItemListener(convertListener);


        keyBarArea.add(keyLabel);
        keyBarArea.add(keyList);
        keyBarArea.add(newKeyLabel);
        keyBarArea.add(newKeyList);
        keyBarArea.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(keyBarArea, BorderLayout.EAST);
    }

    // MODIFIES: this
    // EFFECTS: constructs a sidebar with a save, load, and delete button,
    //          and adds them to the display
    private void createSidebar() {
        JPanel sidebarArea = new JPanel();
        sidebarArea.setLayout(new GridLayout(0,1));

        JButton saveButton = new JButton(saveString);
        saveButton.setActionCommand(saveString);
        saveButton.addActionListener(new SaveListener());

        JButton loadButton = new JButton(loadString);
        loadButton.setActionCommand(loadString);
        loadButton.addActionListener(new LoadListener());

        deleteButton = new JButton(deleteString);
        deleteButton.setActionCommand(deleteString);
        deleteButton.addActionListener(new DeleteListener());
        deleteButton.setEnabled(false);

        sidebarArea.add(saveButton);
        sidebarArea.add(loadButton);
        sidebarArea.add(deleteButton);
        sidebarArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(sidebarArea, BorderLayout.WEST);
    }

    // Listener shared by the two text fields, the convert button, and the two key combo boxes
    private class ConvertListener implements ActionListener, DocumentListener, ItemListener {

        private boolean alreadyEnabled = false;
        private final JButton button;

        // EFFECTS: button is set to the given button
        public ConvertListener(JButton button) {
            this.button = button;
        }


        // MODIFIES: this
        // EFFECTS: if the inputted notes are valid, converts them into a song in the new key,
        //          then adds it to the song list and the display list,
        //          then plays a sound.
        //          if the inputted notes are invalid, plays a beep and selects the notes.
        @Override
        public void actionPerformed(ActionEvent e) {
            String inputNotes = inputNotesText.getText();

            if (!validSong(inputNotes)) {
                Toolkit.getDefaultToolkit().beep();
                inputNotesText.requestFocusInWindow();
                inputNotesText.selectAll();
            } else {
                validSong(inputNotes);
                Song song = new Song(inputNotes, key);
                song.convertToNewKey(newKey);
                song.setName(songName.getText());
                song.setNoteString(song.getNoteList().toString());
                song.setKeyString(song.getKey().getName());
                songList.addSong(song);
                listModel.addElement(song.toString());
                deleteButton.setEnabled(true);
                playSound();
            }
        }

        // EFFECTS: outputs true if the inputted string of notes is valid, false otherwise
        private boolean validSong(String noteList) {
            for (int i = 0; i < noteList.length(); i++) {
                char c = noteList.charAt(i);
                if (c == '#' || c == 'b') {
                    if (i == 0) {
                        return false;
                    }
                    char current = noteList.charAt(i - 1);
                    if (invalidNatural(current)) {
                        return false;
                    }
                } else {
                    if (invalidNatural(noteList.charAt(i))) {
                        return false;
                    }
                }
            }
            return true;
        }

        // EFFECTS: outputs true if the inputted char is not a natural note, false otherwise
        private boolean invalidNatural(char c) {
            return c != 'C' && c != 'D' && c != 'E' && c != 'F' && c != 'G' && c != 'A' && c != 'B';
        }

        // MODIFIES: this
        // EFFECTS: if both text fields are not empty and both combo boxes have selected keys,
        //          then enable the button
        @Override
        public void insertUpdate(DocumentEvent e) {
            if (allComponentsReady()) {
                enableButton();
            }
        }

        // MODIFIES: this
        // EFFECTS: disables the button if all text in the text field gets deleted
        @Override
        public void removeUpdate(DocumentEvent e) {
            handleEmptyTextField(e);
        }

        // MODIFIES: this
        // EFFECTS: if the text field is not empty, then enable the button
        @Override
        public void changedUpdate(DocumentEvent e) {
            if (!handleEmptyTextField(e)) {
                enableButton();
            }
        }

        // MODIFIES: this
        // EFFECTS: enables the button if both text fields are not empty and both combo boxes have valid keys selected
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (allComponentsReady()) {
                enableButton();
            } else {
                disableButton();
            }
        }

        // EFFECTS: returns true if both text fields are not empty and if both combo boxes have valid keys selected
        private boolean allComponentsReady() {
            return !inputNotesText.getText().equals("")
                    && !songName.getText().equals("")
                    && !String.valueOf(keyList.getSelectedItem()).equals("-")
                    && !String.valueOf(newKeyList.getSelectedItem()).equals("-");
        }

        // MODIFIES: this
        // EFFECTS: enables the button if it is not already enabled
        private void enableButton() {
            if (!alreadyEnabled) {
                button.setEnabled(true);
            }
        }

        // MODIFIES: this
        // EFFECTS: disables the button
        private void disableButton() {
            button.setEnabled(false);
        }

        // MODIFIES: this
        // EFFECTS: disables the convert button if one of the text fields is empty and returns true,
        //          false otherwise
        private boolean handleEmptyTextField(DocumentEvent e) {
            if (e.getDocument().getLength() <= 0) {
                button.setEnabled(false);
                alreadyEnabled = false;
                return true;
            }
            return false;
        }

        // EFFECTS: plays a sound after the button is pressed
        private void playSound() {
            try {
                AudioInputStream stream = AudioSystem.getAudioInputStream(new File("sounds/snd_item.wav"));
                AudioFormat format = stream.getFormat();
                DataLine.Info info = new DataLine.Info(Clip.class, format);
                Clip clip = (Clip) AudioSystem.getLine(info);
                clip.open(stream);
                clip.start();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    // Listener for the initial key combo box
    private class KeyListener implements ActionListener {

        // MODIFIES: this
        // EFFECTS: if a valid key is selected in the initial key combo box,
        //          set key to the selected key
        @Override
        public void actionPerformed(ActionEvent e) {
            String keyString = String.valueOf(keyList.getSelectedItem());
            try {
                key = new Key(keyString);
            } catch (InvalidKeyException exception) {
                // do nothing
            }
        }
    }

    // Listener for the new key combo box
    private class NewKeyListener implements ActionListener {

        // MODIFIES: this
        // EFFECTS: if a valid key is selected in the new key combo box,
        //          set newKey to the selected key
        @Override
        public void actionPerformed(ActionEvent e) {
            String newKeyString = String.valueOf(newKeyList.getSelectedItem());
            try {
                newKey = new Key(newKeyString);
            } catch (InvalidKeyException exception) {
                // do nothing
            }
        }
    }

    // Listener for the delete button
    private class DeleteListener implements ActionListener {

        // MODIFIES: this
        // EFFECTS: if an index of the string is selected, remove it from the list and songList,
        //          disables the delete button if it deletes the last item in the list,
        //          and selects the next index if applicable.
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = list.getSelectedIndex();
            if (index != -1) {
                listModel.remove(index);
                songList.removeSong(index);
                int size = listModel.getSize();

                if (size == 0) {
                    deleteButton.setEnabled(false);
                } else {
                    if (index == listModel.getSize()) {
                        index--;
                    }
                    list.setSelectedIndex(index);
                    list.ensureIndexIsVisible(index);

                }
                playSound();
            }
        }

        // EFFECTS: plays a sound after the button is pressed, only if something is deleted
        private void playSound() {
            try {
                AudioInputStream stream = AudioSystem.getAudioInputStream(new File("sounds/snd_damage.wav"));
                AudioFormat format = stream.getFormat();
                DataLine.Info info = new DataLine.Info(Clip.class, format);
                Clip clip = (Clip) AudioSystem.getLine(info);
                clip.open(stream);
                clip.start();
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        }
    }

    // Listener for the save button
    private class SaveListener implements ActionListener {

        // MODIFIES: this
        // EFFECTS: saves the songList to file
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                jsonWriter.open();
                jsonWriter.write(songList);
                jsonWriter.close();
                playSound();
            } catch (FileNotFoundException exception) {
                Toolkit.getDefaultToolkit().beep();
            }
        }

        // EFFECTS: plays a sound after the button is pressed
        private void playSound() {
            try {
                AudioInputStream stream = AudioSystem.getAudioInputStream(new File("sounds/snd_save.wav"));
                AudioFormat format = stream.getFormat();
                DataLine.Info info = new DataLine.Info(Clip.class, format);
                Clip clip = (Clip) AudioSystem.getLine(info);
                clip.open(stream);
                clip.start();
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        }
    }

    // Listener for the load button
    private class LoadListener implements ActionListener {

        // MODIFIES: this
        // EFFECTS: loads the songList on file
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                songList = jsonReader.read();
                printSongList();
                enableDelete();
                playSound();
            } catch (IOException exception) {
                Toolkit.getDefaultToolkit().beep();
            }
        }

        // MODIFIES: this
        // EFFECTS: replaces the current list with the song list on file
        private void printSongList() {
            List<Song> songs = songList.getSongList();
            listModel.clear();
            int i = 0;
            for (Song song : songs) {
                listModel.add(i, song.toString());
                i++;
            }
        }

        // MODIFIES: this
        // EFFECTS: enables the delete button if the list of songs is not empty
        private void enableDelete() {
            if (songList.getSongList().size() > 0) {
                deleteButton.setEnabled(true);
            }
        }

        // EFFECTS: plays a sound after the button is pressed
        private void playSound() {
            try {
                AudioInputStream stream = AudioSystem.getAudioInputStream(new File("sounds/snd_bell.wav"));
                AudioFormat format = stream.getFormat();
                DataLine.Info info = new DataLine.Info(Clip.class, format);
                Clip clip = (Clip) AudioSystem.getLine(info);
                clip.open(stream);
                clip.start();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}
