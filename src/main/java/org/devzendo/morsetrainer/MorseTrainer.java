package org.devzendo.morsetrainer;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MorseTrainer {
	public MorseTrainer() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		// specify the sound to play
	    // (assuming the sound can be played by the audio system)
	    File soundFile = new File("src/main/resources/sound.wav");
	    AudioInputStream sound = AudioSystem.getAudioInputStream(soundFile);

	    // load the sound into memory (a Clip)
	    DataLine.Info info = new DataLine.Info(Clip.class, sound.getFormat());
	    Clip clip = (Clip) AudioSystem.getLine(info);
	    clip.open(sound);

	    // due to bug in Java Sound, explicitly exit the VM when
	    // the sound has stopped.
	    clip.addLineListener(new LineListener() {
	      public void update(LineEvent event) {
	        if (event.getType() == LineEvent.Type.STOP) {
	          event.getLine().close();
	          System.exit(0);
	        }
	      }
	    });

	    // play the sound clip
	    clip.start();		
	}

	public static void main(String[] args) {
		
			try {
				new MorseTrainer();
			} catch (UnsupportedAudioFileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}

}
