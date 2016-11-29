package ua.com.sober.tradetesttask.ui.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ua.com.sober.tradetesttask.R;
import ua.com.sober.tradetesttask.models.Asset;

/**
 * Created by dmitry on 11/29/16.
 */

public class AssetsAdapter extends RecyclerView.Adapter<AssetsAdapter.AssetViewHolder> {

    public interface RecyclerItemClickListener {
        void onItemClickListener(int position);
    }

    private List<Asset> assets;
    private RecyclerItemClickListener recyclerItemClickListener;

    public void setRecyclerItemClickListener(RecyclerItemClickListener recyclerItemClickListener) {
        this.recyclerItemClickListener = recyclerItemClickListener;
    }

    public AssetsAdapter(List<Asset> assets) {
        this.assets = assets;
    }

    @Override
    public AssetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.asset_item, parent, false);
        return new AssetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AssetViewHolder holder, int position) {
        final Asset asset = assets.get(position);
        holder.assetNameTextView.setText(asset.getName());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            holder.currentRateTextView.setText(Html.fromHtml(asset.getCurrentRateString(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            holder.currentRateTextView.setText(Html.fromHtml(asset.getCurrentRateString()));
        }
        if (asset.isDirection()) {
            holder.currentRateTextView.setTextColor(Color.GREEN);
        } else {
            holder.currentRateTextView.setTextColor(Color.RED);
        }

        holder.changeTextView.setText(asset.getChange());

        if (!asset.isEnabled()) {
            holder.assetNameTextView.setTextColor(Color.LTGRAY);
            holder.currentRateTextView.setTextColor(Color.LTGRAY);
            holder.changeTextView.setTextColor(Color.LTGRAY);
        }
    }

    @Override
    public int getItemCount() {
        return assets.size();
    }

    class AssetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView assetNameTextView;
        private TextView currentRateTextView;
        private TextView changeTextView;

        AssetViewHolder(View itemView) {
            super(itemView);
            assetNameTextView = (TextView) itemView.findViewById(R.id.tv_asset_name);
            currentRateTextView = (TextView) itemView.findViewById(R.id.tv_current_rate);
            changeTextView = (TextView) itemView.findViewById(R.id.tv_change);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (recyclerItemClickListener != null)
                recyclerItemClickListener.onItemClickListener(getAdapterPosition());
        }
    }
}