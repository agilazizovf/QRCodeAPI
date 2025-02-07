package com.qr.code;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class QRCodeService {

    public BitMatrix generateQRCode(String url, int width, int height) throws Exception {
        MultiFormatWriter writer = new MultiFormatWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        return writer.encode(url, BarcodeFormat.QR_CODE, width, height, hints);
    }
}

