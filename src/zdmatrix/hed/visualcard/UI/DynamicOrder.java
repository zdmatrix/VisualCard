package zdmatrix.hed.visualcard.UI;

import zdmatrix.hed.visualcard.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class DynamicOrder extends Activity{
	ProgressDialog 		m_Dialog;
	Button				btnReturnMain;
	
	boolean 			bIsRecording = false;//是否录放的标记
	boolean 			bExit = false;
//	boolean				bReturnMain = false;
	boolean				bRead8ByteRandomData = false;
	boolean				bWriteData = false;
	
	String 				strApduCmdBody;
	String				strApduData;
	int 				nRecBufSize;
	int					nPlayBufSize;
	short[]				sLeadCodeBuf = new short[100];
	short[]				sDataBuf = new short[10000];
	short[]				sTxDataBuf;
	
	AudioRecord 		audioRecord;
	AudioTrack 			audioTrack;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maindynamicorder);
        
        Dialog dialog = new AlertDialog.Builder(DynamicOrder.this)
		.setTitle("登陆提示")//设置标题
		.setMessage("这里需要登录！")//设置内容
		.setPositiveButton("确定",//设置确定按钮
        new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int whichButton)
			{
				//点击“确定”转向登陆框
				
				LayoutInflater factory = LayoutInflater.from(DynamicOrder.this);
				//得到自定义对话框
                final View DialogView = factory.inflate(R.layout.dynamicorderdiglog, null);
                //创建对话框
                AlertDialog dlg = new AlertDialog.Builder(DynamicOrder.this)
                .setTitle("登录框")
                .setView(DialogView)//设置自定义对话框的样式
                .setPositiveButton("确定", //设置"确定"按钮
                new DialogInterface.OnClickListener() //设置事件监听
                {
                    public void onClick(DialogInterface dialog, int whichButton) 
                    {
                    	//输入完成后，点击“确定”开始登陆
                    	m_Dialog = ProgressDialog.show
                                   (
                                		   DynamicOrder.this,
                                     "请等待...",
                                     "正在为你登录...", 
                                     true
                                   );
                        
                        new Thread()
                        { 
                          public void run()
                          { 
                            try
                            { 
                              sleep(3000);
                            }
                            catch (Exception e)
                            {
                              e.printStackTrace();
                            }
                            finally
                            {
                            	//登录结束，取消m_Dialog对话框
                            	m_Dialog.dismiss();
                            	
                            }
                          }
                        }.start(); 
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
        				intent.setClass(DynamicOrder.this, MainActivity.class);
        				/* 启动一个新的Activity */
        				startActivity(intent);
        				/* 关闭当前的Activity */
        				DynamicOrder.this.finish();
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
			intent.setClass(DynamicOrder.this, MainActivity.class);
			/* 启动一个新的Activity */
			startActivity(intent);
			/* 关闭当前的Activity */
			DynamicOrder.this.finish();
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
				intent.setClass(DynamicOrder.this, MainActivity.class);
				/* 启动一个新的Activity */
				startActivity(intent);
				/* 关闭当前的Activity */
				DynamicOrder.this.finish();
			}
		}
	}
}
