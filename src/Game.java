import components.WindowFrame;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class Game {

    public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        final int dimension = 15;
        final int unit = 50;
        new WindowFrame().run(dimension, unit);
    }
}
