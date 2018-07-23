package cs3500.music.provider.panels;

import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

import cs3500.music.provider.Note;

/**
 * Represents a panel containing a sheet of notes.
 */
public class SheetPanel extends JPanel {

  private static final int HEIGHT_SCALAR = 15;
  private static final int WIDTH_SCALAR = HEIGHT_SCALAR * 2;

  private int allBeats;
  private int currentBeat;
  private List<Rectangle> canvas;
  private List<Rectangle> immediateNotes;
  private List<Rectangle> sustainedNotes;
  private Line2D marker;

  /**
   * Constructs a preset {@code SheetPanel} object.
   */
  public SheetPanel() {
    super();
    this.setBackground(Color.WHITE);
  }

  /**
   * returns the current beat the sheet panel is at.
   *
   * @return the current beat.
   */
  public int getCurrentBeat() {
    return this.currentBeat;
  }

  /**
   * Sets the sheet panel's beats to the given beats.
   *
   * @param beats the given beats to be set to.
   */
  public void setBeats(int beats) {
    allBeats = beats;
  }

  /**
   * Sets the sheet panel's beats to the given beat.
   *
   * @param beat the given beat to be set to.
   */
  public void setCurrentBeat(int beat) {
    currentBeat = beat;
  }

  /**
   * Sets the values for the blank rectangles occupying the background canvas.
   *
   * @param noteSignatures the list of note signatures to be used.
   */
  public void setCanvas(List<String> noteSignatures) {
    int columns = (int) Math.ceil(allBeats / 4.0);
    int rows = noteSignatures.size();
    canvas = new ArrayList<Rectangle>();
    for (int x = 0; x < rows; x++) {
      for (int y = 0; y < columns; y++) {
        canvas.add(new Rectangle(y * 4, x, 4, 1));
      }
    }
  }

  /**
   * Sets the values for the rectangles corresponding to the notes played
   * in the composition.
   *
   * @param notes the list of notes to be used.
   * @param noteSignatures the list of note signatures to be used.
   */
  public void setPlayingNotes(List<Note> notes, List<String> noteSignatures) {

    immediateNotes = new ArrayList<Rectangle>();
    sustainedNotes = new ArrayList<Rectangle>();

    for (Note playedNote : notes) {
      int relativePitch = 0;
      for (String signature : noteSignatures) {
        if (signature
            .equals(playedNote.genSignature())) {
          break;
        } else {
          relativePitch++;
        }
      }
      immediateNotes.add(new Rectangle(playedNote.getStart(),
          relativePitch, 1, 1));
      sustainedNotes.add(new Rectangle(playedNote.getStart(),
          relativePitch, playedNote.getEnd() - playedNote.getStart(),
          1));
    }
  }

  /**
   * Sets the values for the marker corresponding to the current beat.
   *
   * @param noteSignatures the list of note signatures to be used.
   */
  public void setMarker(List<String> noteSignatures) {
    int rowHeight = noteSignatures.size();
    marker = new Line2D.Double(currentBeat, 0, currentBeat, rowHeight);
  }

  /**
   * Draws the rectangles using the parameters.
   *
   * @param rects the rectangles to be drawn.
   * @param g2d the graphics to draw onto.
   * @param color the color of the rectangles.
   * @param fill whether or not the rectangles will be filled.
   */
  public void drawRects(List<Rectangle> rects, Graphics2D g2d, Color color, boolean fill) {
    g2d.setColor(color);
    if (fill) {
      for (Rectangle rect : rects) {
        g2d.fillRect(rect.x * WIDTH_SCALAR, rect.y * HEIGHT_SCALAR,
            rect.width * WIDTH_SCALAR, rect.height * HEIGHT_SCALAR);
      }
    } else {
      for (Rectangle rect : rects) {
        g2d.drawRect(rect.x * WIDTH_SCALAR, rect.y * HEIGHT_SCALAR,
            rect.width * WIDTH_SCALAR, rect.height * HEIGHT_SCALAR);
      }
    }
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2d = (Graphics2D) g;

    AffineTransform originalTransform = g2d.getTransform();
    g2d.translate(0, this.getPreferredSize().getHeight());
    g2d.scale(1, -1);

    drawRects(sustainedNotes, g2d, Color.GREEN, true);
    drawRects(immediateNotes, g2d, Color.BLACK, true);
    drawRects(canvas, g2d, Color.BLACK, false);

    g2d.setColor(Color.RED);
    g2d.drawLine((int) (marker.getX1() * WIDTH_SCALAR), (int) marker.getY1(),
        (int) (marker.getX2() * WIDTH_SCALAR), (int) (marker.getY2() * HEIGHT_SCALAR));

    g2d.setTransform(originalTransform);
  }
}
