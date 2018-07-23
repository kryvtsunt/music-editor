package cs3500.music.controller;

import cs3500.music.model.IMusicCreatorModel;
import cs3500.music.view.gui.IGuiView;

import java.awt.event.KeyEvent;

public class EnhancedController extends MusicController implements IController {

  private IMusicCreatorModel model;
  private IGuiView view;
  private int beat;

  /**
   * Constructor for MusicControlle.
   *
   * @param model IMusicCreatorModel
   * @param view IView
   */
  public EnhancedController(IMusicCreatorModel model, IGuiView view) {
    super(model, view);
    this.model = model;
    this.view = view;
    this.beat = -1;
    this.configureKeyboardListener();
    this.configureButtonListener();
  }

  private void configureKeyboardListener() {
    KeyboardListener kbl = new KeyboardListener();

    System.out.println("Configuring KeyboardListeners");

    kbl.addKeyPressedMapping(KeyEvent.VK_HOME, new OnHomeKey());
    kbl.addKeyPressedMapping(KeyEvent.VK_END, new OnEndKey());
    kbl.addKeyPressedMapping(KeyEvent.VK_LEFT, new OnLeftKey());
    kbl.addKeyPressedMapping(KeyEvent.VK_RIGHT, new OnRightKey());
    kbl.addKeyPressedMapping(KeyEvent.VK_UP, new OnUpKey());
    kbl.addKeyPressedMapping(KeyEvent.VK_DOWN, new OnDownKey());

    kbl.addKeyTypedMapping('j', new OnJKey());
    kbl.addKeyTypedMapping('k', new OnKKey());

    this.view.addKeyListener(kbl);
  }

  private void configureButtonListener() {
    ButtonListener bl = new ButtonListener();
    System.out.println("Configuring Button Listener");
//    bl.addClickedAction(MouseEvent.BUTTON1, new OnButton1Click());
    this.view.addMouseListener(bl);
  }


//  class OnButton1Click implements Runnable2Args {
//
//    @Override
//    public void run(int x, int y) {
//      if (view.keyAtPosn(x, y)) {
//        AddNote an = view.getAddNote(x, y);
//        System.out.println("a");
//        model.addNote(new Note(an.getPitch(), 1), an.beat);
//        view.update();
//        beat++;
//        view.moveToBeat(beat);
//      }
//    }
//  }


  class OnHomeKey implements Runnable {

    @Override
    public void run() {
      beat = 0;
      view.moveToBeat(beat);

    }
  }

  class OnEndKey implements Runnable {

    @Override
    public void run() {

      beat = model.getScoreLength();
      view.moveToBeat(beat);

    }
  }

  class OnRightKey implements Runnable {

    @Override
    public void run() {
      System.out.println(">");
      view.scrollLeft();
    }
  }

  class OnLeftKey implements Runnable {

    @Override
    public void run() {
      System.out.println("<");
      view.scrollRight();
    }
  }

  class OnUpKey implements Runnable {

    @Override
    public void run() {
      System.out.println("^");
      view.scrollDown();
    }
  }

  class OnDownKey implements Runnable {

    @Override
    public void run() {
      System.out.println("v");
      view.scrollUp();
    }
  }


  class OnJKey implements Runnable {

    @Override
    public void run() {

      System.out.println("J");
      beat--;
      view.moveToBeat(beat);

    }
  }

  class OnKKey implements Runnable {

    @Override
    public void run() {
      System.out.println("K");
        beat++;
        view.moveToBeat(beat);
    }
  }

}
