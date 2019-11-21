package com.android.svod;

import java.io.File;
import android.os.Environment;

public class DirectoryHelper
{

	public void getSdCardPath()
	{
		File sdcardDir = Environment.getExternalStorageDirectory();
		String path = sdcardDir.getParent() +"/"+ sdcardDir.getName();
		Constant.filePath = path + java.io.File.separator +"/testPic";
		System.out.println("__________" + Constant.filePath);
		createFile();
	}

	public void createFile()
	{
		try
		{

			if (Environment.MEDIA_MOUNTED.equals(Environment
					.getExternalStorageState()))
			{

				System.out.println("_______create Directory________");
				File path = new File(Constant.filePath);
				if (!path.exists())
				{

					path.mkdirs();
				}

			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
