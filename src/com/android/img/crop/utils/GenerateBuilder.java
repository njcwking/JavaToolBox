package com.android.img.crop.utils;

import java.io.File;
import java.util.List;

import com.android.img.crop.listener.GenerateListener;
import com.android.img.crop.model.ConfigItem;

public class GenerateBuilder {
	private String rootPath;
	private ConfigItem currentModel;// 当前模式
	private List<ConfigItem> generateModels;// 需要生成的模式
	private GenerateListener listener;
	private String iconName;
	private File file;
	private boolean isRecursion = false;

	public GenerateBuilder setRootPath(String rootPath) {
		this.rootPath = rootPath;
		return this;
	}

	public GenerateBuilder setCurrentMode(ConfigItem model) {
		this.currentModel = model;
		return this;
	}

	public GenerateBuilder setRecursion(boolean isRecursion) {
		this.isRecursion = isRecursion;
		return this;
	}

	public GenerateBuilder setIconName(String iconName) {
		this.iconName = iconName;
		return this;
	}

	public GenerateBuilder setGenerateListener(GenerateListener listener) {
		this.listener = listener;
		return this;
	}

	public GenerateBuilder setGenerateModels(List<ConfigItem> models) {
		this.generateModels = models;
		return this;
	}

	public GenerateBuilder setSaveFile(File file) {
		this.file = file;
		return this;
	}

	public void generate() {
		if (rootPath != null && !rootPath.equals("")) {
			File file = new File(rootPath);
			if ((!file.exists()) || file.isFile()) {
				if (listener != null) {
					listener.generateFail("文件目录不存在 ！");
				}
			} else {
				new GenerateRunnable().start();
			}
		} else {
			if (listener != null) {
				listener.generateFail("文件目录不能为空");
			}
		}
	}

	class GenerateRunnable extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			File srcFile = new File(rootPath);
			if (listener != null) {
				listener.generateStart();
			}
			if (srcFile.exists() && srcFile.isDirectory()) {
				generate(srcFile, "/", isRecursion);
			}
			if (listener != null) {
				listener.generateSuccess();
			}
		}
	}

	private void generate(File srcdir, String dstRelativeDir, boolean isRetry) {
		File[] files = srcdir.listFiles();
		if (files != null) {
			for (File single : files) {
				if (single.isFile()
						&& (single.getName().endsWith(".JPG")
								|| single.getName().endsWith(".PNG")
								|| single.getName().endsWith(".jpg") || single
								.getName().endsWith(".png"))) {
					for (ConfigItem model : generateModels) {
						File dstFile = GenerateBuilder.this.file != null ? new File(
								GenerateBuilder.this.file,
								model.getFolderName()+ dstRelativeDir + single.getName())
								: new File(srcdir.getParentFile(),
										model.getFolderName()
												+ dstRelativeDir
												+ single.getName());
								System.out.println(dstFile.getAbsolutePath());
						// File dstFile = new File(dstDir, single.getName());
						FileUtils.createFolder(dstFile.getParentFile());
						if (single.getName().equals(iconName)) {
							ImageUtils.writeHighQuality(
									ImageUtils.zoomImageIcon(single,
											model.getIconWidth()), dstFile);
						} else {
							ImageUtils.writeHighQuality(
									ImageUtils.zoomImage(single,
											currentModel.getWidth(),
											model.getWidth()), dstFile);
						}
					}
				} else if (single.isDirectory() && isRetry) {
					String relativeString = single.getAbsolutePath().replace(
							srcdir.getAbsolutePath(), "")
							+ "/";
					System.out.println("--"+relativeString);
					generate(
							single,relativeString, isRetry);
				}
			}
		}
	}
}
