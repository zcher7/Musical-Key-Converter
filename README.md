# mKey - Musical Key Converter

#### *Created By Zach Chernenko*

# What is the Purpose of mKey?
The purpose of **mKey** is to convert music notes of one musical key into another. For example, if I play a note D in
the key of C, and I wanted to know what note that is in the key of F, then **mKey** would tell me that it would be a
G. This application will be primarily used by musicians who play multiple instruments or composers that are writing 
multiple parts of a score. This project is of particular interest to me because I am a musician who plays multiple
instruments, and I always find myself wanting to play songs written for one instrument on different instruments.

# How Does One Use mKey?
When inputting notes or keys into **mKey**, please only use one of the following capital letters: 
[A, B, C, D, E, F, G] or a sharp sign (#) or a flat sign (b).

Here is a quick guide on how to use **mKey**:
1. Input the key you are currently playing in
2. Input the desired key you wish to play in
3. Input a note or multiple notes (a song) to be converted
4. Review the rating that your song received
5. Decide whether you want to convert another song

Your song will be randomly rated between 0 and 10, good luck! 

# User Stories

### Phase 1 - Basic Model & UI

As a user, I want to be able to:
- Select what key I am playing in
- Select what key I want to convert to
- Convert multiple notes at the same time
- Convert another set of notes after the first
- Receive feedback on the song that I input

### Phase 2 - Data Persistence

As a user, I want to be able to:
- Name my song
- Add my song to my song list
- Save my song list to file
- Load my song list from file
- View my song list

### Phase 3 - Graphical User Interface

As a user, I want to be able to:
- Have the capabilities of all previous User Stories within the GUI
- Delete a song from my song list
- Hear a sound after each button press

# Phase 4 - Design

### Task 2

Refactored Key.java to throw an InvalidKeyException when the user inputted key is not valid. The following classes and
methods changed as a result:

#### Key
- Key()
- keyToNumNatural()
- keyToNumSharpFlat()

#### ConverterApp
- inputKey()
- inputNewKey()

#### Converter
- KeyListener
    - actionPerformed()
- NewKeyListener
    - actionPerformed()

### Task 3

If I had more time to work on this project, I would do the following refactoring:
- Introduce abstraction in methods that perform very similar tasks (validSong)
- Utilize the map interface for storing information related to keys instead of numerous switch cases
- Split Song into multiple classes that handle different components to improve cohesion
