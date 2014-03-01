

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;


public class GetDataFromFile{
	//private HashMap<String, ArrayList<String>> map=new HashMap<String, ArrayList<String>>();
	public GetDataFromFile(){

	}
	
	
	/*@Override
	public void run() {
		System.out.println("Thread 1 Running"); 
	      BufferedReader in = null;
		
			try {
				in = new BufferedReader(new FileReader("customer.dat"));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	        String line = "";
	        
				try {
					while ((line = in.readLine()) != null) {
					    String parts[] = line.split("\t");
					    if(map.containsKey(parts[0])){
					    	map.get(parts[0]).add(parts[1]);
					    }
-					    else{
					    ArrayList<String> eids=new ArrayList<String>();
					    eids.add(parts[1]);
					    map.put(parts[0],eids);
					    }
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		synchronized(map){
		map.notify();
		}
	}*/
	public HashMap<String, ProductAndUser> readUserInfo(HashMap<String, ProductAndUser> hm){
	
	      BufferedReader in = null;
		
			try {
				in = new BufferedReader(new FileReader("customer.dat"));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	        String line = "";
	        
				try {
					while ((line = in.readLine()) != null) {
					    String parts[] = line.split("\t");
					    System.out.println(parts[0]);
					    if(hm.containsKey(parts[0])){
					    	
					    	hm.get(parts[0]).emailIds.add(parts[1]);
					    }
					    else{
					    	ArrayList<String> eid=new ArrayList<String>();
						    eid.add(parts[1]);
					    	if(parts[0]!=null){
					    
					    		hm.put(parts[0],new ProductAndUser(eid));
					    		hm.get(parts[0]).setPid(parts[0]);
					    	}
					    
					    }
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		try {
			in.close();
			PrintWriter pw = new PrintWriter("customer.dat");
			pw.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		return hm;
	}

}
