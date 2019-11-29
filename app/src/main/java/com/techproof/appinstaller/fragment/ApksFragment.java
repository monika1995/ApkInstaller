package com.techproof.appinstaller.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.techproof.appinstaller.Common.BaseClass;
import com.techproof.appinstaller.Common.Constant;
import com.techproof.appinstaller.Common.DebugLogger;
import com.techproof.appinstaller.R;
import com.techproof.appinstaller.activity.HomeActivity;
import com.techproof.appinstaller.adapter.ApksAdapter;
import com.techproof.appinstaller.adapter.ApkPermissionAdapter;
import com.techproof.appinstaller.model.ApkListModel;
import com.techproof.appinstaller.model.RefreshpageInterface;
import com.techproof.appinstaller.utils.AppUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import app.adshandler.AHandler;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ApksFragment extends Fragment implements ApksAdapter.ApksOnClickListener, RefreshpageInterface {

    @BindView(R.id.rv_apks)
    RecyclerView rvApks;
    @BindView(R.id.txt_totalDownloadedApps)
    TextView txtTotalApps;
    @BindView(R.id.img_apkSorting)
    ImageView imgSorting;
    @BindView(R.id.txt_no_record_found)
    TextView txtNoRecordFound;
    private LinearLayoutManager linearLayoutManager;
    private ApksAdapter apksAdapter;
    private ArrayList<ApkListModel> list;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private String filePath;
    private List<ApkListModel> apkList,sortedList;
    private Dialog dialog;
    private int lastSortingType = 0;
    private ArrayList<String> permissionsList;
    private Toolbar toolbar;
    private int position;
    private ApkListModel selectedItem;
    private HomeActivity homeActivity;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_apks, container, false);

        ButterKnife.bind(this, view);

        homeActivity = (HomeActivity) getActivity();

        list = new ArrayList<>();
        apkList = new ArrayList<>();
        sortedList = new ArrayList<>();
        permissionsList = new ArrayList<>();
        toolbar = getActivity().findViewById(R.id.toolbar);
        selectedItem = new ApkListModel();

        linearLayoutManager = new LinearLayoutManager(getContext());
        rvApks.setLayoutManager(linearLayoutManager);
        apksAdapter = new ApksAdapter(getContext(), apkList,this);
        rvApks.setAdapter(apksAdapter);

        imgSorting.setOnClickListener(view1 -> dialogSorting());

        return view;
    }

    private int getScreenSize()
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        DebugLogger.d("Screensize" +width + " " + height);
        return height;
    }

    private void dialogSorting(){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        PopupWindow pw = new PopupWindow(inflater.inflate(R.layout.dialog_sorting, null), WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT, true);
        int height = getScreenSize();

        if(height>=2120){
            pw.showAtLocation(getActivity().findViewById(R.id.img_sorting), Gravity.END, 50, -500);
        }else {
            pw.showAtLocation(getActivity().findViewById(R.id.img_sorting), Gravity.END, 40, -110);
        }

        View v= pw.getContentView();
        RadioGroup radioGroup = v.findViewById(R.id.rdGroup);
        if(lastSortingType > 0)
        {
            radioGroup.check(lastSortingType);
        }else {
            radioGroup.check(R.id.radio_SortByName);
        }

        RadioButton rdSystemApps = v.findViewById(R.id.radio_systemApps);
        RadioButton rdInstalledApps = v.findViewById(R.id.radio_installedApps);

        rdInstalledApps.setVisibility(View.GONE);
        rdSystemApps.setVisibility(View.GONE);

        radioGroup.setOnCheckedChangeListener((radioGroup1, i) -> {
            if (radioGroup1.getCheckedRadioButtonId()==R.id.radio_SortByName)
            {
                setFilter(R.id.radio_SortByName);
                lastSortingType = R.id.radio_SortByName;
            }else if (radioGroup1.getCheckedRadioButtonId()==R.id.radio_SortBySize)
            {
                setFilter(R.id.radio_SortBySize);
                lastSortingType = R.id.radio_SortBySize;
            }
            pw.dismiss();
        });
    }

    private void setFilter(int type) {
        sortedList.clear();
        if (type == R.id.radio_SortByName)
        {
            sortedList.addAll(apkList);
            Collections.sort(sortedList, new Comparator<ApkListModel>(){
                public int compare(ApkListModel obj1, ApkListModel obj2) {
                    return obj1.getPackageInfo().applicationInfo.loadLabel(getActivity().getPackageManager()).toString().compareToIgnoreCase(obj2.getPackageInfo().applicationInfo.loadLabel(getActivity().getPackageManager()).toString());
                }
            });
        }
        else if (type == R.id.radio_SortBySize)
        {
            sortedList.addAll(apkList);
            Collections.sort(sortedList, new Comparator<ApkListModel>(){
                public int compare(ApkListModel obj1, ApkListModel obj2) {
                    ApplicationInfo tmpInfo = null;
                    long obj1Size = 0,Obj2Size = 0;
                    try {
                        if(obj1.getPackageInfo().applicationInfo.sourceDir!=null) {
                            long size = new File(obj1.getPackageInfo().applicationInfo.sourceDir).length();
                            long fileSizeInKB = size / 1024;
                            long size1 = new File(obj2.getPackageInfo().applicationInfo.sourceDir).length();
                            long fileSizeInKB1 = size1 / 1024;
                            obj1Size = fileSizeInKB / 1024;
                            Obj2Size = fileSizeInKB1 / 1024;
                        }
                        else {
                            long size = new File(obj1.getApkPath()).length();
                            long fileSizeInKB = size / 1024;
                            obj1Size = fileSizeInKB / 1024;
                            long size1 = new File(obj2.getApkPath()).length();
                            long fileSizeInKB1 = size1 / 1024;
                            Obj2Size = fileSizeInKB1 / 1024;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return Integer.parseInt(String.valueOf(Obj2Size - obj1Size));
                }
            });
        }
        txtNoRecordFound.setVisibility(View.GONE);
        rvApks.setVisibility(View.VISIBLE);
        txtTotalApps.setText("Total Downloaded Apks: " + sortedList.size());
        apksAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllApks();
    }

    private void getAllApks() {
        if (checkPermission()) {

            filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
            list.clear();
            getDir(filePath);

            final PackageManager pm = getActivity().getPackageManager();

            apkList.clear();
            apkList.addAll(list);
            /*for (int i = 0; i < list.size(); i++) {
                PackageInfo info = pm.getPackageArchiveInfo(list.get(i).getApkPath(), 0);
                DebugLogger.d(info.packageName);
                apkList.add(info);
            }*/
            setFilter(R.id.radio_SortByName);
            apksAdapter.notifyDataSetChanged();

        } else {
            requestPermission();
        }
        //txtTotalApps.setText("Total Downloaded Apks: " + apkList.size());
    }

    private void getDir(String path) {
        final PackageManager pm = getContext().getPackageManager();
        File tempDir = new File(path + "/");
        if (tempDir.exists()) {
            File fileList[] = tempDir.listFiles();
            if (fileList != null) {
                for (File file : fileList) {
                    if (file.isFile() && file.getName().contains(".apk") ) {
                        PackageInfo info = pm.getPackageArchiveInfo(file.getPath(), 0);
                        if(info!=null) {
                            ApkListModel apkListModel = new ApkListModel();
                            apkListModel.setApkName(file.getName());
                            apkListModel.setApkPath(file.getPath());
                            apkListModel.setPackageInfo(info);
                            list.add(apkListModel);
                        }
                    } else if (file.isDirectory()) {
                        getDir(file.getAbsolutePath());
                    }
                }
            }
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(getActivity(), "Write External Storage permission allows us to read  files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]
                    {android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch(requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getAllApks();
                } else {
                    DebugLogger.d("Permission Denied, You cannot use local drive .");
                    requestPermission();
                }
                break;
        }
    }

    @Override
    public void redirectToPlayStore(String packageName) {
        try {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
        }catch(Exception e) {
            Toast.makeText(getContext(),"Unable to Connect Try Again...", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        AHandler.getInstance().showFullAds(getActivity(),false);
        AppUtils.onClickButtonFirebaseAnalytics(getActivity(), Constant.FIREBASE_APK_PLAYSTORE);
    }

    @Override
    public void apkDetails(ApkListModel apkListModel) {
        setDialogApkDetails(apkListModel);

        AHandler.getInstance().showFullAds(getActivity(),false);
        AppUtils.onClickButtonFirebaseAnalytics(getActivity(), Constant.FIREBASE_APK_FILEINFO);
    }

    private void setDialogApkDetails(ApkListModel apkListModel) {
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_apkdetails);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        ImageView imgApp = dialog.findViewById(R.id.img_apk);
        TextView txtAppName = dialog.findViewById(R.id.txt_apkName);
        TextView txtVersionName = dialog.findViewById(R.id.txt_versionName);
        TextView txtDownloadedDate = dialog.findViewById(R.id.txt_downloaded_date);
        RecyclerView rvPermissions = dialog.findViewById(R.id.rv_permissions);
        TextView txtFilePath = dialog.findViewById(R.id.txt_apkPath);
        ImageView imgDropDown = dialog.findViewById(R.id.img_permission_arrow);
        rvPermissions.setVisibility(View.GONE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvPermissions.setLayoutManager(linearLayoutManager);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
        String installTime = dateFormat.format( new Date(apkListModel.getPackageInfo().lastUpdateTime));
        txtDownloadedDate.setText(installTime);
        txtAppName.setText(apkListModel.getPackageInfo().packageName);
        txtFilePath.setText(apkListModel.getApkPath());
        long fileSizeInMB = 0;
        try {
            //tmpInfo = context.getPackageManager().getApplicationInfo(apkList.get(position).packageName,-1);
            //long size = new File(tmpInfo.sourceDir).length();
            if(apkListModel.getPackageInfo().applicationInfo.sourceDir!=null) {
                long size = new File(apkListModel.getPackageInfo().applicationInfo.sourceDir).length();
                long fileSizeInKB = size / 1024;
                fileSizeInMB = fileSizeInKB / 1024;
            }else {
                long size = new File(apkListModel.getApkPath()).length();
                long fileSizeInKB = size / 1024;
                fileSizeInMB = fileSizeInKB / 1024;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String apkSize = fileSizeInMB + " MB | Version - " + apkListModel.getPackageInfo().versionName;
        txtVersionName.setText(apkSize);
        Drawable icon = getContext().getPackageManager().getApplicationIcon(apkListModel.getPackageInfo().applicationInfo);
        imgApp.setImageDrawable(icon);
        permissionsList.clear();
        permissionsList.addAll(new BaseClass().getListOfPermissions(getContext(),apkListModel.getApkPath()));
        ApkPermissionAdapter permissionAdapter = new ApkPermissionAdapter(getContext(),permissionsList);
        rvPermissions.setAdapter(permissionAdapter);
        /*if(permissionsList.size()>0) {
            rvPermissions.setVisibility(View.VISIBLE);
            permissionText.setVisibility(View.VISIBLE);
        }else {
            rvPermissions.setVisibility(View.GONE);
            permissionText.setVisibility(View.INVISIBLE);
        }*/

        imgDropDown.setOnClickListener(view -> {
            if(rvPermissions.getVisibility()==View.VISIBLE) {
                rvPermissions.setVisibility(View.GONE);
                imgDropDown.setRotation(0);
            }else {
                rvPermissions.setVisibility(View.VISIBLE);
                imgDropDown.setRotation(180);
            }
        });
    }

    @Override
    public void installApk(ApkListModel apkListModel) {
        setInstallDialog(apkListModel);

        AHandler.getInstance().showFullAds(getActivity(),false);
        AppUtils.onClickButtonFirebaseAnalytics(getActivity(), Constant.FIREBASE_APK_INSTALL);
    }


    private void setInstallDialog(ApkListModel apkListModel) {
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_apkinstall);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        ImageView imgApp = dialog.findViewById(R.id.img_apk);
        TextView txtAppName = dialog.findViewById(R.id.txt_apkName);
        TextView txtVersionName = dialog.findViewById(R.id.txt_versionName);
        RecyclerView rvPermissions = dialog.findViewById(R.id.rv_permissions);
        TextView permissionText = dialog.findViewById(R.id.txt_permissions);
        TextView txtInstall = dialog.findViewById(R.id.btn_install);
        ImageView imgDropDown = dialog.findViewById(R.id.img_permission_arrow);
        rvPermissions.setVisibility(View.GONE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvPermissions.setLayoutManager(linearLayoutManager);
        String[] permissions;
        txtAppName.setText(apkListModel.getPackageInfo().packageName);
        //txtAppName.setText(apkListModel.getPackageInfo().applicationInfo.loadLabel(getContext().getPackageManager()).toString());
        long fileSizeInMB = 0;
        try {
            //tmpInfo = context.getPackageManager().getApplicationInfo(apkList.get(position).packageName,-1);
            //long size = new File(tmpInfo.sourceDir).length();
            if(apkListModel.getPackageInfo().applicationInfo.sourceDir!=null) {
                long size = new File(apkListModel.getPackageInfo().applicationInfo.sourceDir).length();
                long fileSizeInKB = size / 1024;
                fileSizeInMB = fileSizeInKB / 1024;
            }else {
                long size = new File(apkListModel.getApkPath()).length();
                long fileSizeInKB = size / 1024;
                fileSizeInMB = fileSizeInKB / 1024;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String apkSize = fileSizeInMB + " MB | Version - " + apkListModel.getPackageInfo().versionName;
        txtVersionName.setText(apkSize);
        Drawable icon = getContext().getPackageManager().getApplicationIcon(apkListModel.getPackageInfo().applicationInfo);
        imgApp.setImageDrawable(icon);

        permissionsList.clear();
        permissionsList.addAll(new BaseClass().getListOfPermissions(getContext(),apkListModel.getApkPath()));
        ApkPermissionAdapter permissionAdapter = new ApkPermissionAdapter(getContext(),permissionsList);
        rvPermissions.setAdapter(permissionAdapter);
        /*if(permissionsList.size()>0) {
            rvPermissions.setVisibility(View.VISIBLE);
            permissionText.setVisibility(View.VISIBLE);
        }else {
            rvPermissions.setVisibility(View.GONE);
            permissionText.setVisibility(View.INVISIBLE);
        }*/

        txtInstall.setOnClickListener(view ->{
            homeActivity.installApk(new File(apkListModel.getApkPath()));
            //installApk(new File(apkListModel.getApkPath()));
            dialog.dismiss();
        });

        imgDropDown.setOnClickListener(view -> {
            if(rvPermissions.getVisibility()==View.VISIBLE) {
                rvPermissions.setVisibility(View.GONE);
                imgDropDown.setRotation(0);
            }else {
                rvPermissions.setVisibility(View.VISIBLE);
                imgDropDown.setRotation(180);
            }
        });
    }

    public void setApkFilter(String newText) {

        List<ApkListModel> newList = new ArrayList<>();

        if(!newText.isEmpty()) {
            for (int i = 0; i < apkList.size(); i++) {
                if ((apkList.get(i).getPackageInfo().applicationInfo.loadLabel(getActivity().getPackageManager()).toString().toLowerCase().contains(newText.toLowerCase()))) {
                    newList.add(apkList.get(i));
                }
            }

            if (newList.size() > 0) {
                apksAdapter.setSearchFilter(newList);
                txtTotalApps.setText("Total Downloaded Apks: " + newList.size());
                txtNoRecordFound.setVisibility(View.GONE);
                rvApks.setVisibility(View.VISIBLE);
            } else {
                txtTotalApps.setText("Total Downloaded Apks: 0");
                txtNoRecordFound.setVisibility(View.VISIBLE);
                rvApks.setVisibility(View.GONE);
            }
        }else {
            if(lastSortingType > 0)
            {
                setFilter(lastSortingType);
            }else {
                setFilter(R.id.radio_SortByName);
            }

        }
    }

    @Override
    public void onClickItem(ApkListModel apkListModel, int pos) {
        toolbar.getMenu().findItem(R.id.action_search).setVisible(false);
        toolbar.getMenu().findItem(R.id.action_more).setVisible(false);
        toolbar.getMenu().findItem(R.id.action_delete).setVisible(true);
        toolbar.getMenu().findItem(R.id.action_share).setVisible(true);

        position = pos;
        selectedItem = apkListModel;

        HomeActivity.count = 0;
    }

    public void deleteApk() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete an APK");
        builder.setMessage("Do you want to delete this APK?").setPositiveButton("Ok",(dialogInterface, i) -> {
            String filename = selectedItem.getApkPath();
            String[] fileArr = filename.split("0");
            File file = new File(Environment.getExternalStorageDirectory() + "/" + fileArr[1]);
            if(file.exists()) {
                file.getAbsoluteFile().delete();
                if (file.exists()) {
                    try {
                        file.getCanonicalPath();
                        file.getCanonicalFile().delete();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (file.exists()) {
                        getContext().deleteFile(file.getName());
                    }
                }
            }
            getchangeItemColor();
            apksAdapter.changeItemColor();
            apksAdapter.notifyItemRemoved(position);
            apksAdapter.notifyDataSetChanged();
        }).setNegativeButton("CANCEL",(dialogInterface, i) -> {
            dialogInterface.dismiss();
            getchangeItemColor();
            apksAdapter.changeItemColor();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(dialogInterface -> {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black));
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black));
        });
        alertDialog.show();
    }

    public void shareApk() {
        File fileWithinMyDir = new File(selectedItem.getApkPath());
        if (fileWithinMyDir.exists()) {
            Uri fileUri = FileProvider.getUriForFile(getContext(), getContext().getPackageName() + ".provider", fileWithinMyDir);
            Intent intent = ShareCompat.IntentBuilder.from(getActivity())
                    .setStream(fileUri) // uri from FileProvider
                    .setType("text/html")
                    .getIntent()
                    .setAction(Intent.ACTION_SEND) //Change if needed
                    .setDataAndType(fileUri, "apk/*")
                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, 101);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101)
        {
            getchangeItemColor();
            apksAdapter.changeItemColor();
        }
    }

    private void getchangeItemColor() {
        toolbar.getMenu().findItem(R.id.action_search).setVisible(true);
        toolbar.getMenu().findItem(R.id.action_more).setVisible(true);
        toolbar.getMenu().findItem(R.id.action_delete).setVisible(false);
        toolbar.getMenu().findItem(R.id.action_share).setVisible(false);
    }

   /* public void addInList(String path)
    {
        try {
            PackageInfo pi = getActivity().getPackageManager().getPackageArchiveInfo(path,PackageManager.GET_META_DATA);
            ApkListModel apkListModel = new ApkListModel();
            apkListModel.setApkPath(path);
            apkListModel.setApkName(new File(path).getName());
            apkListModel.setPackageInfo(pi);
            apkList.add(apkListModel);
            apksAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteInList(String path)
    {
        try {
            PackageInfo pi = getActivity().getPackageManager().getPackageArchiveInfo(path,PackageManager.GET_META_DATA);
            ApkListModel apkListModel = new ApkListModel();
            apkListModel.setApkPath(path);
            apkListModel.setApkName(new File(path).getName());
            apkListModel.setPackageInfo(pi);
            apkList.remove(apkListModel);
            apksAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    @Override
    public void refreshpage() {
        getAllApks();
    }

    public void refreshAdapter()
    {
        apksAdapter.changeItemColor();
    }
}
 
