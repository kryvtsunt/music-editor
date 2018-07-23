package cs3500.music.controller;

import cs3500.music.model.IMusicCreatorModel;
import cs3500.music.model.Note;
import cs3500.music.model.Pitch;
import cs3500.music.model.PitchValue;
import cs3500.music.util.Runnable2Args;
import cs3500.music.view.AddNote;
import cs3500.music.view.INote;
import cs3500.music.view.IPitch;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The representation of the controller for the combined view.
 */
public class CombinedController implements IController {

  private enum ControllerState {
    NullState,
    Paused,
    Playing,
    AddingNote,
    Practice
  }

  private String stateName(ControllerState cs) {
    switch (cs) {

      case NullState:
        return "";
      case Paused:
        return "Paused";
      case Playing:
        return "Playing";
      case AddingNote:
        return "Adding Note";
      case Practice:
        return "Practice Mode";
      default:
        return "";
    }
  }

  private IMusicCreatorModel model;
  private ICombinedView view;
  private ControllerState state;
  private ControllerState reqState;
  private int beat;

  // For Practie mode
  private Set<Character> keysPressed;
  private int octave;

  private AddNote noteBeingAdded;

  private Map rep;
  private int first;
  private int second;

  private Map end;
  private int frst;
  private int scnd;
  private int zer;
  private boolean repeat;

  List list;

  public void requestState(ControllerState cstate) {
    this.reqState = cstate;
  }

  public void handleStateChangeRequest() {
    if (this.reqState != ControllerState.NullState) {
      switch (this.state) {
        case NullState:
          // invalid
          break;
        case Paused: // From paused a state can change to anything
          this.state = reqState;
          break;
        case Playing:
          if (reqState == ControllerState.Paused) {
            this.state = reqState;
          }
          break;
        case AddingNote:
          if (reqState == ControllerState.Paused) {
            this.state = reqState;
          }
          break;
        case Practice:
          if (reqState == ControllerState.Paused) {
            this.state = reqState;
          }
          break;
        default:
          break;
      }
      this.view.setStateName(stateName(this.state));

      this.reqState = ControllerState.NullState;
    }
  }

  /**
   * Constructor for MusicControlle.
   *
   * @param model IMusicCreatorModel
   * @param view IView
   */
  public CombinedController(IMusicCreatorModel model, ICombinedView view) {
    this.model = model;
    this.view = view;
    this.state = ControllerState.Paused;
    this.reqState = ControllerState.NullState;
    this.beat = 0;
    this.noteBeingAdded = new AddNote();
    this.keysPressed = new HashSet<Character>();
    this.octave = 5;
    this.view.setPracticeOctave(this.octave);

    this.first = -1;
    this.second = -1;

    this.frst = -1;
    this.scnd = -1;
    this.zer = -1;

    this.repeat = false;

    this.list = new ArrayList<Integer>();

    this.rep = new HashMap(this.model.getMap());
    this.end = new HashMap(this.model.getMap2());

    this.configureKeyboardListener();
    this.configureButtonListener();
  }

  @Override
  public void run() {
    view.giveViewModel(model);
    this.view.update();
    while (true) {
      // this.view.display(this.model, beat, octave, controller state)

      if (this.beat >= this.model.getScoreLength() + 1) {
        this.state = ControllerState.Paused;
      }
      if (this.state == ControllerState.Paused) {
        this.view.pause();
      } else if (this.state == ControllerState.Playing) {
        if ((this.rep.containsKey(beat))&&(this.end.containsKey(beat)) && (!repeat)) {
          int b = beat;
          this.beat = (int) this.rep.get(b);
          this.rep.remove(b);
          this.view.pause();
          this.repeat = true;
        } else if (this.rep.containsKey(beat)) {
          int b = beat;
          this.beat = (int) this.rep.get(b);
          this.rep.remove(b);
          this.view.pause();
        } else if ((repeat) && (this.end.containsKey(beat))) {
          this.repeat = false;
          int b = beat;
          this.beat = (int) this.end.get(b);
          this.view.pause();
          if (this.end.containsKey(beat)) {
            this.end.remove(b);
            this.end.put(b, this.end.get(beat));
            this.end.remove(beat);
          }
        }
        this.view.play(beat);
        beat++;

      } else if (this.state == ControllerState.AddingNote) {
        beat++;
        model.setNoteLength(noteBeingAdded.beat, noteBeingAdded, beat - noteBeingAdded.beat);

        view.giveViewModel(model);
        view.update();

        view.moveToBeat(beat);
      } else if (this.state == ControllerState.Practice) {
        //System.out.println("1");
        view.moveToBeat(beat);

        Collection<INote> playingNotes = this.model.getAllNotesAtLocation(beat);
        int goodNotes = 0;
        int pressedKeys = 0;

        for (INote nn : playingNotes) {
          // we can skip it if the pitch is outside the right octave
          if ((nn.getPitch().asNumber() / 12) != this.octave) {
            goodNotes++;
          } else if (keysPressed.contains(this.charFromPitch(nn.getPitch()))) {
            goodNotes++;
            pressedKeys++;
          }
        }

        boolean allCorrect = (pressedKeys == this.keysPressed.size())
            && (goodNotes == playingNotes.size());

        if (!allCorrect) {
          this.view.pause();
        } else if (beat < this.model.getScoreLength()) {
          view.play(beat);
          beat++;
        }

      }

      handleStateChangeRequest();

      try {
        Thread.sleep(this.model.getTempo() / 1000);
      } catch (InterruptedException e1) {
        e1.printStackTrace();
      }
    }
  }


