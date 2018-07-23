package cs3500.music.model;

/**
 * PitchValue enum represents all possible pitch values.
 */
public enum PitchValue {
  C(0, "C"),
  CSH(1, "C#"),
  D(2, "D"),
  DSH(3, "D#"),
  E(4, "E"),
  F(5, "F"),
  FSH(6, "F#"),
  G(7, "G"),
  GSH(8, "G#"),
  A(9, "A"),
  ASH(10, "A#"),
  B(11, "B");

  private int number;
  private String letter;

  /**
   * PitchValue Constructor.
   *
   * @param n [int]
   * @param letter [String]
   */
  PitchValue(int n, String letter) {
    this.number = n % 12;
    this.letter = letter;
  }

  /**
   * getNumber() method produces a number representation of the PitchValue.
   *
   * @return [int]
   */
  int getNumber() {
    return this.number;
  }

  /**
   * getLetter() method produces a letter representation of the PitchValue.
   *
   * @return [String]
   */
  public String getLetter() {
    return this.letter;
  }

}
