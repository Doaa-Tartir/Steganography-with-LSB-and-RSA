import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.imageio.ImageIO;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Driver extends Application {

    private static final int SECRET_MESSAGE_BIT_LENGTH = 1024;
    static rsaAlgorithm rsa = new rsaAlgorithm();
    static lsbTechnique lsb = new lsbTechnique();
    FileChooser fileChooser = new FileChooser();
    File selectedImage, initialDirectory = new File("C:/"), modifiedImageFile;
    String selectedImagePath, modifiedImagePath, secretMessage;
    BufferedImage Image, modifiedImage, modifiedImageLoaded;
    File outputImageFile = new File("C:/modified.png");
    Label l3 = new Label("");

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        BorderPane root = new BorderPane();
        VBox vb1 = new VBox();
        GridPane gpane1 = new GridPane();

        Label l0 = new Label("Enter a secret message");
        l0.setStyle("-fx-font-size: 10pt;");
        TextField t0 = new TextField();
        Button btn = new Button("Embed");
        btn.setStyle("-fx-font-size: 10pt;");
        Button btn1 = new Button("Extract");
        btn1.setStyle("-fx-font-size: 10pt;");
        Button btn2 = new Button("Select an image");
        btn2.setStyle("-fx-font-size: 10pt;");

        l3.setStyle("-fx-font-size: 10pt;");

        gpane1.add(btn, 2, 0);
        gpane1.add(btn2, 3, 0);
        gpane1.add(l0, 0, 0);
        gpane1.add(t0, 1, 0);
        gpane1.add(l3, 1, 1);
        gpane1.add(btn1, 0, 1);
        gpane1.setVgap(8);
        gpane1.setHgap(8);
        gpane1.setPadding(new Insets(10, 10, 10, 10));
        vb1.getChildren().addAll(gpane1);
        root.setCenter(vb1);

        btn2.setOnAction(e -> {
            fileChooser.setTitle("Select an Image File");
            fileChooser.setInitialDirectory(initialDirectory);
            selectedImage = fileChooser.showOpenDialog(stage);
            if (selectedImage == null) {
                System.out.println("No image file selected.");
                return;
            }
            selectedImagePath = selectedImage.getAbsolutePath();
            System.out.println("Selected cover image file: " + selectedImagePath);
//			 Load cover image
            try {
                Image = ImageIO.read(new File(selectedImagePath));
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            rsa.keyGeneration(SECRET_MESSAGE_BIT_LENGTH);
        });

        // Embed encrypted message in modified image
        btn.setOnAction(e -> {
            secretMessage = t0.getText().toString();
            modifiedImage = new BufferedImage(Image.getWidth(), Image.getHeight(), BufferedImage.TYPE_INT_RGB);
            try {
                lsb.embedEncryptedMessage(Image, modifiedImage, rsa.encryption(secretMessage));
            } catch (InvalidKeyException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (InvalidKeySpecException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (NoSuchAlgorithmException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (NoSuchPaddingException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (IllegalBlockSizeException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (BadPaddingException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            try {
                ImageIO.write(modifiedImage, "png", outputImageFile);
                System.out.println("Modified image created.");
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        });

        //extract
        btn1.setOnAction(e -> {
            fileChooser.setTitle("Select Modified Image File");
            fileChooser.setInitialDirectory(initialDirectory);
            modifiedImageFile = fileChooser.showOpenDialog(stage);
            if (modifiedImageFile == null) {
                System.out.println("No modified image file selected.");
                return;
            }
            modifiedImagePath = modifiedImageFile.getAbsolutePath();
            System.out.println("modified image file: " + modifiedImagePath);
            // Decrypt message from modified image
            try {
                modifiedImageLoaded = ImageIO.read(outputImageFile);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            try {
                l3.setText(rsa
                        .decryption(lsb.extractEncryptedMessage(modifiedImageLoaded, rsa.encryption(secretMessage))));
            } catch (InvalidKeyException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (InvalidKeySpecException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (NoSuchAlgorithmException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (IllegalBlockSizeException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (BadPaddingException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (NoSuchPaddingException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        });
        Scene scene = new Scene(root, 520, 100);
        stage.setTitle("Steganography with LSB and RSA");
        stage.setScene(scene);
        stage.show();
    }

}
