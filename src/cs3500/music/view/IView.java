package cs3500.music.view;

/**
 * IView represents the view of the program.
 */
public interface IView {


  /**
   * Signals to the view to display what it's given model is.
   */
  public void update();

  /**
   * Gives the View a model to work off of.
   *
   * @param model IViewModel
   */
  public void giveViewModel(IViewModel model);
}
