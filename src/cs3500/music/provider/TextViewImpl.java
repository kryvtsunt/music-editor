package cs3500.music.provider;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import cs3500.music.provider.Note;
import cs3500.music.provider.Utils;

/**
 * Represents a text view implementation.
 */
public class TextViewImpl implements IView {

  private int beats;
  private List<Note> notes;

  /**
   * Prints out the output of the music model following the guidelines:
   *
   * <p> - Has a column representing the beats is printed right-justified with
   * leading spaces as wide as necessary.
   * - Has a sequence of columns, each five characters wide, representing
   * each pitch.
   * - Uses exactly as many columns as needed.
   * - Note-heads are represented as an "  X  ".
   * - Note-sustains are represented as a "  |  ".
   * - Notes not played are represented as "     ".
   * - Every line is exactly the same length. </p>
   */
  public void output(List<Note> notes) {
    String beatPadding = Integer.toString(beats).replaceAll("\\d", " ");
    int offset = beatPadding.length();
    List<Note> noteReps = Utils.genNoteReps(notes);
    String primaryLine = beatPadding + this.genPrimaryLine(noteReps);
    String primaryOutput = this.genSecondaryLines(noteReps, offset);
    String output = primaryLine + "\n" + primaryOutput;
    System.out.print(output);
  }

  /**
   * Returns the first line of output consisting of all the note signatures.
   *
   * @param noteReps the note representatives for the {@code this} notes.
   * @return the string that is the first line of output.
   */
  public String genPrimaryLine(List<Note> noteReps) {
    StringBuilder sb = new StringBuilder();
    List<String> noteSignatures = Utils.genNoteSignatures(noteReps);
    for (String sig : noteSignatures) {
      switch (sig.length()) {
        case 2:
          sb.append("  ").append(sig).append(" ");
          break;
        case 3:
          sb.append(" ").append(sig).append(" ");
          break;
        case 4:
          sb.append(" ").append(sig);
          break;
        default:
          throw new IllegalArgumentException("Invalid Note: Note representation is too long");
      }
    }
    return sb.toString();
  }

  /**
   * Returns one or more lines corresponding to each beat and the notes being played
   * on each beat
   *
   * @param noteReps the list of note representatives.
   * @param offset the padding for each beat.
   * @return the string that is all the lines corresponding to each beat.
   */
  public String genSecondaryLines(List<Note> noteReps, int offset) {
    StringBuilder sb = new StringBuilder();
    String padding = "%" + offset + "d";
    for (int i = 0; i < beats; i++) {
      String eachLine = this.genEachLine(i, noteReps);
      sb.append(String.format(padding, i)).append(eachLine).append("\n");
    }
    return sb.toString();
  }

  /**
   * Returns a line corresponding to the representation of each beat line
   * of the music model.
   *
   * @param beat the current beat being evaluated.
   * @param noteReps the current notes being evaluated.
   * @return the string that is a line corresponding to each note's data with the beat.
   */
  public String genEachLine(int beat, List<Note> noteReps) {
    StringBuilder sb = new StringBuilder();
    List<String> outputs = new ArrayList<String>();
    String seen = "";
    for (Note rep : noteReps) {
      outputs.add("");
      String output;
      String signature = rep.genSignature();
      if (signature.equals(seen)) {
        output = this.genCellOutput(beat, rep);
        this.updateOutputs(outputs, output);
      } else {
        sb.append(outputs.get(0));
        outputs = new ArrayList<String>();
        System.out.println(rep.genSignature());
        output = this.genCellOutput(beat, rep);
        this.updateOutputs(outputs, output);
        seen = signature;
      }
    }
    return sb.toString();
  }

  /**
   * Returns the output corresponding to the note and the beat;
   * if the note is beginning the method returns an "X",
   * if the note is continuing the method returns a "|",
   * if the note is finished the method returns a " ",
   * if the note is a representation the method returns a " ".
   *
   * @param beat the current beat being evaluated.
   * @param rep the current note being evaluated.
   * @return the string that is the data corresponding to the note and the beat.
   */
  public String genCellOutput(int beat, Note rep) {
    int start = rep.getStart();
    int end = rep.getEnd();
    if (start == beat && !(rep.getRep())) {
      return "  X  ";
    } else if (start < beat && end > beat && !(rep.getRep())) {
      return "  |  ";
    } else {
      return "     ";
    }
  }

  /**
   * Updates the list of outputs to prioritize strings indicating the
   * beginning of a note being played or the continuation of a note being played
   * over the absence of a note being played.
   *
   * @param outputs the list of output strings acquired.
   * @param output the output to be added.
   */
  public void updateOutputs(List<String> outputs, String output) {
    switch (output) {
      case "  X  ":
        outputs.add(0, output);
        break;
      case "  |  ":
        outputs.add(0, output);
        break;
      case "     ":
        outputs.add(output);
        break;
      default:
        throw new IllegalArgumentException("Invalid Output");
    }
  }

  @Override
  public void setCurrentBeat(int beat) { //  Does Nothing
  }

  @Override
  public void setBeats(int beats) {
    this.beats = beats;
  }

  @Override
  public void setNotes(List<Note> notes) {
    this.notes = new ArrayList<Note>(notes);
  }

  @Override
  public void setTempo(int tempo) {
    // Does nothing
  }

  @Override
  public void addKeyListener(KeyListener listener) {
    // Unused: AddKeyListener method beyond the scope of view implementation.
  }

  @Override
  public void addMouseListener(MouseListener listener) {
    // Unused: AddMouseListener method beyond the scope of view implementation.
  }

  @Override
  public void initializeView() {
    this.output(notes);
  }

  @Override
  public void refresh() {
    // Unused: Refresh method beyond the scope of view implementation.
  }

  @Override
  public void playback() {
    // Unused: Playback method beyond the scope of view implementation.
  }

}
