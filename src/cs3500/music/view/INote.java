package cs3500.music.view;

public interface INote {

  /**
   * Get the length of the note
   *
   * @return The duration, in number of beats of this INote
   */
  int getDuration();

  IPitch getPitch();

  int getChannel();

  int getVelocity();

  void setPitch(IPitch pitch);
  void setDuration(int beats);
}
