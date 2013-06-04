package zdmatrix.hed.visualcard.FunctionMode;

import android.nfc.tech.IsoDep;
import zdmatrix.hed.visualcard.DataTypeTrans.APDUCmd;
import zdmatrix.hed.visualcard.DataTypeTrans.DataTypeTrans;

public class FunctionMode {
	
	public void updateSelectFileData(int offsetlow, int offsethigh, String data, int length){
		String stroffsetlow = "";
		String stroffsethigh = "";
		String apducmd = "";
		
		int len = data.length();
		int index = 0;
		if(offsetlow < 16)
			stroffsetlow = "0" + Integer.toString(offsetlow, 16);
		else
			stroffsetlow = Integer.toString(offsetlow, 16);
		
		if(offsethigh < 16)
			stroffsethigh = "0" + Integer.toString(offsethigh, 16);
		else
			stroffsethigh = Integer.toString(offsethigh, 16);
		
		apducmd = "00d6" + stroffsethigh + stroffsetlow + "0" + Integer.toString(length, 16);
		
		if(len < (length * 2)){
			while(index < (length * 2 - len)){
				data = "0" + data;
				index ++;
			}
		}
		
		String str = apducmd + data;
		byte[]	apdudisplayoncard = DataTypeTrans.stringHexToByteArray(str);

	}
	
	public static boolean get8ByteRandom(byte[] randomdata, IsoDep isodep){
		boolean bret = false;
		byte[] apdu = DataTypeTrans.stringHexToByteArray(APDUCmd.APDUGet8BytesRandom);
		try{
			
			byte[] ret = isodep.transceive(apdu);
			String sw = DataTypeTrans.bytesArrayToHexString(ret, ret.length - 2, 2);
			if(sw.equals("9000")){
				bret = true;
			}
			return bret;
		}catch(Exception e){
			e.printStackTrace();
			bret = false;
			return bret;
		}
		
	}
	
	public static boolean disNumOnCard(String decnum, IsoDep isodep){
		boolean bret = false;
		String length = decnum.length() < 16 ? ("0" + Integer.toString(decnum.length(), 16)) : Integer.toString(decnum.length(), 16);
		String apducmd = "80bf0100" + length;
		byte[] apdu = new byte[5 + decnum.length()];
		byte[] apdu1 = DataTypeTrans.stringHexToByteArray(apducmd);
		byte[] apdu2 = DataTypeTrans.stringDecToByteArray(decnum);
		System.arraycopy(apdu1, 0, apdu, 0, apdu1.length);
		System.arraycopy(apdu2, 0, apdu, apdu1.length, apdu2.length);
		try{
			if(!isodep.isConnected()){
				isodep.connect();
			}

			byte[] ret = isodep.transceive(apdu);
			String sw = DataTypeTrans.bytesArrayToHexString(ret, ret.length - 2, 2);
			
			if(sw.equals("9000")){
				bret = true;
			}
			return bret;
		}catch(Exception e){
			e.printStackTrace();
			bret = false;
			return bret;
		}
	}
}
