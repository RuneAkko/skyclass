package com.android.sensorecord;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import com.android.domain.StringUtility;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;

// Each new sensor value is written to the buffer, overwriting its previous value.
// The buffer is read from approx. every 50 ms, and the values are written to CSV.
// This ensures consistent time intervals in the dataset.
public class SensorLogger implements SensorEventListener, Runnable {

	//	    static final int SAMPLE_RATE_IN_HZ = 8000;
	static final int SAMPLE_RATE_IN_HZ = 44100;

	    static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ,
	                    AudioFormat.CHANNEL_IN_DEFAULT, AudioFormat.ENCODING_PCM_16BIT);

    static AudioRecord mAudioRecord;
	private  SensorManager mSensorManager = null;
	private String studentcode,courseid,path,mActivity;
	private  SparseArray<Sensor> mSensors = new SparseArray<Sensor>();
	private Thread mThread;
	private DataWriter mWriter;
	private final int mDelay = SensorManager.SENSOR_DELAY_UI;
	private static SensorLogger singleton = null;
	private HashMap<SensorID, Float> mBuffer = new HashMap<SensorID, Float>();
	private long mLastBufferWrite = 0;
	private long mBufferWriteInterval =1000000000L; // in nanoseconds
	public static boolean isFinish = false;
	 short[] buffer = new short[BUFFER_SIZE];
	public SensorLogger(SensorManager sensorManager,String studentcode,String courseid,String path,String mActivity) {
		this.mSensorManager = sensorManager;
		this.studentcode = studentcode;
		this.courseid = courseid;
		this.path = path;
		this.mActivity = mActivity;


		mAudioRecord  = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_IN_DEFAULT,
	             AudioFormat.ENCODING_PCM_16BIT, BUFFER_SIZE);  
				
	}
	
	// This is run by the thread:
	@Override
	public void run() { 
	
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}			
	}

	public void start() {
		Log.v("SensorLogger", "Starting sensor logger...");
		initSensors();	
		initBuffer();
		initDataWriter(studentcode,courseid,path,mActivity);
		if(mAudioRecord.getState() == AudioRecord.STATE_INITIALIZED){
			mAudioRecord.startRecording();
			mThread = new Thread(this);
			mThread.start();
		}
		else {
			stop();
		}


	}
	
	public void stop() {
		Log.v("SensorLogger", "Stopping sensor logger...");
		mAudioRecord.stop();
        mAudioRecord.release();	
		mSensorManager.unregisterListener(this);		
		mWriter.stopWrite();
		mThread.interrupt();			
	}
	@Override
	public void onSensorChanged(final SensorEvent event) {	
		//关闭写文件则不写
		if (mSensors.get(event.sensor.getType()) != null&&mWriter!=null&&!mWriter.isFinish) { // if the sensor is in the sensor array
			updateBuffer(event);
			writeBuffer(event);			
		}
	}
	
	// updates the values in the buffer on the new sensor event
	private void updateBuffer(SensorEvent event) {
		int i = 0;
		for (SensorID sensorID : SensorID.getSensorIDs(event.sensor.getType())) {
			mBuffer.put(sensorID, event.values[i]);
			i++;
		}
	}
	
	/**
	 * set the interval to write the buffer
	 * @param event
	 */
	private void writeBuffer(SensorEvent event) {
		String s = "";	 
		if (mLastBufferWrite == 0) {
			mLastBufferWrite = event.timestamp;			
		}
		else if (event.timestamp - mLastBufferWrite >= mBufferWriteInterval) {			
			int r = mAudioRecord.read(buffer, 0, BUFFER_SIZE);
            long v = 0;
            // 将 buffer 内容取出，进行平方和运算
            for (int i = 0; i < buffer.length; i++) {
                v += buffer[i] * buffer[i];
            }
            // 平方和除以数据总长度，得到音量大小。
            double mean = v / (double) r;
            double volume = 10 * Math.log10(mean);
			mLastBufferWrite = event.timestamp;		
			s =  StringUtility.getDateTime();
			mWriter.writeLine(mBuffer, s,volume);
		}
	}
	
	 
	
	@Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
	
	private void initSensors() {
		int[] sensors = {Sensor.TYPE_ACCELEROMETER, 
						 Sensor.TYPE_GRAVITY,
						 Sensor.TYPE_LINEAR_ACCELERATION,
						 Sensor.TYPE_ROTATION_VECTOR,
						 Sensor.TYPE_GYROSCOPE,
						 Sensor.TYPE_TEMPERATURE,
						 Sensor.TYPE_AMBIENT_TEMPERATURE,
						 Sensor.TYPE_LIGHT,
						 Sensor.TYPE_RELATIVE_HUMIDITY};		
		initSensor(sensors);
	}

	private void initSensor(final int type) {
		Sensor sensor = mSensorManager.getDefaultSensor(type);
		if (sensor == null) Log.e("SensorLogger", "Sensor not working: " + type);
		else {
			mSensors.put(type, sensor);
			mSensorManager.registerListener(this, mSensors.get(type), mDelay);
		}
	}
	
	private void initSensor(final int[] types) {
		for (int type : types) {
			initSensor(type);
		}
	}	
	
	private void initDataWriter(String studentcode,String courseid,String path,String mActivity) {
		mWriter = new DataWriter(mSensors,studentcode,courseid,path,mActivity);
	}
	
	// initialize buffer to all zeros
	private void initBuffer() {
		for (SensorID sensorID : SensorID.getSensorIDs(mSensors))
			mBuffer.put(sensorID, 0f);
	}
	
	 
}
