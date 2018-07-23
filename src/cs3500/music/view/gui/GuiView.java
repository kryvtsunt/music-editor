package cs3500.music.view.gui;


import cs3500.music.model.MusicModel;
import cs3500.music.view.AddNote;
import cs3500.music.view.INote;
import cs3500.music.view.IPitch;
import cs3500.music.view.IViewModel;
import cs3500.music.view.gui.panels.InfoPanel;
import cs3500.music.view.gui.panels.KeyboardPanel;
import cs3500.music.view.gui.panels.NotePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.*;
import java.util.List;

/**
 * An implementation of the GUI view.
 */
public class GuiView implements IGuiView {

  List<Set<INote>> notesAtBeat;
  IViewModel vm;

  private int wPixels;
  private int hPixels;

  private JFrame window;

  private NotePanel displayPanel;
  private KeyboardPanel keyboardPanel;
  private InfoPanel infoPanel;

  int currentBeat;

  /**
   * Creates new GuiView.
   */
  public GuiView(IViewModel viewModel) {

    this.vm = viewModel;
    this.updateNotesAtBeat();

    this.window = new JFrame("Music Editor");
    window.setLayout(new BoxLayout(window.getContentPane(), BoxLayout.Y_AXIS));
    window.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    displayPanel = new NotePanel(this.vm, this.notesAtBeat);
    displayPanel.setPreferredSize(new Dimension(3840, 800));
    displayPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

    keyboardPanel = new KeyboardPanel();
    keyboardPanel.setPreferredSize(new Dimension(3840, 350));
    keyboardPanel.setMaximumSize(new Dimension(3840, 500));
    keyboardPanel.setMinimumSize(new Dimension(300, 100));
    keyboardPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

    infoPanel = new InfoPanel("Paused", 400); // Default numbers change
    infoPanel.setPreferredSize(new Dimension(3840, 50));
    infoPanel.setMaximumSize(new Dimension(3840, 50));
    infoPanel.setMinimumSize(new Dimension(300, 50));
    infoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);


    // Used to be display panel, if this doesnt work then revert
    window.setFocusable(true);
    window.requestFocusInWindow();

    window.setSize(new Dimension(1600, 900));
    this.wPixels = window.getWidth();
    this.hPixels = window.getHeight();

    window.add(displayPanel, 0);
    window.add(keyboardPanel, 1);
    window.add(infoPanel, 2);

