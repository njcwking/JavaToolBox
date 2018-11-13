package com.android.img.crop.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/*
 *文件名过滤辅助类
 */
public class MyFileFilter implements FilenameFilter {

	private List<String> extensions = new ArrayList<String>();

	public MyFileFilter(String... extensions)
	{

		for(String extension:extensions)
		{
			this.extensions.add(extension);
		}
	}

	@Override
	public boolean accept(File dir, String filename)
	{

		// TODO Auto-generated method stub
		boolean flag = false;
		for(String extension:extensions)
		{
			if(filename.endsWith(extension))
			{
				flag = true;
				break;
			}
		}
		return flag;
	}
}
