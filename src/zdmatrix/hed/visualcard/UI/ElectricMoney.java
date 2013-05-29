package zdmatrix.hed.visualcard.UI;


import zdmatrix.hed.visualcard.R;
import zdmatrix.hed.visualcard.UI.Global.ReturnVal;
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
	
	boolean 			bNFCConnected = false;//是否录放的标记
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
	
	NfcAdapter			nfcAdapter;
	PendingIntent		pendingIntent;
	Tag					tagFromIntent;
	IntentFilter		ndef;
	IntentFilter		tech;
	IntentFilter		tag;
	IntentFilter[]		intentfilter;
	IsoDep				isodep;
	
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
	
	Global globalval = new Global();
	Global.ReturnVal retcode = globalval.new ReturnVal();
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
		etRecharge.setHint("请输入充值金额");
		
		etConsume.setOnKeyListener(new EditText.OnKeyListener() {
			public boolean onKey(View arg0, int arg1, KeyEvent arg2)
			{
				strConsumeData = etConsume.getText().toString();
				
				return false;
			}
		});
		etConsume.setHint("请输入消费金额");


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
        	tstDisInfo = Toast.makeText(getApplicationContext(), "该设备不支持NFC", Toast.LENGTH_LONG);
        	tstDisInfo.setGravity(Gravity.CENTER, 0, 0);
            tstDisInfo.show();
        }else if(!nfcAdapter.isEnabled()){
        	tstDisInfo = Toast.makeText(getApplicationContext(), "请在系统设置里打开NFC功能", Toast.LENGTH_LONG);
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
		if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())){
			bNFCConnected = true;
			tstDisInfo = Toast.makeText(getApplicationContext(), "检测到NFC Tag", Toast.LENGTH_LONG);
			tstDisInfo.setGravity(Gravity.CENTER, 0, 0);
			tstDisInfo.show();
		}
		tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		isodep = IsoDep.get(tagFromIntent);
	}
	
	class ClickEvent implements View.OnClickListener {

		public void onClick(View v) {
			if (v == btnRecharge || v == btnConsume || v == btnReadBanlance){
				if(!bNFCConnected){
					tstDisInfo = Toast.makeText(getApplicationContext(), "请把卡放置到有效距离内", Toast.LENGTH_LONG);
					tstDisInfo.setGravity(Gravity.CENTER, 0, 0);
					tstDisInfo.show();
				}else{
				if(v == btnRecharge)
					bRecharge = true;
				else if(v == btnConsume)
					bConsume = true;
				else if(v == btnReadBanlance)
					bReadBanlance = true;
				new ElectriMoneyThread().start();// 开一条线程执行APDU测试
				} 
			}
			else if (v == btnReturnMain) {
				/* 新建一个Intent对象 */
				Intent intent = new Intent();
				/* 指定intent要启动的类 */
				intent.setClass(ElectricMoney.this, MainActivity.class);
				/* 启动一个新的Activity */
				startActivity(intent);
				/* 关闭当前的Activity */
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
					}
					else{
					Global.nBanlanceCash += Integer.parseInt(strRechargeData, 10);
					bRecharge = false;
					handler.post(runnableConfirmDialog);
					/*
					retcode = globalval.waitPushCardButton();
					if(retcode.bLogic){
						globalval.disBanlanceOnCard(Global.nBanlanceCash);
						handler.post(runnableDisCardBanlance);
					}
					*/
					}
				}
				else if(bConsume){
					if(Integer.parseInt(strConsumeData, 10) > Global.nBanlanceCash){
						handler.post(runnableConsumeWarningDialog);
						
					}
					else{
/*
						int length = strConsumeData.length() + 5;
						strApduCmdBody = "08" + "0" + Integer.toHexString(length) 
							+ "80bf0100" + "0" + Integer.toHexString(length - 5);
						globalval.txDataToMCU(strApduCmdBody, strConsumeData, false);
						retcode = globalval.rxDataFromMCU();	
*/
						bConsume = false;
						Global.nBanlanceCash -= Integer.parseInt(strConsumeData, 10);
						handler.post(runnableConfirmDialog);
						/*
						retcode = globalval.waitPushCardButton();
						if(retcode.bLogic){
							globalval.disBanlanceOnCard(Global.nBanlanceCash);
							handler.post(runnableDisCardBanlance);
						}
						*/
					}
				}
				else if(bReadBanlance){
					bReadBanlance = false;
					/*读卡中透明文件存储的余额值
					 * 选择卡文件：00A4000002 + FILEID
					 * 读数据：00B0000004 
					 * 返回：读取正常返回9000
					 */
					
					retcode = globalval.readBanlance("00bf", 0, 0, 4);
					if(retcode.bLogic){
			        Global.nBanlanceCash = Integer.parseInt(retcode.strRetData, 16);
			        
//			        retcode = globalval.disBanlanceOnCard(Global.nBanlanceCash);
			        retcode.strRetInfo = "读余额成功";
					}
					else{
						retcode.strRetInfo = "读余额错误";
			        
					}
					handler.post(runnableDisCardBanlance);
				}
				
			}
