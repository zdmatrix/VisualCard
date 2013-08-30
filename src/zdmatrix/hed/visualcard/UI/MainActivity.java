package zdmatrix.hed.visualcard.UI;

import zdmatrix.hed.visualcard.R;
//import jbd.visualcard.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	Intent 							intent;
	Button 							btnNewUKey;
	Button	 						btnDynamicOrder;
	Button 							btnElectricMoney;
	Button 							btnCardTest;
	Button 							btnPublicKey;
	Button 							btnOTP;
	Button 							btnExit;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
       
        
        btnNewUKey = (Button) findViewById(R.id.btnNewUKey);
//        btnNewUKey.setOnClickListener(new ClickEvent());
        
//        btnDynamicOrder = (Button) findViewById(R.id.btnDynamicOrder);
//        btnDynamicOrder.setOnClickListener(new ClickEvent());
        
        btnElectricMoney = (Button) findViewById(R.id.btnElectricMoney);
//        btnElectricMoney.setOnClickListener(new ClickEvent());
        
        btnCardTest = (Button) findViewById(R.id.btnCardTest);
//        btnAPDUTest.setOnClickListener(new ClickEvent());
        
//        btnPublicKey = (Button) findViewById(R.id.btnPublicKey);
//        btnPublicKey.setOnClickListener(new ClickEvent());
        
        btnOTP = (Button) findViewById(R.id.btnOTP);
//        btnCardOP.setOnClickListener(new ClickEvent());
        
        btnExit = (Button) findViewById(R.id.btnExit);
//        btnExit.setOnClickListener(new ClickEvent());
        

		// ����button���¼���Ϣ 
        btnNewUKey.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v)
			{
				// �½�һ��Intent���� 
				Intent intent = new Intent();
				// ָ��intentҪ�������� 
				intent.setClass(MainActivity.this, NewUKey.class);
				// ����һ���µ�Activity 
				startActivity(intent);
				// �رյ�ǰ��Activity 
				MainActivity.this.finish();
			}
		});

        
        btnElectricMoney.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v)
			{
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, ElectricMoney.class);
				startActivity(intent);
				MainActivity.this.finish();
			}
		});
        
        btnCardTest.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v)
			{
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, CardTest.class);
				startActivity(intent);
				MainActivity.this.finish();
			}
		});


        
        btnOTP.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v)
			{
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, OTP.class);
				startActivity(intent);
				MainActivity.this.finish();
			}
		});
        
        btnExit.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v)
			{
				MainActivity.this.finish();
			}
		});
      
    }
}