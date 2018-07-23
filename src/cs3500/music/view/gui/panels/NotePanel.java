package cs3500.music.view.gui.panels;

//import cs3500.music.model.Note;
import cs3500.music.model.Pitch;
import cs3500.music.view.INote;
import cs3500.music.view.IPitch;
import cs3500.music.view.IViewModel;

import javax.swing.JPanel;
import java.awt.Font;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.geom.Line2D;
import java.util.*;

import static java.lang.Integer.max;


/**
 * NotePanel implements a KeyListener. It represents a GUI panel of the MusicEditor.
 */
public class NotePanel extends JPanel {//implements KeyListener {

  private IViewModel m;
  private List<Set<INote>> notesAtBeat;
  private int cursorBeat;

  // Needed for scrolling
  private int beatInFrame; // The first beat to be shown (Leftmost)
  private int noteInFrame;

  private int numBeats;
  private int numPitches;
  private int lowestPitch;

  private int numBeatCols;
  private int numPitchRows;

  private int pixPerGridHeight = 15;
  private int pixPerGridWidth; // Lets see what happens when we fix this value

  // Image Dimensions
  // Global
  private int wPixels; // = this.getWidth();
  private int hPixels; // = this.getHeight();

  // Les assume a grid of 100 columns and variable rows
  private int gridHeight;
  private final int gridWidth = 100;

  private String stateName;

  /**
   * Constructor for the NotePanel.
   *
   * @param m IViewModel
   * @param notes The individual notes at each location
   */
  public NotePanel(IViewModel m, List<Set<INote>> notes) {
    super();
    if (m != null) {
      this.m = m;
    } else {
      System.out.println("Illegal Argument");
      this.m = m;
    }

    this.wPixels = this.getWidth();
    this.hPixels = this.getHeight();

    this.pixPerGridWidth = this.wPixels / gridWidth;
    this.gridHeight = this.hPixels / this.pixPerGridHeight - 2;

    // Information relevant to scrolling.
    this.beatInFrame = 0;
    this.noteInFrame = numPitchRows;
    this.numBeats = 0;
    this.numPitches = 8 * 12;  // Notes in an octave * number of octaves
    this.lowestPitch = 0;

    this.notesAtBeat = notes;
    //this.notesAtBeat = new ArrayList<Set<INote>>();
  }


  public void setModel(IViewModel model, List<Set<INote>> notes) {
    this.m = model;
    this.numBeats = this.m.getScoreLength();
    this.noteInFrame = this.m.getPitchHigh().asNumber();
    this.notesAtBeat = notes;
  }

  public void update(List<Set<INote>> notes) {
    this.wPixels = this.getWidth();
    this.hPixels = this.getHeight();

    this.pixPerGridWidth = this.wPixels / gridWidth;
    this.gridHeight = this.hPixels / this.pixPerGridHeight - 2;

    this.notesAtBeat = notes;
  }

