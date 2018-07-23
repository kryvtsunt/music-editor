package cs3500.music.provider;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import cs3500.music.provider.Note;
import cs3500.music.provider.Utils;
import cs3500.music.provider.IView;
import cs3500.music.provider.panels.KeyboardPanel;
import cs3500.music.provider.panels.SheetPanel;

/**
 * Represents a Gui View Implementation.
 */
public class GuiViewImpl extends JFrame implements IView {

  private KeyboardPanel keyboardPanel;
  private SheetPanel sheetPanel;

  private JScrollBar bar;
  private int scrollIdx;

  private int beats;
  private int currentBeat;
  private List<Note> notes;
  private List<String> noteSignatures;
  private List<String> reversedNoteSignatures;

  /**
   * Constructs a preset {@code GuiViewImpl} object.
   *
   */
  public GuiViewImpl() {
    this.setTitle("Music Visualizer");
    this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
  }

  /**
   * Initializes the view panel's first values.
   */
  public void activate() {
    this.setLayout(new BorderLayout(5, 5));
    this.configureSheetAndBeatPanel();
    this.configurePitchPanel();
    this.configureKeyboardPanel();
    pack();

  }

  /**
   * Recalibrates the view and updates any changes.
   */
  public void refresh() {
    int oldBeat = sheetPanel.getCurrentBeat();
    sheetPanel.setCurrentBeat(currentBeat);
    sheetPanel.setMarker(noteSignatures);
    sheetPanel.repaint();

    if (currentBeat > oldBeat) {
      scrollIdx++;
    } else if (currentBeat != 0) {
      scrollIdx--;
    }

    if (currentBeat == 0) {
      bar.setValue(0);
      scrollIdx = 0;
    } else if (currentBeat == beats) {
      bar.setValue(bar.getMaximum());
      scrollIdx = 31;
    } else if (scrollIdx < 0 && currentBeat != 0) {
      int oldVal = bar.getValue();
      bar.setValue(oldVal - 951);
      scrollIdx = 31;
    } else if (scrollIdx == 32) {
      int oldVal = bar.getValue();
      bar.setValue(oldVal + 951);
      scrollIdx = 0;
    }

    keyboardPanel.setCurrentBeat(currentBeat);
    keyboardPanel.setPlayingKeys(notes);
    keyboardPanel.repaint();
  }

  @Override
  public void playback() {
    // Unused: Playback method beyond scope of this View Implementation.
  }

  /**
   * Sets the appropriate values for the Pitch Panel and adds it.
   */
  public void configurePitchPanel() {
    JPanel pitchPanel = new JPanel(new GridLayout(0, 1));
    for (String signature : reversedNoteSignatures) {
      JLabel sig = new JLabel("  " + signature);
      pitchPanel.add(sig);
    }
    pitchPanel.setPreferredSize(new Dimension(50, (noteSignatures.size() * 15) + 2));
    this.add(pitchPanel, BorderLayout.LINE_START);
  }

  /**
   * Sets the appropriate values for the Sheet Panel, and the Beat Panel,
   * and adds them both.
   */
  public void configureSheetAndBeatPanel() {

    int width = 1000;

    JPanel beatPanel = new JPanel(new GridLayout(1, 0));
    for (int x = 0; x < beats / 4; x++) {
      JLabel beat = new JLabel(Integer.toString(x * 4));
      beat.setPreferredSize(new Dimension(120, 10));
      beatPanel.add(beat);
    }
    JPanel beatPanelHolder = new JPanel(new FlowLayout(FlowLayout.LEFT, 53, 0));
    beatPanelHolder.add(beatPanel);
    JScrollPane beatScroll = new JScrollPane(beatPanelHolder);
    bar = beatScroll.getHorizontalScrollBar();
    bar.setMaximum(120 * beats);
    beatScroll.setPreferredSize(new Dimension(WIDTH, 20));
    this.add(beatScroll, BorderLayout.PAGE_START);

    sheetPanel = new SheetPanel();
    sheetPanel.setBeats(beats);
    sheetPanel.setCurrentBeat(currentBeat);
    sheetPanel.setCanvas(noteSignatures);
    sheetPanel.setPlayingNotes(notes, noteSignatures);
    sheetPanel.setMarker(noteSignatures);
    sheetPanel.setPreferredSize(new Dimension(((int) Math.ceil(beats / 4.0) * 120) + 2,
            noteSignatures.size() * 15));
    JScrollPane sheetScroll = new JScrollPane(sheetPanel,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    sheetScroll.setPreferredSize(new Dimension(WIDTH, noteSignatures.size() * 15));
    sheetScroll.setHorizontalScrollBar(bar);
    this.add(sheetScroll, BorderLayout.CENTER);
  }

  /**
   * Sets the appropriate values for the keyboard panels and adds it.
   */
  public void configureKeyboardPanel() {
    keyboardPanel = new KeyboardPanel();
    keyboardPanel.setCurrentBeat(currentBeat);
    keyboardPanel.setPlayingKeys(notes);
    keyboardPanel.setPreferredSize(new Dimension(981, 210));
    JPanel keyboardPanelHolder = new JPanel(new FlowLayout(FlowLayout.CENTER));
    keyboardPanelHolder.add(keyboardPanel);
    this.add(keyboardPanelHolder, BorderLayout.PAGE_END);
  }

  /**
   * Returns the key associated with the given position.
   * @param x the x position of the mouse.
   * @param y the y position of the mouse.
   * @return the clicked key in note form.
   */
  public Note getKey(int x, int y) {
    return keyboardPanel.getKey(x, y);
  }

  /**
   * Sets the view's current beat to the given beat.
   * @param beat the given beat to be used.
   */
  public void setCurrentBeat(int beat) {
    if (beat < 0 || beat > this.beats) {
      // Do not advance Beat
    } else {
      this.currentBeat = beat;
    }
  }

  @Override
  public void setBeats(int beats) {
    this.beats = beats;
  }

  @Override
  public void setNotes(List<Note> notes) {
    this.notes = notes;
    this.noteSignatures = Utils.genNoteSignatures(Utils.genNoteReps(notes));
    this.reversedNoteSignatures = new ArrayList<String>(noteSignatures);
    Collections.reverse(reversedNoteSignatures);
  }

  @Override
  public void setTempo(int tempo) {
    // Does nothing currently
  }

  @Override
  public void initializeView() {
    this.activate();
    setVisible(true);
  }

}
