package com.example.farmerorder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RecieveSms extends BroadcastReceiver {
    DatabaseReference myRef;

    public static final String TAG=RecieveSms.class.getSimpleName();
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context,"SMS_RECIEVED",Toast.LENGTH_SHORT).show();
        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle=intent.getExtras();
            SmsMessage[] msgs=null;
            FirebaseDatabase database=FirebaseDatabase.getInstance();
            //myRef=database.getReference();
            String msg_from;
            String query,password,phn,prod_title,prod_type,prod_cat, prod_stock,prod_expiry, prod_price, prod_key,prod_desc,del;
            Long phone;
            double price,prod_base_price,stock;
            boolean delivery = false;

            if(bundle!=null){
                try{
                    Object[] pdus=(Object[]) bundle.get("pdus");
                    msgs=new SmsMessage[pdus.length];
                    for(int i=0;i<msgs.length;i++){
                        msgs[i]=SmsMessage.createFromPdu((byte[])pdus[i]);
                        msg_from=msgs[i].getOriginatingAddress();
                        String msgBody=msgs[i].getMessageBody();
                        Toast.makeText(context,"From: "+ msg_from +",Body: " +msgBody,Toast.LENGTH_SHORT).show();
                        String[] s=msgBody.split(",");
                        Log.d(TAG,msg_from + msgBody);
                        Log.d(TAG,msg_from);

                        if(msgBody.startsWith("*#*")) {
                            for (int k = 1; k < s.length; k++) {
                                Log.d(TAG, s[k]);

                            }
                        }
//                        if (msg_from.startsWith(s[0])) {
//                            phn = msg_from.substring(3);
//                        }
//                        else{
//                            phn=msg_from;
//                        }
//                        phone = Long.parseLong(phn);
                        query = s[1].trim();
                        password = s[2].trim();
                        prod_title = s[3].trim();
                        prod_cat = s[4].trim();
                        prod_type = s[5].trim();
                        prod_stock = s[6].trim();
//                        stock = Integer.parseInt(prod_stock);
                        prod_expiry=s[7].trim();
                        prod_price = s[8].trim();
                        price = Integer.parseInt(prod_price);
                        prod_base_price=Double.parseDouble(s[9].trim());
                        prod_desc=s[10].trim();
                        prod_key = s[11].trim();
                        del = s[12].trim();
//                        delivery = (del.equals("d") || del.equals("D")) ? true : false;

                            myRef = database.getReference(msg_from);
//                            myRef.child("message").setValue(msgBody);
                          myRef.child("query").setValue(query);
                          myRef.child("password").setValue(password);
                          myRef.child("product title").setValue(prod_title);
                          myRef.child("product category").setValue(prod_cat);
                          myRef.child("product type").setValue(prod_type);
                          myRef.child("Stock").setValue(prod_stock);
                          myRef.child("productExpiry").setValue(prod_expiry);
                          myRef.child("price").setValue(prod_price);
                          myRef.child("productBasePrice").setValue(prod_base_price);
                          myRef.child("product key").setValue(prod_key);
                          myRef.child("product Description").setValue(prod_desc);
                          myRef.child("Delivery").setValue(del);
//                          Log.d(TAG, String.valueOf(phone) + " " + query +" " +password + " " + prod_title + " "+prod_cat+
//                                  " "+prod_type+" " + String.valueOf(stock) + " " + prod_expiry + " " + String.valueOf(price) + " " + String.valueOf(prod_base_price) + " "+
//                                  prod_key + " " + prod_desc + " " + String.valueOf(delivery));
                          //*#*,insert,password,prod_title,prod_cat,prod_type,stock,prod_expiry,price,prod_base_price,prod_key,prod_desc,delivery
                        //*#*,delete,password,prod_title
                        //*#*,update,password,prod_title,prod_cat,prod_type,stock,price,prod_base_price,prod_key,prod_desc,delivery
                    }
                }catch (NumberFormatException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
