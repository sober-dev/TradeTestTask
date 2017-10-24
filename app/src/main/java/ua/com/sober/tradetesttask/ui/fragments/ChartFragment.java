package ua.com.sober.tradetesttask.ui.fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.text.DecimalFormat;
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
    private int position;

    private Timer parseTimer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        Bundle bundle = getArguments();
        if (bundle != null) {
            position = bundle.getInt("position", -1);
        }

        currentRateList = new ArrayList<>();
        timeLine = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);

        plot = (XYPlot) view.findViewById(R.id.plot);

        Button buttonNext = (Button) view.findViewById(R.id.button_next);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position < assetList.size() - 1) {
                    position++;
                    currentRateList.clear();
                    timeLine.clear();
                }
            }
        });

        Button buttonPrevious = (Button) view.findViewById(R.id.button_previous);
        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position > 0) {
                    position--;
                    currentRateList.clear();
                    timeLine.clear();
                }
            }
        });

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

    private void graph() {
        XYSeries rateSeries = new SimpleXYSeries(timeLine, currentRateList, "Current rate");

        // setup our line fill paint to be a slightly transparent gradient:
        Paint lineFill = new Paint();
        lineFill.setAlpha(200);

        LineAndPointFormatter formatter = new LineAndPointFormatter(Color.rgb(0, 0, 0), Color.BLUE, Color.RED, null);
        formatter.setFillPaint(lineFill);
        plot.addSeries(rateSeries, formatter);

        plot.setTitle(assetList.get(position).getName());
//        plot.setRangeBoundaries(0, 100000, BoundaryMode.AUTO);
//        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.LEFT).setFormat(new DecimalFormat("#.#####"));
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
        timeLine.add(System.currentTimeMillis());
        currentRateList.add(assetList.get(position).getCurrentRate());
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