  /**
   * The method to configure Keyboard listener.
   */
  private void configureKeyboardListener() {
    KeyboardListener kbl = new KeyboardListener();
    System.out.println("Configuring KeyboardListeners");
    kbl.addKeyReleasedMapping(KeyEvent.VK_HOME, new OnHomeKey());
    kbl.addKeyReleasedMapping(KeyEvent.VK_END, new OnEndKey());
    kbl.addKeyReleasedMapping(KeyEvent.VK_LEFT, new OnLeftKey());
    kbl.addKeyReleasedMapping(KeyEvent.VK_RIGHT, new OnRightKey());
    kbl.addKeyReleasedMapping(KeyEvent.VK_UP, new OnUpKey());
    kbl.addKeyReleasedMapping(KeyEvent.VK_DOWN, new OnDownKey());
    kbl.addKeyReleasedMapping(KeyEvent.VK_PAGE_UP, new OnPageUp());
    kbl.addKeyReleasedMapping(KeyEvent.VK_PAGE_DOWN, new OnPageDown());

    // OCTAVE CONTROLS
    kbl.addKeyTypedMapping('1', new OnNumberKey(1));
    kbl.addKeyTypedMapping('2', new OnNumberKey(2));
    kbl.addKeyTypedMapping('3', new OnNumberKey(3));
    kbl.addKeyTypedMapping('4', new OnNumberKey(4));
    kbl.addKeyTypedMapping('5', new OnNumberKey(5));
    kbl.addKeyTypedMapping('6', new OnNumberKey(6));
    kbl.addKeyTypedMapping('7', new OnNumberKey(7));
    kbl.addKeyTypedMapping('8', new OnNumberKey(8));
    kbl.addKeyTypedMapping('9', new OnNumberKey(9));
    kbl.addKeyTypedMapping('0', new OnNumberKey(0));

    // Played Keys
    kbl.addKeyPressedMapping(KeyEvent.VK_Z, new OnPracticeKeyPressed('z'));
    kbl.addKeyPressedMapping(KeyEvent.VK_S, new OnPracticeKeyPressed('s'));
    kbl.addKeyPressedMapping(KeyEvent.VK_X, new OnPracticeKeyPressed('x'));
    kbl.addKeyPressedMapping(KeyEvent.VK_D, new OnPracticeKeyPressed('d'));
    kbl.addKeyPressedMapping(KeyEvent.VK_C, new OnPracticeKeyPressed('c'));
    kbl.addKeyPressedMapping(KeyEvent.VK_V, new OnPracticeKeyPressed('v'));
    kbl.addKeyPressedMapping(KeyEvent.VK_G, new OnPracticeKeyPressed('g'));
    kbl.addKeyPressedMapping(KeyEvent.VK_B, new OnPracticeKeyPressed('b'));
    kbl.addKeyPressedMapping(KeyEvent.VK_H, new OnPracticeKeyPressed('h'));
    kbl.addKeyPressedMapping(KeyEvent.VK_N, new OnPracticeKeyPressed('n'));
    kbl.addKeyPressedMapping(KeyEvent.VK_J, new OnPracticeKeyPressed('j'));
    kbl.addKeyPressedMapping(KeyEvent.VK_M, new OnPracticeKeyPressed('m'));


    // PRACTICE MODE / PAUSE
    kbl.addKeyTypedMapping('p', new OnPKey());

    // PLAY / PAUSE
    kbl.addKeyTypedMapping(' ', new OnSpace());
    kbl.addKeyTypedMapping('j', new OnJKey());
    kbl.addKeyTypedMapping('k', new OnKKey());

    kbl.addKeyTypedMapping('r', new OnRKey());
    kbl.addKeyTypedMapping('e', new OnEKey());
    kbl.addKeyTypedMapping('q', new OnQKey());

    this.view.addKeyListener(kbl);
  }


