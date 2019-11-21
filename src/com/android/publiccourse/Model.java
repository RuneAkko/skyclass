package com.android.publiccourse;

import java.io.Serializable;

import android.util.Log;

/**
 * User: Sunzk
 * Date: 13-1-16
 * Time: 2013-3-1 9:06
 */
public class Model<KEY extends Serializable> implements Serializable {
	private static final String TAG = "Model";
    KEY id;

    public KEY getId() {
        return id;
    }

    public void setId(KEY id) {
        this.id = id;
    }
    /**
     * id转化成对应的 HashCode
     * lixl 2012-03-04
     */
    public int getIntegerId(){
    	int result = 0;
    	if(id instanceof String){
    		try{
    			result = Integer.parseInt((String)id);
    		}catch(NumberFormatException ee){
    			Log.d(TAG, ee.getMessage());
    		}

        	if(result == 0){
        		result = id.hashCode();
        		if(result < 0)
        			result = Math.abs(result);
//        		Log.d(TAG, "id="+id+",对应hashCode=" + result);
        	}
    	}
    	
    	return result;
    }
}
