package it.polito.toggle;


import com.mortennobel.imagescaling.ResampleFilters;
import com.mortennobel.imagescaling.ResampleOp;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageManipulationTools {
    public static BufferedImage cutImage(File src, int left, int top, int right, int bottom) throws IOException {
        System.out.println("trying to read " + src.getAbsolutePath());
        BufferedImage tmp_image = ImageIO.read(src);
        return tmp_image.getSubimage(left,top,right-left,bottom-top);
    }

    public static File saveCroppedScreenshotToFile(String path, String name, BufferedImage screenshot) throws IOException {
        File outputFile = new File(path + '\\' + name + "_cropped.png");
        ImageIO.write(screenshot,"bmp",outputFile);
        return outputFile;
    }

    public static BufferedImage resizeScreenshot(BufferedImage src, int original_screen_width, int new_screen_width) {
        int current_img_width = src.getWidth();
        int current_img_height = src.getHeight();

        float ratio = new_screen_width / (float) original_screen_width;

        int new_width = (int) (current_img_width*ratio);
        int new_height = (int) (current_img_height*ratio);

        BufferedImage outputImage = new BufferedImage(new_width,new_height,src.getType());
        ResampleOp resizeOp = new ResampleOp(new_width,new_height);
        resizeOp.setFilter(ResampleFilters.getLanczos3Filter());
        BufferedImage scaledImage = resizeOp.filter(src,null);

        //scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(src, 0, 0, new_width, new_height,null);
        g2d.dispose();


        return scaledImage;
    }
}
