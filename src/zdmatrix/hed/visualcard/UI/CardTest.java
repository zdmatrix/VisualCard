package zdmatrix.hed.visualcard.UI;

import zdmatrix.hed.visualcard.R;
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

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class CardTest extends Activity{
	
	
	boolean 			bExit = false;
	boolean				bRead8ByteRandomData = false;
	boolean				bWriteData = false;
	boolean				bSend = false;
	
	boolean				bProbeCard = false;
	boolean				bNFCConnected = false;
	
	int					nNum = 0;
	
	String 				strSW;
	String				strData;
	String				strTagInfo = "";
	String[][]			strTechLists;
	String				strOPStatus = "";
	
	TextView			tvTagInfo;
	
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
	
	NfcAdapter			nfcAdapter;
	PendingIntent		pendingIntent;
	Tag					tagFromIntent;
	IntentFilter		ndef;
	IntentFilter		tech;
	IntentFilter		tag;
	IntentFilter[]		intentFilter;
	
	Handler				handler = null;
	
	Global globalval = new Global();
	Global.ReturnVal retcode = globalval.new ReturnVal();
	
	
	
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
		String intenttype = intent.getAction();
		strTagInfo += "Android NFC Dispatch System dispatch intent: \n" + intenttype + "\n"; 
		if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())){
			bNFCConnected = true;
		}
		tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		strTagInfo += "This Tag Supports as follows: \n";
		for(String techlist : tagFromIntent.getTechList()){
			strTagInfo +=  techlist + "\n";
		}
		tvTagInfo.setText(strTagInfo);
		strTagInfo = "";
		strOPStatus = "检测到NFC Tag";
		etOPStatus.setText(strOPStatus);
		strOPStatus = "";
	}
	
	class ClickEvent implements View.OnClickListener {

		public void onClick(View v) {
			if (v == btnRead8ByteRandomData || v == btnWriteData){
				
				if(v == btnRead8ByteRandomData)
					bRead8ByteRandomData = true;
				else if(v == btnWriteData)
					bWriteData = true;
				
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
			
			new CardTestThread().start();// 开一条线程执行APDU测试
		}
	}
	
	class CardTestThread extends Thread {
		@Override
		public void run() {
			
			
				if(bRead8ByteRandomData){
					byte[] apdu = new byte[5];
					apdu[0] = 0x00;
					apdu[1] = (byte)0x84;
					apdu[2] = 0x00;
					apdu[3] = 0x00;
					apdu[4] = 0x08;
					while(!bNFCConnected){
						strOPStatus = "请将卡靠近手机...";
						etOPStatus.setText(strOPStatus);
					}
					IsoDep isodep = IsoDep.get(tagFromIntent);
					try{
						isodep.connect();
						byte[] sw = isodep.transceive(apdu);
						strSW = bytesToHexString(sw, (sw.length - 2), 2);
						strData = bytesToHexString(sw, 0, 8);
						
						if(strSW.equals("0x9000")){
							strOPStatus = "读8字节随机数成功";
						}
					}catch(Exception e){
						e.printStackTrace();
					}
					
					
				}
				else if(bWriteData){
//					strApduCmdBody = "080880bf010003";
//					strApduData = "31363" + Integer.toString(nNum % 10);
					nNum ++;
					
				}
				
				
				
//				globalval.txDataToMCU(strApduCmdBody, strApduData, true);
				
				
//				retcode = globalval.rxDataFromMCU();

				
				
				

			
			
			handler.post(runnableUi);   

			}
		};	
		
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
		
		//字符序列转换为16进制字符串
		private String bytesToHexString(byte[] src, int startindex, int length) {
			StringBuilder stringBuilder = new StringBuilder("0x");
			if (src == null || src.length <= 0) {
				return null;
			}
			char[] buffer = new char[2];
			for (int i = startindex; i < startindex + length; i++) {
				buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
				buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
				System.out.println(buffer);
				stringBuilder.append(buffer);
			}
			return stringBuilder.toString();
		}
		
		
		
}
