package com.njcwking.box.crop.utils;

import java.io.File;
import java.io.IOException;

public class ConsoleUtils {

	//调用系统的记事本
	public static void invokeConsoleNotepad(File file) throws IOException
	{
		Runtime.getRuntime().exec("notepad "+file.getAbsolutePath());
	}
}
