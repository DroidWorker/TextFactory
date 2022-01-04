package com.example.textfactory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.HttpAuthHandler;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class ShareActivity extends AppCompatActivity {
    boolean flag=false;
    WebView wv;
    Context ctx;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted
                } else {
                    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
                return;
        }
    }

    private class MyWebClient extends WebViewClient
    {
        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request)
        {
            Toast.makeText(ctx, "aaa", Toast.LENGTH_LONG).show();
            if(request.getUrl().toString().endsWith(".ttf"))
            {
                Uri source = Uri.parse(request.getUrl().toString());
                DownloadManager.Request req = new DownloadManager.Request(source);
                req.setDescription("loading my file");
                req.setTitle("myfile.ttf");
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
                    req.allowScanningByMediaScanner();
                    req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                }
                req.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "myfile.ttf");
                DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(req);
            }
            else{
                wv.loadUrl(request.getUrl().toString());
            }
            return true;
        }
        public void onPageFinished(WebView view, String url)
        {
            if (flag)return;
            String s = "function built(type){s = JSON.stringify(data);document.f1.data.value=s;document.f1.name.value=document.fsetting.name.value;document.f1.copy.value=document.fsetting.copy.value;document.f1.letterspace.value=document.fsetting.letterspace.value;document.f1.monospacewidth.value=document.fsetting.monospacewidth.value;\tif(document.fsetting.monospace.checked){document.f1.monospace.value=1;}else{document.f1.monospace.value=0;}document.f1.ascender.value=document.fsetting.ascender.value;document.f1.descender.value=document.fsetting.descender.value;document.f1.linegap.value=document.fsetting.linegap.value;document.f1.wordspacing.value=document.fsetting.wordspacing.value;if(type=='mobile'){document.f1.mobile.value=1;}else if(type=='inspect'){document.f1.inspect.value=1;}else{document.f1.mobile.value=0;document.f1.inspect.value=0;}if(isie){$(window).unbind(\"beforeunload\");document.f1.target='_self';}document.f1.submit();};built('');";
            wv.loadUrl("javascript: var els = document.getElementsByClassName('textbutton');els[els.length-2].click();");
            wv.loadUrl("javascript:var el = document.getElementById('buttonSettingBuild'); el.click();");
            //wv.loadUrl("javascript:"+s);
            //wv.loadUrl("javascript: var els = document.getElementsByClassName('textbutton');els[els.length-2].click();");
            Toast.makeText(ctx, "Спасибо гуглу за это", Toast.LENGTH_LONG).show();
            flag=true;
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            Toast.makeText(ctx, "bbb", Toast.LENGTH_LONG).show();
            if(url.endsWith(".ttf"))
            {
                Uri source = Uri.parse(url);
                DownloadManager.Request request = new DownloadManager.Request(source);
                request.setDescription("loading my file");
                request.setTitle("myfile.ttf");
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                }
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "myfile.ttf");
                DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);
            }
            else{
                wv.loadUrl(url);
            }
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingURL)
        {
            Log.e("WEB_WIEV", "error: "+errorCode + " message: " + description);
        }
    }
    public void clicked(View view)
    {
        wv.loadUrl("javascript: var els = document.getElementsByClassName('textbutton');els[els.length-2].click();");
        wv.loadUrl("javascript:var el = document.getElementById('buttonSettingBuild'); el.click();");

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        ctx = this;

        wv = findViewById(R.id.applyingDialogWV);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setWebViewClient(new MyWebClient());
        wv.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                new AlertDialog.Builder(ctx)
                        .setTitle("App Titler")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        result.confirm();
                                    }
                                })
                        .setNegativeButton(android.R.string.cancel,
                                new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        result.cancel();
                                    }
                                })
                        .create()
                        .show();

                return true;
            }
        });
        wv.setDownloadListener(new DownloadListener(){
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength){
                DownloadManager.Request request = new DownloadManager.Request(
                        Uri.parse(url));

                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "weeeeeeee");
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
                Toast.makeText(getApplicationContext(), "Downloading File", //To notify the Client that the file is being downloaded
                        Toast.LENGTH_LONG).show();
            }
        });
        wv.loadUrl("http://www.pentacom.jp/pentacom/bitfontmaker2/");

        /*int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        wv = findViewById(R.id.applyingDialogWV);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setSupportMultipleWindows(true);
        wv.getSettings().setDomStorageEnabled(true);
        wv.loadUrl("http://www.pentacom.jp/pentacom/bitfontmaker2/");
        //wv.loadUrl("https://trashbox.ru/files30/1574061/com.lenovo.anyshare.gps_6.2.72_ww_4060272.apk/");
        wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wv.setDownloadListener(new DownloadListener(){
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength){
                DownloadManager.Request request = new DownloadManager.Request(
                        Uri.parse(url));

                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "weeeeeeee");
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
                Toast.makeText(getApplicationContext(), "Downloading File", //To notify the Client that the file is being downloaded
                        Toast.LENGTH_LONG).show();
            }
        });
        wv.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                new AlertDialog.Builder(ctx)
                        .setTitle("App Titler")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        result.confirm();
                                    }
                                })
                        .setNegativeButton(android.R.string.cancel,
                                new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        result.cancel();
                                    }
                                })
                        .create()
                        .show();

                return true;
            }
        });
        wv.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        //wv.loadUrl("javascript:s = JSON.stringify(data);document.f1.data.value=s;document.f1.name.value=document.fsetting.name.value;document.f1.copy.value=document.fsetting.copy.value;document.f1.letterspace.value=document.fsetting.letterspace.value;document.f1.monospacewidth.value=document.fsetting.monospacewidth.value;if(document.fsetting.monospace.checked){document.f1.monospace.value=1;}else{document.f1.monospace.value=0;}document.f1.ascender.value=document.fsetting.ascender.value;document.f1.descender.value=document.fsetting.descender.value;document.f1.linegap.value=document.fsetting.linegap.value;document.f1.wordspacing.value=document.fsetting.wordspacing.value;if(type=='mobile'){document.f1.mobile.value=1;}else if(type=='inspect'){document.f1.inspect.value=1;}else{document.f1.mobile.value=0;document.f1.inspect.value=0;}if(isie){$(window).unbind(\"beforeunload\");document.f1.target='_self';}document.f1.submit();");
        /*wv.setWebChromeClient(new WebChromeClient(){

            @Override
            public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, android.os.Message resultMsg)
            {
                Toast.makeText(ctx, "aaaaaaaaaaaaaaaaaaaaaaaaaaAAAAAAAAAAAAAAAAAAAAAa", Toast.LENGTH_LONG).show();
                WebView.HitTestResult result = view.getHitTestResult();
                String data = result.getExtra();
                //Context context = view.getContext();
                Toast.makeText(ctx, data, Toast.LENGTH_LONG).show();
                /*Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(data));
                context.startActivity(browserIntent);*/
 /*               return false;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                new AlertDialog.Builder(ctx)
                        .setTitle("App Titler")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        result.confirm();
                                    }
                                })
                        .setNegativeButton(android.R.string.cancel,
                                new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        result.cancel();
                                    }
                                })
                        .create()
                        .show();

                return true;
            }
        });
        wv.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url)
            {
                Toast.makeText(ctx, "Спасибо гуглу за это", Toast.LENGTH_LONG).show();
                if (flag) return;
                String js = "s = JSON.stringify(data);type = 'mobile'; document.f1.data.value=s;document.f1.name.value='asd';document.f1.copy.value='dsa';document.f1.letterspace.value=1;document.f1.monospacewidth.value=0;document.f1.ascender.value=12;document.f1.descender.value=4;document.f1.linegap.value=1;document.f1.wordspacing.value=5;document.f1.mobile.value=0;document.f1.inspect.value=0;document.f1.target='_self';document.f1.submit();";
                //wv.loadUrl("javascript: document.getElementById('dataview').value = '{\"65\":[0,0,0,96,256,512,0,0,0,0,0,0,0,0,0,0],\"name\":\"\",\"copy\":\"\",\"letterspace\":\"64\",\"basefont_size\":\"512\",\"basefont_left\":\"62\",\"basefont_top\":\"0\",\"basefont\":\"Arial\",\"basefont2\":\"\"}'; document.getElementById('loadbutton2').click();");
                //wv.loadUrl("javascript: "+js);
                //wv.loadUrl("javascript:function built(type){if ( true){s = JSON.stringify(data);document.f1.data.value=s;document.f1.name.value=document.fsetting.name.value;document.f1.copy.value=document.fsetting.copy.value;document.f1.letterspace.value=document.fsetting.letterspace.value;document.f1.monospacewidth.value=document.fsetting.monospacewidth.value;if(document.fsetting.monospace.checked){document.f1.monospace.value=1;}else{document.f1.monospace.value=0;}document.f1.ascender.value=document.fsetting.ascender.value;document.f1.descender.value=document.fsetting.descender.value;document.f1.linegap.value=document.fsetting.linegap.value;document.f1.wordspacing.value=document.fsetting.wordspacing.value;if(type=='mobile'){document.f1.mobile.value=1;}else if(type=='inspect'){document.f1.inspect.value=1;}else{document.f1.mobile.value=0;document.f1.inspect.value=0;}document.f1.target='_self';document.f1.submit();}};built('');");
                wv.loadUrl("javascript: build('');");
                flag= true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                Toast.makeText(ctx, "error code:" + errorCode + " - " + description, Toast.LENGTH_LONG).show();
            }
        });*/
    }
}