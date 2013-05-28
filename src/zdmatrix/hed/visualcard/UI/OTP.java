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
		.setTitle("登陆提示")//设置标题
		.setMessage("请输入动态密码！")//设置内容
		.setPositiveButton("确定",//设置确定按钮
        new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int whichButton)
			{
				//点击“确定”转向登陆框
				
				LayoutInflater factory = LayoutInflater.from(OTP.this);
				//得到自定义对话框
                final View DialogView = factory.inflate(R.layout.dynamicorderdiglog, null);
				
                //创建对话框
                
                
                
                dlg = new AlertDialog.Builder(OTP.this)
                .setTitle("登录框")
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
//                    				m_Dialog.dismiss();
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
                                	/* 新建一个Intent对象 */
                    				Intent intent = new Intent();
                    				/* 指定intent要启动的类 */
                    				intent.setClass(OTP.this, MainActivity.class);
                    				/* 启动一个新的Activity */
                    				startActivity(intent);
                    				/* 关闭当前的Activity */
                    				OTP.this.finish();
                                }
                            })
                            .create();//创建
                            
                            
                        	dialogerr.show();//显示
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
                .setNegativeButton("取消", //设置“取消”按钮
                new DialogInterface.OnClickListener() 
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                    	/* 新建一个Intent对象 */
        				Intent intent = new Intent();
        				/* 指定intent要启动的类 */
        				intent.setClass(OTP.this, MainActivity.class);
        				/* 启动一个新的Activity */
        				startActivity(intent);
        				/* 关闭当前的Activity */
        				OTP.this.finish();
                    }
                })
                .create();//创建
                
                
                	dlg.show();//显示
                
			}
		}).setNeutralButton("退出", 
		new DialogInterface.OnClickListener() 
		{
		public void onClick(DialogInterface dialog, int whichButton)
		{
			/* 新建一个Intent对象 */
			Intent intent = new Intent();
			/* 指定intent要启动的类 */
			intent.setClass(OTP.this, MainActivity.class);
			/* 启动一个新的Activity */
			startActivity(intent);
			/* 关闭当前的Activity */
			OTP.this.finish();
		}
	}).create();//创建按钮

        	
	// 显示对话框
    dialog.show();
    
	
	
		btnReturnMain = (Button) findViewById(R.id.btnReturnMain);
		btnReturnMain.setOnClickListener(new ClickEvent());
	 
		
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
