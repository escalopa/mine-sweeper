package components;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class WindowFrame{

    public void run(int dimension, int unit) throws UnsupportedAudioFileException, IOException, LineUnavailableException {

        playBackgroundMusic("Intergalactic Odyssey.wav");

        //
        int height = (dimension * unit) + unit;
        int width = (dimension * unit) + unit;

        // Add main frame
        JFrame jFrame = new JFrame();
        jFrame.setSize(width,height);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        jFrame.setResizable(false);
        jFrame.setTitle("Mine Sweeper");

        // Add game panel
        WindowPanel windowPanel = new WindowPanel(dimension,height,width, unit);
        windowPanel.loadIcons();
        windowPanel.startGame();
        jFrame.add(windowPanel.field);

        jFrame.setVisible(true);
    }

    private void playBackgroundMusic(String musicName) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(musicName));
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);
        clip.start();
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
}