    //window.pack();
    window.setVisible(true);
  }

  private void updateNotesAtBeat() {
    notesAtBeat = new ArrayList<Set<INote>>();
    for (int i = 0; i <= this.vm.getScoreLength(); i++) {
      notesAtBeat.add(new HashSet<INote>());
    }

    for (int i = 0; i < this.vm.getScoreLength(); i++) {
      Collection<INote> notesStarting = this.vm.getAllNotesAtLocation(i);
      for (INote n : notesStarting) {
        for (int j = 0; j < n.getDuration(); j++) {
          if (i + j < this.notesAtBeat.size()) {
            this.notesAtBeat.get(i + j).add(n);
          }
        }
      }
    }
  }

  @Override
  public void update() {
    this.hPixels = window.getHeight();
    this.wPixels = window.getWidth();

    //this.displayPanel.setSize(new Dimension(this.wPixels, 4 * (this.hPixels / 9)));
    //this.keyboardPanel.setSize(new Dimension(this.wPixels, 4 * (this.hPixels / 9)));
    //this.infoPanel.setSize(new Dimension(this.wPixels, 1 * (this.hPixels / 9)));

    this.updateNotesAtBeat();

    displayPanel.update(this.notesAtBeat);

    keyboardPanel.update();
    keyboardPanel.setPlayedNotes(notesAtBeat.get(currentBeat));
    this.keyboardPanel.setPitchRange(this.displayPanel.getLowPitchInRange(),
      this.displayPanel.getHighPitchInRange());

    infoPanel.update();
    infoPanel.setTempoBPM(1000 * 1000 * 60 / vm.getTempo());

//    System.out.println(String.format("Notes: w, h, x, y: %d %d %d %d",
//      displayPanel.getWidth(),
//      displayPanel.getHeight(),
//      displayPanel.getX(),
//      displayPanel.getY()));
//
//    System.out.println(String.format("Keys: w, h, x, y: %d %d %d %d",
//      keyboardPanel.getWidth(),
//      keyboardPanel.getHeight(),
//      keyboardPanel.getX(),
//      keyboardPanel.getY()));
//
//    System.out.println(String.format("Info: w, h, x, y: %d %d %d %d",
//      infoPanel.getWidth(),
//      infoPanel.getHeight(),
//      infoPanel.getX(),
//      infoPanel .getY()));


    window.repaint();
  }


  @Override
  public void giveViewModel(IViewModel vm) {
    if (vm != null) {
      this.vm = vm;
      this.update();
      this.displayPanel.setModel(this.vm, this.notesAtBeat);
      //this.pack();
      //window.setVisible(true);

    } else {
      throw new IllegalArgumentException("Invalid View Model");
    }
  }

  @Override
  public void setStateName(String name) {
    System.out.println("GUIView:: Set State Name: " + name);
    keyboardPanel.setStateName(name);
    infoPanel.setStateName(name);
    this.window.repaint();
  }

  public void setPracticeOctave(int octave) {
    this.keyboardPanel.setPracticeOctave(octave);
    this.infoPanel.setPracticeOctave(octave);
  }

  @Override
  public void moveToBeat(int beat) {
    this.displayPanel.setBeat(beat);
    if (beat < this.vm.getScoreLength()) {
      this.keyboardPanel.setPlayedNotes(this.notesAtBeat.get(beat));
    }
    else {
      this.keyboardPanel.setPlayedNotes(new HashSet<INote>());
    }
    this.keyboardPanel.setPitchRange(this.displayPanel.getLowPitchInRange(),
      this.displayPanel.getHighPitchInRange());
    this.window.repaint();
  }


  @Override
  public void scrollLeft() {
    this.displayPanel.scrollLeft();
    this.keyboardPanel.setPitchRange(this.displayPanel.getLowPitchInRange(),
      this.displayPanel.getHighPitchInRange());
    this.window.repaint();
  }

  @Override
  public void scrollRight() {
    this.displayPanel.scrollRight();
    this.keyboardPanel.setPitchRange(this.displayPanel.getLowPitchInRange(),
      this.displayPanel.getHighPitchInRange());
    this.window.repaint();
  }

  @Override
  public void scrollUp() {
    //System.out.println("Scroll Up");
    this.displayPanel.scrollUp();
    this.keyboardPanel.setPitchRange(this.displayPanel.getLowPitchInRange(),
      this.displayPanel.getHighPitchInRange());

    this.window.repaint();
  }

  @Override
  public void scrollDown() {
    this.displayPanel.scrollDown();
    this.keyboardPanel.setPitchRange(this.displayPanel.getLowPitchInRange(),
      this.displayPanel.getHighPitchInRange());
    this.window.repaint();

  }

  @Override
  public void scrollToOctave(int octave) {
    this.displayPanel.scrollToOctave(octave);
    this.keyboardPanel.setPitchRange(this.displayPanel.getLowPitchInRange(),
      this.displayPanel.getHighPitchInRange());
    this.window.repaint();
  }

  @Override
  public void addKeyListener(KeyListener kbl) {
    //System.out.println("Adding Keyboard Listener");
    window.addKeyListener(kbl);
    this.displayPanel.addKeyListener(kbl);
  }

  @Override
  public void addMouseListener(MouseListener bl) {
    this.keyboardPanel.addMouseListener(bl);
  }

  @Override
  public boolean keyAtPosn(int x, int y) {
    System.out.println(String.format("GuiView::keyAtPosn:: %d, %d, %d, %d", x, y, this.keyboardPanel.getX(), this.keyboardPanel.getY()));
    System.out.println(String.format("GuiView::keyAtPosn:: %d, %d", x - this.keyboardPanel.getX(), y - this.keyboardPanel.getY()));
    System.out.println(String.format("GuiView::keyAtPosn:: %d, %d", x, y, this.window.getX(), this.window.getY()));

    int relX = x - this.keyboardPanel.getX();
    int relY = y - this.keyboardPanel.getY();

    return this.keyboardPanel.keyAtPosn(x, y);
  }


  @Override
  public IPitch getPitchAt(int x, int y) throws IllegalArgumentException {
    return this.keyboardPanel.getPitchAt(x - this.keyboardPanel.getX(), y - this.displayPanel.getY());
  }
}