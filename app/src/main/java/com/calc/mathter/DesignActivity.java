package com.calc.mathter;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.calc.mathter.utils.Save;
import com.calc.mathter.utils.SaveStates;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;

public class DesignActivity extends AppCompatActivity {

    private ValueCallback<Uri[]> callback;
    private String photoPath;
    WebView show;
    ProgressBar progressBar;
    Save save;
    SaveStates st;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design);
        show = findViewById(R.id.show);
        progressBar = findViewById(R.id.progressBar);
        st = new SaveStates(this);
        try {
            save = new Save(this);
        } catch (JSONException e) {}

        setViewSettings();

        show.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return overrideUrl(view, url);
            }

            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return overrideUrl(view, request.getUrl().toString());
            }

            public boolean overrideUrl(WebView view, String url) {
                if (url.startsWith("mailto:")) {
                    Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
                    startActivity(i);
                    return true;
                } else if (url.startsWith("tg:") || url.startsWith("https://t.me") || url.startsWith("https://telegram.me")) {
                    try {
                        WebView.HitTestResult result = view.getHitTestResult();
                        String data = result.getExtra();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(data));
                        view.getContext().startActivity(intent);
                    } catch (Exception ex) {
                    }
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (save.isFirst()) {
                    try {
                        save.setPoint(url);
                    } catch (JSONException e) { }
                    try {
                        save.setFirst(false);
                    } catch (JSONException e) { }
                    CookieManager.getInstance().flush();
                }
                CookieManager.getInstance().flush();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
        });

        show.setWebChromeClient(new WebChromeClient() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void checkPermission() {
                ActivityCompat.requestPermissions(
                        DesignActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA},
                        1);
            }

            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
                                             FileChooserParams fileChooserParams) {
                int permissionStatus = ContextCompat.checkSelfPermission(DesignActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                    if (callback != null) {
                        callback.onReceiveValue(null);
                    }
                    callback = filePathCallback;
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                            takePictureIntent.putExtra("PhotoPath", photoPath);
                        } catch (IOException ex) {
                        }
                        if (photoFile != null) {
                            photoPath = "file:" + photoFile.getAbsolutePath();
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                    Uri.fromFile(photoFile));
                        } else {
                            takePictureIntent = null;
                        }
                    }
                    Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                    contentSelectionIntent.setType("image/*");
                    Intent[] intentArray;
                    if (takePictureIntent != null) {
                        intentArray = new Intent[]{takePictureIntent};
                    } else {
                        intentArray = new Intent[0];
                    }
                    Intent chooser = new Intent(Intent.ACTION_CHOOSER);
                    chooser.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                    chooser.putExtra(Intent.EXTRA_TITLE, "Photo");
                    chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                    startActivityForResult(chooser, 1);
                    return true;
                } else
                    checkPermission();
                return false;
            }

            private File createImageFile() throws IOException {
                File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "DirectoryNameHere");
                if (!imageStorageDir.exists())
                    imageStorageDir.mkdirs();
                imageStorageDir = new File(imageStorageDir + File.separator + "Photo_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
                return imageStorageDir;
            }


            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setActivated(true);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                    progressBar.setActivated(false);
                }
            }
        });
        String point = st.getPoint();
        show.loadUrl(point);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != 1 || callback == null) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (data == null || data.getData() == null) {
                if (photoPath != null) {
                    results = new Uri[]{Uri.parse(photoPath)};
                }
            } else {
                String dataString = data.getDataString();
                if (dataString != null) {
                    results = new Uri[]{Uri.parse(dataString)};
                }
            }
        }
        callback.onReceiveValue(results);
        callback = null;
    }


    @Override
    protected void onPause() {
        super.onPause();
        CookieManager.getInstance().flush();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CookieManager.getInstance().flush();
    }

    public void setViewSettings() {
        show.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        show.requestFocus(View.FOCUS_DOWN);
        show.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        show.getSettings().setUserAgentString(show.getSettings().getUserAgentString());

        show.getSettings().setJavaScriptEnabled(true);
        show.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        show.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        show.getSettings().setAppCacheEnabled(true);
        show.getSettings().setDomStorageEnabled(true);
        show.getSettings().setDatabaseEnabled(true);
        show.getSettings().setSupportZoom(false);
        show.getSettings().setAllowFileAccess(true);
        show.getSettings().setAllowFileAccess(true);
        show.getSettings().setAllowContentAccess(true);

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.acceptCookie();
        cookieManager.setAcceptThirdPartyCookies(show, true);
        cookieManager.flush();

        show.getSettings().setLoadWithOverviewMode(true);
        show.getSettings().setUseWideViewPort(true);

        show.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        show.getSettings().setPluginState(WebSettings.PluginState.ON);
        show.getSettings().setSavePassword(true);
    }

    @Override
    public void onBackPressed() {
        if(show.canGoBack()){
            show.goBack();
        }else{
            System.exit(0);
        }
    }
}