package cs3500.music.provider.panels;

import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import cs3500.music.provider.Note;

/**
 * Represents a panel containing the keyboard of the composition.
 */
public class KeyboardPanel extends JPanel {

  private static final int WIDTH_SCALAR = 7;
  private static final int HEIGHT_SCALAR = WIDTH_SCALAR * 15;
  private static final int OFFSET = 3;

  private int currentBeat;
  private List<Rectangle> majorKeys;
  private List<Rectangle> primaryMinorKeys;
  private List<Rectangle> secondaryMinorKeys;
  private List<Rectangle> playingMajorKeys;
  private List<Rectangle> playingMinorKeys;

  /**
   * Constructs a preset {@code KeyboardPanel} object.
   */
  public KeyboardPanel() {
    super();
    this.setKeys(10);
    this.setBackground(Color.WHITE);
  }

  /**
   * Sets the current beat of the panel to the given beat.
   *
   * @param beat the given beat to be set to.
   */
  public void setCurrentBeat(int beat) {
    this.currentBeat = beat;
  }

  /**
   * Sets the values for each of the default keys to be displayed.
   *
   * @param octave the octave to be used.
   */
  public void setKeys(int octave) {
    this.majorKeys = new ArrayList<Rectangle>();
    this.primaryMinorKeys = new ArrayList<Rectangle>();
    this.secondaryMinorKeys = new ArrayList<Rectangle>();
    for (int x = 0; x < octave * 7; x++) {
      majorKeys.add(new Rectangle(x * 2, 0, 2, 2));
    }
    for (int y = 0; y < octave; y++) {
      primaryMinorKeys.add(new Rectangle((y * 14) + 1, 1,
          1, 1));
      primaryMinorKeys.add(new Rectangle((y * 14) + 3, 1,
          1, 1));
    }
    for (int z = 0; z < octave; z++) {
      secondaryMinorKeys.add(new Rectangle((z * 14) + 7, 1,
          1, 1));
      secondaryMinorKeys.add(new Rectangle((z * 14) + 9, 1,
          1, 1));
      secondaryMinorKeys.add(new Rectangle((z * 14) + 11, 1,
          1, 1));
    }
  }

  /**
   * Sets the values for the keys being played at the current beat.
   *
   * @param notes the list of notes to be used.
   */
  public void setPlayingKeys(List<Note> notes) {
    this.playingMajorKeys = new ArrayList<Rectangle>();
    this.playingMinorKeys = new ArrayList<Rectangle>();
    List<Note> playingNotes = new ArrayList<Note>();
    for (Note note : notes) {
      if (note.getStart() <= currentBeat && note.getEnd() > currentBeat) {
        playingNotes.add(note);
      }
    }
    for (Note note : playingNotes) {
      int altPitch = note.getPitch().computePitch(note.getOctave(), note.getSharp());
      if (note.getSharp()) {
        playingMinorKeys.add(new Rectangle(altPitch, 1, 1, 1));
      } else {
        playingMajorKeys.add(new Rectangle(altPitch, 0, 2, 2));
      }
    }
  }

  /**
   * Adds the listener that detects mouse events.
   *
   * @param listener the given listener to be added.
   */
  public void addMouseListener(MouseListener listener) {
    // Work in progress.
  }

  /**
   * Gets the current key corresponding to the given position of the mouse.
   *
   * @param x the x position of the mouse.
   * @param y the y position of the mouse.
   * @return the note corresponding to the mouse click.
   */
  public Note getKey(int x, int y) {
    return null;
  }

  /**
   * Draws the rectangles using the given parameters.
   *
   * @param rects the rectangles to be drawn.
   * @param g2d the graphics to draw onto.
   * @param color the color of the rectangles.
   * @param major whether or not the keys are major.
   * @param fill whether or not the rectangles are to be filled.
   */
  public void drawRects(List<Rectangle> rects, Graphics2D g2d, Color color, boolean major,
      boolean fill) {
    g2d.setColor(color);
    if (major) {
      if (fill) {
        for (Rectangle rect : rects) {
          g2d.fillRect(rect.x * WIDTH_SCALAR, rect.y,
              rect.width * WIDTH_SCALAR, rect.height * HEIGHT_SCALAR);
        }
      } else {
        for (Rectangle rect : rects) {
          g2d.drawRect(rect.x * WIDTH_SCALAR, rect.y,
              rect.width * WIDTH_SCALAR, rect.height * HEIGHT_SCALAR);
        }
      }
    } else {
      if (fill) {
        for (Rectangle rect : rects) {
          g2d.fillRect((rect.x * WIDTH_SCALAR) + OFFSET, rect.y * HEIGHT_SCALAR,
              rect.width * WIDTH_SCALAR, rect.height * HEIGHT_SCALAR);
        }
      } else {
        for (Rectangle rect : rects) {
          g2d.drawRect((rect.x * WIDTH_SCALAR) + OFFSET, rect.y * HEIGHT_SCALAR,
              rect.width * WIDTH_SCALAR, rect.height * HEIGHT_SCALAR);
        }
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

    drawRects(playingMajorKeys, g2d, Color.YELLOW, true, true);
    drawRects(majorKeys, g2d, Color.BLACK, true, false);
    drawRects(primaryMinorKeys, g2d, Color.BLACK, false, true);
    drawRects(secondaryMinorKeys, g2d, Color.BLACK, false, true);
    drawRects(playingMinorKeys, g2d, Color.YELLOW, false, true);

    g2d.setTransform(originalTransform);

  }
}
