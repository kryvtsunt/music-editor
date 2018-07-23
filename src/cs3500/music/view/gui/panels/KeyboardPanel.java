package cs3500.music.view.gui.panels;

import cs3500.music.model.Pitch;
import cs3500.music.view.AddNote;
import cs3500.music.view.INote;
import cs3500.music.view.IPitch;

import javax.swing.JPanel;
import java.awt.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class KeyboardPanel extends JPanel {
  private final int numOctaves = 8;
  private final int numWhiteKeysPerOctave = 7;
  private final int numBlackKeysPerOctave = 5;
  private final int numKeysPerOctave = numBlackKeysPerOctave + numWhiteKeysPerOctave;
  private final int numWhiteKeys = numWhiteKeysPerOctave * numOctaves + 1; // extra C

  private final int pitchRangeHeight = 1;

  private final int whiteKeyWidth = 3;
  private final int blackKeyWidth = 2;
  private final int whiteKeyHeight = 9;
  private final int blackKeyHeight = 6;

  private final int keyboardRowStart = pitchRangeHeight;

  private final int keyboardWidth = whiteKeyWidth * numWhiteKeys;
  private final int keyboardHeight = whiteKeyHeight + pitchRangeHeight;

  // non - final fields
  private int wPixels; // = this.getWidth();
  private int hPixels; // = this.getHeight();

  private int pixPerGridWidth;
  private int pixPerGridHeight;

  // to center the piano
  private int pixOffsetX;

  private Set<INote> playedNotes;

  private IPitch lowPitchInView;
  private IPitch highPitchInView;

  private int practiceOctave;

  private String stateName;

  public KeyboardPanel() {
    super();

    setFocusable(true);
    requestFocus();

    this.playedNotes = new HashSet<INote>();
    lowPitchInView = new Pitch(0);
    highPitchInView = new Pitch(0);

    this.update();
    this.stateName = "";
  }

  public void setPitchRange(IPitch low, IPitch high) {
    this.lowPitchInView = low;
    this.highPitchInView = high;
  }

  public void setStateName(String name) {
    System.out.println("State Name Changed to" + name);
    this.stateName = name;
  }

  @Override
  public void paintComponent(Graphics g) {
    this.wPixels = this.getWidth();
    this.hPixels = this.getHeight();

    this.pixPerGridHeight = this.hPixels / this.keyboardHeight;
    this.pixPerGridWidth = this.wPixels / this.keyboardWidth;

    this.pixOffsetX = (this.wPixels - (this.pixPerGridWidth * this.keyboardWidth)) / 2;

//    System.out.println(String.format("Keys: w, h, x, y: %d %d %d %d",
//      this.getWidth(),
//      this.getHeight(),
//      this.getX(),
//      this.getY()));

    Graphics2D g2 = (Graphics2D) g;
    super.paintComponent(g2);

    //g2.setFont(new Font("panel", Font.BOLD, 20));

    paintKeyboard(g2);
  }

  // Set which notes are being played by the piano
  public void setPlayedNotes(Set<INote> pNotes) {
    this.playedNotes = pNotes;
  }

  public void setPracticeOctave(int octave) {
    if (octave < numOctaves) {
      this.practiceOctave = octave;
      System.out.println(Integer.toString(this.practiceOctave));
    }
  }

  // update essentially refreshes the variables
  public void update() {
    wPixels = this.getWidth();
    hPixels = this.getHeight();

    pixPerGridWidth = wPixels / this.keyboardWidth;
    pixPerGridHeight = hPixels / this.keyboardHeight;

    this.pixOffsetX = (this.wPixels - (this.pixPerGridWidth * this.keyboardWidth)) / 2;

//    System.out.println(String.format("Width: %d\tHeight: %d", this.wPixels, this.hPixels));
//    System.out.println(String.format("WidthPx: %d\tHeightPx: %d", this.pixPerGridWidth, this.pixPerGridHeight));
  }

  /**
   * Returns the index of a key in an octave, double use for both white and black notes.
   *
   * @return [int]
   */
  private int getNoteIndex(int note) {
    if (note < 12 && note >= 0) {
      if (note == 0 || note == 1) { // C or C#
        return 0;
      } else if (note == 2 || note == 3) { // D or D#
        return 1;
      } else if (note == 4) { // E
        return 2;
      } else if (note == 5 || note == 6) { // F or F#
        return 3;
      } else if (note == 7 || note == 8) { // G or G#
        return 4;
      } else if (note == 9 || note == 10) { // A or A#
        return 5;
      } else if (note == 11) { // B
        return 6;
      }
    }

    throw new IllegalArgumentException("Argument must be between 0 and 12");
  }

  /**
   * Returns the note by the given index.
   *
   * @return [int]
   */
  private int getBlackNote(int note) {
//    System.out.println("Get Black Note " + Integer.toString(note));
    if (note < 2) {
      return 2 * note + 1;
    } else if (note >= 3 && note <= 5) {
      return 2 * note;
    } else {
      throw new IllegalArgumentException("No Black note recognized");
    }
  }

  /**
   * Returns the note by the given index.
   *
   * @return [int]
   */
  private int getWhiteNote(int note) {
//    System.out.println("Get White Note " + Integer.toString(note));
    if (note <= 2) {
      return 2 * note;
    } else if (note > 2 && note <= 7) {
      return 2 * note - 1;
    } else {
      throw new IllegalArgumentException("Arg must be between 0-7");
    }
  }


  /**
   * Paints the keyboard.
   *
   * @param canvass [Graphics2D]
   */
  private void paintKeyboard(Graphics2D canvass) {

    int lowOctave = lowPitchInView.asNumber() / 12;
    int highOctave = highPitchInView.asNumber() / 12;
    int lowIndex = getNoteIndex(lowPitchInView.asNumber() % 12) + lowOctave * numWhiteKeysPerOctave;
    int highIndex = getNoteIndex(highPitchInView.asNumber() % 12) + highOctave * numWhiteKeysPerOctave + 1;
    // Paint note range
    canvass.setColor(Color.yellow);
    int rectX = (lowIndex * whiteKeyWidth) * pixPerGridWidth + pixOffsetX;
    int rectY = 0;
    Rectangle range = new Rectangle(rectX, rectY,
      (highIndex - lowIndex) * whiteKeyWidth * pixPerGridWidth,
      pitchRangeHeight * pixPerGridHeight);
    canvass.fill(range);

    if (this.stateName.equals("Practice Mode")) {
      canvass.setColor(Color.orange);
      rectX = (practiceOctave * numWhiteKeysPerOctave * whiteKeyWidth) * pixPerGridWidth + pixOffsetX;
      rectY = 0;
      Rectangle pRange = new Rectangle(rectX, rectY,
        (numWhiteKeysPerOctave) * whiteKeyWidth * pixPerGridWidth,
        pitchRangeHeight * pixPerGridHeight);
      canvass.fill(pRange);
    }

    paintWhiteKeys(canvass);
    paintPlayedWhiteKeys(canvass);

    paintBlackKeys(canvass);
    paintPlayedBlackKeys(canvass);
  }

  private void paintWhiteKeys(Graphics2D canvass) {
    // Fill in white keys
    for (int i = 0; i < numWhiteKeys; i++) {
      int rectX = (i * whiteKeyWidth) * pixPerGridWidth + pixOffsetX;
      int rectY = keyboardRowStart * pixPerGridHeight;
      Rectangle key = new Rectangle(rectX, rectY,
        whiteKeyWidth * pixPerGridWidth,
        whiteKeyHeight * pixPerGridHeight);
      canvass.setColor(Color.WHITE);
      canvass.fill(key);
      canvass.setColor(Color.BLACK);
      canvass.draw(key);
    }
  }

  private void paintPlayedWhiteKeys(Graphics2D canvass) {
    // Fill in played white Keys
    canvass.setColor(Color.RED);

    for (INote n: playedNotes) {
      int pitch = n.getPitch().asNumber();
      //System.out.println(n.getPitch().toString());
      int octave = pitch / (numKeysPerOctave);
      int note = pitch % numKeysPerOctave;

      // If the pitch matches a white key
      if (note == 0 || note == 2 || note == 4 || note == 5
        || note == 7 || note == 9 || note == 11) {

        int i = (octave * numWhiteKeysPerOctave) + getNoteIndex(note);
        int rectX = (i * whiteKeyWidth) * pixPerGridWidth + pixOffsetX;
        int rectY = keyboardRowStart * pixPerGridHeight;
        Rectangle key = new Rectangle(
          rectX, rectY,
          whiteKeyWidth * pixPerGridWidth,
          whiteKeyHeight * pixPerGridHeight);
        canvass.fill(key);
      }
    }
  }

  private void paintBlackKeys(Graphics2D canvass) {
    // Black Keys
    canvass.setColor(Color.BLACK);
    for (int i = 0; i < numWhiteKeys - 1; i++) {
      if ((i % 7) != 2 && (i % 7) != 6) { // Skip E and B
        int rectX = i * pixPerGridWidth * whiteKeyWidth
          + (2 * (whiteKeyWidth * pixPerGridWidth) / 3) + pixOffsetX;
        int rectY = keyboardRowStart * pixPerGridHeight;
        Rectangle key = new Rectangle(rectX, rectY,
          blackKeyWidth * pixPerGridWidth,
          blackKeyHeight * pixPerGridHeight);
        canvass.fill(key);
      }
    }
  }

  private void paintPlayedBlackKeys(Graphics2D canvass) {

    // Played Black Keys
    canvass.setColor(Color.RED);
    for (INote n : playedNotes) {
      int pitch = n.getPitch().asNumber();
      int octave = pitch / (numKeysPerOctave);
      int note = pitch % numKeysPerOctave;

      if (note == 1 || note == 3 || note == 6 || note == 8 || note == 10) {
        int i = (octave * numWhiteKeysPerOctave) + getNoteIndex(note);
        int rectX = i * pixPerGridWidth * whiteKeyWidth
          + (2 * (whiteKeyWidth * pixPerGridWidth) / 3) + pixOffsetX;
        int rectY = keyboardRowStart * pixPerGridHeight;
        Rectangle key = new Rectangle(rectX, rectY,
          blackKeyWidth * pixPerGridWidth,
          blackKeyHeight * pixPerGridHeight);
        canvass.fill(key);
      }
    }
  }

  /**
   * Gets an AddNote.
   *
   * @param x [int]
   * @param y [int]
   * @return [AddNote]
   */
  public IPitch getPitchAt(int x, int y) {

    if (this.keyAtPosn(x, y)) {

      // Black Keys
      for (int i = 0; i < numWhiteKeys - 1; i++) { // no black keys right after the last note
        if ((i % 7) != 2 && (i % 7) != 6) { // Skip E and B
          int rectX = i * pixPerGridWidth * whiteKeyWidth
            + (2 * (whiteKeyWidth * pixPerGridWidth) / 3) + pixOffsetX;

          int lenX = blackKeyWidth * pixPerGridWidth;

          int rectY = keyboardRowStart * pixPerGridHeight;
          int lenY = blackKeyHeight * pixPerGridHeight;

          // If withing the black key bounds
          if (x > rectX && x <= rectX + lenX
            && y > rectY && y <= rectY + lenY) {
            return new Pitch((i / 7) * 12 + getBlackNote(i % 7));
            //System.out.println(String.format("%d, note: %s", x, value.pitch.toString()));
          }
        }
      }

      // If not black, get white key location
      for (int i = 0; i < numWhiteKeys; i++) {
        int rectX = i * pixPerGridWidth * whiteKeyWidth + pixOffsetX;
        int lenX = whiteKeyWidth * pixPerGridWidth;
        int rectY = keyboardRowStart * pixPerGridHeight;
        int lenY = whiteKeyHeight * pixPerGridHeight;

        if (x > rectX && x <= rectX + lenX
          && y > rectY && y <= rectY + lenY) {
          return new Pitch((i / 7) * 12 + getWhiteNote(i % 7));
        }
      }

    }
    throw new IllegalStateException("Blargh");
  }

  /**
   * Determines the key at the given position.
   *
   * @param x [int]
   * @param y [int]
   * @return [boolean]
   */
  public boolean keyAtPosn(int x, int y) {
    // might need global coordinates for x and y :(

    // Bouinds
    int minx = pixOffsetX;
    int maxx = minx + numWhiteKeys * whiteKeyWidth * pixPerGridWidth;
    int miny = (keyboardRowStart) * pixPerGridHeight;
    int maxy = miny + whiteKeyHeight * pixPerGridHeight;

    //System.out.println(String.format("Bounds: x: %d - %d y: %d - %d", minx, maxx, miny, maxy));
    boolean returnMe = (x >= minx && x <= maxx && y >= miny && y <= maxy);

    System.out.println(String.format("KeyboardPanel::keyAtPosn: %d, %d", x, y));
    System.out.println(String.format("maxx: %d\t maxy%d\tresult: %s", maxx, maxy, Boolean.toString(returnMe)));
    System.out.println(String.format("Panel dims: %d, %d", this.getWidth(), this.getHeight()));

    return returnMe;
  }

}
