

import java.util.ArrayList;
import java.util.HashMap;
//import java.util.concurrent.ConcurrentHashMap;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class oneClass extends TimerTask {
	static HashMap<String,ProductAndUser> hm=new HashMap<String,ProductAndUser>();
	public static void main(String[] args) {
		
		Timer timer = new Timer();
	    Calendar date = Calendar.getInstance();
	    date.set(
	      Calendar.DATE,
	      Calendar.SUNDAY
	    );
	    date.set(Calendar.HOUR, 0);
	    date.set(Calendar.MINUTE, 0);
	    date.set(Calendar.SECOND, 0);
	    date.set(Calendar.MILLISECOND, 0);
	    // Schedule to run every Sunday in midnight
	    timer.schedule(
	      new oneClass(),
	      0,
	      1000 * 60
	    );
	
		
		
	}
	@Override
	public void run() {
		System.out.println("running timertask");
		GetDataFromFile get=new GetDataFromFile();
		hm=get.readUserInfo(hm);
		
		sendMails sm=new sendMails(hm);
		HashMap<String, ProductAndUser> result =new HashMap<String, ProductAndUser>();
		
		try {
			
			result = sm.call();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!result.isEmpty()){
			System.out.println(result.values());
		}
		
		
	}

}
