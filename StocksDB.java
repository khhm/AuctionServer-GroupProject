import java.io.*;
import java.util.*;

class StocksDB { 

    public Map<String, String> symbolList; 
    private String [] fields; 

    public StocksDB(String cvsFile)  { 
	FileReader fileRd=null; 
	BufferedReader reader=null; 

	try { 
	    fileRd = new FileReader(cvsFile); 
	    reader = new BufferedReader(fileRd); 

	    /* read the CSV file's first line which has 
	     * the names of fields. 
	     */
	    String header = reader.readLine(); 
	    fields = header.split(",");// keep field names 


	    // get a new hash map
	    symbolList = new HashMap<String, String>(); 

	    /* read each line, getting it split by , 
	     * use the indexes to get the key and value 
	     */
	    String [] tokens; 
	    for(String line = reader.readLine(); 
		line != null; 
		line = reader.readLine()) { 
		tokens = line.split(","); 
		symbolList.put(tokens[0], tokens[2]); 
	    }
	    
	    if(fileRd != null) fileRd.close();
	    if(reader != null) reader.close();
	    
	    // I can catch more than one exceptions 
	} catch (IOException e) { 
	    System.out.println(e);
	    System.exit(-1); 
	} catch (ArrayIndexOutOfBoundsException e) { 
	    System.out.println("Malformed CSV file");
	    System.out.println(e);
	}
    }
	
    // public interface 
    public String findPrice(String key) { 
	return symbolList.get(key); 
    }
    public void updatePrice(String symbol,String price){
        symbolList.put(symbol, price);
    }

}
	    
