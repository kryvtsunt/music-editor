package cs3500.music.util;

import cs3500.music.model.IMusicCreatorModel;
import cs3500.music.model.MusicModel;

/**
 * ModelCompBuilder is an implementation of the CompositionBuilder.
 */
public class ModelCompBuilder implements CompositionBuilder<IMusicCreatorModel> {

  private IMusicCreatorModel model;

  public ModelCompBuilder() {
    this.model = new MusicModel();
  }

  @Override
  public IMusicCreatorModel build() {
    return this.model;
  }

  @Override
  public CompositionBuilder<IMusicCreatorModel> setTempo(int tempo) {
    this.model.setTempo(tempo);
    return this;
  }

  @Override
  public CompositionBuilder<IMusicCreatorModel> addNote(int start, int end, int instrument,
      int pitch, int volume) {
    this.model.addNote(pitch, end - start, instrument, volume, start);
    return this;
  }
}
