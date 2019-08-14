package com.medical.waste.ui.activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.text.TextUtils;

import com.medical.waste.base.BaseActivity;
import com.medical.waste.base.BasePresenter;

public abstract class BaseNFCActivity<T extends BasePresenter> extends BaseActivity<T> {
    private IntentFilter[] mFilters;
    private String[][] mTechLists;
    private NfcAdapter nfcAdapter = null;
    private PendingIntent mPendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter tech = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        mFilters = new IntentFilter[]{tech};
        mTechLists = new String[][]{new String[]{MifareClassic.class.getName()}, new String[]{IsoDep.class.getName()}};
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters,
                    mTechLists);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (!intent.getAction().equals(NfcAdapter.ACTION_TECH_DISCOVERED))
            return;
        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        try {
            String s = bytesToHexString(tagFromIntent.getId());
            if (!TextUtils.isEmpty(s)) {
                if (s.startsWith("0x")) {
                    onGetNfcId(s.substring(2));
                } else {
                    onGetNfcId(s);
                }
            }
//            MifareClassic mifare = MifareClassic.get(tagFromIntent);
//            int type = mifare.getType();
//            String sType = "Mifare Classic";
//            switch (type) {
//                case MifareClassic.TYPE_CLASSIC:
//                    sType = "Mifare Classic";
//                    break;
//                case MifareClassic.TYPE_PLUS:
//                    sType = "Mifare Classic PLUS";
//                    break;
//                case MifareClassic.TYPE_PRO:
//                    sType = "Mifare Classic PRO";
//                    break;
//                case MifareClassic.TYPE_UNKNOWN:
//
//                    sType = "Mifare Classic UNKNOWN";
//                    break;
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract void onGetNfcId(String id);

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    private String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("0x");
        if (src == null || src.length <= 0) {
            return null;
        }
        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            // System.out.println(buffer);
            stringBuilder.append(buffer);
        }
        return stringBuilder.toString();
    }
}
