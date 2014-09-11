package com.thenewcircle.mytestexample;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.test.ActivityUnitTestCase;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.Button;

import junit.framework.Assert;

/**
 * Created by lnanek on 9/10/14.
 */
public class MyActivityTest extends ActivityUnitTestCase<MyActivity> {

    public MyActivityTest() {
        super(MyActivity.class);
    }

    @MediumTest
    public void testLaunch() {

        final Intent intent = new Intent(
                getInstrumentation().getContext(), MyActivity.class);
        intent.putExtra(MyActivity.EXTRA_EDITABLE, true);

        final Bundle savedInstanceState = new Bundle();
        savedInstanceState.putBoolean(MyActivity.EXTRA_EDITABLE, true);

        startActivity(intent, savedInstanceState, null);

        MyActivity activity = getActivity();

        final Button button = (Button)
                activity.findViewById(R.id.copyTextButton);

        Assert.assertTrue("expected button enabled", button.isEnabled());

    }

    @MediumTest
    public void testLaunchSecondActivity() throws Throwable {

        final Intent intent = new Intent(
                getInstrumentation().getContext(), MyActivity.class);
        intent.putExtra(MyActivity.EXTRA_EDITABLE, true);

        final Bundle savedInstanceState = new Bundle();
        savedInstanceState.putBoolean(MyActivity.EXTRA_EDITABLE, true);

        startActivity(intent, savedInstanceState, null);

        MyActivity activity = getActivity();

        final Button goToNewScreen = (Button) activity.findViewById(R.id.goToNewScreen);

        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                goToNewScreen.callOnClick();
            }
        });

        final Intent startedIntent = getStartedActivityIntent();
        final ComponentName component = startedIntent.getComponent();
        final String actual = component.getClassName();

        final String expected = MySecondActivity.class.getName();

        Assert.assertEquals("didn't start expected activity",
                expected, actual);

    }


}
