package cs3500.music.controller;

import cs3500.music.model.IMusicCreatorModel;
import cs3500.music.view.IView;


/**
 * MusicController - a class that is responsible for combining a model and a view to produce an
 * output.
 */
public class MusicController implements IController {

  private IMusicCreatorModel model;
  private IView view;

  /**
   * Constructor for MusicControlle.
   *
   * @param model IMusicCreatorModel
   * @param view IView
   */
  public MusicController(IMusicCreatorModel model, IView view) {
    this.model = model;
    this.view = view;
  }

  /**
   * The method that gives a model to the view to display it.
   */
  @Override
  public void run() {
    this.view.giveViewModel(model);
    this.view.update();
  }
}
