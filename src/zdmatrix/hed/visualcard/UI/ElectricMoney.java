package zdmatrix.hed.visualcard.UI;


import zdmatrix.hed.visualcard.R;
import zdmatrix.hed.visualcard.DataCommunication.NFCCommunication;
import zdmatrix.hed.visualcard.UI.NFCObject;
import zdmatrix.hed.visualcard.FunctionMode.FunctionMode;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ElectricMoney extends Activity{
	
	boolean 			bNFCConnected = false;
	boolean				bRecharge = false;
	boolean				bConsume = false;
	boolean				bPushButton = false;
	boolean				bReadBanlance = false;
	
	String 				strSW;
	String				strData;
	String				strBanlance;
	String				strRechargeData;
	String				strConsumeData;
	String[][]			strTechLists;
	
	int					nBanlanceCash;
	
	byte[] 				banlance = new byte[4];
	
	NfcAdapter			nfcAdapter;
	PendingIntent		pendingIntent;
	Tag					tagFromIntent;
	IntentFilter		ndef;
	IntentFilter		tech;
	IntentFilter		tag;
	IntentFilter[]		intentfilter;
	IsoDep				isodep = null;
	
	TextView			tvOpStatus;
	TextView			tvReturnData1;
	TextView			tvReturnData2;
	TextView			tvReturnSW;
	
	EditText			etRecharge;
	EditText    		etConsume;
	EditText    		etBanlance;
	
	Button 				btnReturnMain;
	Button 				btnRecharge;
	Button 				btnConsume;
	Button				btnReadBanlance;
	
	Dialog				dlogWarning;
	Dialog				dlogConfirm;
	ProgressDialog		pdlogProcess;		
	
	Toast				tstDisInfo;
	Handler				handler = null;
	
	NFCObject 			nfcobject;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainelectricmoney);
        
        handler = new Handler();
        
        etRecharge = (EditText)findViewById(R.id.etRecharge);
        etConsume = (EditText)findViewById(R.id.etConsume);
        etBanlance = (EditText)findViewById(R.id.etBanlance);
        
		etRecharge.setOnKeyListener(new EditText.OnKeyListener() {
			public boolean onKey(View arg0, int arg1, KeyEvent arg2)
			{
				strRechargeData = etRecharge.getText().toString();
				
				return false;
			}
		});
		etRecharge.setHint("�������ֵ���");
		
		etConsume.setOnKeyListener(new EditText.OnKeyListener() {
			public boolean onKey(View arg0, int arg1, KeyEvent arg2)
			{
				strConsumeData = etConsume.getText().toString();
				
				return false;
			}
		});
		etConsume.setHint("���������ѽ��");


		btnRecharge = (Button) findViewById(R.id.btnRecharge);
		btnRecharge.setOnClickListener(new ClickEvent());
//		btnRecharge.setEnabled(false);
        
        btnReturnMain = (Button) findViewById(R.id.btnReturnMain);
        btnReturnMain.setOnClickListener(new ClickEvent());
        
        btnConsume = (Button) findViewById(R.id.btnConsume);
        btnConsume.setOnClickListener(new ClickEvent());
