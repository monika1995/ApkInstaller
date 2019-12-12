package com.techproof.appinstaller.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.FileObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.techproof.appinstaller.Common.Constant;
import com.techproof.appinstaller.Common.DebugLogger;
import com.techproof.appinstaller.R;
import com.techproof.appinstaller.model.ApkListModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import app.adshandler.AHandler;
import app.server.v2.Slave;
import butterknife.BindView;

public class ApksAdapter extends RecyclerView.Adapter<ApksAdapter.ViewHolder> {

    private int TYPE_FILE = 1;
    private int TYPE_ADS = 2;

    Context context;
    List<ApkListModel> list;
    ApksOnClickListener onClickListener;
    String selectedIndex;
    private ArrayList<String> adsList;
    private boolean[] selectionList;
    private boolean clickValue = false;

    public ApksAdapter(Context context,List<ApkListModel> apkList,ApksOnClickListener onClickListener)
    {
        this.context = context;
        this.list = apkList;
        this.onClickListener = onClickListener;
        this.adsList = new ArrayList<>();

        if (!Slave.hasPurchased(context)) {
            for (int position = 0; position < this.list.size(); position++) {
                if ((position == 2 || (position % 8 == 0 && position > 8))) {
                    this.list.add(position, new ApkListModel());
                    this.adsList.add("demo");
                }
            }
        }
        selectionList = new boolean[apkList.size()];
    }

