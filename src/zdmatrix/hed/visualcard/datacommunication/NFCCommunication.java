package zdmatrix.hed.visualcard.datacommunication;

import android.nfc.Tag;
import android.nfc.tech.IsoDep;

public class NFCCommunication {
	
	public NFCCommunication(Tag tagFromIntent, byte[] apdu){
		int arraylength = 0;
		IsoDep isodep = IsoDep.get(tagFromIntent);
		try{
			isodep.connect();
			byte[] sw = isodep.transceive(apdu);
			arraylength = sw.length;
		}catch(Exception e){
			e.printStackTrace();
		}
		byte[] ret = new byte[arraylength];
		
	}
}
