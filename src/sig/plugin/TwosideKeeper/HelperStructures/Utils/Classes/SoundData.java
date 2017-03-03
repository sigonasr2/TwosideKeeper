package sig.plugin.TwosideKeeper.HelperStructures.Utils.Classes;

import org.bukkit.Sound;

public class SoundData {
	Sound sound;
	float pitch;
	float vol;
	public SoundData(Sound sound) {
		this.sound=sound;
		this.pitch=1.0f;
		this.vol=1.0f;
	}
	public SoundData(Sound sound, float pitch, float vol) {
		this.sound=sound;
		this.pitch=pitch;
		this.vol=vol;
	}
	public Sound getSound() {
		return sound;
	}
	public float getPitch() {
		return pitch;
	}
	public float getVolume() {
		return vol;
	}
}
