package zdmatrix.hed.visualcard.UI;


import java.util.Random;

import zdmatrix.hed.visualcard.R;
import zdmatrix.hed.visualcard.Alg.EncryptIn3DES;
import zdmatrix.hed.visualcard.DataCommunication.NFCCommunication;
import zdmatrix.hed.visualcard.DataTypeTrans.DataTypeTrans;
import zdmatrix.hed.visualcard.FunctionMode.FunctionMode;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.drawable.BitmapDrawable;

import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
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
	boolean 			bNFCConnected = false;
	
	String 				strSrcAccount;
	String				strDstAccount;
	String				strRechargeData;
	String				strApduCmdBody;
	String				strTxdata;
	String				strAuthCode;
	String				strBanlanceCash;
	String 				strRSAPubKey[] = new String[8];
	String				strSW;
	String				strData;
	String[][]			strTechLists;
	
	String				strInfo = "";
	
	byte[] 				banlance = new byte[4];
	
	byte[]				rsapublickey = new byte[18];
	byte[]				authcode = new byte[6];
	
	NfcAdapter			nfcAdapter;
	PendingIntent		pendingIntent;
	Tag					tagFromIntent;
	IntentFilter		ndef;
	IntentFilter		tech;
	IntentFilter		tag;
	IntentFilter[]		intentfilter;
	IsoDep				isodep;
	
	int					nImageSrcCount[] = new int[480];
	int					nImageDstCount[] = new int[480];
	int					nImageRechargeData[] = new int[480];
	int					nImageAuthCode[] = new int[480];
	int					count = 0;
	int					nBanlanceCash;
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
	
	public static final int DEFAULTSRCACCOUNT = 1234567890;
	public static final int DEFAULTDSTACCOUNT = 1023456789;
	
	private static final int NUM0[]  = { 7, 1, 2, 4, 6, 5, 3, 1 };
	private static final int NUM1[]  = { 2, 2, 6 };
	private static final int NUM2[]  = { 6, 1, 2, 4, 3, 5, 6 };
	private static final int NUM3[]  = { 7, 1, 2, 4, 3, 4, 6, 5 };
	private static final int NUM4[]  = { 6, 1, 3, 4, 2, 4, 6 };
	private static final int NUM5[]  = { 6, 2, 1, 3, 4, 6, 5 };
	private static final int NUM6[]  = { 7, 2, 1, 3, 4, 6, 5, 3 };
	private static final int NUM7[]  = { 3, 1, 2, 6 };
	private static final int NUM8[]  = { 9, 1, 2, 4, 6, 5, 3, 1, 3, 4 };
	private static final int NUM9[]  = { 7, 4, 2, 1, 3, 4, 6, 5 };

	private static final int NUM09[][] = { NUM0, NUM1, NUM2, NUM3, NUM4, NUM5, NUM6, NUM7, NUM8, NUM9 };
	
	private final int ORG[][] ={
			{0, 0},
			{6, 0},
			{0, 6},
			{6, 6},
			{0, 12},
			{6, 12},
	};
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_PROGRESS);
        setProgressBarVisibility(true);
        
        setContentView(R.layout.mainnewukey);
        
        m_ProgressBar = (ProgressBar)findViewById(R.id.ProgressBar01);
        m_ProgressBar.setIndeterminate(false);
        
        etSrcAccount = (EditText) findViewById(R.id.etSrcAccount);
		etSrcAccount.setHint(Integer.toString(DEFAULTSRCACCOUNT));
     	etSrcAccount.setEnabled(false);
		
        etRechargeData = (EditText)findViewById(R.id.etRechargeData);
        etDstAccount = (EditText)findViewById(R.id.etDstAccount);
        etBanlanceCash = (EditText)findViewById(R.id.etBanlanceCash);
        
        etRSAKey = (EditText)findViewById(R.id.etRSAKey);
        

        etDstAccount = (EditText) findViewById(R.id.etDstAccount);
        etDstAccount.setHint(Integer.toString(DEFAULTDSTACCOUNT));
        etDstAccount.setEnabled(false);

