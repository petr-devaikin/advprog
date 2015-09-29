package PhotoViewerPackage;

import java.util.EventListener;

public interface StatusChangeListener extends EventListener {
	public void statusChanged(StatusChangeEvent e);
}
