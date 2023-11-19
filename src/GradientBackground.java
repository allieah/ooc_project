import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;


class GradientBackground {
    public static BufferedImage generateGradientImage(int width, int height, Color color1, Color color2) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        GradientPaint gradient = new GradientPaint(0, 0, color1, 0, height, color2);
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, width, height);

        g2d.dispose();
        return image;
    }
}

class GradientPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BufferedImage gradientImage = GradientBackground.generateGradientImage(getWidth(), getHeight(), new Color(173, 216, 230), Color.BLUE.darker().darker());
        g.drawImage(gradientImage, 0, 0, null);
    }
}