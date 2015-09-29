package PhotoViewerPackage;

import java.util.Vector;

public abstract class StatusChanger {
	private Vector statusChangeListeners;
	
	public void addSunListener(SunListener l) {
	    if (statusChangeListeners == null)
	    	statusChangeListeners = new Vector();
	    statusChangeListeners.addElement(l);
	  }  

	  /** Remove a listener for SunEvents */
	  synchronized public void removeSunListener(SunListener l) {
	    if (listeners == null)
	      listeners = new Vector();
	    listeners.removeElement(l);
	  }

	  /** Fire a SunEvent to all registered listeners */
	  protected void fireSunMoved(boolean rose) {
	    // if we have no listeners, do nothing...
	    if (listeners != null && !listeners.isEmpty()) {
	      // create the event object to send
	      SunEvent event = 
	        new SunEvent(this, rose, new Date());

	      // make a copy of the listener list in case
	      //   anyone adds/removes listeners
	      Vector targets;
	      synchronized (this) {
	        targets = (Vector) listeners.clone();
	      }

	      // walk through the listener list and
	      //   call the sunMoved method in each
	      Enumeration e = targets.elements();
	      while (e.hasMoreElements()) {
	        SunListener l = (SunListener) e.nextElement();
	        l.sunMoved(event);
	      }
	    }
	  }
}
