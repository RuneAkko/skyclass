package com.android.svod;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.R.color;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class DownFileList extends Activity {
private File f=new File("mnt/sdcard/Skyclass");
	private List<Map<String, Object>> allValues = new ArrayList<Map<String, Object>>(); 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.downfilelist);
		allValues=loadFileData(f);
		ListView fileList=(ListView)findViewById(R.id.downfilelist);
		MySimpleAdapter adapter=new MySimpleAdapter(this.getApplicationContext(), allValues, R.layout.downfile, new String[]{"filename","deletebutton"}, new int[]{R.id.filename,R.id.delete});
		fileList.setAdapter(adapter);	
		super.onCreate(savedInstanceState);
	}
	private class MySimpleAdapter extends SimpleAdapter{

		  public MySimpleAdapter(Context context,
		    List<? extends Map<String, ?>> data, int resource,
		    String[] from, int[] to) {
		   super(context, data, resource, from, to);
		   // TODO Auto-generated constructor stub
		  }
		 
		 
		  @Override
		  public View getView(final int position, View convertView, ViewGroup parent) {
		   // TODO Auto-generated method stub
		   View v= super.getView(position, convertView, parent);
		   TextView filename=(TextView)v.findViewById(R.id.filename);
		 
		   filename.setText(allValues.get(position).get("fileName").toString());
		    Button btn=(Button) v.findViewById(R.id.delete);
		    btn.setText("ɾ��");
		    btn.setTag(position);
		   btn.setOnClickListener(new OnClickListener() {
		   
		    @Override
		    public void onClick(final View v) {
		     // TODO Auto-generated method stu
		     Map<String, Object> map = allValues.get(position);  
             final File f = new File("mnt/sdcard/Skyclass/"+map.get("fileName").toString());  

             if (f.isFile()) {  

                 Builder builder = new Builder(DownFileList.this);  
                 builder.setTitle("��ʾ");  
                 builder.setMessage("ȷ��Ҫɾ��" + f.getName() + "��?");  
                 builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){  
                    
				 
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						  if (f.exists()) {  
	                             f.delete();  
	                             Toast.makeText(getApplicationContext(), "�ɹ�ɾ��"+f.getName(), Toast.LENGTH_SHORT).show();
	                         }  

	                         allValues.remove(position);  
	                         notifyDataSetChanged();
					}  
                 }).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {  
                   
				 

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}  
                 });  

                 builder.create().show();  
             }  

		    }
		   }); 
		   return v;
		  }
		}	
	 private List<Map<String, Object>> loadFileData(File dir) {  

		 List<Map<String, Object>> filelist=new ArrayList<Map<String, Object>>();
		 if(dir.exists()){
	        File[] allFiles = dir.listFiles();  
	      

	        if (allFiles != null) {  

	            for (int i = 0; i < allFiles.length; i++) {  
	                File f = allFiles[i];  
	                 Map<String, Object> map = new HashMap<String, Object>();  
	                 if(!(f.getName().contains("temp")))
	                 {  map.put("fileName", f.getName());   
	                    filelist.add(map);
	                 }
	            }
	  
	        } 
		 }
	        return filelist;
	    }  
	  

}
