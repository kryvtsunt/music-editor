package cs3500.music.view.midi;


import cs3500.music.view.IView;

/**
 * The playable implementation of the IView.
 */
public interface PlayableView extends IView {


  void pause();

  void play(int beat);


}
