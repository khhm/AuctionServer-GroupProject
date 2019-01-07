import java.util.*; 

class VisualServer extends MainServer { 
    private static LinkedList<String> msgs; 

    public VisualServer(int socket, StocksDB symbols) { 
	super(socket, symbols); 
	msgs = new LinkedList<String>(); 
    }

    @Override 
    public synchronized void postMSG(String str) { 
	 
	msgs.add(str); 
    }

    public String getMSG() { 
	if(!msgs.isEmpty()) return msgs.remove(); 	    
	return null; 
    }
}
	