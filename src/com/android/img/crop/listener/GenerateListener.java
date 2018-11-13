package com.android.img.crop.listener;

public interface GenerateListener {
	public void generateSuccess();

	public void generateFail(String message);

	public void generateStart();

	public void generating(String filename);
}
