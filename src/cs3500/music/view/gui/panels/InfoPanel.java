package cs3500.music.view.gui.panels;

import javax.swing.JPanel;
import java.awt.*;

public class InfoPanel extends JPanel {
  private String stateName;
  private int tempoBPM;

  private int wPixels;
  private int hPixels;

  private int pixPerGridHeight;
  private int pixPerGridWidth;

  private final int panelWidth = 6;
  private final int panelHeight = 1;

  private int lowPitchDisplay;
  private int highPitchDisplay;

  private int pOctave;

  public InfoPanel(String stateName, int tempo) {
    super();
    this.stateName = stateName;
    this.tempoBPM = tempo;
  }

  @Override
  public void paintComponent(Graphics g) {
    this.wPixels = this.getWidth();
    this.hPixels = this.getHeight();

    this.pixPerGridHeight = this.hPixels / this.panelHeight;
    this.pixPerGridWidth = this.wPixels / this.panelWidth;

//    System.out.println(String.format("Info: w, h, x, y: %d %d %d %d",
//      this.getWidth(),
//      this.getHeight(),
//      this.getX(),
//      this.getY()));

    Graphics2D g2 = (Graphics2D) g;
    super.paintComponent(g2);

    paintInfo(g2);
  }

  public void update() {
    this.wPixels = this.getWidth();
    this.hPixels = this.getHeight();

    this.pixPerGridWidth = wPixels / this.panelWidth;
    this.pixPerGridHeight = hPixels / this.panelHeight;
  }

  // Sets the view to dieplay which notes are shown in teh master view.
  public void setPitchRange() {

  }

  public void setStateName(String name) {
    this.stateName = name;
  }

  public void setPracticeOctave(int octave) {
    this.pOctave = octave;
  }

  public void setTempoBPM(int tempo) {
    if (tempo > 0) {
      this.tempoBPM = tempo;
    }
  }

  private void paintInfo(Graphics2D canvass) {
    // Mode
    canvass.setColor(Color.black);
    canvass.setFont(new Font("panel", Font.BOLD, 30));
    canvass.drawString(this.stateName,
      10,
      panelHeight * pixPerGridHeight / 2);

    // TEMPO
    canvass.drawString("Tempo(BPS): " + Integer.toString(this.tempoBPM),
      2 * pixPerGridWidth + 10,
      panelHeight * pixPerGridHeight / 2);

    // Octave
    if (this.stateName.equals("Practice Mode")) {

      canvass.drawString("Octave: " + Integer.toString(this.pOctave),
        4 * pixPerGridWidth + 10,
        panelHeight * pixPerGridHeight / 2);
    }
  }
}
