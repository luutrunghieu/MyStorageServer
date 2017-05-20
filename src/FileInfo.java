import java.io.File;
import java.io.Serializable;

public class FileInfo implements Serializable{
	private String path;
	private long lastModified;
	private long length;
	
	public FileInfo(File f){
		this.path = f.getPath();
		this.lastModified = f.lastModified();
		this.length = f.length();
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public long getLastModified() {
		return lastModified;
	}
	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}
	public long getLength() {
		return length;
	}
	public void setLength(long length) {
		this.length = length;
	}
	public String getName(){
		String[] arr = this.path.split("\\\\");
		String name = arr[arr.length-1];
		return name;
	}
	
}
