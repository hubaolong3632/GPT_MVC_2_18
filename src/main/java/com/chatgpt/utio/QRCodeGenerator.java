package com.chatgpt.utio;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import javax.imageio.ImageIO;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
//把alipays生成图片
public class QRCodeGenerator {

//    public static void main(String[] args) throws Exception {
////        解析支付宝协议
//        String qrcode = "alipays%3A%2F%2Fplatformapi%2Fstartapp%3FappId%3D09999988%26actionType%3DtoAccount%26goBack%3DNO%26amount%3D0.01%26userId%3D2088342799223962%26memo%3DY_orderNumbere80132d6ba36406fa872";
//        String decodedQrcode = URLDecoder.decode(qrcode, "UTF-8");
//
//        System.out.println(decodedQrcode);
//    }

    public static void main(String[] args) {
//        String qrcode = "alipays%3A%2F%2Fplatformapi%2Fstartapp%3FappId%3D09999988%26actionType%3DtoAccount%26goBack%3DNO%26amount%3D0.01%26userId%3D2088342799223962%26memo%3DY_orderNumbere80132d6ba36406fa872";
        String qrcode = "alipays%3A%2F%2Fplatformapi%2Fstartapp%3FappId%3D09999988%26actionType%3DtoAccount%26goBack%3DNO%26amount%3D0.01%26userId%3D2088342799223962%26memo%3DY_orderNumberc38221feccb949c28f54";
        String filePath = "qrcode.png";
        int width = 300;
        int height = 300;

        try {
            // 解码 qrcode
            String decodedQrcode = URLDecoder.decode(qrcode, "UTF-8");
            System.out.println(decodedQrcode);
            // 创建 BitMatrix 对象
            BitMatrix matrix = new MultiFormatWriter().encode(decodedQrcode, BarcodeFormat.QR_CODE, width, height);
            
            // 将 BitMatrix 对象转换为 BufferedImage 对象
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int bit = matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF;
                    image.setRGB(x, y, bit);
                }
            }
            
            // 将 BufferedImage 对象保存为图片文件
            File file = new File(filePath);
            ImageIO.write(image, "png", file);
            
            System.out.println("二维码生成成功，保存路径为：" + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}