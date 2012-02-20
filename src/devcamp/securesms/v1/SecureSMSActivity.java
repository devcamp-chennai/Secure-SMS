package devcamp.securesms.v1;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class SecureSMSActivity extends Activity {
    /** Called when the activity is first created. */
	
	Button btnSendSMS;
    EditText txtPhoneNo;
    EditText txtMessage;
    EditText txtIMEINumber;
    
    String encKey = new String("AAAABBBB");
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        btnSendSMS = (Button) findViewById(R.id.btnSendSMS);
        txtPhoneNo = (EditText) findViewById(R.id.txtPhoneNo);
        txtMessage = (EditText) findViewById(R.id.txtMessage);
        txtIMEINumber = (EditText) findViewById(R.id.txtIMEINumber);
 
        btnSendSMS.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {                
                String phoneNo = txtPhoneNo.getText().toString();
                String message = txtMessage.getText().toString();  
                String IMEINumber = txtIMEINumber.getText().toString();
                if (phoneNo.length()>0 && IMEINumber.length()>0 && message.length()>0)
					try {
						sendSMS(phoneNo, message,IMEINumber);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						Toast.makeText(getBaseContext(), 
		                        e.getMessage(), 
		                        Toast.LENGTH_SHORT).show();
					}
				else
                    Toast.makeText(getBaseContext(), 
                        "Please enter phone number, IMEI Number and message.", 
                        Toast.LENGTH_SHORT).show();
            }
        }); 
        
        Button inbox = (Button) findViewById(R.id.btnInbox);
        inbox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), SMSRead.class);
                startActivityForResult(myIntent, 0);
            }

        });
    }
    
	private void sendSMS(String phoneNumber, String message,String IMEINumber) throws Exception
    {        
		String encryptedMsg = encryptMessage(message,IMEINumber);
		
        PendingIntent pi = PendingIntent.getActivity(this, 0,
            new Intent(this, SecureSMSActivity.class), 0);                
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, encryptedMsg, pi, null);        
    }   
	
	private String encryptMessage(String message, String IMEINumber)throws Exception
	{
		String enc1;
		String enc2;
		
		try
		{
		enc1 = EncryptSMS.encrypt(encKey,IMEINumber);
		}
		catch(Exception ex)
		{
			throw new Exception("Unexpected error!");
		}
		
		try
		{
		enc2 = EncryptSMS.encrypt(encKey,message);
		}
		catch(Exception ex)
		{
			throw new Exception("Unexpected error!");
		}
		
		String msg = enc1 + ":" + enc2;

		return msg;
	}
	
	
}


