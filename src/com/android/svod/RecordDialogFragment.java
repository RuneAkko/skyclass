package com.android.svod;

import com.android.sensorecord.*;
import com.umeng.analytics.MobclickAgent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class RecordDialogFragment extends DialogFragment {

	Uri uri;
	String studentcode;
	String courseid;
	String mpath;
	String path;
	String activity = "";
	Boolean isOnline;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.dialogforstatus, null);
		Spinner spinner = (Spinner) view.findViewById(R.id.status);
		studentcode = getArguments().getString("studentcode");
		courseid = getArguments().getString("courseid");
		mpath = getArguments().getString("mpath");
		path = getArguments().getString("path");
		isOnline = getArguments().getBoolean("isOnlinePlay");

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.activities_array,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				activity = arg0.getSelectedItem().toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}

		});
		activity = spinner.getSelectedItem().toString();
		builder.setView(view).setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				Intent intent = new Intent(getActivity(), VideoPlayerNewActivity.class);
//				Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
				intent.setDataAndType(uri, "video/mp4");
				intent.putExtra("mpath", mpath);
				//intent.putExtra("mpath","https://kjguanli.xjtudlc.com/data/uploads/ZJZ025/20161116122330183_303506_720p.mp4");
				intent.putExtra("studentcode", studentcode);
				intent.putExtra("courseid", courseid);
				intent.putExtra("path", path);
				intent.putExtra("isOnlinePlay", true);
				intent.putExtra("activity", activity);
				startActivity(intent);
			}
		});
		return builder.create();
	}

	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

}
