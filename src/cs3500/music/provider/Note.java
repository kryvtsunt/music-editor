package cs3500.music.provider;

public interface Note extends Comparable<Note> {

  /**
   * Returns the pitch of the note corresponding to {@code Pitch}.
   *
   * @return the pitch of the note.
   */
  Pitch getPitch();

  /**
   * Returns if the note is sharp or not.
   *
   * @return whether or not the note is sharp.
   */
  boolean getSharp();

  /**
   * Returns the octave of the note from 0-10 inclusive.
   *
   * @return the octave of the note.
   */
  int getOctave();

  /**
   * Returns if the note is merely a representation:
   * a note is a representation if it is a "placeholder" meaning
   * the note isn't played.
   *
   * @return whether or not the note is a representation.
   */
  boolean getRep();

  /**
   * Returns the integer corresponding to the beat that the note begins at.
   *
   * @return the starting beat.
   */
  int getStart();

  /**
   * Returns the integer corresponding to the beat that the note ends at.
   *
   * @return the ending beat.
   */
  int getEnd();

  /**
   * Returns the integer corresponding to the volume at which the note is playing at.
   *
   * @return the volume.
   */
  int getVolume();

  /**
   * Returns the note as a string consisting of:
   * the pitch of the note,
   * the sharp of the note,
   * the octave of the note.
   *
   * @return the note in string form.
   */
  String genSignature();

  /**
   * Returns the integer (from 0-127) corresponding to
   * this note's key on a scale.
   *
   * @return the numerical representation of this note.
   */
  int numericalPitch();

  /**
   * Returns the next note on the scale meaning:
   * if {@code this} not sharp, the next note will be sharp (unless
   * {@code this} note corresponds to an "E" note or "B" note) otherwise,
   * the next note will be the next Pitch (in order of A, B, C, D, E, F, G
   * wrapped around) and the next Octave (0-10 inclusive).
   *
   * @return the next note.
   */
  Note nextNote();

  /**
   * Returns an integer (either negative, 0, or positive) that
   * corresponds to whether the given note is before, equivalent, or
   * after this note.
   *
   * @param note the note to be compared to.
   * @return the position of the note compared to another note.
   */
  int compareTo(Note note);

}
