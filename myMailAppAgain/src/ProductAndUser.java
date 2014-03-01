import java.util.ArrayList;


public class ProductAndUser {
	String productUrl;
	String productId;
	String percentOff;
	String productName;
	ArrayList<String> emailIds;
	
	public ProductAndUser(ArrayList<String> eid) {
		this.emailIds=eid;
	}
	public void setPid(String pid){
		this.productId=pid;
	}
	
	public void setPname(String pname){
		this.productName=pname;
	}
	public void setPoff(String po){
		this.percentOff=po;
	}
	
	public void setPurl(String purl)
	{
		this.productUrl=purl;
	}

}


