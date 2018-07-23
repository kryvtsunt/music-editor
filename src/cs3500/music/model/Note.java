package cs3500.music.model;

import cs3500.music.view.INote;
import cs3500.music.view.IPitch;
import java.util.ArrayList;

/**
 * A Note represents a Duration and a type (Played or non-played), sustained for a
 * number of beats.
 */
public class Note implements INote {

  private IPitch pitch;
  private int duration;
  private int instrument;
  private int volume;

  /**
   * Constructor for a Note object.
   *
   * @param pitch Pitch this corresponds to the JoePitch of the note.
   * @param duration int representing the Number of beats the note is sustained.
   */
  public Note(int pitch, int duration, int instrument, int volume) {
    this.pitch = new Pitch(pitch);
    this.duration = duration;
    this.instrument = instrument;
    this.volume = volume;
    String s;

  }

  public void setDuration( int beats) {
    this.duration = beats;
  }

  /**
   * The shorter constructor for a Pitch, sets Midi related fields to 0
   *
   * @param pitch Pitch that represents the pitch of the note.
   * @param duration int that represents the number of beats the note is played.
   */
  public Note(IPitch pitch, int duration) {
    this.pitch = pitch;
    this.duration = duration;
    this.instrument = 0;
    this.volume = 0;
  }

  public int getDuration() {
    return this.duration;
  }

  public IPitch getPitch() {
    return this.pitch;
  }

  public void setPitch(IPitch pitch) {
    this.pitch = pitch;
  }

  public int getChannel() {
    return this.instrument;
  }

  public int getVelocity() {
    return this.volume;
  }

  /**
   * Creates a Representation of this Note in TypeDuration format.
   *
   * @return String representing this note.
   */
  public String asString() {
    return String.format("%d %d %d %d",
        this.pitch.asNumber(), this.duration, this.instrument, this.volume);
  }
}
