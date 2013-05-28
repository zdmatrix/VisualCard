package zdmatrix.hed.visualcard.UI;


import zdmatrix.hed.visualcard.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

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
	
	boolean 			bIsRecording = false;//是否录放的标记
	boolean				bRecharge = false;
	boolean				bConsume = false;
	boolean				bPushButton = false;
	boolean				bReadBanlance = false;
	
	String 				strApduCmdBody;
	String				strBanlance;
	String				strRechargeData;
	String				strConsumeData;
	
	
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
        
        
        
    }
	
	class ClickEvent implements View.OnClickListener {

		public void onClick(View v) {
			if (v == btnRecharge || v == btnConsume || v == btnReadBanlance){
				bIsRecording = true;
				if(v == btnRecharge)
					bRecharge = true;
				else if(v == btnConsume)
					bConsume = true;
				else if(v == btnReadBanlance)
					bReadBanlance = true;
				new ElectriMoneyThread().start();// 开一条线程执行APDU测试
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
			
			
			while(bIsRecording) {
				
//				retcode = globalval.waitPushCardButton();
//				if(retcode.bLogic){
//					bPushButton = true;
//				}
//				else
//					bPushButton = false;
				
				if(bRecharge){
					
					if(1000 < Integer.parseInt(strRechargeData, 10)){
						handler.post(runnableRechargeWarningDialog);
					}
/*
					int length = strRechargeData.length() + 5;
					

					strApduCmdBody = "08" + "0" + Integer.toHexString(length) 
							+ "80bf0100" + "0" + Integer.toHexString(length - 5);
					
					globalval.txDataToMCU(strApduCmdBody, strRechargeData, false);
					retcode = globalval.rxDataFromMCU();	
*/
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
				bIsRecording = false;
			}
			}
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
								try{
									retcode = globalval.waitPushCardButton();
									if(retcode.bLogic){
										
										retcode = globalval.selectFile("00bf");
		                            	if(retcode.bLogic){
		                            		String str = Integer.toString(Global.nBanlanceCash, 16);
			                            	globalval.updateSelectFileData(0, 0, str, 4);
		                            	}
										
									}
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
		

		
}
