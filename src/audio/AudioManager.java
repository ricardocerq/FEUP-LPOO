package audio;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import logic.Pair;

public class AudioManager extends Thread{

	public enum Sound {SHEEP1, GUNSHOT, GUNCLICK, MUSIC_LPOO};
	
	public static void init() throws IOException{
		AudioManager am = getInstance();
		Sound[] soundEnums = Sound.class.getEnumConstants();
		try{
			for(Sound s : soundEnums){
				am.sounds.put(s, am.loadSound(s.toString().toLowerCase()));
			}
		}catch(Exception e){
			active = false;
			throw new IOException();
		}
		if(active){
			am.serv = Executors.newCachedThreadPool();
			am.start();
		}
	}
	private ExecutorService serv;
	private AudioInputStream loadSound(String name) throws LineUnavailableException, UnsupportedAudioFileException, IOException{
		try{
			//Clip clip = AudioSystem.getClip();
			//AudioInputStream inputStream = AudioSystem.getAudioInputStream(AudioManager.class.getResourceAsStream("sounds/" + name + ".wav"));

			AudioInputStream is = AudioSystem.getAudioInputStream(AudioManager.class.getResource("sounds/" + name + ".wav"));
			return is;
			// AudioSystem.getAudioInputStream(AudioManager.class.getResource("sounds/" + name + ".wav"));
			/*byte[] b = new byte[1024];
			
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			while(is.read(b) != -1){
				bo.write(b);
			}
			//clip.open(inputStream);
			//return clip;
			return bo.toByteArray();*/
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("sounds/" + name);
		}
		return null;
	}
	public static AudioManager getInstance(){
		if(instance  == null)
			instance = new AudioManager();
		return instance;
	}
	private AudioManager(){
		setVolume(.99f);
		
	}
	@Override
	public void run(){
		while(!interrupted()){
			try {
				Pair<Sound, Float> nextSound;
					nextSound = toPlay.take();
				if(interrupted())
					break;
				SoundPlayer player = new SoundPlayer(nextSound.first, nextSound.second);
				serv.execute(player);
			} catch (InterruptedException e) {
			}
		}
	}

	private class SoundPlayer implements Runnable, LineListener{
		Sound sound;
		float vol;
		@Override
		public void run() {
			try {
				Clip clip = findSound(sound);
				playClip(clip, vol);
				
			} catch (IOException |  LineUnavailableException | UnsupportedAudioFileException e) {
				e.printStackTrace();
			}

		}
		public SoundPlayer(Sound s, float volume){
			this.sound = s;
			this.vol = volume;
		}
		@Override
		public void update(LineEvent arg0) {
			if(arg0.getType() == LineEvent.Type.STOP){
				this.notify();
			}
		}
		private void playClip(Clip c, float vol){
			c.setFramePosition(0);
			setVolume(c, vol);
			c.addLineListener(this);
			c.start();
			synchronized(this){
				try {
					this.wait();
				} catch (InterruptedException e) {
				}
			}
			c.close();
		}
	}
	public void setVolume(Clip clip, float change){
		FloatControl gainControl = 
		(FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(convertVolumeValue(change * masterVolume));
	}
	private float convertVolumeValue(float value){
		final int MAX = 6;
		final int MIN = -80;
		return (MIN + (MAX - MIN) * value);
	}
	private EnumMap<Sound, AudioInputStream> sounds = new EnumMap<Sound, AudioInputStream>(Sound.class);
	
	private Clip findSound(Sound nextSound) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
		Clip out =  AudioSystem.getClip();
		AudioInputStream a = sounds.get(nextSound);
		out.open(a);
		sounds.put(nextSound, AudioSystem.getAudioInputStream(AudioManager.class.getResource("sounds/" + nextSound.toString().toLowerCase() + ".wav")));
		//out.open(AudioSystem.getAudioInputStream(AudioManager.class.getResourceAsStream("sounds/" + nextSound + ".wav")));
		return out;
	}
	public void  playSound(Sound sound, float volume){
		if(active)
			toPlay.offer(new Pair<Sound, Float>(sound, volume));
	}
	public void destroy(){
		getInstance().interrupt();
	}
	public void loop(Sound sound, float volume){
		if(active){
			try {
				Clip s = findSound(sound);
				setVolume(s,volume);
				looping.add(s);
				s.loop(Clip.LOOP_CONTINUOUSLY);
				
			} catch (LineUnavailableException|IOException | UnsupportedAudioFileException e) {
				e.printStackTrace();
				System.out.println(sound);
			}
		}
			
	}
	
	
	private static AudioManager instance = null;
	
	private static boolean active = true;
	private float masterVolume = 1;
	private LinkedBlockingQueue<Pair<Sound, Float>> toPlay = new LinkedBlockingQueue<Pair<Sound, Float>>();
	private LinkedList<Clip> looping = new LinkedList<Clip>();
	
	public void setVolume(float volume){
		masterVolume = volume;
		for(Clip c : looping){
			//c.stop();
			setVolume(c,masterVolume);
			//c.loop(Clip.LOOP_CONTINUOUSLY);
		}
	}
	
	public float getVolume(){
		return masterVolume;
	}
	/*public void stopLoops(){
		for(int i = 0 ;i < looping.size(); i++){
			looping.get(i).stop();
			looping.remove(i);
			i--;
		}
	}*/
	public void handleLoops(boolean b) {
		for(Clip s : looping){
			if(b){
				if(!s.isRunning())
					s.start();
			}else {
				if(s.isRunning())
					s.stop();
			}
		}
	}
	
}
