package components;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class Frame {

    public void run(int dimension, int unit) throws UnsupportedAudioFileException, IOException, LineUnavailableException {

//        playBackgroundMusic("Intergalactic Odyssey.wav");

        int additional_height = 37;
        int height = (dimension * unit) + additional_height;
        int width = (dimension * unit);

        Panel panel = new Panel(dimension, unit);
        panel.loadIcons();
        panel.startGame();

        JFrame jFrame = createFrame(width,height);
        jFrame.add(panel.field);
        jFrame.setVisible(true);
    }

    private void playBackgroundMusic(String musicName) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(musicName));
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);
        clip.start();
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    private JFrame createFrame(int width, int height) {
        // Add main frame
        JFrame jFrame = new JFrame();
        jFrame.setSize(width, height);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        jFrame.setResizable(false);
        jFrame.setTitle("Mine Sweeper");
        return jFrame;
    }
}
