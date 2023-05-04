package com.hnzz.commons.base.util;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
/**
 * @author HB on 2023/2/14
 * TODO 二维码工具类
 */

public class QRCodeUtil {
    /**
     * 生成二维码图片
     *
     * @param content 二维码内容
     * @param width   图片宽度
     * @param height  图片高度
     * @param logoPath 二维码中间logo路径
     * @return 生成的二维码图片
     */
    public static BufferedImage createQRCode(String content, int width, int height, String logoPath) {
        try {
            // 设置二维码纠错级别，这里选择最高H级别
            ErrorCorrectionLevel errorCorrectionLevel = ErrorCorrectionLevel.H;

            // 设置二维码参数
            Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, errorCorrectionLevel);
            hints.put(EncodeHintType.MARGIN, 2);

            // 生成二维码矩阵
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height, hints);

            // 创建一个新的 BufferedImage 图片对象，用于存放二维码图片
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            // 将二维码矩阵绘制到 BufferedImage 中
            Graphics2D graphics = image.createGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, width, height);
            graphics.setColor(Color.BLACK);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (bitMatrix.get(x, y)) {
                        graphics.fillRect(x, y, 1, 1);
                    }
                }
            }
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
