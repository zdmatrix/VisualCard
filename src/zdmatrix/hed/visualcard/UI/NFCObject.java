package zdmatrix.hed.visualcard.UI;

import android.app.Application;
import android.nfc.tech.IsoDep;

public class NFCObject extends Application{
	private IsoDep isodep;

	/*
	@Override
	public void onCreate(){
		super.onCreate();
		setNFCObject(null);
	}
	*/
	public IsoDep getNFCObject(){
		return isodep;
	}
	
	public void setNFCObject(IsoDep src){
		this.isodep = src;
	}
}
