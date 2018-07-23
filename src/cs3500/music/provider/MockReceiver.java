package cs3500.music.provider;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

/**
 * Represents a mock receiver.
 */
public class MockReceiver implements Receiver {

  StringBuilder log = new StringBuilder();

  /**
   * Recreates the data that the "real" receiver would obtain
   * through its send method.
   *
   * @param message the given message being sent to the receiver.
   * @param timeStamp the given timestamp associateed with said message.
   */
  @Override
  public void send(MidiMessage message, long timeStamp) {
    if (message instanceof ShortMessage) {
      ShortMessage smsg = (ShortMessage) message;
      log.append(smsg.getCommand()).append(" ")
          .append(smsg.getChannel()).append(" ")
          .append(smsg.getData1()).append(" ")
          .append(smsg.getData2()).append(" ")
          .append(timeStamp).append("\n");
    }
    System.out.print(log.toString());
  }

  @Override
  public void close() {
    // Unused: Close method not relevant to scope of Mocking Implementation.
  }
}
