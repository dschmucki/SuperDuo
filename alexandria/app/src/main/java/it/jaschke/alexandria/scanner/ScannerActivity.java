package it.jaschke.alexandria.scanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zbar.BarcodeFormat;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

/**
 * Created by domi on 22.09.15.
 * Based on https://github.com/dm77/barcodescanner
 */
public class ScannerActivity extends Activity implements ZBarScannerView.ResultHandler {

    private final static String LOG_TAG = ScannerActivity.class.getSimpleName();
    public static final int SCAN_REQUEST = 1;
    public static final String RESULT = "result";

    private ZBarScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<BarcodeFormat> formats = new ArrayList<>();
        formats.add(BarcodeFormat.ISBN13);
        formats.add(BarcodeFormat.EAN13);
        mScannerView = new ZBarScannerView(this);
        mScannerView.setFormats(formats);
        setContentView(mScannerView);
        mScannerView.setupScanner();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        Log.v(LOG_TAG, result.getContents());
        Log.v(LOG_TAG, result.getBarcodeFormat().getName());
        Intent resultIntent = new Intent();
        resultIntent.putExtra(RESULT, result.getContents());
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
