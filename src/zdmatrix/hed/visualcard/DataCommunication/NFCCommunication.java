package zdmatrix.hed.visualcard.DataCommunication;

import android.nfc.Tag;
import android.nfc.tech.IsoDep;

public class NFCCommunication {
	
	public static byte[] dataSwitching(Tag tagFromIntent, byte[] apdu){
		IsoDep isodep = IsoDep.get(tagFromIntent);
		byte[] sw = null;
		try{
			isodep.connect();
			sw = isodep.transceive(apdu);
			isodep.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return sw;
		
	}
}
