package bg.sofia.uni.fmi.mjt.analyzer;

import com.google.zxing.*;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.oned.UPCAReader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageHandler {

    public String receiveUPCCode(String imgPath) {
        BufferedImage bufferImage = null;
        BinaryBitmap bitmap = null;

        try {
            bufferImage = ImageIO.read(new File(imgPath));
            int[] pixels = bufferImage.getRGB(0, 0,
                    bufferImage.getWidth(), bufferImage.getHeight(),
                    null, 0, bufferImage.getWidth());

            RGBLuminanceSource source = new RGBLuminanceSource(bufferImage.getWidth(), bufferImage.getHeight(), pixels);
            bitmap = new BinaryBitmap(new HybridBinarizer(source));


        } catch (IOException e) {
            e.printStackTrace();
        }

        if (bitmap == null) {
            return null;
        }

        UPCAReader reader = new UPCAReader();

        try {
            Result result = reader.decode(bitmap);
            return result.getText();
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }

        return null;
    }
}
