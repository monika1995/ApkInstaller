package com.q4u.apkinstaller.adapter;


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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.q4u.apkinstaller.R;
import com.q4u.apkinstaller.model.ApkListModel;

import java.io.File;
import java.util.List;

import butterknife.BindView;

public class ApksAdapter extends RecyclerView.Adapter<ApksAdapter.ViewHolder> {

    Context context;
    List<ApkListModel> apkList;
    ApksOnClickListener onClickListener;
    String selectedIndex;

    public ApksAdapter(Context context,List<ApkListModel> apkList,ApksOnClickListener onClickListener)
    {
        this.context = context;
        this.apkList = apkList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_apks, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if(selectedIndex!=null) {
            if (String.valueOf(position).equals(selectedIndex)) {
                holder.layoutApks.setBackground(context.getResources().getDrawable(R.color.bg_card));
            } else {
                holder.layoutApks.setBackground(context.getResources().getDrawable(R.color.light_white));
            }
        }else {
            holder.layoutApks.setBackground(context.getResources().getDrawable(R.color.light_white));
        }

        holder.txtApkName.setText(apkList.get(position).getPackageInfo().packageName);
        Drawable icon = context.getPackageManager().getApplicationIcon(apkList.get(position).getPackageInfo().applicationInfo);
        holder.imgApk.setImageDrawable(icon);
        ApplicationInfo tmpInfo = null;
        long fileSizeInMB = 0;
        try {
            //tmpInfo = context.getPackageManager().getApplicationInfo(apkList.get(position).packageName,-1);
            //long size = new File(tmpInfo.sourceDir).length();
            if(apkList.get(position).getPackageInfo().applicationInfo.sourceDir!=null) {
                long size = new File(apkList.get(position).getPackageInfo().applicationInfo.sourceDir).length();
                long fileSizeInKB = size / 1024;
                fileSizeInMB = fileSizeInKB / 1024;
            }else {
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
        holder.layoutApks.setOnLongClickListener(view ->{
                onClickListener.onClickItem(apkList.get(position),position);
                selectedIndex = String.valueOf(position);
                notifyDataSetChanged();
                return false;
          });
        holder.txtFileInfo.setOnClickListener(view -> onClickListener.apkDetails(apkList.get(position)));
        holder.txtInstall.setOnClickListener(view -> onClickListener.installApk(apkList.get(position)));
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

        public ViewHolder(View itemView) {
            super(itemView);
            imgApk = itemView.findViewById(R.id.img_apk);
            txtApkName = itemView.findViewById(R.id.txt_apkName);
            txtVersionName = itemView.findViewById(R.id.txt_versionName);
            txtInstall = itemView.findViewById(R.id.txt_install);
            txtFileInfo = itemView.findViewById(R.id.txt_FileInfo);
            txtPlayStore = itemView.findViewById(R.id.txt_playstore);
            layoutApks = itemView.findViewById(R.id.layout_apks);
        }
    }

    public interface ApksOnClickListener{
        void redirectToPlayStore(String packageName);
        void apkDetails(ApkListModel apkListModel);
        void installApk(ApkListModel apkListModel);
        void onClickItem(ApkListModel apkListModel,int pos);
    }
}
