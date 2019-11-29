package app.adshandler;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import app.pnd.adshandler.R;
import app.rest.request.DataRequest;
import app.server.v2.DataHubHandler;
import app.server.v2.InHouse;
import app.socket.EngineApiController;
import app.socket.EngineClient;
import app.socket.Response;

public class FullPagePromo extends Activity implements DataHubHandler.InHouseCallBack {
    private ImageView adsimage;
    private String type;
    private String clickLink;
    private RelativeLayout imageRL;
    private WebView webView;

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.fullpageprompt);
        adsimage = findViewById(R.id.adsimage);
        Button exit = findViewById(R.id.exit);
        imageRL = findViewById(R.id.imageRL);
        webView = findViewById(R.id.webView);
        System.out.println("here is the type type 0" + " ");
        try {
            Intent intent = getIntent();
            if (intent != null) {
                type = intent.getStringExtra("type");
                System.out.println("here is the type type 1" + " " + type);
            }
        } catch (Exception e) {
            System.out.println("here is the type type 2" + " " + type);
            type = EngineClient.IH_FULL;
        }
        if (type == null)
            type = EngineClient.IH_FULL;

        DataRequest request = new DataRequest();
        EngineApiController mController = new EngineApiController(FullPagePromo.this, new Response() {
            @Override
            public void onResponseObtained(Object response, int responseType, boolean isCachedData) {
                System.out.println("here is the response of INHOUSE" + " " + response);
                new DataHubHandler().parseInHouseService(FullPagePromo.this, response.toString(), FullPagePromo.this);
            }

            @Override
            public void onErrorObtained(String errormsg, int responseType) {
                System.out.println("here is the onerr" + " " + errormsg);
            }
        }, EngineApiController.INHOUSE_CODE);
        System.out.println("here is the type type 3" + " " + type);
        mController.setInHouseType(type);
        mController.getInHouseData(request);


        exit.setOnClickListener(new View.OnClickListener() {


            public void onClick(View arg0) {
                finish();

            }
        });

        adsimage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (clickLink != null && !clickLink.isEmpty()) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    //i.setData(Uri.parse(MasterData.cp_link));
                    i.setData(Uri.parse(clickLink));
                    startActivity(i);
                }
                finish();
            }
        });
    }

    @Override
    public void onInhouseDownload(InHouse inHouse) {
        System.out.println("here is the onInhouseDownload " + inHouse.campType + " " + inHouse.src + " " + inHouse.clicklink);
        if (inHouse.campType != null) {
            if (inHouse.campType.equalsIgnoreCase("html")) {
                imageRL.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                //webView.loadUrl(inHouse.html);
                webView.loadData(inHouse.html, "text/html", null);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setBuiltInZoomControls(true);
                webView.getSettings().setDomStorageEnabled(true);
                webView.getSettings().setDisplayZoomControls(false);

//                webView.setWebViewClient(new NavWebViewClient());

            } else {
                webView.setVisibility(View.GONE);
                imageRL.setVisibility(View.VISIBLE);

                if (inHouse.src != null && !inHouse.src.isEmpty()) {
                    Picasso.get().load(inHouse.src).into(adsimage);
                }
                if (inHouse.clicklink != null && !inHouse.clicklink.isEmpty()) {
                    this.clickLink = inHouse.clicklink;
                }
            }
        }

    }

}
