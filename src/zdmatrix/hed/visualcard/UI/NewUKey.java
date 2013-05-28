package zdmatrix.hed.visualcard.UI;


import zdmatrix.hed.visualcard.R;
import zdmatrix.hed.visualcard.Alg.EncryptIn3DES;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.drawable.BitmapDrawable;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
//import android.view.LayoutInflater;
import android.view.View;
//import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


public class NewUKey extends Activity{
	
	boolean 			bTxDataToCard = false;
	boolean				bReturnMain = false;
	boolean				bGenerateRSAKey = false;
	boolean				bRSAKey = false;
	boolean				bGetImage = false;
	boolean				bReadBanlance = false;
	boolean				bReadBanlanceErr = false;
	
	String 				strSrcAccount;
	String				strDstAccount;
	String				strRechargeData;
	String				strApduCmdBody;
	String				strTxdata;
	String				strAuthCode;
	String				strBanlanceCash;
	String 				strRSAPubKey[] = new String[8];
	
	int					nImageSrcCount[] = new int[480];
	int					nImageDstCount[] = new int[480];
	int					nImageRechargeData[] = new int[480];
	int					nImageAuthCode[] = new int[480];
	int					count = 0;
	static final int 	GUI_STOP_NOTIFIER = 0x108;
	static final int 	GUI_THREADING_NOTIFIER = 0x109;

	byte 				byImageSrcCount[] = new byte[470];
	byte 				byImageDstCount[] = new byte[470];
	byte 				byImageRechargeData[] = new byte[470];
	byte 				byImageAuthCode[] = new byte[470];
					
	EditText			etSrcAccount;
	EditText    		etRechargeData;
	EditText			etDstAccount;
	EditText			etBanlanceCash;
	EditText			etStatus;
	EditText			etRSAKey;
	
	Button 				btnReturnMain;
	Button 				btnTxDataToCard;
	Button				btnGenerateRSAKey;
	Button				btnReadBanlance;
	
	ImageView			imgvAuthCode;
	ImageView			imgvSrcAccount;
	ImageView			imgvDstAccount;
	ImageView			imgvRechargeData;
	
	Resources			res;
	BitmapDrawable		bitmapdrawableAuthCode;
	
	BitmapDrawable		bitmapdrawableSrcCount;
	BitmapDrawable		bitmapdrawableDstCount;
	BitmapDrawable		bitmapdrawableRechargeData;
	
	
	Toast				tstDisInfo;
	
	Bitmap				bitmapAuthCode;
	Bitmap				bitmapSrcCount;
	Bitmap				bitmapDstCount;
	Bitmap				bitmapRechargeData;
	
	Dialog				dlogConfirm;
	Dialog				dlogWarning;
	ProgressDialog 		pdlogProcess;
	ProgressBar 		m_ProgressBar;
	EncryptIn3DES 		encryptin3DES = new EncryptIn3DES();
	Handler				handler = null;
	
