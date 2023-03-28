package game.utils;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

public class SoundManager {

    public static void init() {
        try {
            AL.create();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void playSurroundSound(String soundFile, float volume) {
        try {
            FileInputStream fin = new FileInputStream(soundFile);
            BufferedInputStream bin = new BufferedInputStream(fin);

            WaveData waveFile = WaveData.create(bin);

            int buffer = AL10.alGenBuffers();
            AL10.alBufferData(buffer, waveFile.format, waveFile.data, waveFile.samplerate);
            waveFile.dispose();

            int source = AL10.alGenSources();
            AL10.alSourcei(source, AL10.AL_BUFFER, buffer);
            AL10.alSourcef(source, AL10.AL_GAIN, volume);
            AL10.alSourcef(source, AL10.AL_PITCH, 1.0f);
            AL10.alSource3f(source, AL10.AL_POSITION, 0.0f, 0.0f, 0.0f);
            AL10.alSource3f(source, AL10.AL_VELOCITY, 0.0f, 0.0f, 0.0f);
            AL10.alSourcei(source, AL10.AL_LOOPING, AL10.AL_TRUE);
            AL10.alSourcePlay(source);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void playDamageSound(String soundFile) {
        try {
            FileInputStream fin = new FileInputStream(soundFile);
            BufferedInputStream bin = new BufferedInputStream(fin);

            WaveData waveFile = WaveData.create(bin);

            int buffer = AL10.alGenBuffers();
            AL10.alBufferData(buffer, waveFile.format, waveFile.data, waveFile.samplerate);
            waveFile.dispose();

            int source = AL10.alGenSources();
            AL10.alSourcei(source, AL10.AL_BUFFER, buffer);
            AL10.alSourcef(source, AL10.AL_GAIN, 1.0f);
            AL10.alSourcef(source, AL10.AL_PITCH, 1.0f);
            AL10.alSource3f(source, AL10.AL_POSITION, 0.0f, 0.0f, 0.0f);
            AL10.alSource3f(source, AL10.AL_VELOCITY, 0.0f, 0.0f, 0.0f);
            AL10.alSourcei(source, AL10.AL_PLAYING, AL10.AL_TRUE);
            AL10.alSourcePlay(source);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
