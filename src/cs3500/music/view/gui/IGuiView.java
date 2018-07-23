package cs3500.music.view.gui;

import cs3500.music.model.Pitch;
import cs3500.music.view.AddNote;
import cs3500.music.view.IPitch;
import cs3500.music.view.IView;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

/**
 * IGuiView represents a GUI view of the program.
 */
public interface IGuiView extends IView {


  /**
   * The method that scrolls window left.
   */
  void scrollLeft();

  /**
   * The method that scrolls window right.
   */
  void scrollRight();

  /**
   * The method that scrolls window up.
   */
  void scrollUp();

  /**
   * The method that scrolls window down.
   */
  void scrollDown();


  void scrollToOctave(int octave);

  /**
   * The method that moves user to the given beat.
   *
   * @param beat [int]
   */
  void moveToBeat(int beat);

  /**
   * The method that adds that key listener.
   */
  void addKeyListener(KeyListener kbl);

  void setStateName(String name);

  void setPracticeOctave(int octave);

  /**
   * The method that adds a mouse Listener.
   *
   * @param bl [MouseListener]
   */
  void addMouseListener(MouseListener bl);

  /**
   * The method that defines the key at the given position.
   *
   * @param x [int]
   * @param y [int]
   * @return [boolean]
   */
  boolean keyAtPosn(int x, int y);


  /**
   * the method that gets an AddNote at the given position.
   *
   * @param x [int]
   * @param y [int]
   * @return [AddNote]
   * @throws IllegalArgumentException ...
   */
  IPitch getPitchAt(int x, int y) throws IllegalArgumentException;
}
