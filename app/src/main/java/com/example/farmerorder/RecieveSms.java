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
            String password, phn, prod_title,prod_cat, prod_stock, prod_price, prod_key, del;
            Long phone;
            double price, stock;
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
                        //Log.d(TAG,msg_from + msgBody);
                        //Log.d(TAG,msg_from);
                        if(msgBody.startsWith("*#*")) {
                            for(int k=1;k<s.length;k++) {
                                Log.d(TAG,s[k]);
                            }
                        }
                        phn = s[1];
                        phone = Long.parseLong(phn);
                        password = s[2];
                        prod_title = s[3];
                        prod_cat=s[4];
                        prod_stock = s[5];
                        stock = Integer.parseInt(prod_stock);
                        prod_price = s[6];
                        price = Integer.parseInt(prod_price);
                        prod_key = s[7];
                        del = s[8];
                        delivery = (del.equals("d") || del.equals("D")) ? true : false;



                          myRef = database.getReference(toString().valueOf(phone));
                          myRef.child("password").setValue(password);
                          myRef.child("product title").setValue(prod_title);
                          myRef.child("product category").setValue(prod_cat);
                          myRef.child("Stock").setValue(prod_stock);
                          myRef.child("price").setValue(prod_price);
                          myRef.child("product key").setValue(prod_key);
                          myRef.child("Delivery").setValue(del);
                          Log.d(TAG, String.valueOf(phone) + " " + password + " " + prod_title +
                                  " " + String.valueOf(stock) + " " + String.valueOf(price) + " " +
                                  prod_key + " " + String.valueOf(delivery));
                          //*#*,phone,password,prod_title,prod_cat,stock,price,prod_key,delivery

                    }
                }catch (NumberFormatException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