  /**
   * The method to paintNotes.
   *
   * @param canvass [Graphics2D]
   */
  private void paintNotes(Graphics2D canvass) {

//    System.out.println(String.format("Notes: w, h, x, y: %d %d %d %d",
//      this.getWidth(),
//      this.getHeight(),
//      this.getX(),
//      this.getY()));

    // Note names take up 2 columns
    int pitchNamesOffset = 0;
    int pitchNameRowOffset = 2; // Pitches start on the 2nd row
    int pitchNamesWidth = 4;

    // The notes are whatever is left
    int notesColsOffset = pitchNamesOffset + pitchNamesWidth;
    int noteColWidth = 1;
    int numNoteCols = (gridWidth - notesColsOffset);
    this.numBeatCols = numNoteCols;

    // Measure Names along the top
    int measureNumOffset = 0;
    int measureNumColOffset = pitchNamesWidth;
    int measureNumRows = 1;

    // Notes are everything that's left
    int noteRowsOffset = measureNumRows;
    int noteRowHeight = 1; // 1 grid amt
    int numNoteRows = (gridHeight - noteRowsOffset);
    this.numPitchRows = numNoteRows;

    List<String> pitchStrings = new ArrayList<String>();

    // Get Pitch Names
    for (int i = 0; i <= numPitches; i++) {
      String s = new Pitch(i).asString();
      pitchStrings.add(s);
    }

    // Draw Pitch Names
    canvass.setColor(Color.BLACK);
    for (int row = this.noteInFrame;
         row >= max(this.noteInFrame - this.numPitchRows, 0);
         row--) {
      canvass.drawString(pitchStrings.get(row),
          5,
          (this.noteInFrame - row + pitchNameRowOffset) * pixPerGridHeight);
    }

    // Draw Beat numbers
    canvass.setColor(Color.BLACK);
    for (int beat = this.beatInFrame; beat < beatInFrame + this.numBeatCols; beat++) {
      if (beat % 4 == 0) {
        canvass.drawString(Integer.toString(beat),
            (beat - this.beatInFrame + measureNumColOffset) * pixPerGridWidth,
            1 * pixPerGridHeight);
      }
    }

    // Horizontal lines: Pitches
    canvass.setColor(Color.BLACK);
    for (int row = -1; row <= numNoteRows; row++) {  // Start at -1 to draw topmost line
      Line2D.Double tem = new Line2D.Double((measureNumColOffset) * pixPerGridWidth,
          (pitchNameRowOffset + row) * pixPerGridHeight + (pixPerGridHeight / 3),
          (notesColsOffset + numBeatCols) * pixPerGridWidth,
          (pitchNameRowOffset + row) * pixPerGridHeight + (pixPerGridHeight / 3)
      );
      canvass.draw(tem);
    }

    // Vertical lines: Measures
    // Draw first line
    Line2D.Double tem = new Line2D.Double(
        notesColsOffset * pixPerGridWidth,
        (pitchNameRowOffset - 1) * pixPerGridHeight + (pixPerGridHeight / 3), // top
        notesColsOffset * pixPerGridWidth,
        (pitchNameRowOffset + this.numPitchRows) * pixPerGridHeight + (pixPerGridHeight / 3)
    ); // bottom);
    canvass.draw(tem);

    for (int beat = this.beatInFrame; beat < this.beatInFrame + numBeatCols; beat++) {
      if (beat % 4 == 0) {
        Line2D.Double fl = new Line2D.Double(
            (beat - this.beatInFrame + measureNumColOffset) * pixPerGridWidth,
            (pitchNameRowOffset - 1) * pixPerGridHeight + (pixPerGridHeight / 3), // top
            (beat - this.beatInFrame + measureNumColOffset) * pixPerGridWidth,
            (pitchNameRowOffset + this.numPitchRows) * pixPerGridHeight + (pixPerGridHeight / 3)
        );
        canvass.draw(fl);
      }
    }

    // Draw Last line
    Line2D.Double ll = new Line2D.Double(
        (notesColsOffset + numBeatCols) * pixPerGridWidth,
        (pitchNameRowOffset - 1) * pixPerGridHeight + (pixPerGridHeight / 3), // top
        (notesColsOffset + numBeatCols) * pixPerGridWidth,
        (pitchNameRowOffset + this.numPitchRows) * pixPerGridHeight + (pixPerGridHeight / 3)
    ); // bottom);
    canvass.draw(ll);

    // Print Notes
    for (int beat = beatInFrame; beat < (this.numBeatCols + beatInFrame); beat++) {
      if (beat < this.m.getScoreLength() && !this.notesAtBeat.get(beat).isEmpty()) {

        // There are notes to draw at this location
        for (INote n : this.notesAtBeat.get(beat)) {
          int notePitch = n.getPitch().asNumber();
          // if the note is in frame
          if (notePitch <= this.noteInFrame && notePitch >= this.noteInFrame - this.numPitchRows) {

            // head or tail
            if (this.m.getAllNotesAtLocation(beat).contains(n)) {
              canvass.setColor(Color.BLACK);
            } else {
              canvass.setColor(Color.GREEN);
            }
            int rectX = (beat - beatInFrame + notesColsOffset) * pixPerGridWidth;
            int rectY = (this.noteInFrame - notePitch + noteRowsOffset) * pixPerGridHeight;

            Rectangle rec = new Rectangle(
                rectX,
                rectY + (pixPerGridHeight / 3),
                noteColWidth * pixPerGridWidth,
                noteRowHeight * pixPerGridHeight);
            canvass.fill(rec);
          }
        }
      }
    }

    // Current beat
    if (this.cursorBeat >= this.beatInFrame && this.cursorBeat <= this.beatInFrame + numBeatCols) {
      canvass.setColor(Color.RED);
      Line2D.Double cb = new Line2D.Double(
          ((cursorBeat - this.beatInFrame) + pitchNamesWidth) * pixPerGridWidth,
          (measureNumRows) * pixPerGridHeight + (pixPerGridHeight / 3),
          ((cursorBeat - this.beatInFrame) + pitchNamesWidth) * pixPerGridWidth,
          (this.numPitchRows + measureNumRows + 1) * pixPerGridHeight + (pixPerGridHeight / 3)
      );
      canvass.draw(cb);
    }

    // Draw repetitions and endings.
    for (Object i : this.m.getMap().keySet()) {
      if ((int)i >= beatInFrame && (int)i <= beatInFrame + numNoteCols) {
        canvass.setColor(Color.GREEN);
        Line2D.Double cb = new Line2D.Double(
          (((int) i - this.beatInFrame) + pitchNamesWidth) * pixPerGridWidth,
          (measureNumRows) * pixPerGridHeight + (pixPerGridHeight / 3),
          (((int) i - this.beatInFrame) + pitchNamesWidth) * pixPerGridWidth,
          (this.numPitchRows + measureNumRows + 1) * pixPerGridHeight + (pixPerGridHeight / 3)
        );
        canvass.draw(cb);
      }

      int j = (int) m.getMap().get(i);
      if (j >= beatInFrame && j <= beatInFrame + numNoteCols) {
        if (j != 0) {
          canvass.setColor(Color.blue);
          Line2D.Double cd = new Line2D.Double(
            ((j - this.beatInFrame) + pitchNamesWidth) * pixPerGridWidth,
            (measureNumRows) * pixPerGridHeight + (pixPerGridHeight / 3),
            ((j - this.beatInFrame) + pitchNamesWidth) * pixPerGridWidth,
            (this.numPitchRows + measureNumRows + 1) * pixPerGridHeight + (pixPerGridHeight / 3)
          );
          canvass.draw(cd);
        }
      }
    }
    for (Object i : this.m.getMap2().keySet()) {
      if ((int)i >= beatInFrame && (int)i <= beatInFrame + numNoteCols) {
        canvass.setColor(Color.ORANGE);
        Line2D.Double cb = new Line2D.Double(
          (((int) i - this.beatInFrame) + pitchNamesWidth) * pixPerGridWidth,
          (measureNumRows) * pixPerGridHeight + (pixPerGridHeight / 3),
          (((int) i - this.beatInFrame) + pitchNamesWidth) * pixPerGridWidth,
          (this.numPitchRows + measureNumRows + 1) * pixPerGridHeight + (pixPerGridHeight / 3)
        );
        canvass.draw(cb);
      }
      int j = (int) m.getMap2().get(i);

      if (j >= beatInFrame && j <= beatInFrame + numNoteCols) {
        if (j != 0) {
          canvass.setColor(Color.orange);
          Line2D.Double cd = new Line2D.Double(
            ((j - this.beatInFrame) + pitchNamesWidth) * pixPerGridWidth,
            (measureNumRows) * pixPerGridHeight + (pixPerGridHeight / 3),
            ((j - this.beatInFrame) + pitchNamesWidth) * pixPerGridWidth,
            (this.numPitchRows + measureNumRows + 1) * pixPerGridHeight + (pixPerGridHeight / 3)
          );
          canvass.draw(cd);
        }
      }
    }

  }

