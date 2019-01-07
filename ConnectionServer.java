import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

class ConnectionServer implements Runnable { 
    // some constants 
    public static final int WAIT_AUTH_SYM = 0; 
    public static final int WAIT_FOR_NAME = 1; 
    public static final int AUTH_DONE = 2;

    public static final String WAIT_AUTH_MSG1 = "Name pls!\n";
    public static final String WAIT_AUTH_MSG2 = "Symbol pls!\n";  
    public static final String DONE_MSG = "You can Bid for ";  

    // per connection variables
    private Socket mySocket; // connection socket per thread 
    private int currentState; 
    private String clientName; 
    private String symbol;
    private MainServer mainServer;
    private static double price;
    private static double newPrice;

    public ConnectionServer(MainServer mainServer) { 
	this.mySocket = null; 
	this.currentState = WAIT_FOR_NAME; 
	this.clientName = null; 
	this.mainServer = mainServer; 
	
    }

    public boolean handleConnection(Socket socket) { 
	this.mySocket = socket; 
	Thread newThread = new Thread(this); 
	newThread.start(); 
	return true; 
    }
    
    public void run() { // can not use "throws .." interface is different
	BufferedReader in=null; 
	PrintWriter out=null; 
	try { 
	    in = new 
		BufferedReader(new InputStreamReader(mySocket.getInputStream()));
	    out = new 
		PrintWriter(new OutputStreamWriter(mySocket.getOutputStream()));
		
	    String line, outline=""; 
	    for(line = in.readLine(); 
		line != null && !line.equals("quit"); 
		line = in.readLine()) {
		switch(currentState) { 
		case WAIT_FOR_NAME: 
                     // we are waiting for login name 
		    // name should be the line
		    if(!line.equals("")) { 
			currentState = WAIT_AUTH_SYM; 
			clientName   = line; 
                        outline = WAIT_AUTH_MSG2;
		    }
		    else { 
			outline = WAIT_AUTH_MSG1; 
		    }
		    break;
                case WAIT_AUTH_SYM: 
		    // we are waiting for symbol 
		    //and check whether that symbol is valid
		    if(mainServer.isAuthorized(line)) { 
			currentState = AUTH_DONE; 
                        symbol = line;
                        outline = DONE_MSG+symbol+"\nPrice : "+mainServer.getPrice(symbol)+"\n";
		    }
		    else { 
			outline = "-1\n"; 
		    }
		    break;
		    /*****************************/
		case AUTH_DONE:
                    if(!line.equals(""))
                        newPrice=Double.parseDouble(line);
                        price = mainServer.getPrice(symbol);
                        if(newPrice > price){						//compare the user's bid and current bid
                            mainServer.setPrice(symbol,newPrice);
		            mainServer.postMSG(symbol+","+this.clientName+","+ newPrice); 
		            outline = "Successful Bid!\n";
                            }
                        else{
                            outline = "Bid higher than "+price+"\n";        
                        }
		    break; 
		default: 
		    System.out.println("Undefined state"); 
		    return; 
		} 
                out.print(outline); // Send the bid message 
		out.flush(); // flush to network

	    } 

	    // close everything 
	    out.close(); 
	    in.close(); 
	    this.mySocket.close(); 
	} // exception handeling	     
	catch (IOException e) { 
	    System.out.println(e); 
	} 
    }
}

    
    

