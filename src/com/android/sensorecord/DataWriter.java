package com.android.sensorecord;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.hardware.Sensor;
import android.os.Environment;
import android.util.Log;
import android.util.SparseArray;

import com.google.common.base.Joiner;

public class DataWriter {
	
	boolean mFirstLine = true;
	public boolean isFinish = false;
	FileWriter fileWriter = null;
	public static BufferedWriter writer = null;
	ArrayList<SensorID> mSensorIDs = new ArrayList<SensorID>();
	String studentcode;
	String courseid;
	String path;
	String activity;
	FileUploadTask uploadTask = null;
	public String getStudentcode() {
		return studentcode;
	}

	public void setStudentcode(String studentcode) {
		this.studentcode = studentcode;
	}

	public String getCourseid() {
		return courseid;
	}

	public String getPath() {
		return path;
	}
	public void setCourseid(String courseid) {
		this.courseid = courseid;
	}


	public void setPath(String path) {
		this.path = path;
	}
	public String getActivity(){
		return activity;
	}
	public void setActivity(String activity){
		this.activity = activity;
	}
	String FILENAME ="";
 
	public DataWriter(SparseArray<Sensor> sensors,String studentcode,String courseid,String path,String activity) {
		this.studentcode=studentcode;
		this.courseid=courseid;
		this.path=path;
		this.activity = activity;
		FILENAME= generateFilename();
		File file=new File(Environment.getExternalStorageDirectory(),"record_skyclass");
		if(!file.exists())  file.mkdirs();		
		try {
			fileWriter = new FileWriter(new File(file, FILENAME), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		writer = new BufferedWriter(fileWriter);
		buildSensorIDs(sensors);
		writeFirstLine();
	}
	
	private void buildSensorIDs(SparseArray<Sensor> sensors) {
		for (int i = 0; i < sensors.size(); i++)
			mSensorIDs.addAll(SensorID.getSensorIDs(sensors.valueAt(i).getType()));
	}
	//第一行   数据项名称
	private void writeFirstLine() {
  		ArrayList<String> strings = new ArrayList<String>();		
		for (SensorID sensorID : mSensorIDs)
			strings.add(sensorID.toString());
		strings.add("TIME");
		strings.add("VOICE");
		writeLine(Joiner.on(",").join(strings));
		Log.v("DataWriter", "Write first line: " + Joiner.on(",").join(strings));
	}
	//建立文件写记录数据    
	private void writeLine(String string) {
		try { 			
			if (!mFirstLine) writer.newLine();
			else mFirstLine = false;
			writer.write(string);	
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public void writeLine(HashMap<SensorID, Float> hm, String time,double volume) {
		ArrayList<String> strings = new ArrayList<String>();		
		for (SensorID sensorID : mSensorIDs)
			strings.add(Float.toString(hm.get(sensorID)));
		strings.add(time);
		strings.add(String.valueOf(volume));
		writeLine(Joiner.on(",").join(strings));
	}
	//文件名    studentcode+courseid+视频名称+时间
	private String generateFilename() {
		Date now = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd--HH-mm-ss");
		String name = "";
		try {
			name = java.net.URLEncoder.encode(this.studentcode+"-"+this.courseid+"-"+this.path+"-"+formatter.format(now) + this.activity+".csv", "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return name;
	}
	public void stopWrite(){
		isFinish = true;
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
