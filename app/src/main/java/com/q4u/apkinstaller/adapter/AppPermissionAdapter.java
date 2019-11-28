package com.q4u.apkinstaller.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.q4u.apkinstaller.R;

import java.util.List;

public class AppPermissionAdapter extends RecyclerView.Adapter<AppPermissionAdapter.ViewHolder> {

    Context context;
    String[] permission;

    public AppPermissionAdapter(Context context, String[] permission)
    {
        this.context = context;
        this.permission = permission;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_permissions, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
         holder.txtPermissions.setText(permission[position]);
    }

    @Override
    public int getItemCount() {
        return permission.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtPermissions;

        public ViewHolder(View itemView) {
            super(itemView);
            txtPermissions = itemView.findViewById(R.id.txt_permissionName);
        }
    }
}
