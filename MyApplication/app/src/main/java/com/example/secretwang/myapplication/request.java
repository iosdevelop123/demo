package com.example.secretwang.myapplication;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.Transport;

import java.net.URL;
import java.util.Date;

/**
 * Created by Secret Wang on 2015/12/15.
 */
public class request {

//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message message){
//            super.handleMessage(message);
//            Bundle bundle = message.getData();
//            String string = bundle.getString("value");
//            Log.i("",string);
//        }
//    };
//
//    Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            Message message = new Message();
//            Bundle bundle = new Bundle();
//            bundle.putString("value","请求结果");
//            message.setData(bundle);
//            handler.sendMessage(message);
//        }
//    };

    public SoapObject getResult(String url,String method,String parpm) {
        String string = "";

        String nameSpace = "http://tempuri.org/";
        String methodName = method;
        String requestUrl = url;
        String soapAction = nameSpace + methodName;

        HttpTransportSE httpTransportSE = new HttpTransportSE(requestUrl);
        httpTransportSE.debug = true;

        SoapObject soapObject = new SoapObject(nameSpace,methodName);

        soapObject.addProperty("str_json",parpm);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        SoapObject soapObject1 = new SoapObject();
        try {
            httpTransportSE.call(soapAction,envelope);
            soapObject1 = (SoapObject)envelope.bodyIn;
//            string = soapObject1.toString();
        }catch (Exception e){
            e.printStackTrace();
        }

        return soapObject1;
    }
}
