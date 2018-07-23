package cs3500.music.provider;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.List;

import cs3500.music.provider.Note;
import cs3500.music.provider.IView;
import cs3500.music.provider.MidiViewImpl;

/**
 * Repres  ents a composite view implementation.
 */
public class CompositeViewImpl implements IView {

  GuiViewImpl gui;
  MidiViewImpl midi;

  /**
   * Constructs a {@code CompositeViewImpl} object.
   *
   * @param gui the gui view to be used.
   * @param midi the midi view to be used.
   */
  public CompositeViewImpl(GuiViewImpl gui, MidiViewImpl midi) {
    this.gui = gui;
    this.midi = midi;
  }

  @Override
  public void setCurrentBeat(int beat) {
    gui.setCurrentBeat(beat);
    midi.setCurrentBeat(beat);
  }

  @Override
  public void setBeats(int beats) {
    gui.setBeats(beats);
    midi.setBeats(beats);
  }

  @Override
  public void setNotes(List<Note> notes) {
    gui.setNotes(notes);
    midi.setNotes(notes);
  }

  @Override
  public void setTempo(int tempo) {
    gui.setTempo(tempo);
    midi.setTempo(tempo);
  }

  @Override
  public void addKeyListener(KeyListener listener) {
    gui.addKeyListener(listener);
    midi.addKeyListener(listener);
  }

  @Override
  public void addMouseListener(MouseListener listener) {
    gui.addMouseListener(listener);
  }

  @Override
  public void initializeView() {
    gui.initializeView();
    midi.initializeView();
  }

  @Override
  public void refresh() {
    gui.refresh();
    midi.refresh();
  }

  @Override
  public void playback() {
    midi.playback();
  }
}
