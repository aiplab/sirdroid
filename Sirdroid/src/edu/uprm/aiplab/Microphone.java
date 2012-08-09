package edu.uprm.aiplab;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

public class Microphone extends Thread {
	private int SAMP_RATE = 8000;
	private int DEF_SAMP_RATE = 8000;
	private int record_state;
	private byte[] audio;
	private int len;
	private boolean end;

	public Microphone() {
		len = 0;
		record_state = 0;
		audio = new byte[1000000];
		end = true;
	}

	public void run() {
		end = false;
		// TODO Auto-generated method stub
		int buff_size = 8 * AudioRecord.getMinBufferSize(SAMP_RATE,
				AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
		if (buff_size < 0) {
			// TextView tv = (TextView) findViewById(R.id.textview1);
			// tv.setText("FAILED...");
			SAMP_RATE = DEF_SAMP_RATE;
			buff_size = 8 * AudioRecord
					.getMinBufferSize(SAMP_RATE, AudioFormat.CHANNEL_IN_MONO,
							AudioFormat.ENCODING_PCM_16BIT);
			if (buff_size < 0) {
				return;
			}
		}
		AudioRecord arec = new AudioRecord(MediaRecorder.AudioSource.DEFAULT,
				SAMP_RATE, AudioFormat.CHANNEL_IN_MONO,
				AudioFormat.ENCODING_PCM_16BIT, buff_size);
		arec.startRecording();
		record_state = 1;
		// Log.v("REC","Starting recording...");
		while (record_state == 1) {
			byte[] buffer = new byte[buff_size];

			int N = arec.read(buffer, 0, buffer.length);
			for (int i = 0; i < N; ++i) {
				audio[len + i] = buffer[i];
			}
			len += N;
		}
		// Log.v("REC","Finish");
		arec.stop();
		arec.release();
		end = true;
	}

	public byte[] getWav() {
		long totalAudioLen = len;

		long totalDataLen = totalAudioLen + 36;
		long longSampleRate = SAMP_RATE;
		long byteRate = SAMP_RATE * 2 * 1;

		byte[] header = new byte[44 + len];
		header[0] = 'R'; // RIFF/WAVE header
		header[1] = 'I';
		header[2] = 'F';
		header[3] = 'F';
		header[4] = (byte) (totalDataLen & 0xff);
		header[5] = (byte) ((totalDataLen >> 8) & 0xff);
		header[6] = (byte) ((totalDataLen >> 16) & 0xff);
		header[7] = (byte) ((totalDataLen >> 24) & 0xff);
		header[8] = 'W';
		header[9] = 'A';
		header[10] = 'V';
		header[11] = 'E';
		header[12] = 'f'; // 'fmt ' chunk
		header[13] = 'm';
		header[14] = 't';
		header[15] = ' ';
		header[16] = 16; // 4 bytes: size of 'fmt ' chunk
		header[17] = 0;
		header[18] = 0;
		header[19] = 0;
		header[20] = 1; // format = 1
		header[21] = 0;
		header[22] = (byte) 1; // numchannels
		header[23] = 0;
		header[24] = (byte) (longSampleRate & 0xff);
		header[25] = (byte) ((longSampleRate >> 8) & 0xff);
		header[26] = (byte) ((longSampleRate >> 16) & 0xff);
		header[27] = (byte) ((longSampleRate >> 24) & 0xff);
		header[28] = (byte) (byteRate & 0xff);
		header[29] = (byte) ((byteRate >> 8) & 0xff);
		header[30] = (byte) ((byteRate >> 16) & 0xff);
		header[31] = (byte) ((byteRate >> 24) & 0xff);
		header[32] = (byte) (2 * 1); // block align
		header[33] = 0;
		header[34] = 16; // bits per sample
		header[35] = 0;
		header[36] = 'd';
		header[37] = 'a';
		header[38] = 't';
		header[39] = 'a';
		header[40] = (byte) (totalAudioLen & 0xff);
		header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
		header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
		header[43] = (byte) ((totalAudioLen >> 24) & 0xff);
		// out.write(header, 0, 44);
		for (int i = 0; i < len; ++i)
			header[44 + i] = audio[i];
		return header;
	}

	public void finish() {
		record_state = 0;
	}

	/*
	 * public byte[] getAudio() { return audio; }
	 */

	public int getLen() {
		return len;
	}

	public boolean getEnd() {
		return end;
	}
}
