package cs3500.music.model;

import cs3500.music.view.INote;
import cs3500.music.view.IPitch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * MusicModel is an implementation of the IMusicCreatorModel.
 * It represents all the data for representing music.
 */
public class MusicModel implements IMusicCreatorModel {

  private List<Collection<INote>> notes;
  private IPitch low;
  private IPitch high;
  private int tempo;
  private Map<Integer, Integer> map;
  private Map<Integer, Integer> map2;

  /**
   * Constructor for the MusicModel. Takes no arguments.
   */
  public MusicModel() {
    notes = new ArrayList<Collection<INote>>();
    tempo = 2000000;
    this.map = new HashMap<>();
    this.map2 = new HashMap<>();

  }

  @Override
  public Collection<INote> getAllNotesAtLocation(int location) {
    return this.notes.get(location);
  }

  @Override
  public int getScoreLength() {
    return this.notes.size();
  }

  @Override
  public IPitch getPitchLow() {
    return low;
  }

  @Override
  public IPitch getPitchHigh() {
    return high;
  }

  @Override
  public int getTempo() {
    return this.tempo;
  }

  @Override
  public Map getMap() {
    return this.map;

  }

  @Override
  public Map getMap2() {
    return this.map2;
  }


  @Override
  public int maxKey(int n) {
    int max = -1;
    for (int i: map.keySet()){
      if ((i > max)&&(i < n)) { max = i; }
    }
    return max;
  }

  @Override
  public void addNote(INote n, int beat) {
    if (this.high == null) {
      this.high = n.getPitch();
    }
    if (this.low == null) {
      this.low = n.getPitch();
    }

    if (n.getPitch().asNumber() > high.asNumber()) {
      this.high = n.getPitch();
    } else if (n.getPitch().asNumber() < low.asNumber()) {
      this.low = n.getPitch();
    }

    if (beat >= this.notes.size()) {
      for (int ii = this.notes.size(); ii < beat + n.getDuration(); ii++) {
        this.notes.add(new HashSet<INote>());
      }
    }

    this.notes.get(beat).add(n);
  }

  @Override
  public void addNote(int duration, int pitch, int instrument, int volume, int beat) {
    Note nn = new Note(duration, pitch, instrument, volume);
    this.addNote(nn, beat);
  }

  @Override
  public void setTempo(int tempo) {
    this.tempo = tempo;
  }

  @Override
  public void setNoteLength(int beat, INote note, int length) {
    System.out.println("1 " + Integer.toString(beat) + " " + note.toString());

    for (INote nn: this.notes.get(beat)) {
      if (nn.getPitch().asNumber() == note.getPitch().asNumber()) {
        nn.setDuration(length);
      }
    }
  }


  @Override
  public void deleteNote(Note n, int beat) {
    if ((beat >= this.notes.size()) || (!notes.get(beat).contains(n))) {
      throw new IllegalArgumentException("There is no such note.");
    } else {
      notes.get(beat).remove(n);
    }
  }
}