  private void configureButtonListener() {
    ButtonListener bl = new ButtonListener();
    System.out.println("Configuring Button Listener");
    bl.addPressedAction(MouseEvent.BUTTON1, new OnButton1Press());
    bl.addReleasedAction(MouseEvent.BUTTON1, new OnButton1Release());
    this.view.addMouseListener(bl);
  }


  // used for comparing pitches to characters pressed on the keyboard
  private char charFromPitch(IPitch pp) {
    char cc = '\0';
    switch (pp.asNumber() % 12) {
      case 0:
        cc = 'z';
        break;
      case 1:
        cc = 's';
        break;
      case 2:
        cc = 'x';
        break;
      case 3:
        cc = 'd';
        break;
      case 4:
        cc = 'c';
        break;
      case 5:
        cc = 'v';
        break;
      case 6:
        cc = 'g';
        break;
      case 7:
        cc = 'b';
        break;
      case 8:
        cc = 'h';
        break;
      case 9:
        cc = 'n';
        break;
      case 10:
        cc = 'j';
        break;
      case 11:
        cc = 'm';
        break;
    }
    return cc;
  }

  class OnButton1Press implements Runnable2Args {

    @Override
    public void run(int x, int y) {
      if (state == ControllerState.Paused && view.keyAtPosn(x, y)) {
        requestState(ControllerState.AddingNote);
        // get noteview.keyAtPosn(x, y))
        noteBeingAdded = new AddNote(new Note(view.getPitchAt(x, y), 1), beat);
        noteBeingAdded.setDuration(1);
        model.addNote(noteBeingAdded.note, noteBeingAdded.beat);
        view.giveViewModel(model);
        view.update();
      }
    }
  }

  class OnButton1Release implements Runnable2Args {

    @Override
    public void run(int x, int y) {
      if (state == ControllerState.AddingNote) {
        requestState(ControllerState.Paused);
      }
    }
  }


  class OnHomeKey implements Runnable {

    @Override
    public void run() {
      if (state == ControllerState.Paused) {
        beat = 0;
        view.moveToBeat(beat);
        rep = new HashMap(model.getMap());
        end = new HashMap(model.getMap2());
        repeat = false;
      }
    }
  }

  class OnEndKey implements Runnable {

    @Override
    public void run() {
      if (state == ControllerState.Paused) {
        beat = model.getScoreLength();
        view.moveToBeat(beat);
      }
    }
  }

  // PANNING VIEW
  class OnRightKey implements Runnable {

    @Override
    public void run() {
      if (state == ControllerState.Paused) {
        view.scrollLeft();
      }
    }
  }

  class OnLeftKey implements Runnable {

    @Override
    public void run() {
      if (state == ControllerState.Paused) {
        view.scrollRight();
      }
    }
  }

  class OnUpKey implements Runnable {

    @Override
    public void run() {
      view.scrollDown();
    }
  }

  class OnDownKey implements Runnable {

    @Override
    public void run() {
      view.scrollUp();
    }
  }

  // OCTAVE CONTROLS
  class OnNumberKey implements Runnable {

    private int num;

    OnNumberKey(int num) {
      this.num = num;
    }

    @Override
    public void run() {
      octave = num;
      view.scrollToOctave(octave);
      view.setPracticeOctave(octave);
    }
  }

  // PLAYING NOTES
  class OnPracticeKeyPressed implements Runnable {

    char cc;

    OnPracticeKeyPressed(char character) {
      this.cc = character;
    }

    @Override
    public void run() {
      keysPressed.add(this.cc);
    }
  }

  class OnPracticeKeyReleased implements Runnable {

    char cc;

    OnPracticeKeyReleased(char character) {
      this.cc = character;
    }

