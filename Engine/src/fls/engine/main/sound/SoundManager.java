package fls.engine.main.sound;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.CompoundControl;
import javax.sound.sampled.Control;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class SoundManager {

	public static SoundManager instance = new SoundManager();

	public HashMap<String, Sound> cont;
	private AudioFormat af;
	private boolean supported;

	public SoundManager() {
		this.cont = new HashMap<String, Sound>();
		this.af = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100f, 16, 2, 4, 44100F, false);
		
	}

	public void addSound(String name, String pos) {
		Sound s = new Sound(this, pos);
		if (!s.valid) {
			err("Error loading sound: " + name);
		} else {
			log("Successfully loaded sound: " + name);
			this.cont.put(name, s);
		}
	}

	private Sound getSound(String n) {
		return this.cont.get(n);
	}

	public void stopAll() {
		Object[] a = this.cont.keySet().toArray();
		for (int i = 0; i < this.cont.size(); i++) {
			String key = "" + a[i];
			// this.cont.get(key).stopSound();
		}
	}

	public void log(String msg) {
		System.out.println("[Sound Manager] " + msg);
	}

	public void err(String msg) {
		System.err.println("[Sound Manager] " + msg);
	}

	public static void showMixers() {
		ArrayList<Mixer.Info> mixInfos = new ArrayList<Mixer.Info>(Arrays.asList(AudioSystem.getMixerInfo()));
		Line.Info sourceDLInfo = new Line.Info(SourceDataLine.class);
		Line.Info targetDLInfo = new Line.Info(TargetDataLine.class);
		Line.Info clipInfo = new Line.Info(Clip.class);
		Line.Info portInfo = new Line.Info(Port.class);
		String support = "";
		for (Mixer.Info mixInfo : mixInfos) {
			Mixer mixer = AudioSystem.getMixer(mixInfo);
			if (mixer.isLineSupported(sourceDLInfo))
				support += "SourceDataLine ";
			if (mixer.isLineSupported(clipInfo))
				support += "Clip ";
			if (mixer.isLineSupported(targetDLInfo))
				support += "TargetDataLine ";
			if (mixer.isLineSupported(portInfo))
				support += "Port ";
			System.out.println("Mixer: " + mixInfo.getName() + support + ", " + mixInfo.getDescription());
		}
	}

	public static void probePort() throws Exception {
		ArrayList<Mixer.Info> mixerInfos = new ArrayList<Mixer.Info>(Arrays.asList(AudioSystem.getMixerInfo()));
		Line.Info portInfo = new Line.Info(Port.class);
		for (Mixer.Info mixerInfo : mixerInfos) {
			Mixer mixer = AudioSystem.getMixer(mixerInfo);
			if (mixer.isLineSupported(portInfo)) {
				disp("Found mixer: " + mixerInfo.getName());
				disp("\t" + mixerInfo.getDescription());
				disp("Target Line Supported:");
				ArrayList<Line.Info> targetInfos = new ArrayList<Line.Info>(Arrays.asList(mixer.getTargetLineInfo()));
				for (Line.Info targetInfo : targetInfos) {
					Port.Info pi = (Port.Info) targetInfo;
					disp("\t" + pi.getName() + ", " + (pi.isSource() ? "source" : "taget"));
					showControls(mixer.getLine(targetInfo));
				}
			}
		}
	}

	private static void showControls(Line inLine) throws Exception {
		// must open the line to get
		// at controls
		inLine.open();
		disp("\t\tAvailable controls:");
		ArrayList<Control> ctrls = new ArrayList<Control>(Arrays.asList(inLine.getControls()));
		for (Control ctrl : ctrls) {
			disp("\t\t\t" + ctrl.toString());
			if (ctrl instanceof CompoundControl) {
				CompoundControl cc = ((CompoundControl) ctrl);
				ArrayList<Control> ictrls = new ArrayList<Control>(Arrays.asList(cc.getMemberControls()));
				for (Control ictrl : ictrls)
					disp("\t\t\t\t" + ictrl.toString());
			}
		}
		inLine.close();
	}

	private static void disp(String msg) {
		System.out.println(msg);
	}

}