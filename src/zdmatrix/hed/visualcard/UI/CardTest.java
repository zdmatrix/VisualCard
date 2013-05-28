package zdmatrix.hed.visualcard.UI;

import zdmatrix.hed.visualcard.R;
import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class CardTest extends Activity{
	
	boolean 			bIsRecording = false;//�Ƿ�¼�ŵı��
	boolean 			bExit = false;
//	boolean				bReturnMain = false;
	boolean				bRead8ByteRandomData = false;
	boolean				bWriteData = false;
	boolean				bSend = false;
	
	boolean				bProbeCard = false;
	boolean				bResetCard = false;
	boolean				bPowerDown = false;
	
	int					nNum = 0;
	
	String 				strApduCmdBody;
	String				strApduData;
	
//	TextView			tvOpStatus;
//	TextView			tvReturnData1;
//	TextView			tvReturnData2;
//	TextView			tvReturnSW;
	
	EditText			etOPStatus;
	EditText    		etReturnData;
	EditText    		etReturnSW;
	
	Button 				btnSend;
	Button 				btnReturnMain;
	Button 				btnRead8ByteRandomData;
	Button 				btnProbeCard;
	Button 				btnResetCard;
	Button 				btnPowerDown;
	Button 				btnWriteData;
	
	Handler				handler = null;
	
	Global globalval = new Global();
	Global.ReturnVal retcode = globalval.new ReturnVal();
	
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maincardtest);
        
        handler = new Handler();
        
//        etAPDUCmd = (EditText) findViewById(R.id.etAPDUCmd);
//        etAPDUData = (EditText)findViewById(R.id.etAPDUData);
        
//        etAPDUCmd.setHint("������APDUָ��");
//        etAPDUData.setHint("������APDU����");
        
        
        
        
/*        
		etAPDUCmd.setOnKeyListener(new EditText.OnKeyListener() {
			public boolean onKey(View arg0, int arg1, KeyEvent arg2)
			{
				strApduCmdBody = etAPDUCmd.getText().toString();
				return false;
			}
		});
		
        etAPDUData.setOnKeyListener(new EditText.OnKeyListener() {
			public boolean onKey(View arg0, int arg1, KeyEvent arg2)
			{
				strApduData = etAPDUData.getText().toString();
				return false;
			}
		});

*/
//        btnSend = (Button) findViewById(R.id.btnSend);
//        btnSend.setOnClickListener(new ClickEvent());
                
        btnReturnMain = (Button) findViewById(R.id.btnReturnMain);
        btnReturnMain.setOnClickListener(new ClickEvent());
        
        btnRead8ByteRandomData = (Button) findViewById(R.id.btnRead8ByteRandomData);
        btnRead8ByteRandomData.setOnClickListener(new ClickEvent());
        
        btnWriteData = (Button) findViewById(R.id.btnWriteData);
        btnWriteData.setOnClickListener(new ClickEvent());
        
        btnProbeCard = (Button) findViewById(R.id.btnProbeCard);
        btnProbeCard.setOnClickListener(new ClickEvent());
        
        btnResetCard = (Button) findViewById(R.id.btnResetCard);
        btnResetCard.setOnClickListener(new ClickEvent());
        
        btnPowerDown = (Button) findViewById(R.id.btnPowerDown);
        btnPowerDown.setOnClickListener(new ClickEvent());
        
        etOPStatus = (EditText)findViewById(R.id.etOPStatus);
