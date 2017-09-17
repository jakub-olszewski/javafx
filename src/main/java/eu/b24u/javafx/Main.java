package eu.b24u.javafx;

import java.awt.Color;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import javafx.scene.image.ImageView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

public class Main extends Application {
  StringProperty bankAccountValue = new SimpleStringProperty();
  StringProperty nameValue = new SimpleStringProperty();
  StringProperty titleValue = new SimpleStringProperty();
  StringProperty amountValue = new SimpleStringProperty();

  javafx.scene.image.ImageView selectedImage;

  @Override
  public void start(Stage primaryStage) throws Exception {
    primaryStage.setTitle("QRCode Bank transaction");



    // Document document = new Document();
    // PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("barcode.pdf"));

    String text = "1234567890|PL|92124012340001567890123456|001200|Odbiorca 1|FV 1234/34/2012||| ";

    Label label = new Label(text);



    selectedImage = new javafx.scene.image.ImageView();

    selectedImage.setImage(createImageQRCode(text));

    VBox formBox = new VBox();
    formBox.setSpacing(10);
    formBox.getChildren().add(createTextField("BankAccount", 26, bankAccountValue, primaryStage));
    formBox.getChildren().add(createTextField("Name", 26, nameValue, primaryStage));
    formBox.getChildren().add(createTextField("Title", 26, titleValue, primaryStage));
    formBox.getChildren().add(createTextField("Amount", 6, amountValue, primaryStage));

    Button buttonGenerate = new Button();
    buttonGenerate.setText("Generate");

    formBox.getChildren().add(buttonGenerate);

    // formBox.getChildren().add(selectedImage);
    // formBox.getChildren().add(label);
    // primaryStage.setScene(scene);

    // primaryStage.show();

    HBox root = new HBox();
    root.setSpacing(10);
    root.getChildren().add(selectedImage);
    root.getChildren().add(formBox);
    // root.setMargin(root.getChildren().get(1), new Insets(10, 10, 10, 10));

    buttonGenerate.setOnAction(e -> {
      System.out.println(bankAccountValue.get());
      System.out.println(nameValue.get());
      System.out.println(titleValue.get());
      System.out.println(amountValue.get());
      
      
      
      root.getChildren().remove(0);
      root.getChildren().remove(0);
      
      String amountRaw = amountValue.get();
      amountRaw = amountRaw.replaceAll(",", "");
      amountRaw = amountRaw.replaceAll("\\.", "");
      int size = amountRaw.length();
      String amountValue = "";
      for(int i=0;i<6-size;i++){
        amountValue+="0";
      }
      amountValue+=amountRaw;
      selectedImage.setImage(createImageQRCode("1234567890|PL|"+bankAccountValue.get()+"|"+amountValue+"|"+nameValue.get()+"|"+titleValue.get()+"|||"));
      root.getChildren().add(selectedImage);
      root.getChildren().add(formBox);
    });



    Scene scene = new Scene(root, 700, 200);

    // scene.setRoot(root);

    primaryStage.setScene(scene);
    primaryStage.show();
  }

  private javafx.scene.image.Image createImageQRCode(String text) {
    // TODO Auto-generated method stub
    javafx.scene.image.Image image1 = null;
    // document.open();
    // PdfContentByte cb = writer.getDirectContent();
    BarcodeQRCode barcodeQRCode = new BarcodeQRCode(text, 200, 200, null);
    Image codeQrImage;
    try {
      codeQrImage = barcodeQRCode.getImage();
      codeQrImage.scaleAbsolute(100, 100);
      // document.add(codeQrImage);
      // document.close();

      // BarcodePDF417 barcode = new BarcodePDF417();
      // barcode.setText("Bla bla");
      java.awt.Image img = barcodeQRCode.createAwtImage(Color.BLACK, Color.WHITE);
      BufferedImage outImage =
          new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
      outImage.getGraphics().drawImage(img, 0, 0, null);
      ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
      ImageIO.write(outImage, "png", bytesOut);
      bytesOut.flush();
      byte[] pngImageData = bytesOut.toByteArray();

      // save as file
      // FileOutputStream fos = new FileOutputStream("barcode.png");
      // fos.write(pngImageData);
      // fos.flush();
      // fos.close();



      InputStream myInputStream = new ByteArrayInputStream(pngImageData);
      image1 = new javafx.scene.image.Image(myInputStream);

    } catch (BadElementException | IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }


    return image1;
  }

  private HBox createTextField(String caption, int size, StringProperty title, Stage stage) {
    TextField textField = new TextField();
    // titleTextField.setText("Stage Coach");
    textField.setPrefColumnCount(size);

    HBox hBox = new HBox();
    hBox.setSpacing(10);
    Label captionLabel = new Label(caption);
    hBox.getChildren().add(captionLabel);
    hBox.getChildren().add(textField);

    // Scene scene = new Scene(hBox,270,270);
    title.bind(textField.textProperty());

    // stage.setScene(scene);
    // stage.titleProperty().bind(title);


    // stage.show(); return null;
    return hBox;
  }

  public static void main(String[] args) {
    Application.launch(args);
  }


}
