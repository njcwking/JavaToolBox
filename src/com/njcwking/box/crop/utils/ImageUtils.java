package com.njcwking.box.crop.utils;

import com.njcwking.box.crop.model.ConfigItem;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageUtils {

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

	public static BufferedImage zoomImage(File srcFile, ConfigItem srcConfig, ConfigItem distConfig, int tolerance) {
		BufferedImage result = null;

		try {
			BufferedImage im = ImageIO.read(srcFile);

			/* 原始图像的宽度和高度 */
			int width = im.getWidth();
			int height = im.getHeight();

			// 压缩计算
			float resizeTimes = distConfig.getWidth() * 1f / srcConfig.getWidth();
			float widthRatio = distConfig.getWidth() * 1f / srcConfig.getWidth();
			float heightRadio = distConfig.getHeight() * 1f / srcConfig.getHeight();
			/*
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
			//处理大图,如果在误差范围之内，直接使用目录宽高
			if (width==srcConfig.getWidth() && toWidth >= distConfig.getWidth() - tolerance && toWidth <= distConfig.getWidth() + tolerance) {
				toWidth = distConfig.getWidth();
			}
			if (height == srcConfig.getHeight() && toHeight >= distConfig.getHeight() - tolerance && toHeight <= distConfig.getHeight() + tolerance) {
				toHeight = distConfig.getHeight();
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
