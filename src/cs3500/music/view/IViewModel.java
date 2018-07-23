package cs3500.music.view;

import cs3500.music.model.Note;
import cs3500.music.model.Pitch;

import java.util.Collection;
import java.util.Map;

/**
 * IViewModel is an interface that represents view only model.
 */
public interface IViewModel {

  /**
   * getAllNotesAtLocation is a method that gets all notes at the given beat.
   *
   * @param location int
   * @return Colleaction of Notes at the given beat.
   */
  public Collection<INote> getAllNotesAtLocation(int location);

  /**
   * getScoreLength is a method that gets the highest beat.
   *
   * @return int
   */
  public int getScoreLength();

  /**
   * getPitchLow is a method that gets the lowest pitch.
   *
   * @return Pitch
   */
  public IPitch getPitchLow();

  /**
   * getPitchHigh is a method that gets the highest pitch.
   *
   * @return Pitch
   */
  public IPitch getPitchHigh();

  /**
   * getTempo is a method that gets the music tempo.
   *
   * @return int
   */
  public int getTempo();

  public Map getMap();

  public Map getMap2();

  public int maxKey(int n);


}
