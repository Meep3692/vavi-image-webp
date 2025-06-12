/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


/**
 * TestCase.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2022/03/30 umjammer initial version <br>
 */
public class TestCase {

    String file = "src/test/resources/testdata/test.webp";

    @Test
    @DisplayName("spi")
    void test00() throws Exception {
        String[] rs = ImageIO.getReaderFormatNames();
        System.err.println("-- reader --");
        for (String r : rs) {
            System.err.println(r);
        }
        assertTrue(Arrays.asList(rs).contains("WEBP"));
    }

    @Test
    @DisplayName("spi specified")
    void test01() throws Exception {
        ImageReader ir = ImageIO.getImageReadersByFormatName("webp").next();
        ImageInputStream iis = ImageIO.createImageInputStream(new File(file));
        ir.setInput(iis);
        BufferedImage image = ir.read(0);
        assertNotNull(image);
    }

    @Test
    @DisplayName("spi auto")
    void test02() throws Exception {
        BufferedImage image = ImageIO.read(new File(file));
        assertNotNull(image);
    }

    private void test1Walk(File file){
        if(file.isFile()){
            if(file.getName().endsWith(".webp")){
                System.out.println(file);
                try {
                    ImageIO.read(file);
                    System.out.println(file + ": OK");
                } catch (IOException e) {
                    System.out.println(file + ": " + e);
                    fail();
                }
            }
        }else{
            for(File f : file.listFiles()){
                test1Walk(f);
            }
        }
    }

    @Test
    void test1() throws Exception {
        File dir = new File("src/test/resources/testdata");
        
        test1Walk(dir);
    }

    /** */
    public static void main(String[] args) throws Exception {
        TestCase app = new TestCase();
        app.exec();
    }

    BufferedImage image;

    private void execWalk(File f, JFrame frame, JPanel panel){
        if(f.isFile()){
            try {
                image = ImageIO.read(f);
            } catch (Exception e) {
                System.out.println(f + ": " + e);
            }
            frame.setTitle(f.toString());
            panel.repaint();
        }else{
            for(File file : f.listFiles()){
                execWalk(file, frame, panel);
            }
        }
    }

    /** */
    void exec() throws IOException {
        JFrame frame = new JFrame();
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                g.drawImage(image, 0, 0, this);
            }
        };
        frame.getContentPane().add(panel);
        frame.setVisible(true);

        File dir = new File("src/test/resources/testdata");
        execWalk(dir, frame, panel);
    }
}

/* */
