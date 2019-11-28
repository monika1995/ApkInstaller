package com.q4u.apkinstaller.adapter;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.q4u.apkinstaller.R;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.ViewHolder>{

    Context context;
    List<PackageInfo> appsList;
    AppsOnClickListener onClickListener;
    String selectedIndex;

    public AppsAdapter(Context context,List<PackageInfo> appsList,AppsOnClickListener onClickListener)
    {
        this.context = context;
        this.appsList = appsList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_apps, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        /*if ((appsList.get(position).applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
            // It is a system app
            holder.txtPlayStore.setVisibility(View.GONE);
        } else {
            // It is installed by the user
            holder.txtPlayStore.setVisibility(View.VISIBLE);
        }*/
        if(selectedIndex!=null) {
            if (String.valueOf(position).equals(selectedIndex)) {
                holder.layoutApps.setBackground(context.getResources().getDrawable(R.color.bg_card));
            } else {
                holder.layoutApps.setBackground(context.getResources().getDrawable(R.color.light_white));
            }
        }
        else {
            holder.layoutApps.setBackground(context.getResources().getDrawable(R.color.light_white));
        }

        holder.txtAppName.setText(appsList.get(position).applicationInfo.loadLabel(context.getPackageManager()).toString());
        ApplicationInfo tmpInfo = null;
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
        holder.txtPlayStore.setOnClickListener(view -> {onClickListener.redirectToPlayStore(appsList.get(position).packageName);});
        holder.txtAppDetails.setOnClickListener(view -> onClickListener.appDetails(appsList.get(position),position));
        holder.txtUpdates.setOnClickListener(view -> onClickListener.appUpdates(appsList.get(position)));
        holder.layoutApps.setOnLongClickListener(view ->{
            selectedIndex = String.valueOf(position);
            notifyDataSetChanged();
            onClickListener.onClickItem(appsList.get(position),position);
            return false;});
    }


    public void setSearchFilter(List<PackageInfo> newList)
    {
        appsList.clear();
        appsList.addAll(newList);
        notifyDataSetChanged();
    }

    public void changeItemColor(){
        selectedIndex = null;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return appsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgApp;
        TextView txtAppName;
        TextView txtVersionName;
        TextView txtLaunch;
        TextView txtAppDetails;
        TextView txtUpdates;
        TextView txtPlayStore;
        ConstraintLayout layoutApps;

        public ViewHolder(View itemView) {
            super(itemView);
            imgApp = itemView.findViewById(R.id.img_app);
            txtAppName = itemView.findViewById(R.id.txt_appName);
            txtVersionName = itemView.findViewById(R.id.txt_versionName);
            txtLaunch = itemView.findViewById(R.id.txt_launch);
            txtAppDetails = itemView.findViewById(R.id.txt_appDetails);
            txtUpdates = itemView.findViewById(R.id.txt_updates);
            txtPlayStore = itemView.findViewById(R.id.txt_playstore);
            layoutApps = itemView.findViewById(R.id.layout_apps);
        }
    }

    public interface AppsOnClickListener{
        void launchApps(String packageName);
        void redirectToPlayStore(String packageName);
        void appDetails(PackageInfo packageInfo,int position);
        void appUpdates(PackageInfo packageInfo);
        void onClickItem(PackageInfo packageInfo,int pos);
    }
}
