package com.techproof.appinstaller.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
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
import com.techproof.appinstaller.model.FileListenerService;
import com.techproof.appinstaller.model.RefreshpageInterface;
import com.techproof.appinstaller.utils.AppUtils;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
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
    private List<ApkListModel> selectedItemList;
    private HomeActivity homeActivity;
    List<String> temparr;
    private ProgressDialog progressBar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        System.out.println("bcsdkjdb-ApkFragement--- checked 01");

        homeActivity = (HomeActivity) getActivity();

        selectedItemList = new ArrayList<>();
        temparr = new ArrayList<>();
        selectedItemList.clear();
        list = new ArrayList<>();
        apkList = new ArrayList<>();
        sortedList = new ArrayList<>();
        permissionsList = new ArrayList<>();
        toolbar = getActivity().findViewById(R.id.toolbar);
        selectedItem = new ApkListModel();

        linearLayoutManager = new LinearLayoutManager(getContext());
        rvApks.setLayoutManager(linearLayoutManager);
        apksAdapter = new ApksAdapter(getContext(), sortedList,this);
        rvApks.setAdapter(apksAdapter);

        imgSorting.setOnClickListener(view1 -> dialogSorting());

        System.out.println("bcsdkjdb-ApkFragement---1");

    }

    public void fetchAllApks()
    {
        getAllApks();
       /* new Handler().postDelayed(() ->
                getAllApks(),200);*/
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        System.out.println("bcsdkjdb-ApkFragement--- checked 02");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_apks, container, false);

        ButterKnife.bind(this, view);

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
        }else if(height>=1407){
            pw.showAtLocation(getActivity().findViewById(R.id.img_sorting), Gravity.END, 50, -300);
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
            DebugLogger.d("apklist:" + apkList.size());
            sortedList.addAll(apkList);
            Collections.sort(sortedList, (obj1, obj2) -> obj1.getPackageInfo().packageName.compareToIgnoreCase(obj2.getPackageInfo().packageName));
        }
        else if (type == R.id.radio_SortBySize)
        {
            sortedList.addAll(apkList);
            Collections.sort(sortedList, (obj1, obj2) -> {
                //ApplicationInfo tmpInfo = null;
                int obj1Size = 0,Obj2Size = 0;
                long size = new File(obj1.getApkPath()).length();
                long fileSizeInKB = size / 1024;
                long size1 = new File(obj2.getApkPath()).length();
                long fileSizeInKB1 = size1 / 1024;
                Obj2Size = (int) fileSizeInKB1 / 1024;
                obj1Size = (int) fileSizeInKB / 1024;
                return (Obj2Size - obj1Size);
            });
        }
        txtNoRecordFound.setVisibility(View.GONE);
        rvApks.setVisibility(View.VISIBLE);

        apksAdapter.updateCount(sortedList);

        System.out.println("List " + sortedList.size() + " " +apksAdapter.getListCount());
        int count = sortedList.size() - apksAdapter.getListCount();
        txtTotalApps.setText("Total Downloaded Apks: " + count);

        apksAdapter.notifyDataSetChanged();
        progressBar.dismiss();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void getAllApks() {

        //new getApks().execute();

                progressBar = new ProgressDialog(getContext());
                progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressBar.setMessage("Loading ...");
                progressBar.setIndeterminate(true);
                progressBar.show();
                new Handler().postDelayed(() ->{
                    try {
                        new getApks().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    } ,200);
    }

    private void getDir(String path) {
        final PackageManager pm = getContext().getPackageManager();
        File tempDir = new File(path + "/");
        DebugLogger.d("getDir");
        if (tempDir.exists()) {
            File fileList[] = tempDir.listFiles();
            if (fileList != null) {
                for (File file : fileList) {
                    if (file.isFile() && file.getName().contains(".apk") ) {
                        PackageInfo info = pm.getPackageArchiveInfo(file.getPath(), 0);
                        if(info!=null) {
                            info.applicationInfo.sourceDir = file.getPath();
                            info.applicationInfo.publicSourceDir = file.getPath();
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
        DebugLogger.d("ListSize"+list.size());
    }

    public class getApks extends AsyncTask<Void,Void,Void>{

        private ProgressDialog progressBar;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            list.clear();
        }

        @Override
        protected Void doInBackground(Void... path) {

            filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
            getDir(filePath);

         return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            apkList.clear();
            apkList.addAll(list);
            //progressBar.dismiss();

            System.out.println("bcsdkjdb-ApkFragement---data load");
            if (lastSortingType > 0) {
                setFilter(lastSortingType);
            } else {
                setFilter(R.id.radio_SortByName);
            }

           /* if (!AppUtils.isMyServiceRunning(FileListenerService.class, getContext())) {
                System.out.println("bcsdkjdb-AppFragement---data load");                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    getContext().startForegroundService(new Intent(getContext(), FileListenerService.class));
                } else {
                    getContext().startService(new Intent(getContext(), FileListenerService.class));
                }
            }*/

        }
    }

    @Override
    public void redirectToPlayStore(String packageName) {

        new Handler().postDelayed(() -> {
            try {
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
            }catch(Exception e) {
                Toast.makeText(getContext(),"Unable to Connect Try Again...", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }, 1000);

       /* AHandler.getInstance().showFullAds(getActivity(),false);
        AppUtils.onClickButtonFirebaseAnalytics(getActivity(), Constant.FIREBASE_APK_PLAYSTORE);*/

    }

    @Override
    public void apkDetails(ApkListModel apkListModel) {
        setDialogApkDetails(apkListModel);

        AHandler.getInstance().showFullAds(getActivity(),false);
        AppUtils.onClickButtonFirebaseAnalytics(getActivity(), Constant.FIREBASE_APK_FILEINFO);
    }

    private void setDialogApkDetails(ApkListModel apkListModel) {
        dialog = new Dialog(getActivity(),R.style.AlertDialogCustom);
        dialog.setContentView(R.layout.dialog_apkdetails);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        LinearLayout linearLayout = dialog.findViewById(R.id.adsbanner);
        linearLayout.addView(AHandler.getInstance().getBannerHeader(getActivity()));
        //linearLayout.addView(homeActivity.getBanner());

        CardView cardViewPermissions =  dialog.findViewById(R.id.cardView_permissions);
        ImageView imgApp = dialog.findViewById(R.id.img_apk);
        TextView txtAppName = dialog.findViewById(R.id.txt_apkName);
        TextView txtVersionName = dialog.findViewById(R.id.txt_versionName);
        TextView txtDownloadedDate = dialog.findViewById(R.id.txt_downloaded_date);
        RecyclerView rvPermissions = dialog.findViewById(R.id.rv_permissions);
        TextView txtFilePath = dialog.findViewById(R.id.txt_apkPath);
        ImageView imgDropDown = dialog.findViewById(R.id.img_permission_arrow);
        rvPermissions.setVisibility(View.GONE);
        cardViewPermissions.setVisibility(View.GONE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvPermissions.setLayoutManager(linearLayoutManager);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String installTime = dateFormat.format( new Date(new File(apkListModel.getApkPath()).lastModified()));
        txtDownloadedDate.setText(installTime);
        txtFilePath.setText(apkListModel.getApkPath());
        long fileSizeInMB = 0;
        try {
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
        if(apkListModel.getPackageInfo().applicationInfo!=null) {
            String apkName = apkListModel.getPackageInfo().applicationInfo.loadLabel(getContext().getPackageManager()) + ".apk";
            txtAppName.setText(apkName);
            Drawable icon = getContext().getPackageManager().getApplicationIcon(apkListModel.getPackageInfo().applicationInfo);
            imgApp.setImageDrawable(icon);
        }else {
            txtAppName.setText(apkListModel.getPackageInfo().packageName);
            Drawable icon = getContext().getPackageManager().getApplicationIcon(apkListModel.getPackageInfo().applicationInfo);
            imgApp.setImageDrawable(icon);
        }
        permissionsList.clear();
        try {
            permissionsList.addAll(new BaseClass().getListOfPermissions(getContext(), apkListModel.getApkPath()));
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        ApkPermissionAdapter permissionAdapter = new ApkPermissionAdapter(getContext(),permissionsList);
        rvPermissions.setAdapter(permissionAdapter);

        imgDropDown.setOnClickListener(view -> {
            if(rvPermissions.getVisibility()==View.VISIBLE) {
                rvPermissions.setVisibility(View.GONE);
                cardViewPermissions.setVisibility(View.GONE);
                imgDropDown.setRotation(0);
            }else {
                rvPermissions.setVisibility(View.VISIBLE);
                cardViewPermissions.setVisibility(View.VISIBLE);
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
        dialog = new Dialog(getActivity(),R.style.AlertDialogCustom);
        dialog.setContentView(R.layout.dialog_apkinstall);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
        window.setGravity(Gravity.CENTER);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        LinearLayout linearLayout = dialog.findViewById(R.id.adsbanner);
        linearLayout.addView(AHandler.getInstance().getBannerHeader(getActivity()));
        //linearLayout.addView(homeActivity.getBanner());
        CardView cvPermission = dialog.findViewById(R.id.cardView_permissions);
        ImageView imgApp = dialog.findViewById(R.id.img_apk);
        TextView txtAppName = dialog.findViewById(R.id.txt_apkName);
        TextView txtVersionName = dialog.findViewById(R.id.txt_versionName);
        RecyclerView rvPermissions = dialog.findViewById(R.id.rv_permissions);
        TextView permissionText = dialog.findViewById(R.id.txt_permissions);
        TextView txtInstall = dialog.findViewById(R.id.btn_install);
        TextView txtDownloadedDate = dialog.findViewById(R.id.txt_downloaded_date);
        ImageView imgDropDown = dialog.findViewById(R.id.img_permission_arrow);
        rvPermissions.setVisibility(View.GONE);
        cvPermission.setVisibility(View.GONE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvPermissions.setLayoutManager(linearLayoutManager);
        if(apkListModel.getPackageInfo().applicationInfo!=null){
            String apkName = apkListModel.getPackageInfo().applicationInfo.loadLabel(getContext().getPackageManager()) + ".apk";
            txtAppName.setText(apkName);
        }else {
            txtAppName.setText(apkListModel.getPackageInfo().packageName);
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String installTime = dateFormat.format( new Date(new File(apkListModel.getApkPath()).lastModified()));
        txtDownloadedDate.setText(installTime);
        long fileSizeInMB = 0;
        try {
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

        txtInstall.setOnClickListener(view ->{
            homeActivity.installApk(new File(apkListModel.getApkPath()));
            //installApk(new File(apkListModel.getApkPath()));
            dialog.dismiss();
        });

        imgDropDown.setOnClickListener(view -> {
            if(rvPermissions.getVisibility()==View.VISIBLE) {
                rvPermissions.setVisibility(View.GONE);
                cvPermission.setVisibility(View.GONE);
                imgDropDown.setRotation(0);
            }else {
                rvPermissions.setVisibility(View.VISIBLE);
                cvPermission.setVisibility(View.VISIBLE);
                imgDropDown.setRotation(180);
            }
        });
    }

    public void setApkFilter(String newText) {

        List<ApkListModel> newList = new ArrayList<>();

        if(!newText.isEmpty()) {
            for (int i = 0; i < apkList.size(); i++) {
                if ((apkList.get(i).getPackageInfo().packageName.toLowerCase().contains(newText.toLowerCase()))) {
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
        selectedItemList.add(apkListModel);

        HomeActivity.count = 0;
    }

    @Override
    public void onClickDeleteItem(ApkListModel apkListModel) {
        for(int i =0 ;i<selectedItemList.size();i++)
        {
            if(selectedItemList.get(i).getApkName().equalsIgnoreCase(apkListModel.getApkName()))
            {
                selectedItemList.remove(i);
            }
        }
    }

    public void deleteApk() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete an APK");
        builder.setMessage("Do you want to delete the APKs?").setPositiveButton("Ok",(dialogInterface, i) -> {
           /* for (int j=0;j<selectedItemList.size();j++) {
                String filename = selectedItemList.get(j).getApkPath();
                File file = new File(filename);
                if (getActivity() != null) {
                    Uri imageUriLcl = FileProvider.getUriForFile(getActivity(),
                            getActivity().getApplicationContext().getPackageName() +
                                    ".provider", file);
                    ContentResolver contentResolver = getActivity().getContentResolver();
                    contentResolver.delete(imageUriLcl, null, null);
                }
            }
            progressDialog.dismiss();*/
           // Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
            try {
                new deleteApkAsync().execute().get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            getchangeItemColor();
            apksAdapter.changeItemColor();
            apksAdapter.notifyDataSetChanged();
            refreshpage();
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

    public class deleteApkAsync extends AsyncTask<Void,Void,Void>
    {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please wait.....");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (int j=0;j<selectedItemList.size();j++) {
                String filename = selectedItemList.get(j).getApkPath();
                File file = new File(filename);
                if (getActivity() != null) {
                    Uri imageUriLcl = FileProvider.getUriForFile(getActivity(),
                            getActivity().getApplicationContext().getPackageName() +
                                    ".provider", file);
                    ContentResolver contentResolver = getActivity().getContentResolver();
                    contentResolver.delete(imageUriLcl, null, null);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
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

    @Override
    public void refreshpage() {
        getAllApks();
    }

    public void refreshAdapter()
    {
        selectedItemList.clear();
        apksAdapter.changeItemColor();
    }

}

