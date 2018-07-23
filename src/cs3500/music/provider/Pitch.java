package cs3500.music.provider;

/**
 * The different pitches for each Note.
 */
public enum Pitch {
  A, B, C, D, E, F, G;

  /**
   * Returns the next pitch in traditional scale using
   * {@code this} as well as the sharp:
   * - if the pitch is not sharp:
   * - if {@code this} is "B" the next pitch will be "C".
   * - if {@code this} is "E" the next pitch will be "F".
   * - otherwise the next pitch will be the same as {@code this}.
   * - if the pitch is sharpL
   * - if {@code} this is "B" or "E" the method will return invalid.
   * - otherwise the next pitch will be the next letter in the scale
   * (A B C D E F G (wrapped around)).
   *
   * @param sharp the sharp being evaluated.
   * @return the next pitch.
   */
  public Pitch nextPitch(boolean sharp) {
    Pitch newPitch;
    if (!(sharp)) {
      switch (this) {
        case B:
          newPitch = Pitch.C;
          break;
        case E:
          newPitch = Pitch.F;
          break;
        default:
          newPitch = this;
          break;
      }
    } else {
      switch (this) {
        case A:
          newPitch = Pitch.B;
          break;
        case C:
          newPitch = Pitch.D;
          break;
        case D:
          newPitch = Pitch.E;
          break;
        case F:
          newPitch = Pitch.G;
          break;
        case G:
          newPitch = Pitch.A;
          break;
        default:
          throw new IllegalArgumentException("Invalid sharp: Sharp does not correspond with pitch");
      }
    }
    return newPitch;
  }

  /**
   * Returns if the next note will be sharp:
   * - if {@code this} is "B" or "E", the next note will not be sharp.
   * - otherwise the next note will be sharp.
   *
   * @param sharp the sharp being evaluated.
   * @return the next sharp.
   */
  public boolean nextSharp(boolean sharp) {
    if (!(sharp)) {
      switch (this) {
        case B:
          return false;
        case E:
          return false;
        default:
          return true;
      }
    } else {
      switch (this) {
        case B:
          throw new IllegalArgumentException("Invalid sharp: Sharp does not correspond with pitch");
        case E:
          throw new IllegalArgumentException("Invalid sharp: Sharp does not correspond with pitch");
        default:
          return false;
      }
    }
  }

  /**
   * Returns the octave of the next note:
   * - if {@code this} is "B" the next octave will be 1 higher than the previous.
   * - otherwise the octave will remain the same.
   *
   * @param octave the octave being evaluated.
   * @return the next octave.
   */
  public int nextOctave(int octave) {
    switch (this) {
      case B:
        return octave + 1;
      case E:
        return octave;
      default:
        return octave;
    }
  }

  /**
   * Takes the given integer and finds its corresponding pitch.
   *
   * @param p the given numerical representation of a pitch.
   * @return a corresponding Pitch.
   */
  public static Pitch calculatePitch(int p) {
    int val = p % 12;
    switch (val) {
      case 0:
        return C;
      case 1:
        return C;
      case 2:
        return D;
      case 3:
        return D;
      case 4:
        return E;
      case 5:
        return F;
      case 6:
        return F;
      case 7:
        return G;
      case 8:
        return G;
      case 9:
        return A;
      case 10:
        return A;
      case 11:
        return B;
      default:
        throw new IllegalArgumentException("Invalid");
    }
  }

  /**
   * Uses a pitch, along with an octave and sharp to calculate
   * a corresponding numerical representative for an equivalent key.
   *
   * @param octave the given octave to be used.
   * @param sharp the given sharp to be used.
   * @return an integer corresponding to the numerical representation.
   */
  public int computePitch(int octave, boolean sharp) {
    int base;
    int sh = 0;
    switch (this) {
      case C:
        base = 0;
        break;
      case D:
        base = 2;
        break;
      case E:
        base = 4;
        break;
      case F:
        base = 6;
        break;
      case G:
        base = 8;
        break;
      case A:
        base = 10;
        break;
      case B:
        base = 12;
        break;
      default:
        throw new IllegalArgumentException("Invalid");
    }
    if (sharp) {
      sh = 1;
    }
    return base + (octave * 14) + sh;
  }

  /**
   * Takes the given integer and evaluates whether the  pitch
   * at that point is sharp.
   *
   * @param s the given numerical representation of a pitch.
   * @return whether or not the pitch is sharp.
   */
  public static boolean calculateSharp(int s) {
    int val = s % 12;
    switch (val) {
      case 1:
        return true;
      case 3:
        return true;
      case 6:
        return true;
      case 8:
        return true;
      case 10:
        return true;
      default:
        return false;
    }
  }
}

