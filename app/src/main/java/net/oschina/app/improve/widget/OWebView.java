package net.oschina.app.improve.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import net.oschina.app.AppConfig;
import net.oschina.app.AppContext;
import net.oschina.app.improve.app.AppOperator;
import net.oschina.app.interf.OnWebViewImageListener;
import net.oschina.app.ui.OSCPhotosActivity;
import net.oschina.app.util.StringUtils;
import net.oschina.app.util.TDevice;
import net.oschina.app.util.UIHelper;

/**
 * Created by JuQiu
 * on 16/6/24.
 */

public class OWebView extends WebView {
    public OWebView(Context context) {
        super(context);
        init();
    }

    public OWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public OWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public OWebView(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(context, attrs, defStyleAttr, privateBrowsing);
        init();
    }

    @SuppressLint({"AddJavascriptInterface", "SetJavaScriptEnabled"})
    private void init() {
        setHorizontalScrollBarEnabled(false);

        WebSettings settings = getSettings();
        settings.setDefaultFontSize(14);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);
        settings.setJavaScriptEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            addJavascriptInterface(new OnWebViewImageListener() {
                @Override
                @JavascriptInterface
                public void showImagePreview(String bigImageUrl) {
                    if (bigImageUrl != null && !StringUtils.isEmpty(bigImageUrl)) {
                        OSCPhotosActivity.showImagePreview(getContext(), bigImageUrl);
                    }
                }
            }, "mWebViewImageListener");
        }
    }

    public void loadDetailDataAsync(final String content, Runnable finishCallback) {
        this.setWebViewClient(new OWebClient(finishCallback));
        Context context = getContext();
        if (context != null && context instanceof Activity) {
            final Activity activity = (Activity) context;
            AppOperator.runOnThread(new Runnable() {
                @Override
                public void run() {
                    final String body = setupWebContent(content, true, true, "");
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //loadData(body, "text/html; charset=UTF-8", null);
                            loadDataWithBaseURL("", body, "text/html", "UTF-8", "");
                        }
                    });
                }
            });
        } else {
            Log.e(OWebView.class.getName(), "loadDetailDataAsync error, the Context isn't ok");
        }
    }

    @Override
    public void destroy() {
        setWebViewClient(null);

        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(false);

        removeJavascriptInterface("mWebViewImageListener");
        removeAllViewsInLayout();

        removeAllViews();
        //clearCache(true);

        super.destroy();
    }

    private static String setupWebContent(String content, boolean isShowHighlight, boolean isShowImagePreview, String css) {
        // 读取用户设置：是否加载文章图片--默认有wifi下始终加载图片
        if (AppContext.get(AppConfig.KEY_LOAD_IMAGE, true)
                || TDevice.isWifiOpen()) {
            // 过滤掉 img标签的width,height属性
            content = content.replaceAll("(<img[^>]*?)\\s+width\\s*=\\s*\\S+", "$1");
            content = content.replaceAll("(<img[^>]*?)\\s+height\\s*=\\s*\\S+", "$1");

            // 添加点击图片放大支持
            if (isShowImagePreview) {
                content = content.replaceAll("(<img[^>]+src=\")(\\S+)\"",
                        "$1$2\" onClick=\"javascript:mWebViewImageListener.showImagePreview('$2')\"");
            }
        } else {
            // 过滤掉 img标签
            content = content.replaceAll("<\\s*img\\s+([^>]*)\\s*>", "");
        }

        // 过滤table的内部属性
        content = content.replaceAll("(<table[^>]*?)\\s+border\\s*=\\s*\\S+", "$1");
        content = content.replaceAll("(<table[^>]*?)\\s+cellspacing\\s*=\\s*\\S+", "$1");
        content = content.replaceAll("(<table[^>]*?)\\s+cellpadding\\s*=\\s*\\S+", "$1");


        return String.format(
                "<!DOCTYPE html>"
                        + "<html>"
                        + "<head>"
                        + (isShowHighlight ? "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/css/shCore.css\">" : "")
                        + (isShowHighlight ? "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/css/shThemeDefault.css\">" : "")
                        + "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/css/common_new.css\">"
                        + "%s"
                        + "</head>"
                        + "<body>"
                        + "<div class='body-content'>"
                        + "%s"
                        + "</div>"
                        + (isShowHighlight ? "<script type=\"text/javascript\" src=\"file:///android_asset/shCore.js\"></script>" : "")
                        + (isShowHighlight ? "<script type=\"text/javascript\" src=\"file:///android_asset/brush.js\"></script>" : "")
                        + (isShowHighlight ? "<script type=\"text/javascript\">SyntaxHighlighter.all();</script>" : "")
                        + "</body>"
                        + "</html>"
                , (css == null ? "" : css), content);
    }

    private static class OWebClient extends WebViewClient implements Runnable {
        private Runnable mFinishCallback;
        private boolean mDone = false;

        OWebClient(Runnable finishCallback) {
            super();
            mFinishCallback = finishCallback;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mDone = false;
            // 当webview加载2秒后强制回馈完成
            view.postDelayed(this, 2800);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            run();
        }

        @Override
        public synchronized void run() {
            if (!mDone) {
                mDone = true;
                mFinishCallback.run();
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            UIHelper.showUrlRedirect(view.getContext(), url);
            return true;
        }
    }
}
