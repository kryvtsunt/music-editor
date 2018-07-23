package cs3500.music.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

/**
 * The implementation of the KeyListener.
 */
public class KeyboardListener implements KeyListener {

  // There's going to be some sort of map between keys and method calls
  private Map<Integer, Runnable> releasedMap;
  private Map<Character, Runnable> typedMap;
  private Map<Integer, Runnable> pressedMap;

  /**
   * Constructor for the KeyboardListener.
   */
  public KeyboardListener() {
    this.releasedMap = new HashMap<Integer, Runnable>();
    this.typedMap = new HashMap<Character, Runnable>();
    this.pressedMap = new HashMap<Integer, Runnable>();
  }

  /**
   * method that adds key typed mapping.
   *
   * @param keyCode [Character]
   * @param callback [Runnable]
   */
  public void addKeyTypedMapping(Character keyCode, Runnable callback) {
    this.typedMap.put(keyCode, callback);
  }

  @Override
  public void keyTyped(KeyEvent e) {
    if (typedMap.containsKey(e.getKeyChar())) {
      this.typedMap.get(e.getKeyChar()).run();
    }
  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (pressedMap.containsKey(e.getKeyCode())) {
      this.pressedMap.get(e.getKeyCode()).run();
    }
  }

  /**
   * The method that adds the key mapping.
   */
  public void addKeyPressedMapping(Integer keyCode, Runnable callback) {
    this.pressedMap.put(keyCode, callback);
  }

  @Override
  public void keyReleased(KeyEvent e) {
    //System.out.print("Keyboard Event: ");
    if (releasedMap.containsKey(e.getKeyCode())) {
      this.releasedMap.get(e.getKeyCode()).run();
    }
  }

  /**
   * The method that adds the key mapping.
   */
  public void addKeyReleasedMapping(Integer keyCode, Runnable callback) {
    this.releasedMap.put(keyCode, callback);
  }
}
