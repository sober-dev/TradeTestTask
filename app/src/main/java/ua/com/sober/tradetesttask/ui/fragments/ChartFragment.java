package ua.com.sober.tradetesttask.ui.fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ua.com.sober.tradetesttask.R;
import ua.com.sober.tradetesttask.models.Asset;
import ua.com.sober.tradetesttask.util.AssetsParser;

/**
 * Created by dmitry on 11/29/16.
 */

public class ChartFragment extends Fragment {

    private static final String TAG = ChartFragment.class.getSimpleName();

    private XYPlot plot;

    private List<Asset> assetList;
    private List<Float> currentRateList;
    private List<Long> timeLine;

    private Timer parseTimer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        currentRateList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);

        plot = (XYPlot) view.findViewById(R.id.plot);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        parseTimer = new Timer();
        ParseTimerTask parseTimerTask = new ParseTimerTask();
        parseTimer.schedule(parseTimerTask, 0, 1000);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (parseTimer != null) {
            parseTimer.cancel();
        }
    }

    private void graph() {
        XYSeries rateSeries = new SimpleXYSeries(currentRateList, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Current rate");

        // setup our line fill paint to be a slightly transparent gradient:
        Paint lineFill = new Paint();
        lineFill.setAlpha(200);

        LineAndPointFormatter formatter = new LineAndPointFormatter(Color.rgb(0, 0, 0), Color.BLUE, Color.RED, null);
        formatter.setFillPaint(lineFill);
        plot.addSeries(rateSeries, formatter);

        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
            private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                long timestamp = ((Number) obj).longValue();
                Date date = new Date(timestamp);
                return dateFormat.format(date, toAppendTo, pos);
            }

            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;
            }
        });
    }

    private void updateCurrentRate() {
        currentRateList.add(assetList.get(0).getCurrentRate());
    }

    private class ParseTimerTask extends TimerTask {

        @Override
        public void run() {
            Document document = null;
            try {
                document = Jsoup.connect("https://trade.opteck.com/trade/iframe?view=table").get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (document != null) {
                assetList = AssetsParser.parse(document);
            }

            if (isAdded()) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateCurrentRate();
                        plot.clear();
                        graph();
                        plot.redraw();
                    }
                });
            }
        }
    }
}
