package zdmatrix.hed.visualcard.UI;



import java.lang.reflect.Field;

import zdmatrix.hed.visualcard.R;
//import jbd.visualcard.R;
import zdmatrix.hed.visualcard.DataCommunication.NFCCommunication;
import zdmatrix.hed.visualcard.UI.NFCObject;
import zdmatrix.hed.visualcard.DataTypeTrans.DataTypeTrans;
import zdmatrix.hed.visualcard.FunctionMode.FunctionMode;



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
import android.widget.TextView;
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
	byte[]				authcode = new byte[6];
	String 				strRandom = "";
	String 				strAnswerCode = "";
	
	Button				btnReturnMain;
	Button				btnGenerateChallenge;
	ImageView			imgLoadedImage;
	Bitmap				bitmap;
	Bitmap				bitmap1;
	Bitmap				bitmap2;
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
	IsoDep				isodep = null;
	
	
	String[][]			strTechLists;
	String				strSW;
	
	EditText			etOTPAnswer;
	EditText			etOTPChallenge;
	
	NFCObject 			nfcobject;
	View				toastRoot;
	TextView			tvToast;
	
	int					clicknum = 0;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maindynamicorder);
        
        
        res = getResources();
        imgLoadedImage = (ImageView)findViewById(R.id.imgLoadedImage);
        bitmap = BitmapFactory.decodeResource(res, R.drawable.codeaple);
        bitmap1 = BitmapFactory.decodeResource(res, R.drawable.bmp1);
        bitmap2 = BitmapFactory.decodeResource(res, R.drawable.bmp2);
        
        btnGenerateChallenge = (Button)findViewById(R.id.btnGenerateChallenge);
        btnGenerateChallenge.setOnClickListener(new ClickEvent());
        
        btnReturnMain = (Button) findViewById(R.id.btnReturnMain);
		btnReturnMain.setOnClickListener(new ClickEvent());
        
		etOTPChallenge = (EditText)findViewById(R.id.etOTPChallenge);
//		etOTPAnswer = (EditText)findViewById(R.id.etOTPAnswer);
		
		
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
        
        nfcobject = (NFCObject)getApplicationContext();
        
        if(isodep == null)
        	isodep = nfcobject.getNFCObject();
	 
		
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
			if(v == btnGenerateChallenge){
				if(isodep == null){
					NFCCommunication.nfcConnectFailed(getApplicationContext());
				}else{
					new OTPThread().start();
				}
				
			}
			
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
			
				
			
		}
	}
	
	public class OTPThread extends Thread{
		@Override
		public void run(){
					
						
//						waitCardButtonPushed();
//					handler.post(runnableGone);
			clicknum ++;
					for(int i = 0; i < 6; i ++){
	            		nRandom[i] = Integer.valueOf((int)(Math.random() * 10));
	            		strRandom += (String.valueOf(nRandom[i]));
	            	}
	                bRandom = DataTypeTrans.intArray2ByteArray(nRandom);
	                
	                handler.post(runnableDialog);
	                try{
						Thread.sleep(500);
					}catch(Exception e){
						e.printStackTrace();
					}
	                FunctionMode.disNumOnCard(strRandom, isodep);
	                
	                strRandom = "";
	                
//	                FunctionMode.sendOTPCmdToCard(isodep);
	                FunctionMode.getAuthCode(isodep, authcode);
	                handler.post(runnableGuid);
	                

	                if(FunctionMode.waitCardButtonPushed(isodep)){
	                	try{
							Thread.sleep(500);
						}catch(Exception e){
							e.printStackTrace();
						}
	                	for(int i = 0; i < authcode.length; i ++){
	                		strAnswerCode += String.valueOf((int)(authcode[i] - 0x30));
	                	}
//	                	strAnswerCode = DataTypeTrans.bytesArrayToHexString(authcode, 0, authcode.length);
	                	FunctionMode.disNumOnCard(strAnswerCode, isodep);
//	                	handler.post(runnableDisAnswerCode);
	                	
	                }
	                handler.post(runnableInputAnswer);
					
	                
		}
	                	
	                
		}
	
	Runnable runnableDisAnswerCode = new Runnable(){
		@Override
		public void run(){
			etOTPAnswer.setText(strAnswerCode);
		}
	};
	
	Runnable runnableGuid = new Runnable(){
		@Override
		public void run(){
			tstDisInfo = Toast.makeText(getApplicationContext(), "请确认生成的挑战码和下发到卡上的挑战码一致！\r\n" +
            		"若一致，请按下卡上按钮生成应答码！\r\n", Toast.LENGTH_LONG);
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
					
	                etOTPAnswer = (EditText)DialogView.findViewById(R.id.etOTPAnswer);
	                etOTPAnswer.setText(strAnswerCode);
	                
	                //创建对话框
	                
	               
	                
	                dlg = new AlertDialog.Builder(OTP.this)
	                .setTitle("确认应答码")
	                .setMessage("请确认卡上显示的应答码和下面对话框中应答码是否一致\r\n" +
	                		"若一致，请按“确定”按钮登录")
	                
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
	                    	
	                    	
	                    	
//	                    	EditText etOTPAnswer = (EditText)DialogView.findViewById(R.id.etOTPAnswer);
	                    	
//	                    	String strOTPAnswer = etOTPAnswer.getText().toString();
//	                    	etOTPAnswer.setText(strAnswerCode);
	                    	if(strAnswerCode.length() == 6){
	                    		bVerify = true;
	                    	}else{
	                    		bVerify = false;
	                    	}
	                    	strAnswerCode = "";
	                    	handler.post(disSucesseMsg);
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
	                    		Thread.sleep(1000);
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
	
	Runnable runnableGone = new Runnable(){
		@Override
		public void run(){
			imgLoadedImage.setVisibility(ImageView.GONE);
			
		}
	};
	
	Runnable runnableDis = new Runnable(){
		@Override
		public void run(){
			switch(clicknum % 3){
			case 0:
				imgLoadedImage.setImageBitmap(bitmap);
				break;
			case 1:
				imgLoadedImage.setImageBitmap(bitmap1);
				break;
			case 2:
				imgLoadedImage.setImageBitmap(bitmap2);
				break;
			default:
				break;
			}
			
			
		}
	};
	
	Runnable disSucesseMsg = new Runnable(){
		@Override
		public void run(){
			
			tstDisInfo = new Toast(getApplicationContext()); 
			tstDisInfo = Toast.makeText(getApplicationContext(), "登录成功！", Toast.LENGTH_SHORT);
			toastRoot = getLayoutInflater().inflate(R.layout.toast, null);
			tvToast = (TextView)toastRoot.findViewById(R.id.tvToast);
			tvToast.setText("登录成功！");
			
			tstDisInfo.setDuration(Toast.LENGTH_LONG); 
			tstDisInfo.setGravity(Gravity.CENTER, 0, 0);
			tstDisInfo.setView(toastRoot);
			tstDisInfo.show();
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

		
			
			
			
			
}