	Global globalval = new Global();
	Global.ReturnVal retcode = globalval.new ReturnVal();
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_PROGRESS);
        setProgressBarVisibility(true);
        
        setContentView(R.layout.mainnewukey);
        
        m_ProgressBar = (ProgressBar)findViewById(R.id.ProgressBar01);
        m_ProgressBar.setIndeterminate(false);
        
        etSrcAccount = (EditText) findViewById(R.id.etSrcAccount);
		etSrcAccount.setHint(Integer.toString(Global.DEFAULTSRCACCOUNT));
     	etSrcAccount.setEnabled(false);
		
        etRechargeData = (EditText)findViewById(R.id.etRechargeData);
        etDstAccount = (EditText)findViewById(R.id.etDstAccount);
        etBanlanceCash = (EditText)findViewById(R.id.etBanlanceCash);
        
        etRSAKey = (EditText)findViewById(R.id.etRSAKey);
        

       	
        
		etDstAccount.setOnKeyListener(new EditText.OnKeyListener() {
			public boolean onKey(View arg0, int arg1, KeyEvent arg2)
			{
				strDstAccount = etDstAccount.getText().toString();
				return false;
			}
		});
		
		etRechargeData.setOnKeyListener(new EditText.OnKeyListener() {
			public boolean onKey(View arg0, int arg1, KeyEvent arg2)
			{
				strRechargeData = etRechargeData.getText().toString();
				return false;
			}
		});

		btnReturnMain = (Button) findViewById(R.id.btnReturnMain);
        btnReturnMain.setOnClickListener(new ClickEvent());
        
        btnReadBanlance = (Button)findViewById(R.id.btnReadBanlance);
        btnReadBanlance.setOnClickListener(new ClickEvent());
        
        btnTxDataToCard = (Button) findViewById(R.id.btnTxDataToCard);
        btnTxDataToCard.setOnClickListener(new ClickEvent());
        
        
        
        btnGenerateRSAKey = (Button) findViewById(R.id.btnGenerateRSAKey);
        btnGenerateRSAKey.setOnClickListener(new ClickEvent());
        
        res = getResources();
        
        handler = new Handler();
  
        
        
	}       
	
	class ClickEvent implements View.OnClickListener { 
		public void onClick(View v) {
        	if (v == btnTxDataToCard){
        		
        		bTxDataToCard = true;
        		new NewUKeyThread().start();// 开一条线程执行APDU测试
        	}
        	
        	else{
        		if(v == btnGenerateRSAKey){
        			bGenerateRSAKey = true;
        			new NewUKeyThread().start();
        		}
        		else {
        			if(v == btnReadBanlance){
        				bReadBanlance = true;
            			new NewUKeyThread().start();
        			}
        			else if(v == btnReturnMain) {
        		
                	/* 新建一个Intent对象 */
                		Intent intent = new Intent();
        			/* 指定intent要启动的类 */
                		intent.setClass(NewUKey.this, MainActivity.class);
        			/* 启动一个新的Activity */
                		startActivity(intent);
        			/* 关闭当前的Activity */
                		NewUKey.this.finish();
                	}
        		}
        	}
        	
        	
        }
	}

	
	class NewUKeyThread extends Thread {
		@Override
		public void run() {
			String sw = "";
			String readkeysw = "";
			
			String errinfo[] = new String[1000];
			int index = 0;
			strSrcAccount = Integer.toString(Global.DEFAULTSRCACCOUNT);

			
			
			
			
			if(bGenerateRSAKey){
				handler.post(runnableDisProgressBar);
				retcode = globalval.resetCard();
				
				retcode = globalval.generateRSAKeyPair();
				if(retcode.bLogic){
					bRSAKey = true;
					retcode = globalval.getReadyPublicKey();
					if(retcode.bLogic){

						for(count = 0; count < 8; count ++){
							retcode = globalval.readPublicKey();
							sw = globalval.intToString(retcode.nData, retcode.nLength - 2, retcode.nLength);
							if(7 == count){
								readkeysw = "9000";
							}
							else
								readkeysw = "61" + Integer.toHexString(128 - (count + 1) * Global.FILELENGTHONETIME);
							if(sw.equals(readkeysw)){
								strRSAPubKey[count] = globalval.intToString(retcode.nData, 0, retcode.nLength - 2);
								retcode.strRetInfo = "第" + (count + 1) + " 次取公钥成功";
								}
							else{
								retcode.strRetInfo = "第" + (count + 1) + " 次取公钥失败";
								errinfo[index] = retcode.strRetInfo;
								index ++;
								break;
							}
							
							if(count == 7){
								Message m = new Message();
								m.what = GUI_STOP_NOTIFIER;
								progressBarHandler.sendMessage(m);
							}
							else{
								Message m = new Message();
								m.what = GUI_THREADING_NOTIFIER;
								progressBarHandler.sendMessage(m);
							}
							
							handler.post(runnableDisRetInfo);
						}
						
					}
				
					else{
						retcode.strRetInfo += "准备公钥操作失败";
						errinfo[index] = retcode.strRetInfo;
						index ++;
					}
				}
				else{
					retcode.strRetInfo += "生成公私钥对失败";
					errinfo[index] = retcode.strRetInfo;
					index ++;
				}

				retcode.strRetInfo = "取公钥成功完成！";
				bGenerateRSAKey = false;
				handler.post(runnableDisRSAPubKey); 
			}
			
			else{
				if(bTxDataToCard){
					retcode = globalval.readBanlance("00bf", 0, 0, 4);
				    Global.nBanlanceCash = Integer.parseInt(retcode.strRetData, 16);
					
					
					if(Global.nBanlanceCash < Integer.parseInt(strRechargeData, 10)){
						handler.post(runnableWarningDialog);
					}
					else{
					strAuthCode = "";			
					
					nImageSrcCount = globalval.getImageData(strSrcAccount);
					nImageDstCount = globalval.getImageData(strDstAccount);
					nImageRechargeData = globalval.getImageData(strRechargeData);
					
					for(int i = 0; i < 470; i ++){
						byImageSrcCount[i] = (byte)nImageSrcCount[i]; 
						byImageDstCount[i] = (byte)nImageDstCount[i]; 
						byImageRechargeData[i] = (byte)nImageRechargeData[i]; 
					}

					//取认证码，00C0000006
					retcode = globalval.getAuthCode();
					if(retcode.bLogic){
						for(int i = 0; i < 6; i ++){
							strAuthCode += Integer.toString(retcode.nData[i] - 48);
						}
						nImageAuthCode = globalval.getImageData(strAuthCode);
						for(int i = 0; i < 470; i ++)
							byImageAuthCode[i] = (byte)nImageAuthCode[i];		
					}
						
					
					globalval.disAuthorCodeOnCard(strAuthCode);
					handler.post(runnableDisImage); 
					
					handler.post(runnableConfirmDialog);
/*					
					retcode = globalval.waitPushCardButton();
					if(retcode.bLogic){
						Global.nBanlanceCash -= Integer.parseInt(strRechargeData, 10);
						handler.post(runnableDealDialog);
					}
*/
/*
					retcode = globalval.resetCard();
					
					while(bTxDataToCard) {
						strSrcAccount = Integer.toString(Global.DEFAULTSRCACCOUNT);
						if(null == strDstAccount)
							strDstAccount = "";
						if(null == strRechargeData)
							strRechargeData = "";

						strTxdata = strSrcAccount + strDstAccount + strRechargeData;
						if(bRSAKey){
							retcode = globalval.generateBitmapFile(strTxdata);
							sw= globalval.intToString(retcode.nData, retcode.nLength - 2, retcode.nLength);
							if(sw.equals("9000")){
								for(int i = 0; i < Global.GETIMAGEDATATIMES; i ++){		//30 = 470 / 16,每次取16个数据回来
									retcode = globalval.getReadyBitmapFile(i);

//									handler.post(runnableDisRetInfo);
									if(retcode.bLogic){
										retcode = globalval.readBitmapFile(i);

//										handler.post(runnableDisRetInfo);
										if(retcode.bLogic){
											bGetImage = true;
											System.arraycopy(retcode.nData, 0, nImageAuthCode, i * Global.FILELENGTHONETIME, Global.FILELENGTHONETIME);

										}
										else{
											bGetImage = false;
											break;
										}		
									}
								}
							}
							if(bGetImage){
								for(int i = 0; i < 470; i ++)
									byImageAuthCode[i] = (byte)nImageAuthCode[i];
								retcode.strRetInfo = "取图片成功";
								handler.post(runnableDisImage); 
								handler.post(runnableDisDialog);
							}
						}
						else{
							retcode.strRetInfo += "生成RSA公私钥错误";
						}
						
						bTxDataToCard = false;
					}

*/	
					strTxdata = "";
					bTxDataToCard = false;
				}
				}
				
				else{
					if(bReadBanlance){

						bReadBanlance = false;
						/*读卡中透明文件存储的余额值
						 * 选择卡文件：00A4000002 + FILEID
						 * 读数据：00B0000004 
						 * 返回：读取正常返回9000
						 */
						
						retcode = globalval.readBanlance("00bf", 0, 0, 4);
						if(retcode.bLogic){
				        Global.nBanlanceCash = Integer.parseInt(retcode.strRetData, 16);
				        
//				        retcode = globalval.disBanlanceOnCard(Global.nBanlanceCash);
				        retcode.strRetInfo = "读余额成功";
						}
						else{
							retcode.strRetInfo = "读余额错误";
				        
						}
						handler.post(runnableDisCardBanlance);
					}
				}
				
		         
			}	
		
			
			
			
			
		}
		
		};	
		
		Runnable runnableDisRSAPubKey = new Runnable(){
			@Override
			public void run(){
				String rsapubkey = "";
				for(String str:strRSAPubKey)
					rsapubkey += str;
				etRSAKey.setText(rsapubkey);
//				etStatus.setText(retcode.strRetInfo);
			}
		};
		
		Runnable runnableDisCardBanlance = new Runnable(){
			@Override
			public void run(){
//				etStatus.setText(retcode.strRetInfo);
				if(retcode.bLogic)
					etBanlanceCash.setText(Integer.toString(Global.nBanlanceCash));
				else
					etBanlanceCash.setText("读余额错误");
			}
		};
		
		Runnable runnableDisRetInfo = new Runnable(){
			@Override
			public void run(){
//				etStatus.setText(retcode.strRetInfo);
				
			}
		};
		
		Runnable runnableDisImage = new Runnable(){
			@Override
			public void run(){
				
				
				imgvSrcAccount = (ImageView)findViewById(R.id.imgvSrcAccount);
				bitmapSrcCount = Bytes2Bitmap(byImageSrcCount);
				bitmapdrawableSrcCount = new BitmapDrawable(bitmapSrcCount);
				
				imgvDstAccount = (ImageView)findViewById(R.id.imgvDstAccount);
				bitmapDstCount = Bytes2Bitmap(byImageDstCount);
				bitmapdrawableDstCount = new BitmapDrawable(bitmapDstCount);
				
				imgvRechargeData = (ImageView)findViewById(R.id.imgvRechargeData);
				bitmapRechargeData = Bytes2Bitmap(byImageRechargeData);
				bitmapdrawableRechargeData = new BitmapDrawable(bitmapRechargeData);
				
				imgvAuthCode = (ImageView)findViewById(R.id.imgvAuthCode);
				bitmapAuthCode = Bytes2Bitmap(byImageAuthCode);
				bitmapdrawableAuthCode = new BitmapDrawable(bitmapAuthCode);
				
				imgvSrcAccount.setImageDrawable(bitmapdrawableSrcCount);
				imgvDstAccount.setImageDrawable(bitmapdrawableDstCount);
				imgvRechargeData.setImageDrawable(bitmapdrawableRechargeData);
				imgvAuthCode.setImageDrawable(bitmapdrawableAuthCode);
				
				
//				etStatus.setText(retcode.strRetInfo);
//				etBanlanceCash.setText(Integer.toString(Global.nBanlanceCash));
		
			}
		};
		
		
		
		Runnable runnableDisToast = new Runnable(){
			@Override
			public void run(){
				
				tstDisInfo = Toast.makeText(getApplicationContext(),"转账金额大于余额，请重新输入", Toast.LENGTH_LONG);
				tstDisInfo.setGravity(Gravity.CENTER, 0, 0);
				tstDisInfo.show();
			}
		};
		
		Runnable runnableConfirmDialog = new Runnable(){
			@Override
			public void run(){
				
				
				
				dlogConfirm = new AlertDialog.Builder(NewUKey.this)
				.setTitle("确认交易")
				.setMessage("确认交易？")
				.setPositiveButton("确认", 
				new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						pdlogProcess = ProgressDialog.show(NewUKey.this, "请按下可视卡上按钮", "", true);
						new Thread(){
							public void run(){
								try{
									retcode = globalval.waitPushCardButton();
									if(retcode.bLogic){
										Global.nBanlanceCash -= Integer.parseInt(strRechargeData, 10);
										retcode = globalval.selectFile("00bf");
		                            	if(retcode.bLogic){
		                            		String str = Integer.toString(Global.nBanlanceCash, 16);
			                            	globalval.updateSelectFileData(0, 0, str, 4);
		                            	}
										
									}
									sleep(2000);
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
		Runnable runnableDealDialog = new Runnable(){
			@Override
			public void run(){
				
				dlogConfirm = new AlertDialog.Builder(NewUKey.this)
				.setTitle("确认提示")//设置标题
				.setMessage("确认交易！")//设置内容
				.setPositiveButton("确定",//设置确定按钮
		        new DialogInterface.OnClickListener() 
				{
//					public void onClick(DialogInterface dialog, int whichButton)
//					{
						//点击“确定”转向登陆框
						
//						LayoutInflater factory = LayoutInflater.from(NewUKey.this);
						//得到自定义对话框
//		                final View DialogView = factory.inflate(R.layout.newukeydialog, null);
		                //创建对话框
//		                AlertDialog dlg = new AlertDialog.Builder(NewUKey.this)
//		                .setTitle("确认提示")
//		                .setView(DialogView)//设置自定义对话框的样式
//		                .setPositiveButton("确定", //设置"确定"按钮
//		                new DialogInterface.OnClickListener() //设置事件监听
//		                {
		                    public void onClick(DialogInterface dialog, int whichButton) 
		                    {
		                    	//输入完成后，点击“确定”开始登陆
		                    	
		                    	pdlogProcess = ProgressDialog.show
		                                   (
		                                		   NewUKey.this,
		                                     "请等待...",
		                                     "", 
		                                     true
		                                   );
		                        
		                        new Thread()
		                        { 
		                          public void run()
		                          { 
		                            try
		                            { 
		                            	sleep(1000);
		                            	globalval.selectFile("00bf");
		                            	String str = Integer.toString(Global.nBanlanceCash, 16);
		                            	globalval.updateSelectFileData(0, 0, str, 4);
		                            }
		                            catch (Exception e)
		                            {
		                              e.printStackTrace();
		                            }
		                            finally
		                            {
		                            	//登录结束，取消m_Dialog对话框
		                            	pdlogProcess.dismiss();
		                            	
		                            }
		                          }
		                        }.start(); 
		                    }
//		                })
		                
//		                .create();//创建
//		                dlg.show();//显示
//					}
				}).create();//创建按钮

			// 显示对话框
			dlogConfirm.show();
			

		
			}
		};
		
		Runnable runnableWarningDialog = new Runnable(){
			@Override
			public void run(){
				dlogWarning = new AlertDialog.Builder(NewUKey.this)
				.setTitle("输入错误")
				.setMessage("转账金额大于余额！请重新输入")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						pdlogProcess = ProgressDialog.show(NewUKey.this, "", "", true);
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
		
				
		Bitmap Bytes2Bitmap(byte[] b) {
			if (b.length != 0) {
				return BitmapFactory.decodeByteArray(b, 0, b.length);
			} else {
				return null;
			}
		}
		
		
Handler progressBarHandler = new Handler(){
			
			public void handleMessage(Message msg){
				switch(msg.what){
				case GUI_STOP_NOTIFIER:
					m_ProgressBar.setVisibility(View.GONE);
					Thread.currentThread().interrupt();
					break;
				case GUI_THREADING_NOTIFIER:
					if(!Thread.currentThread().isInterrupted()){
						m_ProgressBar.setProgress((count + 1) * 25);
						setProgress((count + 1) * 10 * 200);
					}
					break;
				default:
					break;
				}
				super.handleMessage(msg);
			}
		};
		
		
 		Runnable runnableDisProgressBar = new Runnable(){
			@Override
			public void run(){
				m_ProgressBar.setVisibility(View.VISIBLE);
				m_ProgressBar.setMax(200);
				m_ProgressBar.setProgress(0);
			}
		};
		
		
		
}
