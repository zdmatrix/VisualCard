package zdmatrix.hed.visualcard.FunctionMode;

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
//		byte[] ret = NFCCommunication.dataSwitching()
	}
}
