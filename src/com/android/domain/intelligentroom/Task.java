package com.android.domain.intelligentroom;

import java.util.ArrayList;
import java.util.HashMap;

public class Task {

	public static final int TASK_REQUESTONLINECOURSE=1;
	public static final int TASK_REQUESTNOONLINECOURSE=2;
	public static final int TASK_REFRESH=6;
	public static final int TASK_NUMBER=3;
	public static final int TASK_ONLINESTATE=4;
	public static final int TASK_LEARNLOG=5;
    private int taskId;
    private HashMap<String,Object> params;
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public HashMap<String, Object>getParams() {
		return params;
	}
	public void setParams(HashMap<String, Object> params) {
		this.params = params;
	}
    
}
