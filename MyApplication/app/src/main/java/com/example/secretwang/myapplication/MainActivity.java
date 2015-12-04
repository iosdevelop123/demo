package com.example.secretwang.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Arrays;

public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String[] PLANETS = new String[]{"一手", "两手", "三手", "四手", "五手", "六手", "七手", "八手"};
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private Button buyMoreButton;
    private Button buyLessButton;
    private Button allSellButton;

    private GoogleApiClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
       Button setBtn=(Button) findViewById(R.id.setBtn);
 //    跳转设置界面
        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
//                startActivity(intent);
                View outerView = LayoutInflater.from(MainActivity.this).inflate(R.layout.wheel_view, null);
                WheelView wv = (WheelView) outerView.findViewById(R.id.wheel_view_wv);
                wv.setOffset(2);
                wv.setItems(Arrays.asList(PLANETS));
                wv.setSeletion(3);
                wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                    @Override
                    public void onSelected(int selectedIndex, String item) {
                        Log.d(TAG, "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
                    }
                });

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("设置您委托的产品类型和手数")
                        .setView(outerView)
                        .setPositiveButton("OK", null)
                        .show();
            }
        });
//        跳转个人中心界面
        Button userBtn=(Button)findViewById(R.id.usrBtn);
        userBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,UserActivity.class);
                startActivity(intent);
            }
        });
        //    持仓按钮点击事件
        Button holdButton = (Button)findViewById(R.id.button);
        holdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,holdActivity.class);
                startActivity(intent);
            }
        });
//        看多按钮
        buyMoreButton = (Button)findViewById(R.id.button2);
        buyMoreButton.setOnClickListener(buyMoreClick);
//        看空按钮
        buyLessButton = (Button)findViewById(R.id.button4);
        buyLessButton.setOnClickListener(buyLessButtonClick);
//        全部卖出按钮
        allSellButton = (Button)findViewById(R.id.button3);
        allSellButton.setOnClickListener(allSellClick);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


//    全部卖出按钮点击事件
    View.OnClickListener allSellClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i(">>>>>","全部卖出");
            buyMoreButton.setText("看多");
            buyLessButton.setText("看空");
        }
    };
//    看多按钮点击事件
    View.OnClickListener buyMoreClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (buyMoreButton.getText().toString().equals("看多")) {
                buyMoreButtonClick();
            }else if (buyMoreButton.getText().toString().equals("追单")){
                buyMoreButtonBuyOnce();
            }else if (buyMoreButton.getText().toString().equals("反向开仓")){
                buyMoreButtonReverse();
            }
        }
    };
    private void buyMoreButtonClick() {
        Log.i(">>>>>>>>>>>>>>>","看多买入");
        buyMoreButton.setText("追单");
        buyLessButton.setText("反向开仓");
    }
    private void buyMoreButtonBuyOnce() {Log.i(">>>>>", "看多追单买入");}
    private void buyMoreButtonReverse(){
        Log.i(">>>>>>","看多反向开仓");
        buyMoreButton.setText("追单");
        buyLessButton.setText("反向开仓");
    }

//  看空按钮点击事件
    View.OnClickListener buyLessButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (buyLessButton.getText().toString().equals("看空")) {
                buyLessButtonClick();
            }else if (buyLessButton.getText().toString().equals("追单")){
                buyLessButtonBuyOnce();
            }else if (buyLessButton.getText().toString().equals("反向开仓")){
                buyLessButtonReverse();
            }
        }
    };
    private void buyLessButtonClick() {
        Log.i(">>>>>","看空买入");
        buyLessButton.setText("追单");
        buyMoreButton.setText("反向开仓");
    }
    private void buyLessButtonBuyOnce() {
        Log.i(">>>>>","追单买入");
    }
    private void  buyLessButtonReverse() {
        Log.i(">>>>>>","看空反向开仓");
        buyLessButton.setText("追单");
        buyMoreButton.setText("反向开仓");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.secretwang.myapplication/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.secretwang.myapplication/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
