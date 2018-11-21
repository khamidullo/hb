package uz.hbs.utils;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageUtil {
	private static final Logger _log = LoggerFactory.getLogger(ImageUtil.class);
	
	public static boolean checkDimensions(InputStream is, int height, int width){
		try {
			Image image = ImageIO.read(is);
			if (height > image.getHeight(null) || width > image.getWidth(null)) return false;
		} catch (IOException e) {
			_log.error("IOException", e);
		}
		return true;
	}
	
}
