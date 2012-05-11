package org.hf.pad;

import java.io.IOException;
import java.io.Serializable;

public final class HsunSeri implements Serializable{

	private static final long serialVersionUID = 1L;

	private String lastName;
	
	private String firstName;

	private void writeObject(java.io.ObjectOutputStream out)
	throws IOException {
		
          out.defaultWriteObject();
	}

	
	private void readObject(java.io.ObjectInputStream in)
	throws IOException, ClassNotFoundException {
           in.defaultReadObject();
	}


}
