package cs3500.music.provider;

import java.util.Comparator;

/**
 * Represents a Note Comparator.
 */
public class NoteComparator implements Comparator<Note> {

  /**
   * Returns an integer (either negative, 0, or positive)
   * corresponding to whether the first note is before, equivalent, or after
   * the second note.
   *
   * @param note1 the given note that needs comparing.
   * @param note2 the given note to be compared to.
   * @return integer corresponding to comparison value.
   */
  @Override
  public int compare(Note note1, Note note2) {
    return note1.compareTo(note2);
  }
}
