import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class lsbTechnique {
    // Embed encrypted secret message in least significant bits of cover image
    // pixels
    //3
    public void embedEncryptedMessage(BufferedImage coverImage, BufferedImage modifiedImage,
                                      byte[] encryptedSecretMessage) {
        int index = 0;
        for (int i = 0; i < coverImage.getWidth(); i++) {
            for (int j = 0; j < coverImage.getHeight(); j++) {
                int pixel = coverImage.getRGB(i, j);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = pixel & 0xff;

                if (index < encryptedSecretMessage.length * Byte.SIZE) {
                    int bit = (encryptedSecretMessage[index / Byte.SIZE] >> (Byte.SIZE - index % Byte.SIZE - 1)) & 0x01;
                    red = (red & 0xfe) | bit;
                    index++;
                }
                modifiedImage.setRGB(i, j, (red << 16) | (green << 8) | blue);
            }
        }
    }

    //4 (for decryption)
    // Extract encrypted secret message from least significant bits of modified image pixels using lsbTechnique

    public byte[] extractEncryptedMessage(BufferedImage modifiedImageLoaded, byte [] encryptedSecretMessage) {
        byte[] encryptedSecretMessageLoadedLSB = new byte[encryptedSecretMessage.length];
        int index = 0;
        for (int i = 0; i < modifiedImageLoaded.getWidth(); i++) {
            for (int j = 0; j < modifiedImageLoaded.getHeight(); j++) {
                if (index < encryptedSecretMessageLoadedLSB.length * Byte.SIZE) {
                    int pixel = modifiedImageLoaded.getRGB(i, j);
                    int red = (pixel >> 16) & 0xff;
                    encryptedSecretMessageLoadedLSB[index / Byte.SIZE] |= (byte) ((red & 0x01) << (Byte.SIZE - index % Byte.SIZE - 1));
                    index++;
                }
            }
        }
        return encryptedSecretMessageLoadedLSB;
    }
}
