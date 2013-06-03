package zdmatrix.hed.visualcard.UI;



import java.lang.reflect.Field;

import zdmatrix.hed.visualcard.R;
import zdmatrix.hed.visualcard.DataTypeTrans.DataTypeTrans;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class OTP extends Activity{
	
	boolean				bVerify = false;
	boolean				bEntryOK = false;
	boolean				bNFCConnected = false;
	
//	String 				strAccount;
//	String				strOTPPin;
	
//	EditText			etOTPAccount;
//	EditText			etOTPPin;
	
	int[] 				nRandom = new int[6];
	byte[] 				bRandom = new byte[6];
	String 				strRandom = "";
	
	Button				btnReturnMain;
	Button				btnGenerateChallenge;
	ImageView			imgLoadedImage;
	Bitmap				bitmap;
	Resources			res;
	Handler				handler;
	ProgressDialog 		m_Dialog;
	AlertDialog 		dlg;
	
	Toast				tstDisInfo;
	
	NfcAdapter			nfcAdapter;
	PendingIntent		pendingIntent;
	Tag					tagFromIntent;
	IntentFilter		ndef;
	IntentFilter		tech;
	IntentFilter		tag;
	IntentFilter[]		intentfilter;
	IsoDep				isodep;
	
	
	String[][]			strTechLists;
	String				strSW;
	
	EditText			etOTPAnswer;
	EditText			etOTPChallenge;
	
	Global globalval = new Global();
	Global.ReturnVal retcode = globalval.new ReturnVal();
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maindynamicorder);
        
        
        res = getResources();
        imgLoadedImage = (ImageView)findViewById(R.id.imgLoadedImage);
        bitmap = BitmapFactory.decodeResource(res, R.drawable.codeaple);
        
        btnGenerateChallenge = (Button)findViewById(R.id.btnGenerateChallenge);
        btnGenerateChallenge.setOnClickListener(new ClickEvent());
        
        btnReturnMain = (Button) findViewById(R.id.btnReturnMain);
		btnReturnMain.setOnClickListener(new ClickEvent());
        
		etOTPChallenge = (EditText)findViewById(R.id.etOTPChallenge);
		etOTPAnswer = (EditText)findViewById(R.id.etOTPAnswer);
		
		handler = new Handler();
        
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
		try{
			isodep.connect();
		}catch(Exception e){
			e.printStackTrace();
			tstDisInfo = Toast.makeText(getApplicationContext(), "isodep.connect失败", Toast.LENGTH_LONG);
			tstDisInfo.setGravity(Gravity.CENTER, 0, 0);
			tstDisInfo.show();
		}
	}
	
	class ClickEvent implements View.OnClickListener {

		public void onClick(View v) {
			if (v == btnReturnMain) {
				/* 新建一个Intent对象 */
				Intent intent = new Intent();
				/* 指定intent要启动的类 */
				intent.setClass(OTP.this, MainActivity.class);
				/* 启动一个新的Activity */
				startActivity(intent);
				/* 关闭当前的Activity */
				OTP.this.finish();
			}
			
			if(v == btnGenerateChallenge){
				new OTPThread().start();
			}
		}
	}
	
	public class OTPThread extends Thread{
		@Override
		public void run(){
					if(!bNFCConnected){
						handler.post(runnableDisDisconnect);
					}
					else{
						
//						waitCardButtonPushed();
						
					for(int i = 0; i < 6; i ++){
	            		nRandom[i] = Integer.valueOf((int)(Math.random() * 10));
	            		strRandom += (String.valueOf(nRandom[i]));
	            	}
	                bRandom = DataTypeTrans.intArray2ByteArray(nRandom);
	                
	                handler.post(runnableDialog);
					
	                disNumOnCard(strRandom);
	                
	                strRandom = "";
	                
	                sendOTPCmdToCard();
	                /*
	                try{
	                	Thread.sleep(500);
	                }catch(Exception e){
	                	e.printStackTrace();
	                }
	                */
	                handler.post(runnableInputAnswer);
					}
	                
		}
	                	
	                
		}
	
	Runnable runnableDisDisconnect = new Runnable(){
		@Override
		public void run(){
			tstDisInfo = Toast.makeText(getApplicationContext(), "NFC未检测到，请重新进行NFC连接", Toast.LENGTH_LONG);
			tstDisInfo.setGravity(Gravity.CENTER, 0, 0);
			tstDisInfo.show();
		}
	};
	
	Runnable runnableInputAnswer = new Runnable(){
		@Override
		public void run(){
			
					
					//点击“确定”转向登陆框
					
					
					LayoutInflater factory = LayoutInflater.from(OTP.this);
					//得到自定义对话框
	                final View DialogView = factory.inflate(R.layout.dynamicorderdiglog, null);
					
	                
	                
	                //创建对话框
	                
	               
	                
	                dlg = new AlertDialog.Builder(OTP.this)
	                .setTitle("输入应答码")
	                .setMessage("请输入应答码并按确定登录")
	                
	                .setView(DialogView)//设置自定义对话框的样式
	                .setPositiveButton("确定", //设置"确定"按钮
	                new DialogInterface.OnClickListener() //设置事件监听
	                {
	                    public void onClick(DialogInterface dialog, int whichButton) 
	                    {
	                    	m_Dialog = ProgressDialog.show(OTP.this,
	                    			"请等待.....",
	                    			"正在登录.....",
	                    			true
	                    			);
	                    	
//	                    	final EditText etOTPPin = (EditText)DialogView.findViewById(R.id.etOTPPin);
	                    	
	                    	
	                    	
	                    	EditText etOTPAnswer = (EditText)DialogView.findViewById(R.id.etOTPAnswer);
	                    	
	                    	String strOTPAnswer = etOTPAnswer.getText().toString();
	                    	
	                    	if(strOTPAnswer.length() == 6){
	                    		bVerify = true;
	                    	}else{
	                    		bVerify = false;
	                    	}
/*	                    	
	                    	char[] cotp = strOTPAnswer.toCharArray();
	                    	String[] sotp = new String[cotp.length];
	                    	int[] notp = new int[cotp.length];
	                    	int sum = 0;
	                    	for(int i = 0; i < cotp.length; i ++){
	                    		sotp[i] = String.valueOf(cotp, i, 1);
	                    		notp[i] = Integer.parseInt(sotp[i], 10);
	                    		if(i != (cotp.length - 1)){
	                    			sum += notp[i];
	                    			sum &= 0x0f;
	                    		}
	                    	}
	                    	
	                    	sum &= 0x0f;
	                    	
	                    	if(sum < 9){
	                    		if(sum == notp[cotp.length - 1]){
	                    			bVerify = true;
	                    		}
	                    		else{
	                    			bVerify = false;
	                    		}
	                    	}

	                    		
	                    	else{
	                    		sum += 6;
	                    		sum &= 0x0f;
	                    		if(sum == notp[cotp.length - 1]){
	                    			bVerify = true;
	                    		}
	                    		else{
	                    			bVerify = false;
	                    		}
	                    	}
	                    	
*/
	                    	//输入完成后，点击“确定”开始登陆
	/*                        
	                        new Thread()
	                        { 
	                          public void run()
	                          { 
	                            try
	                            { 
	                         	sleep(1000);
	                              
	                            }
	                            catch (Exception e)
	                            {
	                              e.printStackTrace();
	                            }

	                            finally
	                            {
	                            	//登录结束，取消m_Dialog对话框

	                            	if(bVerify){
	                                	handler.post(runnableDis);
	                                	m_Dialog.dismiss();
	                                }
	                            	else
	                            		keepDialog(m_Dialog);
	                            }

	                          }
	                        }.start();
	*/                       
	                        
	                    	try{
	                    		Thread.sleep(2000);
	                    	}catch(Exception e){
	                    		Log.v("zdmatrix", "err mesage is " + e);
	                    	}
	                    	
	                    	if(!bVerify){
	                    		m_Dialog.dismiss();
	                        	Dialog dialogerr = new AlertDialog.Builder(OTP.this)
	                    		.setTitle("输入错误")//设置标题
	                    		.setMessage("请重新输入动态密码！")//设置内容
	                    		.setPositiveButton("确定",  null//设置确定按钮
	/*
	                    			new DialogInterface.OnClickListener() {
	                    			public void onClick(DialogInterface dialog, int whichButton)
	                    			{
//	                    				m_Dialog.dismiss();
	                                	keepDialog(dialog);
	                    			}
	                    		}
	*/
	                    		)
	                    		.setNegativeButton("取消", //设置“取消”按钮
	                                    new DialogInterface.OnClickListener() 
	                            {
	                                public void onClick(DialogInterface dialog, int whichButton)
	                                {
	                                	Intent intent = new Intent();
	                    				intent.setClass(OTP.this, MainActivity.class);
	                    				startActivity(intent);
	                    				OTP.this.finish();
	                                }
	                            })
	                            .create();//创建
	                            
	                            
	                        	dialogerr.show();//显示
	                        	keepDialog(dialog);
//	                        	handler.post(runnableKeepDialog);
	                        }
	                        else{
	                        	m_Dialog.dismiss();
	                        	closeDialog(dialog);
	                        	handler.post(runnableDis);
	                        }
	                    }
	                })
	                .setNegativeButton("取消", //设置“取消”按钮
	                new DialogInterface.OnClickListener() 
	                {
	                    public void onClick(DialogInterface dialog, int whichButton)
	                    {
	                    	Intent intent = new Intent();
	        				intent.setClass(OTP.this, MainActivity.class);
	        				startActivity(intent);
	        				OTP.this.finish();
	                    }
	                })
	                .create();//创建
	                
	                
	                	dlg.show();//显示
	                
				

		}
		
	};
	
	Runnable runnableDialog = new Runnable(){
		@Override
		public void run(){
			
			etOTPChallenge.setText(strRandom);
			
		}
	};
	
	Runnable runnableDis = new Runnable(){
		@Override
		public void run(){
			imgLoadedImage.setImageBitmap(bitmap);
		}
	};
	
	Runnable runnableKeepDialog = new Runnable(){
		@Override
		public void run(){
			keepDialog(dlg);
		}
	};
	
	private void keepDialog(DialogInterface mDialogInterface){ 

		   if(mDialogInterface!=null){ 

		      try { 

		         Field field = mDialogInterface.getClass() .getSuperclass().getDeclaredField( "mShowing" ); 

		         field.setAccessible( true ); // 将mShowing变量设为false，表示对话框已关闭 

		         field.set(mDialogInterface, false ); 

		      } catch (Exception e){ 

		         e.printStackTrace(); 

		      } 

		   } 

		} 

		

		private void closeDialog(DialogInterface mDialogInterface){ 

		    if(mDialogInterface!=null){

		       try { 

		          Field field = mDialogInterface.getClass() .getSuperclass().getDeclaredField( "mShowing" ); 

		          field.setAccessible( true );

		          field.set(mDialogInterface, true ); 

		          mDialogInterface.dismiss(); 

		       }catch (Exception e){ 

		          e.printStackTrace(); 

		       } 

		    } 

		}

		public boolean disNumOnCard(String authcode){

			boolean bret = false;
			String apducmd = "80bf010006";
			byte[] apdu = new byte[11];
			byte[] apdu1 = DataTypeTrans.stringHexToByteArray(apducmd);
			byte[] apdu2 = DataTypeTrans.stringDecToByteArray(authcode);
			System.arraycopy(apdu1, 0, apdu, 0, apdu1.length);
			System.arraycopy(apdu2, 0, apdu, apdu1.length, apdu2.length);
			try{
				if(!isodep.isConnected()){
					isodep.connect();
				}

//				isodep.setTimeout(1000);

				byte[] ret = isodep.transceive(apdu);
				strSW = bytesToHexString(ret, ret.length - 2, 2);
				
				if(strSW.equals("9000")){
					bret = true;
					
				}
				return bret;
			}catch(Exception e){
				e.printStackTrace();
				bret = false;
				return bret;
			}
		}
			
			//字符序列转换为16进制字符串
			private String bytesToHexString(byte[] src, int startindex, int length) {
				StringBuilder stringBuilder = new StringBuilder("");
				if (src == null || src.length <= 0) {
					return null;
				}
				char[] buffer = new char[2];
				for (int i = startindex; i < startindex + length; i++) {
					buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
					buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
//					System.out.println(buffer);
					stringBuilder.append(buffer);
				}
				return stringBuilder.toString();
			}
			
			public void sendOTPCmdToCard(){
				boolean bret = false;
				String apducmd = "80bf050000";
				byte[] apdu = DataTypeTrans.stringHexToByteArray(apducmd);
				try{
					if(!isodep.isConnected()){
						isodep.connect();
					}
					isodep.setTimeout(30000);
					byte[] ret = isodep.transceive(apdu);
					strSW = DataTypeTrans.byteToString(ret, ret.length - 2, 2);
					if(strSW.equals("9000")){
						bret = true;
					}
				}catch(Exception e){
					e.printStackTrace();
					
				}
			}
			
			public boolean waitCardButtonPushed(){
				boolean bret = false;
				String apducmd = "80bf060000";
				byte[] apdu = DataTypeTrans.stringHexToByteArray(apducmd);
				try{
					if(!isodep.isConnected()){
						isodep.connect();
					}
					
					byte[] ret = isodep.transceive(apdu);
					strSW = bytesToHexString(ret, ret.length - 2, 2);
					
					if(strSW.equals("9000")){
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
