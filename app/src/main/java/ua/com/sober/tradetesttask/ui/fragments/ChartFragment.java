package ua.com.sober.tradetesttask.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;

/**
 * Created by dmitry on 11/29/16.
 */

public class ChartFragment extends Fragment {

    public static final String TAG = "ChartFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
}
