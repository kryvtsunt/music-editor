package cs3500.music.view;

/**
 * a class that represents a note reader to use in the text view.
 */
public class NoteReader {

  private boolean hasNote;
  private int beatsLeft;
  private boolean firstBeat;

  /**
   * Constructor for the NoteReader.
   */
  public NoteReader() {
    this.hasNote = false;
    this.firstBeat = false;
    this.beatsLeft = 0;
  }

  /**
   * nextBeat produces a String value of the pitch at the next beat.
   *
   * @return String
   */
  public String nextBeat() {
    String retString;

    if (hasNote) {
      if (firstBeat) {
        firstBeat = false;
        retString = "  X  ";
      } else {
        retString = "  |  ";
      }

      beatsLeft -= 1;
      if (beatsLeft == 0) {
        hasNote = false;
      }
    } else {
      retString = "     ";
    }

    return retString;
  }

  /**
   * giveNoteDuration gives duration to the note, avoiding overlaping.
   */
  public void giveNoteDuration(int noteDuration) {
    // There shouldn't be overlapping notes
    if (!hasNote) {
      hasNote = true;
      firstBeat = true;
      beatsLeft = noteDuration;
    } else {
      throw new IllegalStateException("You can't add a note to an occupied Note Reader");
    }
  }
}