/*        
		etDstAccount.setOnKeyListener(new EditText.OnKeyListener() {
			public boolean onKey(View arg0, int arg1, KeyEvent arg2)
			{
				strDstAccount = etDstAccount.getText().toString();
				return false;
			}
		});
*/		
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
		isodep = NFCCommunication.nfcConnectInit(intent, getApplicationContext());
	}
	
	class ClickEvent implements View.OnClickListener { 
		public void onClick(View v) {
			if(isodep == null){
				NFCCommunication.nfcConnectFailed(getApplicationContext());
			}else{
				if(v == btnTxDataToCard){
					bTxDataToCard = true;
				}
        		if(v == btnGenerateRSAKey){
        			bGenerateRSAKey = true;
        		}
        		if(v == btnReadBanlance){
        				bReadBanlance = true;
            	}
        		if(v == btnReturnMain) {
        		
                	/* 新建一个Intent对象 */
                		Intent intent = new Intent();
        			/* 指定intent要启动的类 */
                		intent.setClass(NewUKey.this, MainActivity.class);
        			/* 启动一个新的Activity */
                		startActivity(intent);
        			/* 关闭当前的Activity */
                		NewUKey.this.finish();
                }
        		
        		new NewUKeyThread().start();// 开一条线程执行APDU测试
			}
		}
	}

	
	class NewUKeyThread extends Thread {
		@Override
		public void run() {
			
			strSrcAccount = Integer.toString(DEFAULTSRCACCOUNT);
			strDstAccount = Integer.toString(DEFAULTDSTACCOUNT);

			if(bGenerateRSAKey){
				handler.post(runnableDisProgressBar);
				if(FunctionMode.generateRSAKeyPair(isodep)){
					if(FunctionMode.getReadyPublicKey(isodep)){
						for(count = 0; count < 8; count ++){
							if(FunctionMode.readPublicKey(count, isodep, rsapublickey)){
								strRSAPubKey[count] = DataTypeTrans.bytesArrayToHexString(rsapublickey, 0, 16);
								if(count == 7){
									Message m = new Message();
									m.what = GUI_STOP_NOTIFIER;
									progressBarHandler.sendMessage(m);
								}else{
									Message m = new Message();
									m.what = GUI_THREADING_NOTIFIER;
									progressBarHandler.sendMessage(m);
								}
							}else{
								handler.post(disRSAKeyToastMsg);;
								break;
							}
							
						}
						handler.post(runnableDisRSAPubKey);
					}else{
						handler.post(disGetReadyKeyToastMsg);
					}
				}else{
					handler.post(disGenerateKeyToastMsg);
				}
				bGenerateRSAKey = false;
				 
			}
			
			else{
				if(bTxDataToCard){
					if(FunctionMode.selectFile("00bf", isodep)){
						if((nBanlanceCash = FunctionMode.readSelectFileData(isodep)) >= 0){
							if(nBanlanceCash < Integer.parseInt(strRechargeData, 10)){
								handler.post(runnableWarningDialog);
							}
							else{
							strAuthCode = "";
						}
					}
					
					nImageSrcCount = getImageData(strSrcAccount);
					nImageDstCount = getImageData(strDstAccount);
					nImageRechargeData = getImageData(strRechargeData);
					
					for(int i = 0; i < 470; i ++){
						byImageSrcCount[i] = (byte)nImageSrcCount[i]; 
						byImageDstCount[i] = (byte)nImageDstCount[i]; 
						byImageRechargeData[i] = (byte)nImageRechargeData[i]; 
					}

					//取认证码，00C0000006
					
					if(FunctionMode.getAuthCode(isodep, authcode)){
						int[] code = DataTypeTrans.byteArray2IntArray(authcode);
						for(int i = 0; i < code.length; i ++)
							strAuthCode += Integer.toString(code[i] - 48);
						nImageAuthCode = getImageData(strAuthCode);
						for(int i = 0; i < 470; i ++)
							byImageAuthCode[i] = (byte)nImageAuthCode[i];
						FunctionMode.disNumOnCard(strAuthCode, isodep);
						handler.post(runnableDisImage); 
						
						handler.post(runnableConfirmDialog);
					}
					
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
						
						if(FunctionMode.selectFile("00bf", isodep)){
							if((nBanlanceCash = FunctionMode.readSelectFileData(isodep)) >= 0){
								handler.post(runnableDisCardBanlance);
							}
						}
						
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
			}
		};
		
		Runnable runnableDisCardBanlance = new Runnable(){
			@Override
			public void run(){
//				etStatus.setText(retcode.strRetInfo);
				
				etBanlanceCash.setText(Integer.toString(nBanlanceCash));
				
			}
		};
		
		Runnable runnableDisRetInfo = new Runnable(){
			@Override
			public void run(){
//				etStatus.setText(retcode.strRetInfo);
				etRSAKey.setText(strInfo);
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
		
		Runnable disGenerateKeyToastMsg = new Runnable(){
			@Override
			public void run(){
				tstDisInfo = Toast.makeText(getApplicationContext(), "生成公钥失败", Toast.LENGTH_SHORT);
				tstDisInfo.setGravity(Gravity.CENTER, 0, 0);
				tstDisInfo.show();
			}
			
		};
		
		Runnable disGetReadyKeyToastMsg = new Runnable(){
			@Override
			public void run(){
				tstDisInfo = Toast.makeText(getApplicationContext(), "准备公钥失败", Toast.LENGTH_SHORT);
				tstDisInfo.setGravity(Gravity.CENTER, 0, 0);
				tstDisInfo.show();
			}
			
		};
		
		Runnable disRSAKeyToastMsg = new Runnable(){
			@Override
			public void run(){
				tstDisInfo = Toast.makeText(getApplicationContext(), "取公钥失败", Toast.LENGTH_SHORT);
				tstDisInfo.setGravity(Gravity.CENTER, 0, 0);
				tstDisInfo.show();
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
//						pdlogProcess = ProgressDialog.show(NewUKey.this, "请按下可视卡上按钮", "", true);
						new Thread(){
							public void run(){
								try{
//									retcode = globalval.waitPushCardButton();
									nBanlanceCash -= Integer.parseInt(strRechargeData, 10);
									String str = Integer.toString(nBanlanceCash, 16);
									if(FunctionMode.selectFile("00bf", isodep)){
										FunctionMode.updateSelectFileData(str, isodep);
									}
									
									sleep(2000);
								}catch(Exception e){
									e.printStackTrace();
								}
								finally{
//									pdlogProcess.dismiss();
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
		                            	FunctionMode.selectFile("00bf", isodep);
		                            	String str = Integer.toString(nBanlanceCash, 16);
		                            	FunctionMode.updateSelectFileData(str, isodep);
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
		
		public int[] getImageData(String sdata){
			int length = sdata.length();
			int ndata[] = DataTypeTrans.stringDecToIntArray(sdata);
			int ndataarray[] = new int[470];
			for(int i = 0; i < 470; i ++)
				ndataarray[i] = 0xff;
			
			// 位图文件的类型，必须为BM
			ndataarray[0x00] = 0x42;		// B
			ndataarray[0x01] = 0x4D;		// M

			// 位图文件的大小，以字节为单位
			ndataarray[0x02] = 0xD6;
			ndataarray[0x03] = 0x01;
			//把4张图片合成在一起，4 × 470
//			ndataarray[0x02] = 0x58;
//			ndataarray[0x03] = 0x07;
			//
			ndataarray[0x04] = 0x00;
			ndataarray[0x05] = 0x00;

			// 位图文件保留字，必须为0
			ndataarray[0x06] = 0x00;
			ndataarray[0x07] = 0x00;
			ndataarray[0x08] = 0x00;
			ndataarray[0x09] = 0x00;

			// 位图数据的起始位置，以相对于位图
			ndataarray[0x0A] = 0x3E;
			ndataarray[0x0B] = 0x00;
			ndataarray[0x0C] = 0x00;
			ndataarray[0x0D] = 0x00;

			// 位图信息头的长度
			ndataarray[0x0E] = 0x28;
			ndataarray[0x0F] = 0x00;
			ndataarray[0x10] = 0x00;
			ndataarray[0x11] = 0x00;

			// 位图的宽度
			ndataarray[0x12] = 0xC0;
			ndataarray[0x13] = 0x00;
			ndataarray[0x14] = 0x00;
			ndataarray[0x15] = 0x00;

			// 位图的高度
			ndataarray[0x16] = 0x11;
//			ndataarray[0x16] = 0x44;		//为了4张图拼接在一起
			ndataarray[0x17] = 0x00;
			ndataarray[0x18] = 0x00;
			ndataarray[0x19] = 0x00;

			// 位图的位面数
			ndataarray[0x1A] = 0x01;
			ndataarray[0x1B] = 0x00;

			// 每个象素的位数
			ndataarray[0x1C] = 0x01;
			ndataarray[0x1D] = 0x00;

			// 压缩说明
			ndataarray[0x1E] = 0x00;
			ndataarray[0x1F] = 0x00;
			ndataarray[0x20] = 0x00;
			ndataarray[0x21] = 0x00;

			// 用字节数表示的位图数据的大小，该数必须是4的倍数
			ndataarray[0x22] = 0x98;
			ndataarray[0x23] = 0x01;
			
//			ndataarray[0x22] = 0x1a;
//			ndataarray[0x23] = 0x07;
			
			ndataarray[0x24] = 0x00;
			ndataarray[0x25] = 0x00;

			// 用象素/米表示的水平分辨率
			ndataarray[0x26] = 0x00;
			ndataarray[0x27] = 0x00;
			ndataarray[0x28] = 0x00;
			ndataarray[0x29] = 0x00;

			// 用象素/米表示的垂直分辨率
			ndataarray[0x2A] = 0x00;
			ndataarray[0x2B] = 0x00;
			ndataarray[0x2C] = 0x00;
			ndataarray[0x2D] = 0x00;

			// 位图使用的颜色数
			ndataarray[0x2E] = 0x00;
			ndataarray[0x2F] = 0x00;
			ndataarray[0x30] = 0x00;
			ndataarray[0x31] = 0x00;

			// 指定重要的颜色数
			ndataarray[0x32] = 0x00;
			ndataarray[0x33] = 0x00;
			ndataarray[0x34] = 0x00;
			ndataarray[0x35] = 0x00;

			// 调色板 (黑色)
			ndataarray[0x36] = 0x00;
			ndataarray[0x37] = 0x00;
			ndataarray[0x38] = 0x00;
			ndataarray[0x39] = 0x00;

			// 调色板 (底色)
			ndataarray[0x3A] = 0xFF;
			ndataarray[0x3B] = 0xFF;
			ndataarray[0x3C] = 0xFF;
			ndataarray[0x3D] = 0x00;
			
			
			int xstart = 0;
			switch(length){
			case 15:
			case 16:
				xstart = 0;
				break;
				
			case 13:
			case 14:
				xstart = 12;
				break;
				
			case 11:
			case 12:
				xstart = 24;
				break;
				
			case 9:
			case 10:
				xstart = 36;
				break;
			
			case 7:
			case 8:
				xstart = 48;
				break;
				
			case 5:
			case 6:
				xstart = 60;
				break;
			
			case 3:
			case 4:
				xstart = 72;
				break;	
				
			case 1:
			case 2:
				xstart = 84;
				break;		
				
			default:
				break;
				
				
			}
			int ystart = 0;
			
			for(int i = 0; i < length; i ++){
				int num = ndata[i] & 0x0f;
				display7Seg(xstart, ystart, NUM09[num], ndataarray);
				xstart += 12;
			}
			
			return ndataarray;
		}
		
		public byte[] getWholeImage(int[] src, byte[] dst, int index){
			byte[] tmp = new byte[470];
			for(int i = 0; i < 470; i ++)
				tmp[i] = (byte)src[i];
			System.arraycopy(tmp, 0, dst, index * 470, 470);
			return dst;
		}
		
		private void display7Seg (int nx, int ny, final int[] staticbuf, int[] buf)
		{
			int		z = 0;
			int[][] p = new int[6][];
			for(int i = 0; i < 6; i ++)
				p[i] = new int[2];
			Random	rand = new Random();
			
			
			
			int x1, y1, x2 = 0, y2 = 0;

			for ( z = 0; z < 6; z++ )
			{
				int random = rand.nextInt(3);
				p[z][0] = ORG[z][0] + random;
				random = rand.nextInt(3);
				p[z][1] = ORG[z][1] + random;
			}
			
			x1 = p[staticbuf[1] - 1][0];
			y1 = p[staticbuf[1] - 1][1];
			for ( z = 2; z <= staticbuf[0]; z++ )
			{
				x2 = p[staticbuf[z] - 1][0];
				y2 = p[staticbuf[z] - 1][1];
//				if((ny + y2) < 0 ){
//					Log.v("zdmatrix", "显示7段错误，输入的n2 = " + ny + "，输入的y2 = " + y2);
//				}
				drawLine (nx + x1, ny + y1, nx + x2, ny + y2, 0, buf);
	/*
				// 将边线加宽
				if ( x1 < 7 && x2 < 7 )
					drawLine ( nx + x1 - 1, ny + y1, nx + x2 - 1, ny + y2, 0, buf );
				else if ( x1 >= 7 && x2 >= 7 )
					drawLine ( nx + x1 + 1, ny + y1, nx + x2 + 1, ny + y2, 0, buf );
				else if ( y1 < 7 && y2 < 7 )
					drawLine ( nx + x1, ny + y1 - 1, nx + x2, ny + y2 - 1, 0, buf );
				else
					drawLine ( nx + x1, ny + y1 + 1, nx + x2, ny + y2 + 1, 0, buf );
	*/
				// 交换坐标
				x1 = x2;
				y1 = y2;
				x2 = 0;
				y2 = 0;
			}

			
		}
		
		private void drawLine(int x0, int y0, int x1, int y1, int color, int[] buf){
			boolean steep = false;
			int deltax;
			int deltay;
			int error;
			int ystep;
			int x;
			int y;
			steep = Math.abs(y1 - y0) > Math.abs(x1 - x0);
			if ( steep )
			{
				int tmp = 0;
				tmp = x0;
				x0 = y0;
				y0 = tmp;
				
				tmp = x1;
				x1 = y1;
				y1 = tmp;
//				swap ( x0, y0 );
//				swap ( x1, y1 );
			}

			if ( x0 > x1 )
			{
				int tmp = x0;
				x0 = x1;
				x1 = tmp;
				
				tmp = y0;
				y0 = y1;
				y1 = tmp;
//				swap ( x0, x1 );
//				swap ( y0, y1 );
			}

			deltax = x1 - x0;

			deltay = Math.abs( y1 - y0 );

			error = deltax / 2;

			y = y0;

			if ( y0 < y1 )
				ystep = 1;
			else
				ystep = -1;

			for ( x = x0; x <= x1; x ++ )
			{

				if ( steep )
					setPixel ( y, x, color, buf);
					
				else{
					
//					if(y < 0)
//						Log.v("zdmatrix", "输入的y像素错误，为：" + y);
					setPixel ( x, y, color, buf);
				}
				error = error - deltay;

				if ( error < 0 )
				{
					y = y + ystep;
					error = error + deltax;
				}
			}
		}

		private void setPixel(int x, int y, int color, int[] buf){
			int gx = 0x3E + ((16 - y) * 24) + (x / 8);
			int gPixelMask = (0x80 >> (x % 8));
			buf[gx] &= ~gPixelMask;
		}	
}
