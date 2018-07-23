package cs3500.music.view;

import cs3500.music.model.Note;
import cs3500.music.model.Pitch;

/**
 * The class to add a note.
 */
public class AddNote implements INote {

  public INote note;
  public int beat;

  public AddNote() {
    note = null;
    beat = 0;
  }

  public AddNote(INote note, int beat) {
    this.note = note;
    this.beat = beat;
  }

  @Override
  public int getDuration() {
    return 1;
  }

  @Override
  public IPitch getPitch() {
    if (this.note != null) {
      return this.note.getPitch();
    }
    else {
      return null;
    }
  }

  @Override
  public int getChannel() {
    return 1;
  }

  @Override
  public int getVelocity() {
    return 100;
  }

  @Override
  public void setPitch(IPitch pitch) {
    if (this.note != null) {
      this.note.setPitch(pitch);
    }
    else {
      this.note = new Note(pitch, 1);
    }
  }

  @Override
  public void setDuration(int beats) {
    this.note.setDuration(beats);
  }
}
