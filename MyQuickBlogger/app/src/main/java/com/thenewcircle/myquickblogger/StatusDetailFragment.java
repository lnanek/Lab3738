package com.thenewcircle.myquickblogger;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.marakana.android.yamba.clientlib.YambaClient;

/**
 * @author Lance Nanek (c07793)
 */
public class StatusDetailFragment extends Fragment {

    public static StatusDetailFragment newInstance(int index) {
        StatusDetailFragment f = new StatusDetailFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);

        return f;
    }

    public int getShownIndex() {
        return getArguments().getInt("index", 0);
    }

    public StatusDetailFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_status_detail, container);
    }

}
