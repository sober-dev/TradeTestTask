package ua.com.sober.tradetesttask.ui.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
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
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        assetList = new ArrayList<>();
        adapter = new AssetsAdapter(assetList);
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.assets_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.setRecyclerItemClickListener(this);

        // Only for test load data
        ParseDataTask parseDataTask = new ParseDataTask();
        parseDataTask.execute();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_assets, container, false);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onItemClickListener(int position) {
        navigateToChartFragment(assetList.get(position));
    }

    private void navigateToChartFragment(Asset asset) {
        ChartFragment chartFragment = new ChartFragment();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, chartFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private class ParseDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Document document = null;
            try {
                document = Jsoup.connect("https://trade.opteck.com/trade/iframe?view=table").get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (document != null) {
                assetList = AssetsParser.parse(document);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.swap(assetList);
        }
    }
}
