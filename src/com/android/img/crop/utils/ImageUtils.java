package com.android.img.crop.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageUtils {

	/**
	 * @param im
	 *            原始图像
	 * @param resizeTimes
	 *            倍数,比如0.5就是缩小一半,0.98等等double类型
	 * @return 返回处理后的图像
	 */
	public static BufferedImage zoomImage(File srcfile, int srcWidth, int dstWidth) {

		BufferedImage result = null;

		try {
			BufferedImage im = ImageIO.read(srcfile);

			/* 原始图像的宽度和高度 */
			int width = im.getWidth();
			int height = im.getHeight();

			// 压缩计算
			float resizeTimes = dstWidth * 1f / srcWidth; /*
														 * 这个参数是要转化成的倍数,如果是1就是转化成1倍
														 */
			System.out.println("resizeTimes-->"+resizeTimes);
			/* 调整后的图片的宽度和高度 */
			int toWidth = (int) (width * resizeTimes);
			int toHeight = (int) (height * resizeTimes);
			if(toWidth<=0)
			{
				toWidth = 1;
			}
			if(toHeight<=0)
			{
				toHeight = 1;
			}
			/* 新生成结果图片 */
			result = new BufferedImage(toWidth, toHeight,
					BufferedImage.TYPE_INT_ARGB);
			result.getGraphics().drawImage(
					im.getScaledInstance(toWidth, toHeight,
							java.awt.Image.SCALE_SMOOTH), 0, 0, null);

		} catch (Exception e) {
			System.out.println("创建缩略图发生异常" + e.getMessage());
		}

		return result;

	}
	
	public static BufferedImage zoomImageIcon(File srcfile, int dstWidth) {

		BufferedImage result = null;

		try {
			BufferedImage im = ImageIO.read(srcfile);

			/* 原始图像的宽度和高度 */
			int width = im.getWidth();
			int height = im.getHeight();

			// 压缩计算
			float resizeTimes = dstWidth * 1f / width; /*
														 * 这个参数是要转化成的倍数,如果是1就是转化成1倍
														 */
			System.out.println("resizeTimes-->"+resizeTimes);
			/* 调整后的图片的宽度和高度 */
			int toWidth = (int) (width * resizeTimes);
			int toHeight = (int) (height * resizeTimes);
			if(toWidth<=0)
			{
				toWidth = 1;
			}
			if(toHeight<=0)
			{
				toHeight = 1;
			}
			/* 新生成结果图片 */
			result = new BufferedImage(toWidth, toHeight,
					BufferedImage.TYPE_INT_ARGB);
			result.getGraphics().drawImage(
					im.getScaledInstance(toWidth, toHeight,
							java.awt.Image.SCALE_SMOOTH), 0, 0, null);

		} catch (Exception e) {
			System.out.println("创建缩略图发生异常" + e.getMessage());
		}

		return result;

	}

	public static boolean writeHighQuality(BufferedImage im, File file) {
		/*
		 * try { 输出到文件流 FileOutputStream newimage = new
		 * FileOutputStream(fileFullPath); JPEGImageEncoder encoder =
		 * JPEGCodec.createJPEGEncoder(newimage); JPEGEncodeParam jep =
		 * JPEGCodec.getDefaultJPEGEncodeParam(im); 压缩质量 jep.setQuality(1f,
		 * true); encoder.encode(im, jep); 近JPEG编码 newimage.close(); return
		 * true; } catch (Exception e) { return false; }
		 */
		try {
			ImageIO.write(im, "png", file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
