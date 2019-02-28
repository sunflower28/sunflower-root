package com.sunflower.tools;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public final class ImageUtil {

	private ImageUtil() {
	}

	public static void mergeImage(URL base, URL headImg, File wxacode, String nickName,
			File outFile) throws IOException {
		BufferedImage image = ImageIO.read(base);
		Graphics2D g = image.createGraphics();
		BufferedImage image2 = ImageIO.read(wxacode);
		g.drawImage(image2, (image.getWidth() - image2.getWidth()) / 2, 717,
				image2.getWidth(), image2.getHeight(), null);
		BufferedImage image3 = ImageIO.read(headImg);
		int newWidth = image3.getWidth() * 2;
		int newHeight = image3.getHeight() * 2;
		BufferedImage imageTemp = new BufferedImage(newWidth, newHeight, 6);
		Graphics2D g2 = imageTemp.createGraphics();
		g2.setClip(0, 0, newWidth, newHeight);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
				RenderingHints.VALUE_STROKE_DEFAULT);
		g2.drawImage(image3, 0, 0, newWidth, newHeight, null);
		g2.dispose();
		g.drawImage(imageTemp, (image.getWidth() - imageTemp.getWidth()) / 2, 200,
				imageTemp.getWidth(), imageTemp.getHeight(), null);
		g.setFont(new Font("PingFang SC", 0, 36));
		g.setColor(new Color(180, 38, 39));
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
				RenderingHints.VALUE_STROKE_DEFAULT);
		g.drawString(nickName,
				(image.getWidth() - g.getFontMetrics().stringWidth(nickName)) / 2, 560);
		g.setPaint(new Color(255, 239, 220));
		g.setStroke(new BasicStroke(5.0F));
		g.drawOval((image.getWidth() - imageTemp.getWidth()) / 2, 200, newWidth,
				newHeight);
		g.dispose();
		ImageIO.write(image, "png", outFile);
	}

}