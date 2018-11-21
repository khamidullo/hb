package uz.hbs.beans;

import org.apache.wicket.util.io.IClusterable;

public class Image implements IClusterable {
	private static final long serialVersionUID = 1L;
	private String filename;
	private long filesize;
	private byte[] data;
	
	public Image() {
		
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public long getFilesize() {
		return filesize;
	}

	public void setFilesize(long filesize) {
		this.filesize = filesize;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
}
