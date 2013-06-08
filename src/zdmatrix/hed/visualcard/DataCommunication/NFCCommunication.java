package zdmatrix.hed.visualcard.DataCommunication;

import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.view.Gravity;
import android.widget.Toast;

public class NFCCommunication {
	static Toast tstDisInfo;
		
	public static IsoDep nfcConnectInit(Intent intent, Context context){
		IsoDep isodep = null;
		if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())){
			Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			isodep = IsoDep.get(tagFromIntent);
			try{
				isodep.connect();
				tstDisInfo = Toast.makeText(context, "检测到NFC Tag并建立连接", Toast.LENGTH_SHORT);
				tstDisInfo.setGravity(Gravity.CENTER, 0, 0);
				tstDisInfo.show();
			}catch(Exception e){
				e.printStackTrace();
				tstDisInfo = Toast.makeText(context, "与NFC Tag建立连接失败", Toast.LENGTH_SHORT);
				tstDisInfo.setGravity(Gravity.CENTER, 0, 0);
				tstDisInfo.show();
			}
		}else{
			isodep = null;
		}
		return isodep;
	}
	
	public static void nfcConnectFailed(Context context){
		tstDisInfo = Toast.makeText(context, "NFC连接失败", Toast.LENGTH_SHORT);
		tstDisInfo.setGravity(Gravity.CENTER, 0, 0);
		tstDisInfo.show();
	}
}
