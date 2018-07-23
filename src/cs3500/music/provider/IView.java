package cs3500.music.provider;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.List;

import cs3500.music.provider.Note;

public interface IView {

  /**
   * Sets {@code this} view's currentBeat to the given beat.
   *
   * @param beat the given beat be used.
   */
  void setCurrentBeat(int beat);

  /**
   * Sets {@code this} view's total beats to the given beats.
   *
   * @param beats the given beats to be used.
   */
  void setBeats(int beats);

  /**
   * Sets {@code this} view's notes to the given notes.
   *
   * @param notes the given notes to be used.
   */
  void setNotes(List<Note> notes);

  /**
   * Sets {@code this} view's notes to the given notes.
   *
   * @param tempo the given tempo to be used.
   */
  void setTempo(int tempo);

  /**
   * Configures {@code this} view to listen for events using the given
   * key listener.
   *
   * @param listener the given KeyListener to be used.
   */
  void addKeyListener(KeyListener listener);

  /**
   * Configures {@code this} view to listen for events using the given
   * mouse listener.
   *
   * @param listener the given MouseListener to be used.
   */
  void addMouseListener(MouseListener listener);

  /**
   * Begins the view and sets in its first state.
   */
  void initializeView();

  /**
   * Recreates the view based on changed parameters.
   */
  void refresh();

  /**
   * Streams the view back sonically.
   */
  void playback();
}