    public void updateCount(List<ApkListModel> appsList) {
        this.list = appsList;
        this.adsList = new ArrayList<>();
        if (!Slave.hasPurchased(context)) {
            for (int position = 0; position < this.list.size(); position++) {
                if ((position == 2 || (position % 8 == 0 && position > 8))) {
                    this.list.add(position, new ApkListModel());
                    this.adsList.add("demo");
                }
            }
        }
        selectionList = new boolean[appsList.size()];
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_apks, parent, false);
        return new ViewHolder(listItem);
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

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (getItemViewType(position) == TYPE_FILE) {
            holder.adsLayout.setVisibility(View.GONE);
            holder.layoutApks.setVisibility(View.VISIBLE);
            if(selectedIndex!=null) {
                if (String.valueOf(position).equals(selectedIndex)) {
                    holder.layoutApks.setBackground(context.getResources().getDrawable(R.color.bg_card));
                } else {
                    holder.layoutApks.setBackground(context.getResources().getDrawable(R.color.light_white));
                }
            }else {
                holder.layoutApks.setBackground(context.getResources().getDrawable(R.color.light_white));
            }

            holder.checkBox.setChecked(selectionList[position]);

            if (selectionList[position]) {
                holder.txtFileInfo.setEnabled(false);
                holder.txtPlayStore.setEnabled(false);
                holder.txtInstall.setEnabled(false);
                holder.layoutApks.setBackground(context.getResources().getDrawable(R.color.bg_card));
            } else {
                holder.txtFileInfo.setEnabled(true);
                holder.txtPlayStore.setEnabled(true);
                holder.txtInstall.setEnabled(true);
                holder.layoutApks.setBackground(context.getResources().getDrawable(R.color.light_white));
            }

            if (clickValue) {
                holder.checkBox.setVisibility(View.VISIBLE);
            } else {
                holder.checkBox.setVisibility(View.GONE);
            }


            if(list.get(position).getPackageInfo()!=null) {
                String apkName = list.get(position).getPackageInfo().applicationInfo.loadLabel(context.getPackageManager()) + ".apk";
                holder.txtApkName.setText(apkName);
                Drawable icon = context.getPackageManager().getApplicationIcon(list.get(position).getPackageInfo().applicationInfo);
                holder.imgApk.setImageDrawable(icon);

                long fileSizeInMB = 0;
                try {
                    //tmpInfo = context.getPackageManager().getApplicationInfo(apkList.get(position).packageName,-1);
                    //long size = new File(tmpInfo.sourceDir).length();
                    if (list.get(position).getPackageInfo().applicationInfo.sourceDir != null) {
                        long size = new File(list.get(position).getPackageInfo().applicationInfo.sourceDir).length();
                        long fileSizeInKB = size / 1024;
                        fileSizeInMB = fileSizeInKB / 1024;
                    } else {
                        long size = new File(list.get(position).getApkPath()).length();
                        long fileSizeInKB = size / 1024;
                        fileSizeInMB = fileSizeInKB / 1024;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String apkSize = fileSizeInMB + " MB | Version - " + list.get(position).getPackageInfo().versionName;
                holder.txtVersionName.setText(apkSize);
                holder.txtPlayStore.setOnClickListener(view -> onClickListener.redirectToPlayStore(list.get(position).getPackageInfo().packageName));
                holder.txtFileInfo.setOnClickListener(view -> onClickListener.apkDetails(list.get(position)));
                holder.txtInstall.setOnClickListener(view -> onClickListener.installApk(list.get(position)));

                holder.checkBox.setOnClickListener(v -> {
                    CheckBox checkBox = (CheckBox) v;
                    selectionList[position] = checkBox.isChecked();
                    if (checkBox.isChecked()) {
                        holder.layoutApks.setBackground(context.getResources().getDrawable(R.color.bg_card));
                        holder.txtFileInfo.setEnabled(false);
                        holder.txtPlayStore.setEnabled(false);
                        holder.txtInstall.setEnabled(false);
                        selectionList[position] = true;
                        onClickListener.onClickItem(list.get(position), position);
                    } else {
                        holder.layoutApks.setBackground(context.getResources().getDrawable(R.color.light_white));
                        selectionList[position] = false;
                        holder.txtFileInfo.setEnabled(true);
                        holder.txtPlayStore.setEnabled(true);
                        holder.txtInstall.setEnabled(true);
                        onClickListener.onClickDeleteItem(list.get(position));
                    }
                });

                holder.layoutApks.setOnLongClickListener(view -> {
                    clickValue = true;
                    if (clickValue) {
                        selectionList[position] = true;
                        holder.layoutApks.setBackground(context.getResources().getDrawable(R.color.bg_card));
                    } else {
                        selectionList[position] = false;
                        holder.layoutApks.setBackground(context.getResources().getDrawable(R.color.light_white));
                    }
                    onClickListener.onClickItem(list.get(position), position);
                    notifyDataSetChanged();
                    return false;
                });


                if (clickValue) {
                    holder.layoutApks.setOnClickListener(v -> {
                        holder.checkBox.performClick();
                    });
                } else {
                    holder.layoutApks.setOnClickListener(null);
                }
            }
        }else {
            holder.adsLayout.setVisibility(View.VISIBLE);
            holder.layoutApks.setVisibility(View.GONE);
            holder.adsLayout.removeAllViews();
            holder.adsLayout.addView(AHandler.getInstance().getNativeMedium((Activity) context));
        }
    }

    public void setSearchFilter(List<ApkListModel> newList)
    {
        list.clear();
        list.addAll(newList);
        notifyDataSetChanged();
    }

    public void changeItemColor(){
        clickValue = false;
        for(int i = 0;i<selectionList.length;i++)
        {
            selectionList[i] = false;
        }
        notifyDataSetChanged();
    }

    public int getListCount()
    {
        return adsList.size();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgApk;
        TextView txtApkName;
        TextView txtVersionName;
        TextView txtInstall;
        TextView txtFileInfo;
        TextView txtPlayStore;
        ConstraintLayout layoutApks;
        LinearLayout adsLayout;
        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            imgApk = itemView.findViewById(R.id.img_apk);
            txtApkName = itemView.findViewById(R.id.txt_apkName);
            txtVersionName = itemView.findViewById(R.id.txt_versionName);
            txtInstall = itemView.findViewById(R.id.txt_install);
            txtFileInfo = itemView.findViewById(R.id.txt_FileInfo);
            txtPlayStore = itemView.findViewById(R.id.txt_playstore);
            layoutApks = itemView.findViewById(R.id.layout_apks);
            adsLayout = itemView.findViewById(R.id.ads_layout);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }

    public interface ApksOnClickListener{
        void redirectToPlayStore(String packageName);
        void apkDetails(ApkListModel apkListModel);
        void installApk(ApkListModel apkListModel);
        void onClickItem(ApkListModel apkListModel,int pos);
        void onClickDeleteItem(ApkListModel ApkListModel);
    }
}
