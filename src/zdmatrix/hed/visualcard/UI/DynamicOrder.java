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
	
	boolean 			bIsRecording = false;//�Ƿ�¼�ŵı��
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
		.setTitle("��½��ʾ")//���ñ���
		.setMessage("������Ҫ��¼��")//��������
		.setPositiveButton("ȷ��",//����ȷ����ť
        new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int whichButton)
			{
				//�����ȷ����ת���½��
				
				LayoutInflater factory = LayoutInflater.from(DynamicOrder.this);
				//�õ��Զ���Ի���
                final View DialogView = factory.inflate(R.layout.dynamicorderdiglog, null);
                //�����Ի���
                AlertDialog dlg = new AlertDialog.Builder(DynamicOrder.this)
                .setTitle("��¼��")
                .setView(DialogView)//�����Զ���Ի������ʽ
                .setPositiveButton("ȷ��", //����"ȷ��"��ť
                new DialogInterface.OnClickListener() //�����¼�����
                {
                    public void onClick(DialogInterface dialog, int whichButton) 
                    {
                    	//������ɺ󣬵����ȷ������ʼ��½
                    	m_Dialog = ProgressDialog.show
                                   (
                                		   DynamicOrder.this,
                                     "��ȴ�...",
                                     "����Ϊ���¼...", 
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
                            	//��¼������ȡ��m_Dialog�Ի���
                            	m_Dialog.dismiss();
                            	
                            }
                          }
                        }.start(); 
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
        				intent.setClass(DynamicOrder.this, MainActivity.class);
        				/* ����һ���µ�Activity */
        				startActivity(intent);
        				/* �رյ�ǰ��Activity */
        				DynamicOrder.this.finish();
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
			intent.setClass(DynamicOrder.this, MainActivity.class);
			/* ����һ���µ�Activity */
			startActivity(intent);
			/* �رյ�ǰ��Activity */
			DynamicOrder.this.finish();
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
				intent.setClass(DynamicOrder.this, MainActivity.class);
				/* ����һ���µ�Activity */
				startActivity(intent);
				/* �رյ�ǰ��Activity */
				DynamicOrder.this.finish();
			}
		}
	}
}
