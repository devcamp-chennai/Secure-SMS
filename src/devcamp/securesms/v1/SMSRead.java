package devcamp.securesms.v1;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.lang.reflect.Field;
import android.telephony.TelephonyManager;

public class SMSRead extends ListActivity {
	
	String encKey = new String("AAAABBBB");
	public ListView lv = null;
	public String strItem = new String("");
	
	@Override
	  public void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.smsinbox);
	      
	      ArrayList<String> listItems=new ArrayList<String>();
	      ArrayAdapter<String> adapter;

	      Uri uriSMSURI = Uri.parse("content://sms/inbox");
	      Cursor cur = getContentResolver().query(uriSMSURI, null, null, null,null);

	      while (cur.moveToNext()) {
	          //listItems.add("From :" + cur.getString(2) + " : " + cur.getString(11));         
	    	  
	    	  String str = new String(cur.getString(11));
	    	  
	    	  
	    	  int n = str.indexOf(":");
              //TextView txtMessage = (TextView) findViewById(R.id.txt);
    	      	String IMEINumber = new String("");
    	      	String msgFinal = new String("");
              try {
            	  //IMEINumber = str.substring(0, n);
              	IMEINumber = EncryptSMS.decrypt(encKey, str.substring(0, n+1));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					msgFinal = "(Decrypt Failed for this message.)";
				}
              TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
              String deviceIMEI = telephonyManager.getDeviceId();
              
              if (IMEINumber.equals(deviceIMEI))
              {
	                try {
	                	//msgFinal = str.substring(n+1);
						msgFinal = EncryptSMS.decrypt(encKey, str.substring(n+1));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
						msgFinal = "(Decrypt Failed for this message.)";
					}
              }
              else
              {
              	msgFinal = "(Decrypt Failed for this message.)";
              }
              
              
            	  msgFinal = "From :" + cur.getString(2) + ":" + msgFinal;
            	  
        
              
              listItems.add(msgFinal);
              
              
	    	  
              
	    	  
	    	  
	      }
	      
	      adapter=new ArrayAdapter<String>(this,
	    		  android.R.layout.simple_list_item_1,
	    		    listItems);
	      setListAdapter(adapter);
	      
	      Button back = (Button) findViewById(R.id.btnBack);
	        back.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View view) {
	                Intent myIntent = new Intent(view.getContext(), SecureSMSActivity.class);
	                startActivityForResult(myIntent, 0);
	            }

	        });
	      

}

	private void ShowAlert(String msg)
	{
		AlertDialog alertDialog;
		alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Debug");
		alertDialog.setMessage(msg);
		alertDialog.show();	
	}
	

}