//			}
		};	
		
		class WatiButtonThread extends Thread {
			@Override
			public void run() {
				retcode = globalval.waitPushCardButton();
				if(retcode.bLogic){
					bPushButton = true;
				}
				else
					bPushButton = false;
				
				while(bPushButton){
					retcode = globalval.disBanlanceOnCard(Global.nBanlanceCash);
					handler.post(runnableDisCardBanlance);
					bPushButton = false;
				}
			};	
		}
		
		
		Runnable runnableDisToast = new Runnable(){
			@Override
			public void run(){
				
				tstDisInfo = Toast.makeText(getApplicationContext(),"消费金额大于余额，请重新输入", Toast.LENGTH_LONG);
				tstDisInfo.setGravity(Gravity.CENTER, 0, 0);
				tstDisInfo.show();
				

			}
		};
		
		Runnable runnableDisCardBanlance = new Runnable(){
			@Override
			public void run(){
				
				if(retcode.bLogic)
					etBanlance.setText(Integer.toString(Global.nBanlanceCash));
				else
					etBanlance.setText("读余额错误");
			}
		};
		
		Runnable runnableDisBanlance = new Runnable(){
			@Override
			public void run(){
				
				etBanlance.setText(Integer.toString(Global.nBanlanceCash));
			}
		};
		
		Runnable runnableRechargeWarningDialog = new Runnable(){
			@Override
			public void run(){
				dlogWarning = new AlertDialog.Builder(ElectricMoney.this)
				.setTitle("输入错误")
				.setMessage("充值金额过大，请不要超过1000！请重新输入")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
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
				.setTitle("输入错误")
				.setMessage("消费金额大于余额！请重新输入")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
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
				.setTitle("确认交易")
				.setMessage("确认交易？")
				.setPositiveButton("确认", 
				new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						pdlogProcess = ProgressDialog.show(ElectricMoney.this, "请按下可视卡上按钮", "", true);
						new Thread(){
							public void run(){
								byte[] apdubtn = new byte[5];
								apdubtn[0] = (byte) 0x80;
								apdubtn[1] = (byte) 0xbf;
								apdubtn[2] = (byte) 0x06;
								apdubtn[3] = (byte) 0x00;
								apdubtn[4] = (byte) 0x00;
								try{
									isodep.connect();
									byte[] sw = isodep.transceive(apdubtn);
									strSW = bytesToHexString(sw, (sw.length - 2), 2);
									if(strSW.equals("0x9000")){
										byte[] apduselect = new byte[7];
										apduselect[0] = 0x00;
										apduselect[1] = (byte) 0xa4;
										apduselect[2] = 0x00;
										apduselect[3] = 0x00;
										apduselect[4] = 0x02;
										apduselect[5] = 0x00;
										apduselect[6] = (byte) 0xbf;
										try{
											isodep.connect();
											sw = isodep.transceive(apduselect);
											strSW = bytesToHexString(sw, (sw.length - 2), 2);
											if(strSW.equals("0x9000")){
												String str = Integer.toString(Global.nBanlanceCash, 16); 
											}
											isodep.close();
										}catch(Exception e){
											e.printStackTrace();
										}
									}
									isodep.close();
								}catch(Exception e){
									e.printStackTrace();
								}
								finally{
									pdlogProcess.dismiss();
								}
							}
						}.start();
					}
				})
				.setNeutralButton("取消",
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
//						System.out.println(buffer);
						stringBuilder.append(buffer);
					}
					return stringBuilder.toString();
				}
				
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
					
					apducmd = "080" + Integer.toString(length + 5, 16) + "00d6" + stroffsethigh + stroffsetlow + "0" + Integer.toString(length, 16);
					
					if(len < (length * 2)){
						while(index < (length * 2 - len)){
							data = "0" + data;
							index ++;
						}
					}
					
					String str = apducmd + data;
					byte[]	apdudisplayoncard = new byte[str.length() / 2];
					for(int i = 0; i < apdudisplayoncard.length; i ++){
						apdudisplayoncard[i] = 
					}
					txDataToMCU(apducmd, data, true);
					ReturnVal retcode = rxDataFromMCU();
					
					String sw= intToString(retcode.nData, retcode.nLength - 2, retcode.nLength);
					if(!sw.equals("9000"))
						retcode.bLogic = false;
					else{
						
						retcode.bLogic = true;
					}
					
				}
}
