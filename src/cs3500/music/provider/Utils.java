package cs3500.music.provider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;


/**
 * Represents the Utils class containing factory wide methods.
 */
public class Utils {

  /**
   * Sorts given notes according to where each note falls
   * on traditional scale.
   */
  public static void sortNotes(List<Note> notes) {
    Collections.sort(notes, new NoteComparator());
  }

  /**
   * Returns a list of note representatives which include:
   * all notes in {@code this} notes,
   * all notes not included in {@code this} notes starting from the
   * "lowest" note (in terms of Pitch, Sharp, and Octave) to the
   * "highest" note (in terms of Pitch, Sharp, and Octave).
   *
   * @return a list of notes including note representatives.
   */
  public static List<Note> genNoteReps(List<Note> notes) {
    int idx = 0;
    List<Note> noteReps = new ArrayList<Note>();
    sortNotes(notes);
    for (int i = 0; i < notes.size() - 1; i++) {
      idx += 1;
      String primarySig = notes.get(i).genSignature();
      String secondarySig = notes.get(i + 1).genSignature();
      noteReps.add(notes.get(i));
      Note next = notes.get(i).nextNote();
      // nextNote should create a repr, probably
      while (!(primarySig.equals(secondarySig)
          || next.genSignature().equals(primarySig)
          || next.genSignature().equals(secondarySig))) {
        noteReps.add(next);
        next = next.nextNote();
      }
    }

    noteReps.add(notes.get(idx));
    return noteReps;
  }


  /**
   * Returns a list containing the signature forms of each note in the given list
   * without duplicates.
   *
   * @return a list of strings containing note signatures.
   */
  public static List<String> genNoteSignatures(List<Note> notes) {
    List<String> noteSignatures = new ArrayList<String>();
    sortNotes(notes);
    for (Note note : notes) {
      noteSignatures.add(note.genSignature());
    }
    noteSignatures = removeDuplicates(noteSignatures);
    return noteSignatures;
  }

  /**
   * Takes a list of note signatures and deletes all duplicate signatures in the list.
   *
   * @param noteSignatures the given list of strings to be altered.
   * @return the altered list of note signatures.
   */
  public static List<String> removeDuplicates(List<String> noteSignatures) {
    return new ArrayList<String>(new LinkedHashSet<String>(noteSignatures));
  }

}
