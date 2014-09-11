package com.thenewcircle.mytestexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MyActivity extends Activity {

    public static final String EXTRA_EDITABLE
            = MyActivity.class.getPackage() + ".EXTRA_EDITABLE";

    private TextView firstTextView;
    private TextView secondTextView;
    private Button copyTextButton;
    private Button goToNewScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        final boolean editable =
                getIntent().getBooleanExtra(EXTRA_EDITABLE, false);

        firstTextView = (TextView) findViewById(R.id.firstTextView);
        secondTextView = (TextView) findViewById(R.id.secondTextView);
        copyTextButton = (Button) findViewById(R.id.copyTextButton);

        copyTextButton.setEnabled(editable);

        copyTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyText();
            }
        });


        goToNewScreen = (Button) findViewById(R.id.goToNewScreen);
        goToNewScreen.setEnabled(editable);
        goToNewScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(MyActivity.this, MySecondActivity.class);
                startActivity(intent);
            }
        });

    }

    public void copyText() {

        final String textToMove = firstTextView.getText().toString();
        secondTextView.setText(textToMove);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
