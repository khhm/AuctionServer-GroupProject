import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class MainServer { 

         
    public static final int BASE_PORT = 2000;  // define the server port   


   
    private ServerSocket serverSocket=null;  // server Socket for main server 
    private StocksDB allowedSymbols=null;     // allowed stocks for bidding 

    public MainServer(int socket, StocksDB symbols) {
	this.allowedSymbols = symbols; 

	try { 
	    this.serverSocket = new ServerSocket(socket); 
	} catch (IOException e) { 
	    System.out.println(e); 
	}
    }

    //to check the validation of stock symbol
    public boolean isAuthorized(String symbol) { 
	return this.allowedSymbols.findPrice(symbol) != null;
    }    
    //to get current price of the symbol
    public synchronized double getPrice(String symbol) { 
	return Double.parseDouble(this.allowedSymbols.findPrice(symbol));
    }	
   //to update stock price 
    public synchronized void setPrice(String symbol,double price) {
	allowedSymbols.updatePrice(symbol,price+"");
    }
    

    public void postMSG(String msg) { 
	// all threads print to same screen 
	System.out.println(msg); 
    }
    
    public void server_loop() { 
	try { 
	    while(true) { 
		Socket socket = this.serverSocket.accept(); 
		ConnectionServer worker = new ConnectionServer(this); 
		worker.handleConnection(socket); 
	    }
	} catch(IOException e) { 
	    System.out.println(e);
	}
    }
}


	




