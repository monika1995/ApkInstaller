package com.techproof.appinstaller.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.techproof.appinstaller.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import app.adshandler.AHandler;
import app.server.v2.Slave;

public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.ViewHolder> {
    private int TYPE_FILE = 1;
    private int TYPE_ADS = 2;

    private Context context;
    private List<PackageInfo> appsList;
    private AppsOnClickListener onClickListener;
    //private String selectedIndex;
    private ArrayList<String> adsList;
    private boolean[] selectionList;
    private boolean clickValue = false;

    public AppsAdapter(Context context, List<PackageInfo> appsList, AppsOnClickListener onClickListener) {
        this.context = context;
        this.appsList = appsList;
        this.onClickListener = onClickListener;
        this.adsList = new ArrayList<>();

        if (!Slave.hasPurchased(context)) {
            for (int position = 0; position < this.appsList.size(); position++) {
                if ((position == 2 || (position % 8 == 0 && position > 8))) {
                    this.appsList.add(position, new PackageInfo());
                    this.adsList.add("demo");
                }
            }
        }

        selectionList = new boolean[appsList.size()];
    }

    public void updateCount(List<PackageInfo> appsList) {
        this.appsList = appsList;
        this.adsList = new ArrayList<>();
        if (!Slave.hasPurchased(context)) {
            for (int position = 0; position < this.appsList.size(); position++) {
                if ((position == 2 || (position % 8 == 0 && position > 8))) {
                    this.appsList.add(position, new PackageInfo());
                    this.adsList.add("demo");
                }
            }
        }
        selectionList = new boolean[appsList.size()];
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (!Slave.hasPurchased(context)) {
            if ((position == 2 || (position % 8 == 0 && position > 8))) {
                return TYPE_ADS;
            } else {
                return TYPE_FILE;
            }
        } else {
            return TYPE_FILE;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_apps, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (getItemViewType(position) == TYPE_FILE) {
            holder.adsLayout.setVisibility(View.GONE);
            holder.layoutApps.setVisibility(View.VISIBLE);

            holder.txtAppName.setText(appsList.get(position).applicationInfo.loadLabel(context.getPackageManager()).toString());
            long fileSizeInMB = 0;
            try {
                long size = new File(appsList.get(position).applicationInfo.sourceDir).length();
                long fileSizeInKB = size / 1024;
                fileSizeInMB = fileSizeInKB / 1024;
            } catch (Exception e) {
                e.printStackTrace();
            }
            String apkSize = fileSizeInMB + " MB | Version - " + appsList.get(position).versionName;
            holder.txtVersionName.setText(apkSize);

            Drawable icon = context.getPackageManager().getApplicationIcon(appsList.get(position).applicationInfo);
            holder.imgApp.setImageDrawable(icon);

            holder.txtLaunch.setOnClickListener(view -> onClickListener.launchApps(appsList.get(position).packageName));
            holder.txtPlayStore.setOnClickListener(view -> onClickListener.redirectToPlayStore(appsList.get(position).packageName));
            holder.txtAppDetails.setOnClickListener(view -> onClickListener.appDetails(appsList.get(position), position));
            holder.txtUpdates.setOnClickListener(view -> onClickListener.appUpdates(appsList.get(position)));

            holder.checkBox.setChecked(selectionList[position]);

            if (selectionList[position]) {
                holder.txtAppDetails.setEnabled(false);
                holder.txtUpdates.setEnabled(false);
                holder.txtPlayStore.setEnabled(false);
                holder.txtLaunch.setEnabled(false);
                holder.layoutApps.setBackground(context.getResources().getDrawable(R.color.bg_card));
            } else {
                holder.txtAppDetails.setEnabled(true);
                holder.txtUpdates.setEnabled(true);
                holder.txtPlayStore.setEnabled(true);
                holder.txtLaunch.setEnabled(true);
                holder.layoutApps.setBackground(context.getResources().getDrawable(R.color.light_white));
            }

            if (clickValue) {
                holder.checkBox.setVisibility(View.VISIBLE);
            } else {
                holder.checkBox.setVisibility(View.GONE);
            }


            holder.checkBox.setOnClickListener(v -> {
                CheckBox checkBox = (CheckBox) v;
                selectionList[position] = checkBox.isChecked();
                if (checkBox.isChecked()) {
                    holder.txtAppDetails.setEnabled(false);
                    holder.txtUpdates.setEnabled(false);
                    holder.txtPlayStore.setEnabled(false);
                    holder.txtLaunch.setEnabled(false);
                    holder.layoutApps.setBackground(context.getResources().getDrawable(R.color.bg_card));
                    //selectedIndex = String.valueOf(position);
                    selectionList[position] = true;
                    onClickListener.onClickItem(appsList.get(position), position);
                } else {
                    holder.txtAppDetails.setEnabled(true);
                    holder.txtUpdates.setEnabled(true);
                    holder.txtPlayStore.setEnabled(true);
                    holder.txtLaunch.setEnabled(true);
                    holder.layoutApps.setBackground(context.getResources().getDrawable(R.color.light_white));
                    //selectedIndex = String.valueOf(position);
                    selectionList[position] = false;
                    onClickListener.onClickDeleteItem(appsList.get(position));
                }
            });

            holder.layoutApps.setOnLongClickListener(view -> {
                clickValue = true;
                if (clickValue) {
                    selectionList[position] = true;
                    holder.layoutApps.setBackground(context.getResources().getDrawable(R.color.bg_card));
                } else {
                    selectionList[position] = false;
                    holder.layoutApps.setBackground(context.getResources().getDrawable(R.color.light_white));
                }
                //selectedIndex = String.valueOf(position);
                onClickListener.onClickItem(appsList.get(position), position);
                notifyDataSetChanged();
                return false;
            });


            holder.layoutApps.setOnClickListener(v -> {
                clickValue = true;
                holder.checkBox.performClick();
            });


        } else {
            holder.adsLayout.setVisibility(View.VISIBLE);
            holder.layoutApps.setVisibility(View.GONE);
            holder.adsLayout.removeAllViews();
            holder.adsLayout.addView(AHandler.getInstance().getNativeMedium((Activity) context));
        }
    }

    public int getListCount() {
        return adsList.size();
    }

    public void setSearchFilter(List<PackageInfo> newList) {
        appsList.clear();
        appsList.addAll(newList);
        notifyDataSetChanged();
    }

    public void changeItemColor() {
        clickValue = false;
        for(int i = 0;i<selectionList.length;i++)
        {
            selectionList[i] = false;
        }
          notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return appsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgApp;
        TextView txtAppName;
        TextView txtVersionName;
        TextView txtLaunch;
        TextView txtAppDetails;
        TextView txtUpdates;
        TextView txtPlayStore;
        ConstraintLayout layoutApps;
        LinearLayout adsLayout;
        CheckBox checkBox;

        private ViewHolder(View itemView) {
            super(itemView);
            imgApp = itemView.findViewById(R.id.img_app);
            txtAppName = itemView.findViewById(R.id.txt_appName);
            txtVersionName = itemView.findViewById(R.id.txt_versionName);
            txtLaunch = itemView.findViewById(R.id.txt_launch);
            txtAppDetails = itemView.findViewById(R.id.txt_appDetails);
            txtUpdates = itemView.findViewById(R.id.txt_updates);
            txtPlayStore = itemView.findViewById(R.id.txt_playstore);
            layoutApps = itemView.findViewById(R.id.layout_apps);
            adsLayout = itemView.findViewById(R.id.ads_layout);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }

    public interface AppsOnClickListener {
        void launchApps(String packageName);

        void redirectToPlayStore(String packageName);

        void appDetails(PackageInfo packageInfo, int position);

        void appUpdates(PackageInfo packageInfo);

        void onClickItem(PackageInfo packageInfo, int pos);

        void onClickDeleteItem(PackageInfo packageInfo);
    }
}
