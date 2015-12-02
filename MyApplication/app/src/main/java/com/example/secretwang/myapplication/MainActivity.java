package com.example.secretwang.myapplication;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity {

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        看多按钮
        buyMoreButton = (Button)findViewById(R.id.button2);
        buyMoreButton.setOnClickListener(buyMoreClick);
//        看空按钮
        buyLessButton = (Button)findViewById(R.id.button4);
        buyLessButton.setOnClickListener(buyLessButtonClick);
//        全部卖出
        allSellButton = (Button)findViewById(R.id.button3);
        allSellButton.setOnClickListener(allSellClick);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    View.OnClickListener allSellClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i(">>>>>","全部卖出");
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
