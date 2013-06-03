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
        	tstDisInfo = Toast.makeText(getApplicationContext(), "���豸��֧��NFC", Toast.LENGTH_LONG);
        	tstDisInfo.setGravity(Gravity.CENTER, 0, 0);
            tstDisInfo.show();
        }else if(!nfcAdapter.isEnabled()){
        	tstDisInfo = Toast.makeText(getApplicationContext(), "����ϵͳ�������NFC����", Toast.LENGTH_LONG);
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
			tstDisInfo = Toast.makeText(getApplicationContext(), "��⵽NFC Tag", Toast.LENGTH_LONG);
			tstDisInfo.setGravity(Gravity.CENTER, 0, 0);
			tstDisInfo.show();
		}
		tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		isodep = IsoDep.get(tagFromIntent);
		try{
			isodep.connect();
		}catch(Exception e){
			e.printStackTrace();
			tstDisInfo = Toast.makeText(getApplicationContext(), "isodep.connectʧ��", Toast.LENGTH_LONG);
			tstDisInfo.setGravity(Gravity.CENTER, 0, 0);
			tstDisInfo.show();
		}
	}
	
	class ClickEvent implements View.OnClickListener {

		public void onClick(View v) {
			if (v == btnReturnMain) {
				/* �½�һ��Intent���� */
				Intent intent = new Intent();
				/* ָ��intentҪ�������� */
				intent.setClass(OTP.this, MainActivity.class);
				/* ����һ���µ�Activity */
				startActivity(intent);
				/* �رյ�ǰ��Activity */
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
			tstDisInfo = Toast.makeText(getApplicationContext(), "NFCδ��⵽�������½���NFC����", Toast.LENGTH_LONG);
			tstDisInfo.setGravity(Gravity.CENTER, 0, 0);
			tstDisInfo.show();
		}
	};
	
	Runnable runnableInputAnswer = new Runnable(){
		@Override
		public void run(){
			
					
					//�����ȷ����ת���½��
					
					
					LayoutInflater factory = LayoutInflater.from(OTP.this);
					//�õ��Զ���Ի���
	                final View DialogView = factory.inflate(R.layout.dynamicorderdiglog, null);
					
	                
	                
	                //�����Ի���
	                
	               
	                
	                dlg = new AlertDialog.Builder(OTP.this)
	                .setTitle("����Ӧ����")
	                .setMessage("������Ӧ���벢��ȷ����¼")
	                
	                .setView(DialogView)//�����Զ���Ի������ʽ
	                .setPositiveButton("ȷ��", //����"ȷ��"��ť
	                new DialogInterface.OnClickListener() //�����¼�����
	                {
	                    public void onClick(DialogInterface dialog, int whichButton) 
	                    {
	                    	m_Dialog = ProgressDialog.show(OTP.this,
	                    			"��ȴ�.....",
	                    			"���ڵ�¼.....",
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
	                    	//������ɺ󣬵����ȷ������ʼ��½
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
	                            	//��¼������ȡ��m_Dialog�Ի���

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
	                    		.setTitle("�������")//���ñ���
	                    		.setMessage("���������붯̬���룡")//��������
	                    		.setPositiveButton("ȷ��",  null//����ȷ����ť
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
	                    		.setNegativeButton("ȡ��", //���á�ȡ������ť
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
	                            .create();//����
	                            
	                            
	                        	dialogerr.show();//��ʾ
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
	                .setNegativeButton("ȡ��", //���á�ȡ������ť
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
	                .create();//����
	                
	                
	                	dlg.show();//��ʾ
	                
				

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

		         field.setAccessible( true ); // ��mShowing������Ϊfalse����ʾ�Ի����ѹر� 

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
			
			//�ַ�����ת��Ϊ16�����ַ���
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
