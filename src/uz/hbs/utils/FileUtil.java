package uz.hbs.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.wicket.Application;
import org.apache.wicket.util.file.Folder;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uz.hbs.MyWebApplication;

public class FileUtil {
	private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

	public static String saveFile(InputStream is, String subPath, String filename) {
		String path = getUploadFolder().getAbsolutePath();

		if (path.endsWith("/"))
			path = path.concat(subPath);
		else
			path = path.concat("/").concat(subPath);

		File newFolder = new File(path);
		newFolder.mkdirs();

		File newFile = new File(newFolder, filename);

		if (!newFile.exists()) {
			try {
				FileOutputStream fos = new FileOutputStream(newFile);
				int data = 0;
				while ((data = is.read()) != -1) {
					fos.write(data);
				}
				fos.flush();
				fos.close();
				return getUploadFolder().getName() + "/" + subPath + "/" + filename;
			} catch (FileNotFoundException e) {
				logger.error("FileNotFoundException", e);
			} catch (IOException e) {
				logger.error("IOException", e);
			}
		}
		return null;
	}

	public static Folder getReportFolder() {
		return ((MyWebApplication) Application.get()).getReportFolder();
	}

	public static Folder getUploadFolder() {
		return ((MyWebApplication) Application.get()).getUploadFolder();
	}

	public static String getFileExtension(String clientFileName) {
		if (clientFileName == null)
			return null;

		if (clientFileName.indexOf(".") == -1) {
			return "";
		}
		return clientFileName.substring(clientFileName.lastIndexOf(".") + 1, clientFileName.length());
	}

	public static boolean deleteFile(String filename) {
		return new File(filename).delete();
	}

	public static String getBase64FromFile(String file) {
		try {
			FileInputStream fis = new FileInputStream(file);
			byte[] bytes = new byte[fis.available()];
			fis.read(bytes);
			fis.close();
			return new String(Base64.encode(bytes));
		} catch (Exception e) {
			logger.error("Exception", e);
		}
		return null;
	}
}
