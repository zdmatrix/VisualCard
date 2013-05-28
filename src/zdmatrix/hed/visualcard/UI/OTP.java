package zdmatrix.hed.visualcard.UI;



import java.lang.reflect.Field;

import zdmatrix.hed.visualcard.R;



import android.app.Activity;
import android.app.AlertDialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class OTP extends Activity{
	
	boolean				bVerify = false;
	boolean				bEntryOK = false;
	
//	String 				strAccount;
//	String				strOTPPin;
	
//	EditText			etOTPAccount;
//	EditText			etOTPPin;
	
	Button				btnReturnMain;
	ImageView			imgLoadedImage;
	Bitmap				bitmap;
	Resources			res;
	Handler				handler;
	ProgressDialog 		m_Dialog;
	AlertDialog 		dlg;
	
	Global globalval = new Global();
	Global.ReturnVal retcode = globalval.new ReturnVal();
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maindynamicorder);
        
        
        res = getResources();
        imgLoadedImage = (ImageView)findViewById(R.id.imgLoadedImage);
        bitmap = BitmapFactory.decodeResource(res, R.drawable.codeaple);
        handler = new Handler();
        
        
        
        
        Dialog dialog = new AlertDialog.Builder(OTP.this)
		.setTitle("��½��ʾ")//���ñ���
		.setMessage("�����붯̬���룡")//��������
		.setPositiveButton("ȷ��",//����ȷ����ť
        new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int whichButton)
			{
				//�����ȷ����ת���½��
				
				LayoutInflater factory = LayoutInflater.from(OTP.this);
				//�õ��Զ���Ի���
                final View DialogView = factory.inflate(R.layout.dynamicorderdiglog, null);
				
                //�����Ի���
                
                
                
                dlg = new AlertDialog.Builder(OTP.this)
                .setTitle("��¼��")
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
                    	
//                    	final EditText etOTPPin = (EditText)DialogView.findViewById(R.id.etOTPPin);
                    	
                    	
                    	EditText etOTPPin = (EditText)DialogView.findViewById(R.id.etOTPPin);
                        
                    	String strOTPPin = etOTPPin.getText().toString();
                    	
                    	char[] cotp = strOTPPin.toCharArray();
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
//                    	if((sum % 10) == notp[cotp.length - 1])
                    		
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
//                    				m_Dialog.dismiss();
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
                                	/* �½�һ��Intent���� */
                    				Intent intent = new Intent();
                    				/* ָ��intentҪ�������� */
                    				intent.setClass(OTP.this, MainActivity.class);
                    				/* ����һ���µ�Activity */
                    				startActivity(intent);
                    				/* �رյ�ǰ��Activity */
                    				OTP.this.finish();
                                }
                            })
                            .create();//����
                            
                            
                        	dialogerr.show();//��ʾ
                        	keepDialog(dialog);
//                        	handler.post(runnableKeepDialog);
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
                    	/* �½�һ��Intent���� */
        				Intent intent = new Intent();
        				/* ָ��intentҪ�������� */
        				intent.setClass(OTP.this, MainActivity.class);
        				/* ����һ���µ�Activity */
        				startActivity(intent);
        				/* �رյ�ǰ��Activity */
        				OTP.this.finish();
                    }
                })
                .create();//����
                
                
                	dlg.show();//��ʾ
                
			}
		}).setNeutralButton("�˳�", 
		new DialogInterface.OnClickListener() 
		{
		public void onClick(DialogInterface dialog, int whichButton)
		{
			/* �½�һ��Intent���� */
			Intent intent = new Intent();
			/* ָ��intentҪ�������� */
			intent.setClass(OTP.this, MainActivity.class);
			/* ����һ���µ�Activity */
			startActivity(intent);
			/* �رյ�ǰ��Activity */
			OTP.this.finish();
		}
	}).create();//������ť

        	
	// ��ʾ�Ի���
    dialog.show();
    
	
	
		btnReturnMain = (Button) findViewById(R.id.btnReturnMain);
		btnReturnMain.setOnClickListener(new ClickEvent());
	 
		
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
		}
	}
	
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

}
