package ua.com.sober.tradetesttask.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

import ua.com.sober.tradetesttask.R;
import ua.com.sober.tradetesttask.models.Asset;
import ua.com.sober.tradetesttask.ui.fragments.AssetsFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            return;
        }

        AssetsFragment assetsFragment = new AssetsFragment();
        assetsFragment.setArguments(getIntent().getExtras());

        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, assetsFragment)
                .commit();
    }
}
