package org.devzendo.morsetrainer;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MorseTrainer {
	private static final int SAMPLE_RATE = 48000;
	private static final double TWO_PI = Math.PI * 2.0;

	public MorseTrainer() throws UnsupportedAudioFileException, IOException,
			LineUnavailableException, InterruptedException {
		// specify the sound to play
		// (assuming the sound can be played by the audio system)
		// File soundFile = new File("src/main/resources/sound.wav");
		// AudioInputStream sound = AudioSystem.getAudioInputStream(soundFile);

		// load the sound into memory (a Clip)
		// DataLine.Info info = new DataLine.Info(Clip.class,
		// sound.getFormat());
		// Clip clip = (Clip) AudioSystem.getLine(info);
		// clip.open(sound);

		Clip clip = createDit(18, 600);
//		System.out.println("data length is " + data.length);
		// due to bug in Java Sound, explicitly exit the VM when
		// the sound has stopped.
//		clip.addLineListener(new LineListener() {
//			public void update(LineEvent event) {
//				if (event.getType() == LineEvent.Type.STOP) {
//					event.getLine().close();
//					System.exit(0);
//				}
//			}
//		});

		
		// play the sound clip
//		System.out.println("one");
		clip.start();
		clip.drain();
//		System.out.println("drained");
		Thread.sleep(1000);
//		Clip clip2 = createDit(12, 650);
//		clip2.start();
//		clip2.drain();
//		Thread.sleep(2000);
	}

	private Clip createDit(int wpm, int freqHz) throws LineUnavailableException {
		// http://sv8gxc.blogspot.co.uk/2010/09/morse-code-101-in-wpm-bw-snr.html
		double ditLengthSeconds = (1.2 / wpm);
		int samples = (int)(SAMPLE_RATE * ditLengthSeconds);
//		System.out.println(wpm + " wpm, dit length (secs) " + ditLengthSeconds + ", samples " + samples);
		// must fill in sin(0) to sin(2pi) freqHz times in SAMPLE_RATE bytes
		
		// Create sine wave without ramp-up/down
		byte out[] = createSine(samples, freqHz);
		
		// Ramp up at start and down at end
		ramp(out, wpm);

		return bytesToClip(out);
	}

	private void ramp(byte[] out, int wpm) {
		double ditLengthSeconds = (1.2 / wpm);
		double rampLength = ditLengthSeconds / 16.0;
		int rampLengthSamples = (int)(SAMPLE_RATE * rampLength);
//		System.out.println("ramplength seconds " + rampLength + " in samples " + rampLengthSamples);
		for (int i = 0; i < (int)rampLengthSamples; i++) {
			double d = (double)i / rampLengthSamples;
			out[i] *= d;
		}
		for (int i = (int)rampLengthSamples - 1; i > 0; i--) {
			double d = (double)i / rampLengthSamples;
			out[samples - i] *= d;
		}
	}

	private byte[] createSine(int samples, int freqHz) {
		int cycleLength = SAMPLE_RATE / freqHz;
		double dCycleLength = cycleLength;

		byte out[] = new byte[(int) samples];
		for (int i = 0; i < samples; i++) {
			int x = i % cycleLength;
			double prop = (double)x / dCycleLength;
			double sinprop = prop * TWO_PI;
			double val = Math.sin(sinprop);
			int iVal = ((int)(val * 127) + 256);
			byte bVal = (byte)(iVal & 0xff);
			out[i] = bVal;
		}
		return out;
	}

	private Clip bytesToClip(byte[] out) throws LineUnavailableException {
		AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
				(float)SAMPLE_RATE, 8, 1, 1, SAMPLE_RATE,
				false);
		DataLine.Info info = new DataLine.Info(Clip.class, format);
		Clip clip = (Clip) AudioSystem.getLine(info);
		clip.open(format, out, 0, out.length);
		return clip;
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
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
