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
					/* �½�һ��Intent���� */
					Intent intent = new Intent();
					/* ָ��intentҪ�������� */
					intent.setClass(OTP.this, MainActivity.class);
					/* ����һ���µ�Activity */
					startActivity(intent);
					/* �رյ�ǰ��Activity */
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
			tstDisInfo = Toast.makeText(getApplicationContext(), "��ȷ�����ɵ���ս����·������ϵ���ս��һ�£�\r\n" +
            		"��һ�£��밴�¿��ϰ�ť����Ӧ���룡\r\n", Toast.LENGTH_LONG);
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
					
	                etOTPAnswer = (EditText)DialogView.findViewById(R.id.etOTPAnswer);
	                etOTPAnswer.setText(strAnswerCode);
	                
	                //�����Ի���
	                
	               
	                
	                dlg = new AlertDialog.Builder(OTP.this)
	                .setTitle("ȷ��Ӧ����")
	                .setMessage("��ȷ�Ͽ�����ʾ��Ӧ���������Ի�����Ӧ�����Ƿ�һ��\r\n" +
	                		"��һ�£��밴��ȷ������ť��¼")
	                
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
	                    		Thread.sleep(1000);
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
			tstDisInfo = Toast.makeText(getApplicationContext(), "��¼�ɹ���", Toast.LENGTH_SHORT);
			toastRoot = getLayoutInflater().inflate(R.layout.toast, null);
			tvToast = (TextView)toastRoot.findViewById(R.id.tvToast);
			tvToast.setText("��¼�ɹ���");
			
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

		
			
			
			
			
}
