package com.techproof.appinstaller.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.FileObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.techproof.appinstaller.R;
import com.techproof.appinstaller.model.ApkListModel;

import java.io.File;
import java.util.List;

import app.adshandler.AHandler;
import app.server.v2.Slave;
import butterknife.BindView;

public class ApksAdapter extends RecyclerView.Adapter<ApksAdapter.ViewHolder> {

    private int TYPE_FILE = 1;
    private int TYPE_ADS = 2;

    Context context;
    List<ApkListModel> apkList;
    ApksOnClickListener onClickListener;
    String selectedIndex;

    public ApksAdapter(Context context,List<ApkListModel> apkList,ApksOnClickListener onClickListener)
    {
        this.context = context;
        this.apkList = apkList;
        this.onClickListener = onClickListener;

        if (!Slave.hasPurchased(context)) {
            for (int position = 0; position < this.apkList.size(); position++) {
                if ((position == 2 || (position % 8 == 0 && position > 8))) {
                    this.apkList.add(position, new ApkListModel());
                }
            }
        }
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

            if(apkList.get(position).getPackageInfo()!=null) {
                holder.txtApkName.setText(apkList.get(position).getPackageInfo().packageName);
                Drawable icon = context.getPackageManager().getApplicationIcon(apkList.get(position).getPackageInfo().applicationInfo);
                holder.imgApk.setImageDrawable(icon);

                ApplicationInfo tmpInfo = null;
                long fileSizeInMB = 0;
                try {
                    //tmpInfo = context.getPackageManager().getApplicationInfo(apkList.get(position).packageName,-1);
                    //long size = new File(tmpInfo.sourceDir).length();
                    if (apkList.get(position).getPackageInfo().applicationInfo.sourceDir != null) {
                        long size = new File(apkList.get(position).getPackageInfo().applicationInfo.sourceDir).length();
                        long fileSizeInKB = size / 1024;
                        fileSizeInMB = fileSizeInKB / 1024;
                    } else {
                        long size = new File(apkList.get(position).getApkPath()).length();
                        long fileSizeInKB = size / 1024;
                        fileSizeInMB = fileSizeInKB / 1024;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String apkSize = fileSizeInMB + " MB | Version - " + apkList.get(position).getPackageInfo().versionName;
                holder.txtVersionName.setText(apkSize);
                holder.txtPlayStore.setOnClickListener(view -> onClickListener.redirectToPlayStore(apkList.get(position).getPackageInfo().packageName));
                holder.layoutApks.setOnLongClickListener(view -> {
                    onClickListener.onClickItem(apkList.get(position), position);
                    selectedIndex = String.valueOf(position);
                    notifyDataSetChanged();
                    return false;
                });
                holder.txtFileInfo.setOnClickListener(view -> onClickListener.apkDetails(apkList.get(position)));
                holder.txtInstall.setOnClickListener(view -> onClickListener.installApk(apkList.get(position)));
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
        apkList.clear();
        apkList.addAll(newList);
        notifyDataSetChanged();
    }

    public void changeItemColor(){
        selectedIndex = null;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return apkList.size();
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
        }
    }

    public interface ApksOnClickListener{
        void redirectToPlayStore(String packageName);
        void apkDetails(ApkListModel apkListModel);
        void installApk(ApkListModel apkListModel);
        void onClickItem(ApkListModel apkListModel,int pos);
    }
}
