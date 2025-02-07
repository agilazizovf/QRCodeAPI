package com.qr.code;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

@RestController
@RequestMapping("/api/qrcode")
public class QRCodeScannerController {

    private final QRCodeService qrCodeService;
    private static final Logger logger = LoggerFactory.getLogger(QRCodeScannerController.class);

    public QRCodeScannerController(QRCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    @PostMapping("/scan")
    public String scanQRCode(@RequestParam("file") MultipartFile file) throws IOException {
        logger.info("Received file: {}", file.getOriginalFilename());
        if (file.isEmpty()) {
            logger.error("File is empty");
            throw new IllegalArgumentException("File is empty");
        }
        BufferedImage image = ImageIO.read(file.getInputStream());
        if (image == null) {
            logger.error("Invalid image file: {}", file.getOriginalFilename());
            throw new IllegalArgumentException("Invalid image file");
        }
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        MultiFormatReader reader = new MultiFormatReader();
        try {
            String text = reader.decode(bitmap).getText();
            logger.info("QR Code scanned successfully: {}", text);
            return text;
        } catch (Exception e) {
            logger.error("Error while decoding QR Code", e);
            throw new RuntimeException("An error occurred while scanning the QR code", e);
        }
    }

    @GetMapping(value = "/generate", produces = MediaType.IMAGE_PNG_VALUE)
    public void generateQRCode(@RequestParam("url") String url, HttpServletResponse response) throws Exception {
        BitMatrix bitMatrix = qrCodeService.generateQRCode(url, 300, 300); // 300x300 QR code
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        try (OutputStream outputStream = response.getOutputStream()) {
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        }
    }

}

