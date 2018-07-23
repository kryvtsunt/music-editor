package cs3500.music;

//import cs3500.music.adapters.CompositeViewAdapter;
//import cs3500.music.adapters.GuiViewAdapter;
//import cs3500.music.adapters.MidiViewAdapter;
//import cs3500.music.adapters.TextViewAdapter;
import cs3500.music.controller.IController;
import cs3500.music.controller.MusicController;
import cs3500.music.controller.EnhancedController;
import cs3500.music.controller.CombinedController;
import cs3500.music.controller.ICombinedView;
import cs3500.music.model.IMusicCreatorModel;
import cs3500.music.util.CompositionBuilder;
import cs3500.music.util.ModelCompBuilder;
import cs3500.music.util.MusicReader;
import cs3500.music.view.gui.IGuiView;
import cs3500.music.view.TextView;
import cs3500.music.view.Combined;
import cs3500.music.view.gui.GuiView;
import cs3500.music.view.IView;
import cs3500.music.view.midi.MidiViewImpl;


import java.io.FileNotFoundException;
import java.io.FileReader;


/**
 * MusicEditor is the main method in the program
 * It accepts two arguments, the first is the path to the music text file.
 * The second argument is the type of display, text, visual, audio, or test audio.
 */
public class MusicEditor {

  /**
   * This method is the main thread of the function.
   *
   * @param args the path of the file to read, and the mode of view.
   */
  public static void main(String[] args) throws InterruptedException {
    IMusicCreatorModel model;
    IController mc;
    StringBuilder sb = new StringBuilder();

    if (args.length != 2) {
      System.out.println("Use: <Path to music file> <text|gui|visual>");
    }

    // Make the model
    CompositionBuilder<IMusicCreatorModel> cb = new ModelCompBuilder();
    FileReader music = null;
    try {
      music = new FileReader(args[0]);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    if (music == null) {
      throw new IllegalArgumentException("Invalid argument args[0]: " + args[1]);
    }

    model = MusicReader.parseFile(music, cb);

    // Make the View
    if (args[1].equals("text")) {
      IView view = new TextView();
      mc = new MusicController(model, view);
    } else if (args[1].equals("audio")) {
      IView view = new MidiViewImpl();
      mc = new MusicController(model, view);
    } else if (args[1].equals("test-audio")) {
      IView view = new MidiViewImpl(sb);
      mc = new MusicController(model, view);
    } else if (args[1].equals("visual+")) {
      IGuiView view = new GuiView(model);
      mc = new EnhancedController(model, view);
    } else if (args[1].equals("combined")) {
      ICombinedView view = new Combined(model);
      mc = new CombinedController(model, view);
    }
//    else if (args[1].equals("text2")) {
//      IView view = new TextViewAdapter();
//      mc = new MusicController(model, view);
//    }
//    else if (args[1].equals("audio2")) {
//      IView view = new MidiViewAdapter();
//      mc = new MusicController(model, view);
//    }
//
//    else if (args[1].equals("visual+2")) {
//      IGuiView view = new GuiViewAdapter();
//      mc = new EnhancedController(model, view);
//    }
//    else if (args[1].equals("combined2")) {
//      ICombinedView view = new CompositeViewAdapter();
//      mc = new CombinedController(model, view);
//    }
    else {
      throw new IllegalArgumentException("Invalid input");
    }
    mc.run();

    System.out.println(sb.toString());

  }
}
