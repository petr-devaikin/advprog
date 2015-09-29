package PhotoViewerPackage;

import java.util.EventObject;

public class StatusChangeEvent extends EventObject {
	private String status;
	
	public StatusChangeEvent(Object source, String status) {
	    super(source);
	    
	    this.status = status;
	}
	
	public String getStatus() {
		return status;
	}
}
