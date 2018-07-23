package cs3500.music.view;


import cs3500.music.controller.ICombinedView;
import cs3500.music.view.gui.GuiView;
import cs3500.music.view.gui.IGuiView;
import cs3500.music.view.midi.MidiViewImpl;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;


/**
 * An implementation of the PlayableView and IGuiView.
 */
public class Combined implements ICombinedView {


  private IGuiView gui;
  private MidiViewImpl midi;


  /**
   * Creates new Combined view.
   */
  public Combined(IViewModel vm) {
    this.gui = new GuiView(vm);
    this.midi = new MidiViewImpl();
  }


  @Override
  public void update() {
    this.gui.update();
    this.midi.update();
  }

  public void play(int beat) {
    this.gui.moveToBeat(beat);
    this.midi.play(beat);
    //System.out.println("play2");
  }
  //System.out.println("la");

  @Override
  public void pause() {
    this.midi.pause();
    //System.out.println("pause2");
  }

  @Override
  public void scrollUp() {
    this.gui.scrollUp();
  }

  @Override
  public void scrollDown() {
    this.gui.scrollDown();
  }

  @Override
  public void scrollToOctave(int octave) {
    this.gui.scrollToOctave(octave);
  }


  @Override
  public void giveViewModel(IViewModel vm) {
    if (vm != null) {

      this.gui.giveViewModel(vm);
      this.midi.giveViewModel(vm);

    } else {
      throw new IllegalArgumentException("Invalid View Model");
    }
  }

  public void setPracticeOctave(int octave) {
    this.gui.setPracticeOctave(octave);
  }

  @Override
  public void moveToBeat(int beat) {
    this.gui.moveToBeat(beat);
  }

  @Override
  public void scrollLeft() {
    this.gui.scrollLeft();
  }

  @Override
  public void scrollRight() {
    this.gui.scrollRight();
  }

  @Override
  public void addKeyListener(KeyListener kbl) {
    this.gui.addKeyListener(kbl);
  }

  @Override
  public void setStateName(String name) {
    this.gui.setStateName(name);
  }

  @Override
  public void addMouseListener(MouseListener ml) {
    this.gui.addMouseListener(ml);
  }

  @Override
  public boolean keyAtPosn(int x, int y) {
    return this.gui.keyAtPosn(x, y);
  }

  @Override
  public IPitch getPitchAt(int x, int y) throws IllegalArgumentException {
    return this.gui.getPitchAt(x, y);
  }

}