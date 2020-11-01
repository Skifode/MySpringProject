package main.services;

import com.github.cage.Cage;
import com.github.cage.image.EffectConfig;
import com.github.cage.image.Painter;
import com.github.cage.image.Painter.Quality;
import com.github.cage.image.ScaleConfig;
import com.github.cage.token.RandomCharacterGeneratorFactory;
import com.github.cage.token.RandomTokenGenerator;
import java.awt.Color;
import java.util.Random;

public class CyrillicCage extends Cage {

  protected static final int HEIGHT = 35;
  protected static final int WIDTH = 100;
  protected static final char[] TOKEN_DEFAULT_CHARACTER_SET;
  public static final char[] RUSS_CONSONANTS = "кнгшщзхфвпрлджясмтб".toCharArray();
  public static final char[] RUSS_VOWELS = "уеаоэяию".toCharArray();
  public static final char[] SOME_SPICE = "йъьёыц".toCharArray();

  public CyrillicCage() {
    this(new Random());
  }

  protected CyrillicCage(Random rnd) {
    super(new Painter(
        WIDTH,
        HEIGHT,
        Color.WHITE,
        Quality.MAX,
        new EffectConfig(
            true,
            true,
            false,
            true,
            new ScaleConfig(1F, 1F)),
        rnd),
        null,
        null,
        null,
        Cage.DEFAULT_COMPRESS_RATIO,
        new RandomTokenGenerator(
            rnd,
            new RandomCharacterGeneratorFactory(
                TOKEN_DEFAULT_CHARACTER_SET,
                null,
                rnd),
            4,
            2),
        rnd);
  }

  static {
    TOKEN_DEFAULT_CHARACTER_SET = (
        String.valueOf(RUSS_CONSONANTS)
            + String.valueOf(RUSS_VOWELS)
            + String.valueOf(RandomCharacterGeneratorFactory.ARABIC_NUMERALS)
            + String.valueOf(RandomCharacterGeneratorFactory.ARABIC_NUMERALS)
            + String.valueOf(SOME_SPICE)
            + String.valueOf(SOME_SPICE))
        .replaceAll("[013зЗб]", "")
        .toCharArray();

    //        можно будет добавить
    //            + new String(RUSS_CONSONANTS).toUpperCase()
    //            + new String(RUSS_VOWELS).toUpperCase()
  }
}
