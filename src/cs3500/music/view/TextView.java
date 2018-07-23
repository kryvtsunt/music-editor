package cs3500.music.view;

import cs3500.music.model.Note;
import cs3500.music.model.Pitch;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.floor;
import static java.lang.Math.max;
import static java.lang.Math.log10;

/**
 * Test View is a View for the music creator project.
 * It implements the style guide for a
 */
public class TextView implements IView {

  private IViewModel data;
  private Appendable writeOut;

  public TextView() {
    data = null;
    writeOut = System.out;
  }

  public TextView(IViewModel vm, Appendable wr) {
    data = vm;
    writeOut = wr;
  }

  /**
   * This method is how the View gets the Data representation to display.
   *
   * @param vm The IViewModel of the data.
   */
  public void giveViewModel(IViewModel vm) {
    if (vm != null) {
      data = vm;
    } else {
      throw new IllegalArgumentException("Invalid View Model");
    }
  }

  @Override
  public void update() {
    // The basic model is the following:
    // - create a stringbuilder
    // - form the header
    // - iterate through each beat of the music
    // - get the collection of notes that start each beat
    // - print strinbuilder's string

    if (data == null) {
      throw new IllegalStateException("The ViewModel hasn't been initialized");
    }

    int length = data.getScoreLength();
    StringBuilder sb = new StringBuilder();
    IPitch low = data.getPitchLow();
    IPitch high = data.getPitchHigh();

    int cwidth = 5;

    int padding = max(1, (int) (1 + floor(log10(length))));

    // header
    for (int i = 0; i < padding; i++) {
      sb.append(" "); // pad space
    }

    // It doesn't seem right to have the view need to know what a pitch is, perhaps this
    // should be remodelled perhaps pitch should be made iterable
    for (int pitchInt = low.asNumber(); pitchInt <= high.asNumber(); pitchInt++) {
      String pitch = new Pitch(pitchInt).asString();
      if (pitch.length() == 2) {
        sb.append(String.format("  %s ", pitch));
      } else if (pitch.length() == 3) {
        sb.append(String.format(" %s ", pitch));
      } else if (pitch.length() == 4) {
        sb.append(String.format(" %s", pitch));
      } else {
        throw new IllegalArgumentException("Invalid pitch: " + pitch);
      }
    }
    // terminate header in a new line
    sb.append('\n');
    // end header

    //body

    List<NoteReader> noteReaderList = new LinkedList<NoteReader>();
    for (int pitchInt = low.asNumber(); pitchInt <= high.asNumber(); pitchInt++) {
      noteReaderList.add(new NoteReader());
    }

    for (int beat = 0; beat < length; beat++) {
      // Beat Number, need to add 1
      sb.append(String.format("%" + Integer.toString(padding) + "s", beat));

      Collection<INote> nts = data.getAllNotesAtLocation(beat);

      for (INote nn : nts) {
        int pitchIndex = nn.getPitch().asNumber() - low.asNumber();
        noteReaderList.get(pitchIndex).giveNoteDuration(nn.getDuration());
      }
      // Add the beats for this turn
      for (NoteReader ntrd : noteReaderList) {
        sb.append(ntrd.nextBeat());
      }
      // end line
      sb.append('\n');
    }

    try {
      writeOut.append(sb.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
