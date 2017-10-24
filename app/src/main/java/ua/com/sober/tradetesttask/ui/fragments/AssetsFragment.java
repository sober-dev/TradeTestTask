package ua.com.sober.tradetesttask.ui.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ua.com.sober.tradetesttask.R;
import ua.com.sober.tradetesttask.models.Asset;
import ua.com.sober.tradetesttask.ui.adapters.AssetsAdapter;
import ua.com.sober.tradetesttask.util.AssetsParser;

/**
 * Created by dmitry on 11/29/16.
 */

public class AssetsFragment extends Fragment implements AssetsAdapter.RecyclerItemClickListener {

    private static final String TAG = AssetsFragment.class.getSimpleName();

    private AssetsAdapter adapter;
    private List<Asset> assetList;

    private Timer parseTimer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        adapter = new AssetsAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assets, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.assets_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.setRecyclerItemClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        parseTimer = new Timer();
        ParseTimerTask parseTimerTask = new ParseTimerTask();
        parseTimer.schedule(parseTimerTask, 0, 1000);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (parseTimer != null) {
            parseTimer.cancel();
            parseTimer = null;
        }
    }

    @Override
    public void onItemClickListener(int position) {
        navigateToChartFragment(position);
    }

    private void navigateToChartFragment(int position) {
        ChartFragment chartFragment = new ChartFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        chartFragment.setArguments(bundle);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, chartFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private class ParseTimerTask extends TimerTask {

        @Override
        public void run() {
            Document document = null;
            try {
                document = Jsoup.connect(AssetsParser.ASSETS_URL).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (document != null) {
                assetList = AssetsParser.parse(document);
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.swap(assetList);
                }
            });
        }
    }
}