//        tvReturnData1 = (TextView)findViewById(R.id.tvReturnData1);
//        tvReturnData2 = (TextView)findViewById(R.id.tvReturnData2);
        etReturnSW = (EditText)findViewById(R.id.etReturnSW);
        etReturnData = (EditText)findViewById(R.id.etReturnData);
       
    }
	
	class ClickEvent implements View.OnClickListener {

		public void onClick(View v) {
			if (v == btnRead8ByteRandomData || v == btnWriteData || v == btnProbeCard 
					|| v == btnResetCard || v == btnPowerDown){
				bIsRecording = true;
				if(v == btnRead8ByteRandomData)
					bRead8ByteRandomData = true;
				else if(v == btnWriteData)
					bWriteData = true;
				else if(v == btnProbeCard)
					bProbeCard = true;
				else if(v == btnResetCard)
					bResetCard = true;
				else if(v == btnPowerDown)
					bPowerDown = true;
				
				new CardTestThread().start();// ��һ���߳�ִ��APDU����
			} 
			else if (v == btnReturnMain) {
				/* �½�һ��Intent���� */
				Intent intent = new Intent();
				/* ָ��intentҪ�������� */
				intent.setClass(CardTest.this, MainActivity.class);
				/* ����һ���µ�Activity */
				startActivity(intent);
				/* �رյ�ǰ��Activity */
				CardTest.this.finish();
			}
		}
	}
	
	class CardTestThread extends Thread {
		@Override
		public void run() {
			
			while(bIsRecording) {
				
//				while(0 != times --){
				if(bRead8ByteRandomData){
					strApduCmdBody = "48050084000008";
					strApduData = null;
					
				}
				else if(bWriteData){
					strApduCmdBody = "080880bf010003";
					strApduData = "31363" + Integer.toString(nNum % 10);
					nNum ++;
					
				}
				else if(bProbeCard){
					strApduCmdBody = "01";
					strApduData = null;
					
				}
				else if(bResetCard){
					strApduCmdBody = "02";
					strApduData = null;
					
				}
				else if(bPowerDown){
					strApduCmdBody = "03";
					strApduData = null;
					
				}
				
				globalval.txDataToMCU(strApduCmdBody, strApduData, true);
				
				
				retcode = globalval.rxDataFromMCU();
//				globalval.resetCard();
//				handler.post(runnableUi);
				
//				globalval.echoTest(strApduCmdBody, strApduData, true);
				
				bIsRecording = false;
				

			}
			
			handler.post(runnableUi);   

			}
		};	
		
		Runnable runnableUi = new Runnable(){
			@Override
			public void run(){
				
				Global.ReturnDataInString ret = globalval.getRetDataInStr(retcode);
				
				
				switch(retcode.nPcbCharacter){
				case 0x80:
					if(bProbeCard){
						etOPStatus.setText("̽���ɹ������Ѳ���");
						
					}
					else{ 
						if(bResetCard){
							ret.strData = globalval.intToString(retcode.nData, 0, retcode.nLength);
							etOPStatus.setText("���ϵ縴λ�ɹ�");
						}
						else
							etOPStatus.setText("������ɹ�");
					}
					break;
				case 0x81:
					if(bProbeCard){
						etOPStatus.setText("̽��ʧ�ܣ���δ����");
						
					}
					else{ 
						if(bResetCard){
							etOPStatus.setText("���ϵ縴λʧ�ܣ���δ����");
						}
						else
							etOPStatus.setText("������ʧ�ܣ���δ����");
					}
					
					break;
				case 0x82:
					etOPStatus.setText("���ϵ縴λʧ��");
					break;
				case 0x83:
					etOPStatus.setText("��֧�ֵĿ�����");
					break;	
				case 0x88:
					if(bRead8ByteRandomData){
						etOPStatus.setText("�������APDUָ������ɹ�");
						ret.strData = globalval.intToString(retcode.nData, 0, retcode.nLength - 2);
					}
					if(bWriteData)
						etOPStatus.setText("д������APDUָ������ɹ�");
					break;
				case 0x89:
					etOPStatus.setText("������Ӧ");
					break;
				case 0x8a:
					etOPStatus.setText("��֧�ֵ�APDUָ��");
					break;
				case 0x8b:
					etOPStatus.setText("APDU������ʱ");
					break;
				default:
					etOPStatus.setText("�޷�ʶ��");
					break;
				}
				
				
				bRead8ByteRandomData = false;
				bWriteData = false;
				bSend = false;
				bProbeCard = false;
				bResetCard = false;
				bPowerDown = false;
				
				
				
				
				
				etReturnData.setText(ret.strData);	
				etReturnSW.setText(ret.strSW);	
			
				
			}
		};
		

		
}
