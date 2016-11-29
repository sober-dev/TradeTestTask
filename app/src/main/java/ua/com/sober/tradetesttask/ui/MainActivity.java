package ua.com.sober.tradetesttask.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ua.com.sober.tradetesttask.R;
import ua.com.sober.tradetesttask.ui.fragments.AssetsFragment;
import ua.com.sober.tradetesttask.ui.fragments.ChartFragment;

public class MainActivity extends AppCompatActivity {

    private AssetsFragment assetsFragment;
    private ChartFragment chartFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        assetsFragment = new AssetsFragment();
        fragmentTransaction.add(R.id.container, assetsFragment);
        fragmentTransaction.commit();

    }
}