  @Override
  public void paintComponent(Graphics g) {
    this.wPixels = this.getWidth();
    this.hPixels = this.getHeight();

    this.pixPerGridWidth = this.wPixels / gridWidth;
    this.gridHeight = this.hPixels / this.pixPerGridHeight - 2;

    Graphics2D g2 = (Graphics2D) g;
    super.paintComponent(g2);

    g2.setFont(new Font("panel", Font.BOLD, this.pixPerGridHeight));

    paintNotes(g2);
  }

  public void scrollToOctave(int octave) {
    int notesPerOctave = 12;
    if ( octave < (8 - (numPitchRows / notesPerOctave))) {
      this.noteInFrame = octave * notesPerOctave + numPitchRows;
    }
  }

//  @Override
//  public Dimension getPreferredSize() {
//    int width = this.wPixels;
//    int height = this.get;
//    return new Dimension(width, height);
//  }

  /**
   * Sets the cursorBeat beat.
   *
   * @param beat [int]
   */
  public void setBeat(int beat) {
    this.cursorBeat = beat;

    // Beginning of notes
    if (beat < this.numBeatCols / 2) {
      this.beatInFrame = 0;
    }
    // past the end
    else if (beat > max(this.numBeatCols, this.m.getScoreLength())) {
        this.beatInFrame = beat - numBeatCols;
    }
    // end of notes
    else if (beat > this.m.getScoreLength() - (this.numBeatCols / 2)) {
      if (this.m.getScoreLength() > this.numBeatCols) {
        this.beatInFrame = this.m.getScoreLength() - this.numBeatCols;
      }

      else {
        this.beatInFrame = 0;
      }
    }

    else {
      this.beatInFrame = beat - this.numBeatCols / 2;
    }

    this.repaint();
  }

  public IPitch getLowPitchInRange() {
    return new Pitch(max(this.noteInFrame - this.numPitchRows, 0));
  }

  public IPitch getHighPitchInRange() {
    return new Pitch(this.noteInFrame);
  }
  /**
   * Scroll the frame to the left: Notes move <--
   */
  public void scrollLeft() {
    if (this.beatInFrame < (this.numBeats)) {
      this.beatInFrame++;
    }
  }

  /**
   * Scroll the frame to the right: Notes move <--
   */
  public void scrollRight() {
    if (this.beatInFrame > 0) {
      this.beatInFrame--;
    }
  }

  /**
   * Scroll the frame up: Notes move ^
   */
  public void scrollUp() {
    if (noteInFrame > lowestPitch + numPitchRows) {
      noteInFrame--;
    }
  }

  /**
   * Scroll the frame down:  Notes move v
   */
  public void scrollDown() {
    if (noteInFrame < numPitches + lowestPitch) {
      noteInFrame++;
    }
  }
}