    @Override
    public void run() {
      keysPressed.remove(this.cc);
    }
  }


  class OnJKey implements Runnable {

    @Override
    public void run() {
      if (state == ControllerState.Paused) {
        if (beat > 0) {
          beat--;
        }
        view.moveToBeat(beat);
      }
    }
  }

  class OnKKey implements Runnable {

    @Override
    public void run() {
      if (state == ControllerState.Paused) {
        beat++;
        view.moveToBeat(beat);
      }
    }
  }

  /**
   * The class that represents an on Space key event.
   */
  class OnSpace implements Runnable {

    @Override
    public void run() {
      if (state == ControllerState.Paused) {
        requestState(ControllerState.Playing);
      } else if (state == ControllerState.Playing) {
        requestState(ControllerState.Paused);
      }
    }
  }

  /**
   * The class toggles the mode between paused and practice mode.
   */
  class OnPKey implements Runnable {

    @Override
    public void run() {
      if (state == ControllerState.Paused) {
        requestState(ControllerState.Practice);
      } else if (state == ControllerState.Practice) {
        requestState(ControllerState.Paused);
      }
    }
  }

  // Page Up and Page Down change

  /**
   * The class that represents an on Space key event.
   */
  class OnPageUp implements Runnable {

    @Override
    public void run() {
      if (state == ControllerState.Paused) {
        int currentTempo = model.getTempo();
        int tempoBPM = (1000 * 1000 * 60) / currentTempo;
        if (tempoBPM < 1000) {
          tempoBPM += 5;
        }
        model.setTempo((1000 * 1000 * 60) / tempoBPM);
        view.update();
      }
    }
  }

  /**
   * The class that represents an on Space key event.
   */
  class OnPageDown implements Runnable {

    @Override
    public void run() {
      if (state == ControllerState.Paused) {
        int currentTempo = model.getTempo();
        int tempoBPM = (1000 * 1000 * 60) / currentTempo;
        if (tempoBPM > 5) {
          tempoBPM -= 5;
        }
        model.setTempo((1000 * 1000 * 60) / tempoBPM);
        view.update();
      }
    }
  }

  class OnRKey implements Runnable {

    @Override
    public void run() {
      if ((state == ControllerState.Paused)&&(!list.contains(beat))) {
        if (first == -1) {
          first = beat;
          return;
        }
        int a = Math.max(first, beat);
        int b = Math.min(first, beat);
        for (int i = b+1; i < a; i++){
          if (list.contains(i)){return; }
        }
        if (b == a) {
          model.getMap().put(a, 0);
          for (int i = 0; i < a; i++) {
            if (!list.contains(i)) {
              list.add(i);
            }
          }
        } else {
          model.getMap().put(a, b);
          for (int i = b+1; i < a; i++) {
            if (!list.contains(i)) {
              list.add(i);
            }
          }
        }
        first = -1;
        view.update();
      }
    }
  }


  class OnEKey implements Runnable {

    @Override
    public void run() {
      if ((state == ControllerState.Paused)&&(!list.contains(beat))) {
        if (zer == -1) {
          zer = beat;
          return;
        }
        if (frst == -1) {
          frst = beat;
          return;
        }
        if ((frst != -1)&&(scnd != -1) && (beat > frst)) {
          for (int i = scnd+1; i < beat; i++){
            if (list.contains(i)){return; }
          }
          if (!model.getMap().keySet().contains(first)){
            model.getMap2().put(frst, scnd);
            for (int i = frst+1; i < scnd; i++) {
              if (!list.contains(i)) {
                list.add(i);
              }
            }
          }
          frst = scnd;
          scnd = beat;
          model.getMap().put(frst, zer);
          for (int i = zer+1; i < frst; i++) {
            if (!list.contains(i)) {
              list.add(i);
            }
          }
          model.getMap2().put(frst, scnd);
          for (int i = frst+1; i < scnd; i++) {
            if (!list.contains(i)) {
              list.add(i);
            }
          }
          view.update();
          return;
        }
        if (beat > frst) {
          for (int i = frst+1; i < beat; i++){
            if (list.contains(i)){return; }
          }
          scnd = beat;
        }
      }
    }
  }
  class OnQKey implements Runnable {

    @Override
    public void run() {
      zer = -1;
      frst = -1;
      first = -1;
      scnd = -1;
      }
    }


}