//      btnConsume.setEnabled(false);
        
        btnReadBanlance = (Button)findViewById(R.id.btnReadBanlance);
        btnReadBanlance.setOnClickListener(new ClickEvent());
        
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(nfcAdapter == null){
        	tstDisInfo = Toast.makeText(getApplicationContext(), "���豸��֧��NFC", Toast.LENGTH_SHORT);
        	tstDisInfo.setGravity(Gravity.CENTER, 0, 0);
            tstDisInfo.show();
        }else if(!nfcAdapter.isEnabled()){
        	tstDisInfo = Toast.makeText(getApplicationContext(), "����ϵͳ�������NFC����", Toast.LENGTH_SHORT);
        	tstDisInfo.setGravity(Gravity.CENTER, 0, 0);
            tstDisInfo.show();
        }
        
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        
        ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        tech = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        tag = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        
        intentfilter = new IntentFilter[]{ndef, tech, tag};
        
        strTechLists = new String[][]{new String[]{IsoDep.class.getName(),
        		MifareClassic.class.getName(), NfcA.class.getName(), NfcB.class.getName()}};
        
        nfcobject = (NFCObject)getApplicationContext();
        if(isodep == null){
        	isodep = nfcobject.getNFCObject();
        }
        	
    }
	
	@Override
	public void onResume(){
		super.onResume();
		nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentfilter, strTechLists);
	}
	
	@Override
	public void onPause(){
		super.onPause();
		nfcAdapter.disableForegroundDispatch(this);
	}
	
	@Override
	public void onNewIntent(Intent intent){
		isodep = NFCCommunication.nfcConnectInit(intent, getApplicationContext());
		nfcobject.setNFCObject(isodep);
	}
	
	class ClickEvent implements View.OnClickListener {

		public void onClick(View v) {
			if (v == btnRecharge || v == btnConsume || v == btnReadBanlance){
				if(isodep == null){
					NFCCommunication.nfcConnectFailed(getApplicationContext());
				}else {
					if(v == btnRecharge)
						bRecharge = true;
					else if(v == btnConsume)
						bConsume = true;
					else if(v == btnReadBanlance)
						bReadBanlance = true;
					new ElectriMoneyThread().start();// ��һ���߳�ִ��APDU����
				} 
			}
			if (v == btnReturnMain) {
				/* �½�һ��Intent���� */
				Intent intent = new Intent();
				/* ָ��intentҪ�������� */
				intent.setClass(ElectricMoney.this, MainActivity.class);
				/* ����һ���µ�Activity */
				startActivity(intent);
				/* �رյ�ǰ��Activity */
				ElectricMoney.this.finish();
			}
		}
	}
	
	class ElectriMoneyThread extends Thread {
		@Override
		public void run() {
				
			if(bRecharge){
					
				if(1000 < Integer.parseInt(strRechargeData, 10)){
					handler.post(runnableRechargeWarningDialog);
				}else{
					nBanlanceCash += Integer.parseInt(strRechargeData, 10);
					bRecharge = false;
					handler.post(runnableConfirmDialog);
					
				}
			}else if(bConsume){
				if(Integer.parseInt(strConsumeData, 10) > nBanlanceCash){
					handler.post(runnableConsumeWarningDialog);
						
				}else{
					bConsume = false;
					nBanlanceCash -= Integer.parseInt(strConsumeData, 10);
					handler.post(runnableConfirmDialog);
				}
			}else if(bReadBanlance){
				bReadBanlance = false;
					/*������͸���ļ��洢�����ֵ
					 * ѡ���ļ���00A4000002 + FILEID
					 * �����ݣ�00B0000004 
					 * ���أ���ȡ��������9000
					 */
				if(FunctionMode.selectFile("00bf", isodep)){
					if((nBanlanceCash = FunctionMode.readSelectFileData(isodep)) >= 0){
						handler.post(runnableDisCardBanlance);
					}else{
						if(!isodep.isConnected()){
							handler.post(runnableDisconnect);
						}
					}
				}else{
					if(!isodep.isConnected()){
						handler.post(runnableDisconnect);
					}
				}
				
			}
		}
	};	
	
	Runnable runnableDisconnect = new Runnable(){
		@Override
		public void run(){
			NFCCommunication.nfcConnectFailed(getApplicationContext());
				
		}
	};
	
	Runnable runnableDisCardBanlance = new Runnable(){
		@Override
		public void run(){
			etBanlance.setText(Integer.toString(nBanlanceCash));
				
		}
	};
		
	Runnable runnableRechargeWarningDialog = new Runnable(){
		@Override
		public void run(){
			dlogWarning = new AlertDialog.Builder(ElectricMoney.this)
			.setTitle("�������")
			.setMessage("��ֵ�������벻Ҫ����1000������������")
			.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					pdlogProcess = ProgressDialog.show(ElectricMoney.this, "", "", true);
					new Thread(){
						public void run(){
							pdlogProcess.dismiss();
						}
					}.start();
				}
			}).create();
			dlogWarning.show();
		}
	};
		
	Runnable runnableConsumeWarningDialog = new Runnable(){
		@Override
		public void run(){
			dlogWarning = new AlertDialog.Builder(ElectricMoney.this)
			.setTitle("�������")
			.setMessage("���ѽ�����������������")
			.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				pdlogProcess = ProgressDialog.show(ElectricMoney.this, "", "", true);
					new Thread(){
						public void run(){
							pdlogProcess.dismiss();
						}
					}.start();
				}
			}).create();
			dlogWarning.show();
		}
	};
		
	Runnable runnableConfirmDialog = new Runnable(){
		@Override
		public void run(){
			dlogConfirm = new AlertDialog.Builder(ElectricMoney.this)
			.setTitle("ȷ�Ͻ���")
			.setMessage("ȷ�Ͻ��ף�")
			.setPositiveButton("ȷ��", 
			new DialogInterface.OnClickListener() {
					
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
//					pdlogProcess = ProgressDialog.show(ElectricMoney.this, "�밴�¿��ӿ��ϰ�ť", "", true);
					new Thread(){
						public void run(){
							if(FunctionMode.selectFile("00bf", isodep)){
								String str = Integer.toString(nBanlanceCash, 16); 
								FunctionMode.updateSelectFileData(str, isodep);
							}else{
								if(!isodep.isConnected()){
									NFCCommunication.nfcConnectFailed(getApplicationContext());
								}
							}
						}
					}.start();
				}
			})
			.setNeutralButton("ȡ��",
			new DialogInterface.OnClickListener() {
					
				@Override
				public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
/*
						pdlogProcess = ProgressDialog.show(NewUKey.this, "", "", true);
						new Thread(){
							public void run(){
								pdlogProcess.dismiss();
							}
						}.start();
*/
				}
			}).create();
				
			Window dlogconfirmWindows = dlogConfirm.getWindow();
			WindowManager.LayoutParams lp = dlogconfirmWindows.getAttributes();
			dlogconfirmWindows.setGravity(Gravity.LEFT | Gravity.TOP);
			lp.x = 20;
			lp.y = 0;
			dlogconfirmWindows.setAttributes(lp);
				
			dlogConfirm.show();
		}
	};
		
}