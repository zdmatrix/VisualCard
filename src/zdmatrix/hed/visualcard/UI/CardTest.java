package zdmatrix.hed.visualcard.UI;

import zdmatrix.hed.visualcard.R;
import zdmatrix.hed.visualcard.DataCommunication.NFCCommunication;
import zdmatrix.hed.visualcard.DataTypeTrans.DataTypeTrans;
import zdmatrix.hed.visualcard.FunctionMode.FunctionMode;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;

import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.os.Bundle;
import android.os.Handler;

import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class CardTest extends Activity{
	
	
	boolean 			bExit = false;
	boolean				bRead8ByteRandomData = false;
	boolean				bWriteData = false;
	boolean				bSend = false;
	
	boolean				bProbeCard = false;
	boolean				bNFCConnected = false;
	
	int					nNum = 0;
	int					nRandomTimes = 0;
	byte[]				bRamdomData = new byte[8];
	
	String 				strSW;
	String				strData;
	String				strTagInfo = "";
	String[][]			strTechLists;
	String				strOPStatus = "";
	
	TextView			tvTagInfo;
	
	Toast				tstDisInfo;
	
	EditText			etOPStatus;
	EditText    		etReturnData;
	EditText    		etReturnSW;
	
	Button 				btnSend;
	Button 				btnReturnMain;
	Button 				btnRead8ByteRandomData;
	Button 				btnProbeCard;
	Button 				btnResetCard;
	Button 				btnPowerDown;
	Button 				btnWriteData;
	
	IsoDep				isodep = null;
	NfcAdapter			nfcAdapter;
	PendingIntent		pendingIntent;
	Tag					tagFromIntent;
	IntentFilter		ndef;
	IntentFilter		tech;
	IntentFilter		tag;
	IntentFilter[]		intentFilter;
	
	Handler				handler = null;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maincardtest);
        
        handler = new Handler();
                
        btnReturnMain = (Button) findViewById(R.id.btnReturnMain);
        btnReturnMain.setOnClickListener(new ClickEvent());
        
        btnRead8ByteRandomData = (Button) findViewById(R.id.btnRead8ByteRandomData);
        btnRead8ByteRandomData.setOnClickListener(new ClickEvent());
        
        btnWriteData = (Button) findViewById(R.id.btnWriteData);
        btnWriteData.setOnClickListener(new ClickEvent());
        
        etOPStatus = (EditText)findViewById(R.id.etOPStatus);

        etReturnSW = (EditText)findViewById(R.id.etReturnSW);
        etReturnData = (EditText)findViewById(R.id.etReturnData);
        
        tvTagInfo = (TextView)findViewById(R.id.tvTagInfo);
        
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(nfcAdapter == null){
        	etOPStatus.setText("该设备不支持NFC");
        }
        if(!nfcAdapter.isEnabled()){
        	etOPStatus.setText("请在系统设置中启用NFC功能");
        }
        
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        tech = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        tag = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        
        intentFilter = new IntentFilter[]{ndef, tech, tag};
        
        strTechLists = new String[][]{new String[]{MifareClassic.class.getName(),
        		NfcA.class.getName(), NfcB.class.getName(), IsoDep.class.getName()}};
    }
	
	@Override
	public void onResume(){
		super.onResume();
		nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilter, strTechLists);
	}
	
	@Override
	public void onPause(){
		super.onPause();
		nfcAdapter.disableForegroundDispatch(this);
	}
	
	@Override
	public void onNewIntent(Intent intent){
		isodep = NFCCommunication.nfcConnectInit(intent, getApplicationContext());
	}
	
	class ClickEvent implements View.OnClickListener {

		public void onClick(View v) {
			if (v == btnRead8ByteRandomData || v == btnWriteData){
				if(isodep == null){
					NFCCommunication.nfcConnectFailed(getApplicationContext());
				}else {
					if(v == btnRead8ByteRandomData){
						new GetRandomDataThread().start();
					}
					if(v == btnWriteData)
						new WriteDataThread().start();
				}
			} 
			
			if (v == btnReturnMain) {
				/* 新建一个Intent对象 */
				Intent intent = new Intent();
				/* 指定intent要启动的类 */
				intent.setClass(CardTest.this, MainActivity.class);
				/* 启动一个新的Activity */
				startActivity(intent);
				/* 关闭当前的Activity */
				CardTest.this.finish();
			}
			
			
		}
	}
	
	class GetRandomDataThread extends Thread{
		@Override
		public void run(){
			nRandomTimes ++;
			if(FunctionMode.get8ByteRandom(bRamdomData, isodep)){
				strSW = "0x9000";
				strData = DataTypeTrans.bytesArrayToHexString(bRamdomData, 0, 8);
				strOPStatus = "第" + nRandomTimes + "次读随机数成功";
			}else{
				strSW = null;
				strData = null;
				
				if(!isodep.isConnected()){
					strOPStatus = "NFC连接失败";
				}else{
					strOPStatus = "第" + nRandomTimes + "次读随机数失败";
				}
			}
			
			handler.post(runnableUi);
		}
	}
	
	class WriteDataThread extends Thread{
		@Override
		public void run(){
			nNum ++;
			strData = null;
			String code = "16" + Integer.toString(nNum % 10, 16);
			if(FunctionMode.disNumOnCard(code, isodep)){
				strSW = "0x9000";
				strOPStatus = "第" + nNum + "次写数据成功";
			}else{
				strSW = null;
				if(!isodep.isConnected()){
					strOPStatus = "NFC连接失败";
				}else{
					strOPStatus = "第" + nNum + "次写数据失败";
				}
			}
			
			handler.post(runnableUi);
		}
	}
	
	Runnable runnableUi = new Runnable(){
		@Override
		public void run(){

			bRead8ByteRandomData = false;
			bWriteData = false;
				
			etOPStatus.setText(strOPStatus);
			etReturnSW.setText(strSW);
			etReturnData.setText(strData);
		}
	};
				
		
}
