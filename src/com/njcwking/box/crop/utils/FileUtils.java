package com.njcwking.box.crop.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Chen
 */
public class FileUtils {

	/** 文件是否存在 **/
	public static boolean exist(File file) {

		if (file == null) {
			return false;
		}
		return file.exists();
	}

	public static boolean exist(String path) {
		return exist(new File(path));
	}

	/** 创建文件 **/
	public static boolean createFile(File file) {

		if (file == null) {
			return false;
		}
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean createFile(String path) {
		return createFile(new File(path));
	}

	/** 创建文件夹 **/
	public static boolean createFolder(File folder) {
		if (folder == null) {
			return false;
		}
		return folder.mkdirs();
	}

	public static boolean createFolder(String path) {
		return createFolder(new File(path));
	}

	/** 删除文件 **/
	public static boolean deleteFile(File file) {

		if (file == null) {
			return false;
		}
		return file.delete();
	}

	public static boolean deleteFile(String path) {
		return deleteFile(new File(path));
	}

	/** 删除文件夹 **/
	public static boolean deleteFolder(File folder) {

		if (folder == null) {
			return false;
		}
		return deleteFolder(folder, true);
	}

	private static boolean deleteFolder(File folder, boolean remain) {

		if (exist(folder)) {
			File[] files = folder.listFiles();
			if (files != null) {
				for (File file : files) {
					if (file.isDirectory()) {
						deleteFolder(file, remain);
					} else {
						deleteFile(file);
					}
				}
			}
			return folder.delete();
		} else {
			return true;
		}
	}

	public static boolean deleteFolder(String path) {
		return deleteFile(new File(path));
	}

	/** 获取以指定文件目录下以指定后缀名的文件 **/
	public static List<File> getAllWithEnd(File folder, String... extensions) {
		if (folder == null) {
			return null;
		}
		List<File> files = fileAllFilter(folder, extensions);
		return files;
	}

	public static List<File> getWidthEnd(String folder, String... extensions) {
		return getAllWithEnd(new File(folder), extensions);
	}

	public static List<File> getWithEnd(File folder, String... extensions) {
		if (folder == null) {
			return null;
		}
		List<File> files =fileFilter(folder,extensions);
		return files;
	}

	public static List<File> getAllWidthEnd(String folder, String... extensions) {
		return getWithEnd(new File(folder), extensions);
	}

	/** 过滤文件 **/
	private static List<File> fileAllFilter(File file, 
			String... extensions) {
		List<File> files = new ArrayList<File>();
		File[] allFiles = file.listFiles();
		File[] allExtensionFiles = file.listFiles(new MyFileFilter(extensions));
		if (allExtensionFiles != null) {
			for (File single : allExtensionFiles) {
				files.add(single);
			}
		}
		if (allFiles != null) {
			for (File single : allFiles) {
				if (single.isDirectory()) {
					files.addAll(fileAllFilter(single, extensions));
				}
			}
		}
		return files;
	}

	private static List<File> fileFilter(File file, 
			String... extensions) {
		List<File> files = new ArrayList<File>();
		File[] allExtensionFiles = file.listFiles(new MyFileFilter(extensions));
		if (allExtensionFiles != null) {
			for (File single : allExtensionFiles) {
				files.add(single);
			}
		}
		return files;
	}

	/**
	 * 复制文件
	 * 
	 * @throws IOException
	 **/
	public static boolean copyFile(File src, File dst) throws IOException {
		if (src == null || dst == null) {
			return false;
		}
		if (!dst.exists()) {
			FileUtils.createFile(dst);
		}
		FileInputStream inputStream = null;
		inputStream = new FileInputStream(src);
		return copyFile(inputStream, dst);
	}

	private static boolean copyFile(InputStream inputStream, File dst)
			throws IOException {
		FileOutputStream outputStream = null;
		outputStream = new FileOutputStream(dst);
		int readLen = 0;
		byte[] buf = new byte[1024];
		while ((readLen = inputStream.read(buf)) != -1) {
			outputStream.write(buf, 0, readLen);
		}
		outputStream.flush();
		inputStream.close();
		outputStream.close();
		return true;
	}

	public static boolean copyFile(String src, String dst) throws IOException {
		return copyFile(new File(src), new File(dst));
	}

	/** 复制目录 **/
	public static boolean copyFolder(File srcDir, File destDir)
			throws IOException {

		if ((!srcDir.exists())) {
			return false;
		}
		if (srcDir.isFile() || destDir.isFile())
			return false;// 判断是否是目录
		if (!destDir.exists()) {
			destDir.mkdirs();
		}
		File[] srcFiles = srcDir.listFiles();
		int len = srcFiles.length;
		for (int i = 0; i < len; i++) {
			if (srcFiles[i].isFile()) {
				// 获得目标文件
				File destFile = new File(destDir.getPath() + "//"
						+ srcFiles[i].getName());
				copyFile(srcFiles[i], destFile);
			} else if (srcFiles[i].isDirectory()) {
				File theDestDir = new File(destDir.getPath() + "//"
						+ srcFiles[i].getName());
				copyFolder(srcFiles[i], theDestDir);
			}
		}
		return true;
	}

	public static boolean copyFolder(String srcDir, String desDir)
			throws IOException {

		return copyFolder(new File(srcDir), new File(desDir));
	}

	/** 移动文件或目录 **/
	public static boolean move(File src, File dst) {
		if (src == null || dst == null) {
			return false;
		}
		dst.mkdirs();
		return src.renameTo(dst);
	}

	public static boolean move(String src, String dst) {
		return move(new File(src), new File(dst));
	}

	/** 获取文件大小 **/
	public static long getFileSize(File file) {

		if (file == null) {
			throw new NullPointerException("File path error!");
		}
		return file.length();
	}

	/** 获取文件夹大小 **/
	public static long getFolderSize(File folder) {

		if (folder == null) {
			throw new NullPointerException("Folder path error!");
		}
		if (!folder.exists()) {
			return 0;
		}
		return getFolderSize(folder, true);
	}

	/**
	 * @param folder
	 * @param remain
	 *            保留字段（没用处）
	 * @return
	 */
	private static long getFolderSize(File folder, boolean remain) {
		long size = 0;
		File[] files = folder.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					size += getFolderSize(folder, remain);
				} else {
					size += getFileSize(file);
				}
			}
		}
		return size;
	}

}
