package com.thenewcircle.myquickblogger;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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

    private View contentView;

    public StatusDetailFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        contentView = inflater.inflate(
                R.layout.fragment_status_detail, null);

        return contentView;
    }

    public void showDetails(final YambaClient.Status status) {
        Log.d("StatusDetailFragment", "showDetails = " + status);

        final TextView dateView =
                (TextView) contentView.findViewById(R.id.timelineItemDate);
        if ( null != status.getCreatedAt() ) {
            dateView.setText(status.getCreatedAt().toString());
        } else {
            dateView.setText("");
        }

        final TextView usernameView =
                (TextView) contentView.findViewById(R.id.timelineItemUsername);
        usernameView.setText(status.getUser());

        final TextView statusView =
                (TextView) contentView.findViewById(R.id.timelineItemStatus);
        statusView.setText(status.getMessage());

    }

}
