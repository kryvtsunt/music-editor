package cs3500.music.view.midi;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

/**
 * A Mock Reciever shares an interface with a Midi Receiver, but will only log each sent message
 * Instead of playing music.
 */
public class MockReceiver implements Receiver {

  private StringBuilder sb;

  public MockReceiver(StringBuilder sb) {
    this.sb = sb;
  }

  /**
   * Send a message to the Mock Receiver, which will only be logged internally.
   *
   * @param message The Message that is to be logged.
   * @param timeStamp The time stamp of the given message.
   */
  @Override
  public void send(MidiMessage message, long timeStamp) {
    sb.append(String.format("Timestamp %d: Message: %s\n", timeStamp, message.getMessage()));
  }

  @Override
  public void close() {
    // Does nothing
  }
}
