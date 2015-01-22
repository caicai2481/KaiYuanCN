package net.oschina.app.ui;

import net.oschina.app.AppContext;
import net.oschina.app.R;
import net.oschina.app.util.QrCodeUtils;

import org.kymjs.kjframe.utils.FileUtils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.ImageView;

import com.google.zxing.WriterException;

public class MyQrodeDialog extends Dialog {

    private ImageView mIvCode;
    private Bitmap bitmap;

    private MyQrodeDialog(Context context, boolean flag,
            OnCancelListener listener) {
        super(context, flag, listener);
    }

    @SuppressLint("InflateParams")
    private MyQrodeDialog(Context context, int defStyle) {
        super(context, defStyle);
        View contentView = getLayoutInflater().inflate(
                R.layout.dialog_my_qr_code, null);
        mIvCode = (ImageView) contentView.findViewById(R.id.iv_qr_code);
        try {
            bitmap = QrCodeUtils.Create2DCode(String.format(
                    "http://my.oschina.net/u/%s", AppContext.getInstance()
                            .getLoginUid()));
            mIvCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        contentView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                dismiss();
                if (FileUtils.bitmapToFile(bitmap,
                        FileUtils.getSavePath("OSChina") + "/myqrcode.png")) {
                    AppContext.showToast("二维码已保存到oschina文件夹下");
                } else {
                    AppContext.showToast("SD卡不可写，二维码保存失败");
                }
                return false;
            }
        });
        super.setContentView(contentView);
    }

    public MyQrodeDialog(Context context) {
        this(context, R.style.quick_option_dialog);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setGravity(Gravity.CENTER);
    }
}
