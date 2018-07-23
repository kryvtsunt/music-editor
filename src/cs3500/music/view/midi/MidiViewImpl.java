package cs3500.music.view.midi;

import cs3500.music.view.INote;
import cs3500.music.view.IViewModel;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.InvalidMidiDataException;
import java.util.Collection;
import javax.sound.midi.Track;

/**
 * MIDIViewImpl is a MIDI implementation of the IView, plays the audio track
 * specified by the given model.
 */
public class MidiViewImpl implements PlayableView {

  private Synthesizer synth;
  private Receiver receiver;
  private IViewModel model;
  private Sequencer sequencer;
  private Sequence sequence;

  /**
   * Constructor for the MidiViewImpl.
   */
  public MidiViewImpl() {

    try {
      this.synth = MidiSystem.getSynthesizer();
      this.receiver = synth.getReceiver();
      this.sequencer = MidiSystem.getSequencer();
      this.synth.open();
      this.sequencer.open();
      this.sequence = new Sequence(Sequence.PPQ, 1);
    } catch (MidiUnavailableException e) {
      e.printStackTrace();
    } catch (InvalidMidiDataException e) {
      e.printStackTrace();
    }
  }

  /**
   * This constructor  creates a mock device and receiver that accept the given StringBuilder.
   *
   * @param sb StringBuilder to be used as a console log
   */
  public MidiViewImpl(StringBuilder sb) {
    try {
      this.synth = new MockMidiDevice(sb);
      this.receiver = synth.getReceiver();
      this.sequencer = MidiSystem.getSequencer();
      this.sequencer.open();
      this.sequence = new Sequence(Sequence.PPQ, 1);
      this.sequencer.setTempoInMPQ(this.model.getTempo());
      this.sequencer.stop();
    } catch (MidiUnavailableException e) {
      e.printStackTrace();
    } catch (InvalidMidiDataException e) {
      e.printStackTrace();
    }
  }


  @Override
  public void update() {
    if (model == null) {
      throw new IllegalStateException("Model not initialized");
    }
    int length = model.getScoreLength();
    Track track = this.sequence.createTrack();
    for (int beat = 0; beat < length; beat++) {
      Collection<INote> notes = model.getAllNotesAtLocation(beat);
      for (INote nn : notes) {
        MidiEvent start = null;
        MidiEvent stop = null;
        try {
          start = new MidiEvent(new ShortMessage(ShortMessage.NOTE_ON, nn.getChannel(),
              nn.getPitch().asNumber(), nn.getVelocity()), beat);
          stop = new MidiEvent(new ShortMessage(ShortMessage.NOTE_OFF, nn.getChannel(),
              nn.getPitch().asNumber(), nn.getVelocity()), beat + nn.getDuration());
        } catch (InvalidMidiDataException e) {
          e.printStackTrace();
        }
        track.add(start);
        track.add(stop);
      }
    }
    try {
      this.sequencer.setSequence(this.sequence);
    } catch (InvalidMidiDataException e) {
      e.printStackTrace();
    }


  }

  @Override
  public void giveViewModel(IViewModel model) {
    this.model = model;
  }


  @Override
  public void pause() {
    if (this.sequencer.isRunning()) {
      this.sequencer.stop();
    }
  }

  @Override
  public void play(int beat) {
    if (!this.sequencer.isRunning()) {
      this.sequencer.setTickPosition(beat);
      this.sequencer.setTempoInMPQ(this.model.getTempo());
      this.sequencer.start();
      this.sequencer.setTempoInMPQ(this.model.getTempo());
    }
  }
}
