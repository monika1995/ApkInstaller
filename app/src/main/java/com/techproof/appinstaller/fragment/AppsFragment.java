package com.techproof.appinstaller.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.techproof.appinstaller.Common.BaseClass;
import com.techproof.appinstaller.Common.Constant;
import com.techproof.appinstaller.Common.DebugLogger;
import com.techproof.appinstaller.R;
import com.techproof.appinstaller.activity.HomeActivity;
import com.techproof.appinstaller.adapter.AppPermissionAdapter;
import com.techproof.appinstaller.adapter.AppsAdapter;
import com.techproof.appinstaller.model.FileListenerService;
import com.techproof.appinstaller.model.RefreshpageInterface;
import com.techproof.appinstaller.model.request.CheckUpdateRequest;
import com.techproof.appinstaller.model.request.DataRequest;
import com.techproof.appinstaller.model.response.CheckUpdateResponse;
import com.techproof.appinstaller.model.response.DataResponse;
import com.techproof.appinstaller.network.ApiClient;
import com.techproof.appinstaller.network.ApiInterface;
import com.techproof.appinstaller.network.MCrypt;
import com.techproof.appinstaller.utils.AppUtils;
import com.wang.avi.AVLoadingIndicatorView;

import org.jsoup.Jsoup;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import app.adshandler.AHandler;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppsFragment extends Fragment implements AppsAdapter.AppsOnClickListener, RefreshpageInterface {

    @BindView(R.id.rv_apps)
    RecyclerView rvApps;
    @BindView(R.id.txt_totalApps)
    TextView txtTotalApps;
    @BindView(R.id.txt_no_record_found)
    TextView txtNoRecordFound;
    @BindView(R.id.img_sorting)
    ImageView imgSorting;
    private LinearLayoutManager linearLayoutManager;
    private AppsAdapter appsAdapter;
    private List<PackageInfo> appsList, sortedList;
    private Dialog dialog, dialog1;
    private int lastSortingType = 0;
    private String appVersionName;
    private ImageView imgAndroid;
    private TextView textView, txtPleaseWait, txtloadingUpdate;
    private TextView btnUpdate;
    private AVLoadingIndicatorView progressBar;
    HomeActivity homeActivity;

    private ApiInterface apiInterface;
    private CheckUpdateRequest checkUpdateRequest;
    private List<String> pkglist;
    private Gson gson;
    private MCrypt mcrypt = new MCrypt();
    private Toolbar toolbar;
    int position;
    private PackageInfo selectedItem;
    private List<PackageInfo> selectedItemList;
    int UNINSTALL_REQUEST_CODE = 101;
    int DELETE_REQUEST_CODE = 102;
    private static List<String> temparr;
    private static List<Boolean> selectionArr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_apps, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        System.out.println("bcsdkjdb-AppFragement---");

        homeActivity = (HomeActivity) getActivity();
        ButterKnife.bind(this, view);
        if (getActivity() != null) {
            toolbar = getActivity().findViewById(R.id.toolbar);
        }
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        temparr = new ArrayList<>();
        selectedItem = new PackageInfo();
        selectedItemList = new ArrayList<>();
        selectedItemList.clear();
        selectionArr = new ArrayList<>();
        pkglist = new ArrayList<>();
        appsList = new ArrayList<>();
        sortedList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getContext());
        rvApps.setLayoutManager(linearLayoutManager);
        appsAdapter = new AppsAdapter(getContext(), sortedList, this);
        rvApps.setAdapter(appsAdapter);


        imgSorting.setOnClickListener(view1 -> dialogSorting());

        System.out.println("bcsdkjdb-AppFragement---1");

        getAllApps();
        //new Handler().postDelayed(() -> getAllApps(),1000);
    }

    private int getScreenSize() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        DebugLogger.d("Screensize" + width + " " + height);
        return height;
    }

    private void dialogSorting() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        PopupWindow pw = new PopupWindow(inflater.inflate(R.layout.dialog_sorting, null), WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        int height = getScreenSize();

        if (height >= 2120) {
            pw.showAtLocation(getActivity().findViewById(R.id.img_sorting), Gravity.END, 50, -420);
        } else if (height >= 1407) {
            pw.showAtLocation(getActivity().findViewById(R.id.img_sorting), Gravity.END, 50, -250);
        } else {
            pw.showAtLocation(getActivity().findViewById(R.id.img_sorting), Gravity.END, 40, -110);
        }

        View v = pw.getContentView();
        RadioGroup radioGroup = v.findViewById(R.id.rdGroup);
        if (lastSortingType > 0) {
            radioGroup.check(lastSortingType);
        } else {
            radioGroup.check(R.id.radio_SortByName);
        }

        RadioButton rdSystemApps = v.findViewById(R.id.radio_systemApps);
        RadioButton rdInstalledApps = v.findViewById(R.id.radio_installedApps);

        rdInstalledApps.setVisibility(View.VISIBLE);
        rdSystemApps.setVisibility(View.VISIBLE);

        radioGroup.setOnCheckedChangeListener((radioGroup1, i) -> {
            if (radioGroup1.getCheckedRadioButtonId() == R.id.radio_systemApps) {
                setFilter(R.id.radio_systemApps);
                lastSortingType = R.id.radio_systemApps;
            } else if (radioGroup1.getCheckedRadioButtonId() == R.id.radio_installedApps) {
                setFilter(R.id.radio_installedApps);
                lastSortingType = R.id.radio_installedApps;
            } else if (radioGroup1.getCheckedRadioButtonId() == R.id.radio_SortByName) {
                setFilter(R.id.radio_SortByName);
                lastSortingType = R.id.radio_SortByName;
            } else if (radioGroup1.getCheckedRadioButtonId() == R.id.radio_SortBySize) {
                setFilter(R.id.radio_SortBySize);
                lastSortingType = R.id.radio_SortBySize;
            }
            pw.dismiss();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private void getAllApps() {
        try {
            new getApps(AppsFragment.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setFilter(int type) {
        sortedList.clear();
        if (type == R.id.radio_systemApps) {
            for (PackageInfo apps : appsList) {
                if ((apps.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                    sortedList.add(apps);
                }
            }

        } else if (type == R.id.radio_installedApps) {
            for (PackageInfo apps : appsList) {
                if ((apps.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 1) {
                    sortedList.add(apps);
                }
            }

        } else if (type == R.id.radio_SortByName) {
            sortedList.addAll(appsList);
            Collections.sort(sortedList, (obj1, obj2) -> {
                if (getActivity() != null) {
                    if (getActivity().getPackageManager() != null) {
                        return obj1.applicationInfo.loadLabel(getActivity().getPackageManager()).toString().compareToIgnoreCase(obj2.applicationInfo.loadLabel(getActivity().getPackageManager()).toString());
                    }
                }
                return 0;
            });

        } else if (type == R.id.radio_SortBySize) {
            sortedList.addAll(appsList);
            Collections.sort(sortedList, (obj1, obj2) -> {
                ApplicationInfo tmpInfo = null;
                long obj1Size = 0, Obj2Size = 0;
                try {
                    long size = new File(obj1.applicationInfo.sourceDir).length();
                    long fileSizeInKB = size / 1024;
                    long size1 = new File(obj2.applicationInfo.sourceDir).length();
                    long fileSizeInKB1 = size1 / 1024;
                    obj1Size = fileSizeInKB / 1024;
                    Obj2Size = fileSizeInKB1 / 1024;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return Integer.parseInt(String.valueOf(Obj2Size - obj1Size));
            });
        }
        txtNoRecordFound.setVisibility(View.GONE);
        rvApps.setVisibility(View.VISIBLE);

        appsAdapter.updateCount(sortedList);

        int count = (sortedList.size() - appsAdapter.getListCount());
        txtTotalApps.setText("Total Apps: " + count);

        appsAdapter.notifyDataSetChanged();

    }

    @Override
    public void launchApps(String packageName) {

        new Handler().postDelayed(() -> {

            if(getContext()!=null) {
                if( packageName!=null && !packageName.equals("")) {
                    Intent launchIntent = getContext().getPackageManager().getLaunchIntentForPackage(packageName);
                    startActivity(launchIntent);
                }
            }

        }, 1000);

        /*AHandler.getInstance().showFullAds(getActivity(),false);
        AppUtils.onClickButtonFirebaseAnalytics(getActivity(), Constant.FIREBASE_APP_LAUNCH);*/

    }

    @Override
    public void redirectToPlayStore(String packageName) {

        new Handler().postDelayed(() -> {
            try {
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
            } catch (Exception e) {
                Toast.makeText(getContext(), "Unable to Connect Try Again...", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }, 1000);


        /*AppUtils.onClickButtonFirebaseAnalytics(getActivity(), Constant.FIREBASE_APP_PLAYSTORE);
        AHandler.getInstance().showFullAds(getActivity(),false);*/
    }

    @Override
    public void appDetails(PackageInfo packageInfo, int position) {
        setDialogAppDetails(packageInfo);

        AppUtils.onClickButtonFirebaseAnalytics(getActivity(), Constant.FIREBASE_APP_DETAILS);
        AHandler.getInstance().showFullAds(getActivity(), false);

    }

    private void setDialogAppDetails(PackageInfo packageInfo) {
        dialog = new Dialog(getActivity(), R.style.AlertDialogCustom);
        dialog.setContentView(R.layout.dialog_appdetails);
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

        CardView cardViewPermissions = dialog.findViewById(R.id.cardView_permissions);
        ImageView imgApp = dialog.findViewById(R.id.img_app);
        TextView txtAppName = dialog.findViewById(R.id.txt_appName);
        TextView txtVersionName = dialog.findViewById(R.id.txt_versionName);
        TextView txtInstallationDate = dialog.findViewById(R.id.txt_installation_date);
        TextView btnUninstall = dialog.findViewById(R.id.btn_uninstall);
        RecyclerView rvPermissions = dialog.findViewById(R.id.rv_permissions);
        ImageView imgDropDown = dialog.findViewById(R.id.img_permission_arrow);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvPermissions.setLayoutManager(linearLayoutManager);
        rvPermissions.setVisibility(View.GONE);
        cardViewPermissions.setVisibility(View.GONE);
        String[] permissions;
        @SuppressLint("SimpleDateFormat")
        //SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY")
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String installTime = dateFormat.format(new Date(packageInfo.firstInstallTime));
        txtInstallationDate.setText(installTime);
        long fileSizeInMB = 0;
        try {
            long size = new File(packageInfo.applicationInfo.sourceDir).length();
            long fileSizeInKB = size / 1024;
            fileSizeInMB = fileSizeInKB / 1024;
        } catch (Exception e) {
            e.printStackTrace();
        }
        String apkSize = fileSizeInMB + " MB | Version - " + appsList.get(position).versionName;
        txtVersionName.setText(apkSize);

        txtAppName.setText(packageInfo.applicationInfo.loadLabel(getContext().getPackageManager()).toString());
        Drawable icon = getContext().getPackageManager().getApplicationIcon(packageInfo.applicationInfo);
        imgApp.setImageDrawable(icon);
        try {
            PackageInfo permission = getContext().getPackageManager().getPackageInfo(packageInfo.packageName, PackageManager.GET_PERMISSIONS);
            permissions = permission.requestedPermissions;
            AppPermissionAdapter permissionAdapter = new AppPermissionAdapter(getContext(), permissions);
            rvPermissions.setAdapter(permissionAdapter);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        btnUninstall.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
            intent.setData(Uri.parse("package:" + packageInfo.packageName));
            intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
            startActivityForResult(intent, UNINSTALL_REQUEST_CODE);
        });

        imgDropDown.setOnClickListener(view -> {
            if (rvPermissions.getVisibility() == View.VISIBLE) {
                rvPermissions.setVisibility(View.GONE);
                cardViewPermissions.setVisibility(View.GONE);
                imgDropDown.setRotation(0);
            } else {
                rvPermissions.setVisibility(View.VISIBLE);
                cardViewPermissions.setVisibility(View.VISIBLE);
                imgDropDown.setRotation(180);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UNINSTALL_REQUEST_CODE) {
            DebugLogger.d("ResultCode" + resultCode);
            if (dialog != null) {
                dialog.dismiss();
            }
            if (resultCode == getActivity().RESULT_OK) {
                DebugLogger.d("onActivityResult: user accepted the (un)install");
                // Toast.makeText(getActivity(), "Uninstalled", Toast.LENGTH_SHORT).show();
            } else if (resultCode == getActivity().RESULT_CANCELED) {
                DebugLogger.d("onActivityResult: user canceled the (un)install");
            } else if (resultCode == getActivity().RESULT_FIRST_USER) {
                DebugLogger.d("onActivityResult: failed to (un)install");
            }
            refreshpage();
        } else if (requestCode == DELETE_REQUEST_CODE) {

            if (selectedItemList.size() > 0) {
                if (resultCode == getActivity().RESULT_OK) {
                    DebugLogger.d("onActivityResult: user accepted the (un)install");
                } else if (resultCode == getActivity().RESULT_CANCELED) {
                    DebugLogger.d("onActivityResult: user canceled the (un)install");
                } else if (resultCode == getActivity().RESULT_FIRST_USER) {
                    DebugLogger.d("onActivityResult: failed to (un)install");
                }
                deleteApp();
            } else {
                selectedItemList.clear();
            }
            //Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
            getchangeItemColor();
            appsAdapter.changeItemColor();
            refreshpage();
        } else if (requestCode == 103) {
            getchangeItemColor();
            appsAdapter.changeItemColor();
        }
    }

    private void getchangeItemColor() {
        toolbar.getMenu().findItem(R.id.action_search).setVisible(true);
        toolbar.getMenu().findItem(R.id.action_more).setVisible(true);
        toolbar.getMenu().findItem(R.id.action_delete).setVisible(false);
        toolbar.getMenu().findItem(R.id.action_share).setVisible(false);
    }

    @Override
    public void appUpdates(PackageInfo packageInfo) {
        boolean isNetwork = BaseClass.isNetworkAvailable(getContext());
        if (isNetwork) {
            setDialogUpdates(packageInfo);
            try {
                new Handler().postDelayed(() -> {
                    try {
                        new VersionChecker().execute(packageInfo.packageName).get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }, 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }

            AppUtils.onClickButtonFirebaseAnalytics(getActivity(), Constant.FIREBASE_APP_CHECKUPDATES);
            AHandler.getInstance().showFullAds(getActivity(), false);
        } else {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void setDialogUpdates(PackageInfo packageInfo) {
        appVersionName = packageInfo.versionName;
        dialog1 = new Dialog(getActivity(), R.style.AlertDialogCustom);
        dialog1.setContentView(R.layout.dialog_updates);
        Window window = dialog1.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);
        dialog1.setCancelable(true);
        dialog1.setCanceledOnTouchOutside(true);
        dialog1.show();

        LinearLayout linearLayout = dialog1.findViewById(R.id.adsbanner);
        linearLayout.addView(AHandler.getInstance().getBannerHeader(getActivity()));
        //linearLayout.addView(homeActivity.getBanner());
        ImageView imgApp = dialog1.findViewById(R.id.img_app);
        TextView txtAppName = dialog1.findViewById(R.id.txt_appName);
        TextView txtVersionName = dialog1.findViewById(R.id.txt_versionName);
        btnUpdate = dialog1.findViewById(R.id.btn_update);
        imgAndroid = dialog1.findViewById(R.id.img_android);
        textView = dialog1.findViewById(R.id.txt_Update_status);
        btnUpdate = dialog1.findViewById(R.id.btn_update);
        progressBar = dialog1.findViewById(R.id.progress_updates);
        txtPleaseWait = dialog1.findViewById(R.id.txt_pleasewait);
        txtloadingUpdate = dialog1.findViewById(R.id.txt_loadingUpdates);
        imgAndroid.setVisibility(View.GONE);

        progressBar.show();
        btnUpdate.setEnabled(false);

        String appName = packageInfo.applicationInfo.loadLabel(getContext().getPackageManager()).toString();
        String currentVersion = packageInfo.versionName;
        String packageName = packageInfo.packageName;
        txtAppName.setText(appName);
        Drawable icon = getContext().getPackageManager().getApplicationIcon(packageInfo.applicationInfo);
        imgApp.setImageDrawable(icon);
        ApplicationInfo tmpInfo = null;
        long fileSizeInMB = 0;
        try {
            long size = new File(packageInfo.applicationInfo.sourceDir).length();
            long fileSizeInKB = size / 1024;
            fileSizeInMB = fileSizeInKB / 1024;
        } catch (Exception e) {
            e.printStackTrace();
        }
        String apkSize = fileSizeInMB + " MB | Version - " + currentVersion;
        txtVersionName.setText(apkSize);


        btnUpdate.setOnClickListener(view ->
        {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
            }
            dialog1.dismiss();
        });
    }

    @SuppressLint("StaticFieldLeak")
    public class VersionChecker extends AsyncTask<String, String, String> {
        private String newVersion;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + params[0])
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select(".hAyfc .htlgb")
                        .get(7)
                        .ownText();
                publishProgress(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return newVersion;
        }

        @SuppressLint("NewApi")
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            progressBar.setVisibility(View.INVISIBLE);
            txtPleaseWait.setVisibility(View.INVISIBLE);
            txtloadingUpdate.setVisibility(View.INVISIBLE);
            DebugLogger.d("onProgressUpdate......" + newVersion);
            try {
                String mLatestVersionName = newVersion;
                if (mLatestVersionName.equals("Varies with device")) {
                    pkglist.clear();
                    pkglist.add(values[0]);
                    checkUpdateRequest = new CheckUpdateRequest(getContext(), pkglist);
                    hitApi(checkUpdateRequest);
                } else if (!appVersionName.equals(mLatestVersionName)) {
                    textView.setText(getString(R.string.update_available));
                    imgAndroid.setVisibility(View.VISIBLE);
                    btnUpdate.setEnabled(true);
                    btnUpdate.setBackground(getContext().getDrawable(R.color.green));
                } else {
                    textView.setText(getString(R.string.no_update_available));
                    imgAndroid.setVisibility(View.INVISIBLE);
                    btnUpdate.setBackground(getContext().getDrawable(R.color.dark_grey));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    private String getEncrypterString(String jsonStr) {
        String value = "";
        MCrypt mcrypt = new MCrypt();
        try {
            value = MCrypt.bytesToHex(mcrypt.encrypt(jsonStr));
        } catch (Exception e) {
            DebugLogger.d("exception encryption" + " " + e);
            e.printStackTrace();
        }
        return value;
    }

    private void hitApi(CheckUpdateRequest checkUpdateRequest) {
        gson = new Gson();
        String json = gson.toJson(checkUpdateRequest);
        String encryptRequest = getEncrypterString(json);
        DataRequest dataRequest = new DataRequest();
        dataRequest.data = encryptRequest;
        Call<DataResponse> checkUpdateCall = apiInterface.checkUpdate(dataRequest);
        checkUpdateCall.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                if (response.code() == 200) {
                    DataResponse data = new DataResponse();
                    data.data = response.body().data;
                    try {
                        String dresponse = new String(mcrypt.decrypt(data.data));
                        CheckUpdateResponse checkUpdateResponse = gson.fromJson(json, CheckUpdateResponse.class);
                        if (checkUpdateResponse.getAppDetails().get(0).currentversion.equals(appVersionName)) {
                            textView.setText(getString(R.string.update_available));
                            imgAndroid.setVisibility(View.VISIBLE);
                            btnUpdate.setEnabled(true);
                            btnUpdate.setBackground(getContext().getResources().getDrawable(R.color.green));
                        } else {
                            textView.setText(getString(R.string.no_update_available));
                            imgAndroid.setVisibility(View.INVISIBLE);
                            btnUpdate.setBackground(getContext().getResources().getDrawable(R.color.dark_grey));
                        }
                        DebugLogger.d("Decryption:" + dresponse);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {
                DebugLogger.d("CheckUpdateRequest:" + t.getMessage());
            }
        });
    }

    public void setAppFilter(String newText) {

        List<PackageInfo> newList = new ArrayList<>();

        if (!newText.isEmpty()) {
            for (int i = 0; i < appsList.size(); i++) {
                if ((appsList.get(i).applicationInfo.loadLabel(getActivity().getPackageManager()).toString().toLowerCase().contains(newText.toLowerCase()))) {
                    newList.add(appsList.get(i));
                }
            }

            if (newList.size() > 0) {
                appsAdapter.setSearchFilter(newList);
                txtTotalApps.setText("Total Apps: " + newList.size());
                txtNoRecordFound.setVisibility(View.GONE);
                rvApps.setVisibility(View.VISIBLE);
            } else {
                txtTotalApps.setText("Total Apps: 0");
                txtNoRecordFound.setVisibility(View.VISIBLE);
                rvApps.setVisibility(View.GONE);
            }
        } else {
            if (lastSortingType > 0) {
                setFilter(lastSortingType);
            } else {
                setFilter(R.id.radio_SortByName);
            }
        }
    }

    @Override
    public void onClickItem(PackageInfo packageInfo, int pos) {
        toolbar.getMenu().findItem(R.id.action_search).setVisible(false);
        toolbar.getMenu().findItem(R.id.action_more).setVisible(false);
        toolbar.getMenu().findItem(R.id.action_delete).setVisible(true);
        toolbar.getMenu().findItem(R.id.action_share).setVisible(true);

        position = pos;
        selectedItem = packageInfo;
        selectedItemList.add(packageInfo);

        HomeActivity.count = 0;
    }

    @Override
    public void onClickDeleteItem(PackageInfo packageInfo) {
        for (int i = 0; i < selectedItemList.size(); i++) {
            if (selectedItemList.get(i).packageName.equalsIgnoreCase(packageInfo.packageName)) {
                selectedItemList.remove(i);
            }
        }
    }

    public void deleteApp() {

        for (int i = 0; i < selectedItemList.size(); i++) {

            Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
            intent.setData(Uri.parse("package:" + selectedItemList.get(i).packageName));
            intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
            startActivityForResult(intent, DELETE_REQUEST_CODE);

            selectedItemList.remove(i);
        }
    }

    public void shareApp() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Hey check out my app at: https://play.google.com/store/apps/details?id=" + selectedItem.packageName);
        sendIntent.setType("text/plain");
        startActivityForResult(sendIntent, 103);
    }

    public void refreshAdapter() {
        selectedItemList.clear();
        appsAdapter.changeItemColor();
    }

    @Override
    public void refreshpage() {
        getAllApps();
    }

    private static class getApps extends AsyncTask<List<PackageInfo>, Void, List<PackageInfo>> {
        private PackageManager pm;
        private ProgressDialog progressDialog;
        WeakReference<AppsFragment> reference;

        getApps(AppsFragment fragment) {
            reference = new WeakReference<>(fragment);
            pm = reference.get().getActivity().getPackageManager();
            progressDialog = new ProgressDialog(fragment.getActivity());
        }

        @Override
        protected void onPreExecute() {

            progressDialog.setMessage("Loading....");
            progressDialog.setIndeterminate(true);
            progressDialog.show();

            super.onPreExecute();
        }

        @Override
        protected List<PackageInfo> doInBackground(List<PackageInfo>... voids) {

            reference.get().appsList.clear();
            List<PackageInfo> apps = reference.get().getActivity().getPackageManager().getInstalledPackages(0);
            for (PackageInfo packageInfo : apps) {
                if (pm.getLaunchIntentForPackage(packageInfo.packageName) != null) {
                    reference.get().appsList.add(packageInfo);
                }
            }
            //publishProgress(appsList);

            return reference.get().appsList;
        }

        @Override
        protected void onPostExecute(List<PackageInfo> result) {
            super.onPostExecute(result);

            if (reference.get().lastSortingType > 0) {
                reference.get().setFilter(reference.get().lastSortingType);
            } else {
                reference.get().setFilter(R.id.radio_SortByName);
            }

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            System.out.println("my list is here" + " " + result.size());

            /*if (!AppUtils.isMyServiceRunning(FileListenerService.class, reference.get().getContext())) {
                System.out.println("bcsdkjdb-AppFragement---data load");                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    reference.get().getContext().startForegroundService(new Intent(reference.get().getContext(), FileListenerService.class));
                } else {
                    reference.get().getContext().startService(new Intent(reference.get().getContext(), FileListenerService.class));
                }
            }*/
        }
    }
}
