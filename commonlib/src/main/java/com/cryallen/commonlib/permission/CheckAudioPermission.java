package com.cryallen.commonlib.permission;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import com.cryallen.commonlib.utils.LogUtils;

/***
 * 检测是否有录音权限
 * @author Allen
 * @DATE 2019-11-19
 ***/
public class CheckAudioPermission {
	private final static String TAG = CheckAudioPermission.class.getSimpleName();

	public static final int STATE_RECORDING = -1;
	public static final int STATE_NO_PERMISSION = -2;
	public static final int STATE_SUCCESS = 1;

	private CheckAudioPermission() {
		throw new UnsupportedOperationException("u can't instantiate me...");
	}

	/**
	 * 用于检测是否具有录音权限
	 *
	 * @return
	 */
	public static int getRecordState() {
		if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N){
			LogUtils.e(TAG,"Android N版本以下不需要检测");
			return STATE_SUCCESS;
		}

		int minBuffer = AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
		AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.DEFAULT, 44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, (minBuffer * 100));
		short[] point = new short[minBuffer];
		int readSize = 0;
		try {
			audioRecord.startRecording();//检测是否可以进入初始化状态
		} catch (Exception e) {
			if (audioRecord != null) {
				audioRecord.release();
				audioRecord = null;
				LogUtils.e(TAG,"无法进入录音初始状态");
			}
			return STATE_NO_PERMISSION;
		}
		if (audioRecord.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING) {
			//6.0以下机型都会返回此状态，故使用时需要判断bulid版本
			//检测是否在录音中
			if (audioRecord != null) {
				audioRecord.stop();
				audioRecord.release();
				audioRecord = null;
				LogUtils.e(TAG,"录音机被占用");
			}
			return STATE_RECORDING;
		} else {
			//检测是否可以获取录音结果
			readSize = audioRecord.read(point, 0, point.length);
			if (readSize <=0) {
				if (audioRecord != null) {
					audioRecord.stop();
					audioRecord.release();
					audioRecord = null;
				}
				LogUtils.e(TAG,"录音的结果为空");
				return STATE_NO_PERMISSION;

			} else {
				if (audioRecord != null) {
					audioRecord.stop();
					audioRecord.release();
					audioRecord = null;

				}
				return STATE_SUCCESS;
			}
		}
	}
}
