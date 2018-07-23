package cs3500.music.model;


import cs3500.music.view.IPitch;

/**
 * class Pitch represents a single Pitch.
 */
public class Pitch implements IPitch {

  private PitchValue value;
  private int octave;
  // symbol that represents this pitch
  private char symbol = 'X';

  /**
   * Pitch Constructor .
   *
   * @param value [PitchValue] value of the pitch
   * @param octave [int] number of the octave of the pitch
   */
  public Pitch(PitchValue value, int octave) throws IllegalArgumentException {
    if ((octave < 0) || (octave > 7)) {
      throw new IllegalArgumentException("Illegal argument");
    }
    this.value = value;
    this.octave = octave;
  }

  /**
   * Pitch Constructor .
   *
   * @param n [int] number of the pitch
   */
  public Pitch(int n) throws IllegalArgumentException {
    if ((n < 0) || (n > 200)) {
      throw new IllegalArgumentException("Illegal argument");
    }
    this.value = getPitchValue(n);
    this.octave = n / 12;
  }

  /**
   * Pitch Constructor .
   *
   * @param s [String] string representation of the pitch.
   */
  public Pitch(String s) throws IllegalArgumentException {
    if ((s.length() == 2) && (s.substring(0, 1).matches("C||D||E||F||G||A||H")) && (
        (s.charAt(1) >= '0') && (
            s.charAt(1) <= '7'))) {
      this.value = getPitchValue(s.substring(0, 1));
      this.octave = Integer.parseInt(s.substring(1, 2));
    } else if ((s.length() == 3) && (s.substring(0, 2).matches("C#||D#||F#||G#||A#")) && (
        (s.charAt(2) >= '0') && (
            s.charAt(2) <= '7'))) {
      this.value = getPitchValue(s.substring(0, 2));
      this.octave = Integer.parseInt(s.substring(2, 3));
    } else {
      throw new IllegalArgumentException("Illegal Arguments.");
    }
  }

  /**
   * getPitchValue() method produces a PitchValue be the given string.
   *
   * @param s [String] given string
   * @return [PitchValue]
   */
  private PitchValue getPitchValue(String s) throws IllegalArgumentException {
    switch (s) {
      case "C":
        return PitchValue.C;
      case "C#":
        return PitchValue.CSH;
      case "D":
        return PitchValue.D;
      case "D#":
        return PitchValue.DSH;
      case "E":
        return PitchValue.E;
      case "F":
        return PitchValue.F;
      case "F#":
        return PitchValue.FSH;
      case "G":
        return PitchValue.G;
      case "G#":
        return PitchValue.GSH;
      case "A":
        return PitchValue.A;
      case "A#":
        return PitchValue.ASH;
      case "H":
        return PitchValue.B;
      default:
        throw new IllegalArgumentException("Illegal Arguments.");
    }
  }

  /**
   * getPitchValue() method produces a PitchValue be the given number.
   *
   * @param n [int] given number.
   * @return [PitchValue]
   */
  private PitchValue getPitchValue(int n) throws IllegalArgumentException {
    switch (n % 12) {
      case 0:
        return PitchValue.C;
      case 1:
        return PitchValue.CSH;
      case 2:
        return PitchValue.D;
      case 3:
        return PitchValue.DSH;
      case 4:
        return PitchValue.E;
      case 5:
        return PitchValue.F;
      case 6:
        return PitchValue.FSH;
      case 7:
        return PitchValue.G;
      case 8:
        return PitchValue.GSH;
      case 9:
        return PitchValue.A;
      case 10:
        return PitchValue.ASH;
      case 11:
        return PitchValue.B;
      default:
        throw new IllegalArgumentException("Illegal Arguments.");
    }
  }

  /**
   * toNumber() method produces an integer value of the pitch.
   *
   * @return [int] integer value
   */
  public int asNumber() {
    return (this.value.getNumber() + (this.octave * 12));
  }

  /**
   * tString() method produces a String value of the pitch.
   *
   * @return [String] String value
   */
  public String asString() {
    return value.getLetter() + String.valueOf(octave);
  }


}
