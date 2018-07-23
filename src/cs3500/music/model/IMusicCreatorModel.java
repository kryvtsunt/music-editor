package cs3500.music.model;

import cs3500.music.view.INote;
import cs3500.music.view.IViewModel;

/**
 * IMusicCreator extends the IViewModel.
 * This interface describes everything a Music Creator Model does.
 * Very basically, a Music model does the following:
 *
 * <p><ul>
 * <li> addNote: Adds the described note at the given pitch and location
 * <li> deleteNote: Delete a given note object in the Music Model
 * </ul>
 */
public interface IMusicCreatorModel extends IViewModel {

  /**
   * addNote method adds the note at the certain beat.
   *
   * @param n Note
   * @param beat int
   */
  public void addNote(INote n, int beat);

  /**
   * addNote method adds the pitch of the specific duration, instrument and volume to the certain
   * beat.
   *
   * @param pitch int
   * @param duration int
   * @param instrument int
   * @param volume int
   * @param beat int
   */
  public void addNote(int pitch, int duration, int instrument, int volume, int beat);

  /**
   * setTempo method sets the tempo of the music.
   *
   * @param tempo int
   */
  public void setTempo(int tempo);

  void setNoteLength(int beat, INote note, int length);


  /**
   * deleteNote method deltes the Note from the crtain beat.
   *
   * @param n Note
   * @param beat int
   */
  public void deleteNote(Note n, int beat);


}
