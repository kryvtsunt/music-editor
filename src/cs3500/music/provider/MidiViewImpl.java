package cs3500.music.provider;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.Synthesizer;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Transmitter;
import javax.sound.midi.Receiver;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.Track;

/**
 * A skeleton for MIDI playback.
 */
public class MidiViewImpl implements cs3500.music.provider.IView {

  private int currentBeat;
  private int beats;
  private int tempo;
  private List<Note> notes;
  private Sequencer sequencer;
  private Transmitter transmitter;
  private Synthesizer synthesizer;
  private Receiver receiver;

  /**
   * Constructs a {@code MidiViewImpl} object using the dummy Mock Synthesizer.
   *
   * @param mock the mock midi device to be used.
   */
  public MidiViewImpl(MockMidiDevice mock) {
    try {
      this.beats = 0;
      this.currentBeat = 0;
      this.sequencer = MidiSystem.getSequencer();
      sequencer.open();
      transmitter = sequencer.getTransmitter();
      synthesizer = mock;
      synthesizer.open();
      receiver = mock.getReceiver();
      transmitter.setReceiver(receiver);
    } catch (MidiUnavailableException m) {
      m.printStackTrace();
    }
  }

  /**
   * Constructs a {@code MidiViewImpl} object using presets.
   */
  public MidiViewImpl() {
    try {
      this.beats = 0;
      this.currentBeat = 0;
      this.sequencer = MidiSystem.getSequencer();
      sequencer.open();
    } catch (MidiUnavailableException e) {
      e.printStackTrace();
    }
  }

  /**
   * Relevant classes and methods from the javax.sound.midi library:
   * <ul>
   *  <li>{@link MidiSystem#getSynthesizer()}</li>
   *  <li>{@link Synthesizer}
   *    <ul>
   *      <li>{@link Synthesizer#open()}</li>
   *      <li>{@link Synthesizer#getReceiver()}</li>
   *      <li>{@link Synthesizer#getChannels()}</li>
   *    </ul>
   *  </li>
   *  <li>{@link Receiver}
   *    <ul>
   *      <li>{@link Receiver#send(MidiMessage, long)}</li>
   *      <li>{@link Receiver#close()}</li>
   *    </ul>
   *  </li>
   *  <li>{@link MidiMessage}</li>
   *  <li>{@link ShortMessage}</li>
   *  <li>{@link MidiChannel}
   *    <ul>
   *      <li>{@link MidiChannel#getProgram()}</li>
   *      <li>{@link MidiChannel#programChange(int)}</li>
   *    </ul>
   *  </li>
   * </ul>
   * @see <a href="https://en.wikipedia.org/wiki/General_MIDI">
   *   https://en.wikipedia.org/weiki/General_MIDI
   *   </a>
   */

  /**
   * Creates the sequence to be used.
   *
   * @return a sequence for where the notes will be stored.
   */
  public Sequence build() {
    Sequence sequence;
    try {
      sequence = new Sequence(Sequence.PPQ, 1);
      addNotesToSequence(sequence);
    } catch (InvalidMidiDataException m) {
      m.printStackTrace();
      throw new IllegalArgumentException("Oh well");
    }
    return sequence;
  }

  /**
   * Adds the notes from the composition to the sequence.
   *
   * @param sequence the sequence to be used.
   * @throws InvalidMidiDataException if something goes wrong.
   */
  public void addNotesToSequence(Sequence sequence) throws InvalidMidiDataException {
    Track primary = sequence.createTrack();
    for (Note note : notes) {
      ShortMessage noteOn = new ShortMessage(ShortMessage.NOTE_ON, 0,
          note.numericalPitch(), note.getVolume());
      ShortMessage noteOff = new ShortMessage(ShortMessage.NOTE_OFF, 0,
          note.numericalPitch(), note.getVolume());

      MidiEvent noteStart = new MidiEvent(noteOn, note.getStart());
      MidiEvent noteEnd = new MidiEvent(noteOff, note.getEnd());

      primary.add(noteStart);
      primary.add(noteEnd);
    }
  }

  /**
   * Connects the sequencer to the synthesizer,
   * through the use of a transmitter and a receiver.
   */
  public void setConnection() {
    try {
      transmitter = sequencer.getTransmitter();
      synthesizer = MidiSystem.getSynthesizer();
      synthesizer.open();
      receiver = synthesizer.getReceiver();
      transmitter.setReceiver(receiver);
    } catch (MidiUnavailableException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void setCurrentBeat(int beat) {
    this.sequencer.setTickPosition(beat);
  }

  @Override
  public void setBeats(int beats) {
    this.beats = beats;
  }

  @Override
  public void setNotes(List<Note> notes) {
    this.notes = new ArrayList<Note>(notes);
  }

  @Override
  public void setTempo(int tempo) {
    this.tempo = tempo;
  }

  @Override
  public void addKeyListener(KeyListener listener) {
    // this.addKeyListener(listener);
    // Needs further testing
  }

  @Override
  public void addMouseListener(MouseListener listener) {
    // Unused currently.
  }

  @Override
  public void initializeView() {
    try {
      sequencer.setTempoInMPQ(tempo);
      sequencer.setSequence(this.build());
      setConnection();
      sequencer.setTempoInMPQ(tempo);
      sequencer.stop();
    } catch (InvalidMidiDataException m) {
      m.printStackTrace();
    }
  }

  @Override
  public void refresh() {
    sequencer.start();
    sequencer.setTempoInMPQ(tempo);
  }

  @Override
  public void playback() {
    if (sequencer.isRunning()) {
      sequencer.stop();
    }
  }
}
