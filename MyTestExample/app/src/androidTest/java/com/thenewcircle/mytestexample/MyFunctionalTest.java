package com.thenewcircle.mytestexample;

import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.robotium.solo.Solo;

/**
 * Created by lnanek on 9/10/14.
 */
public class MyFunctionalTest extends ActivityInstrumentationTestCase2 {

    private MyActivity myActivity;

    private TextView firstTextView;
    private TextView secondTextView;
    private Button copyTextButton;

    private Solo solo;

    public MyFunctionalTest() {
        super(MyActivity.class);
    }

    @MediumTest
    public void testSoloExample() throws Throwable {
        final View decorView = myActivity.getWindow().getDecorView();

        ViewAsserts.assertOnScreen(decorView, copyTextButton);

        solo.clickOnText("Copy Text");

        final String expectedText = firstTextView.getText().toString();
        final String actualText = secondTextView.getText().toString();

        assertEquals("The actual text from the second view did not match the text from the first",
                expectedText, actualText);

    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        setActivityInitialTouchMode(true);

        myActivity = (MyActivity) getActivity();

        firstTextView = (TextView) myActivity.findViewById(R.id.firstTextView);
        secondTextView = (TextView) myActivity.findViewById(R.id.secondTextView);
        copyTextButton = (Button) myActivity.findViewById(R.id.copyTextButton);

    }

    @MediumTest
    public void testClickMeButton_onClick() throws Throwable {
        final View decorView = myActivity.getWindow().getDecorView();

        ViewAsserts.assertOnScreen(decorView, copyTextButton);

        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                copyTextButton.callOnClick();
            }
        });

        final String expectedText = firstTextView.getText().toString();
        final String actualText = secondTextView.getText().toString();

        assertEquals("The actual text from the second view did not match the text from the first",
                expectedText, actualText);

    }

    @MediumTest
    public void testClickMeButton_layout() {
        final View decorView = myActivity.getWindow().getDecorView();

        ViewAsserts.assertOnScreen(decorView, copyTextButton);

        final ViewGroup.LayoutParams layoutParams =
                copyTextButton.getLayoutParams();
        assertNotNull(layoutParams);
        assertEquals(layoutParams.width,
                WindowManager.LayoutParams.WRAP_CONTENT);
        assertEquals(layoutParams.height,
                WindowManager.LayoutParams.WRAP_CONTENT);
    }
}
