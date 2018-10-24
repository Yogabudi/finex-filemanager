
package filemanager;

import filemanager.webviewui.WebViewUI;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *
 * @author Yoga Budi Yulianto
 */
public class FaceDetector {
  
  // format : [ index_wajah ][ koordinat_wajah (x/y/lebar/tinggi) ]
  // [0] = x wajah
  // [1] = y wajah
  // [2] = lebar wajah
  // [3] = tinggi wajah
  public static int[][] deteksiWajah(Berkas fileGambar)
          throws IOException, InterruptedException {
    
    int[][] dataWajah = null;
    ArrayList<String> output = new ArrayList<>();
    
    ProcessBuilder pb = new ProcessBuilder(
            "python3",
            "src/filemanager/tools/face_detection_cli.py",
            "--cpus", "-1",
            fileGambar.getObjekFile().getAbsolutePath());
    
    pb.redirectErrorStream(true);
    Process proc = pb.start();

    BufferedReader buffer = new BufferedReader(
            new InputStreamReader(proc.getInputStream()));
    String baris;

    while((baris = buffer.readLine()) != null) {
      output.add(baris);
      System.out.println(baris);
    }
    
    dataWajah = new int[output.size()][4];
    
    for(int i = 0; i < output.size(); i++) {
      String info[] = output.get(i).split(",");
            
      int left = Integer.parseInt(info[4]);
      int top = Integer.parseInt(info[1]);
      int right = Integer.parseInt(info[2]);
      int bottom = Integer.parseInt(info[3]);
      
      dataWajah[i][0] = left;
      dataWajah[i][1] = top;
      dataWajah[i][2] = right - left;
      dataWajah[i][3] = bottom - top;

      System.out.println("x : " + dataWajah[i][0]);
      System.out.println("y : " + dataWajah[i][1]);
      System.out.println("lebar : " + dataWajah[i][2]);
      System.out.println("tinggi : " + dataWajah[i][3]);
    }

    buffer.close();
    
    return dataWajah;
  }
  
  public static void ambilWajahDanSimpan(WebViewUI ui,
          Berkas berkasGambar, String namaFileFoto, BufferedImage gambarSumber,
          int x, int y, int lebar, int tinggi) throws IOException {
    
    String namaFolderWajah = berkasGambar.getObjekFile().getParent() +
            "/.wajah_" + berkasGambar.getObjekFile().getName();
    
    BufferedImage subGambar = gambarSumber.getSubimage(x, y, lebar, tinggi);
    BufferedImage bufferWajah = new BufferedImage(
            lebar, tinggi, BufferedImage.TYPE_INT_RGB);
    
    Graphics2D grap = bufferWajah.createGraphics();
    grap.drawImage(subGambar, 0, 0, null);
    grap.dispose();
    
    if(!Files.exists(Paths.get(namaFolderWajah), LinkOption.NOFOLLOW_LINKS)) {
      Berkas.buatFolderBaru(namaFolderWajah, ui);
    }
    
    Berkas fileWajah = new Berkas(ui, namaFolderWajah + "/" + namaFileFoto + ".png");
    ImageIO.write(bufferWajah, "png", fileWajah.getObjekFile());
  }
  
  public static Berkas prosesGambar(WebViewUI ui,
          Berkas berkasGambar, int[][] dataKordWajah) throws IOException {
    
    BufferedImage bufferGambar = ImageIO.read(berkasGambar.getObjekFile());
    
    BufferedImage bufferHasil = new BufferedImage(
            bufferGambar.getWidth(),
            bufferGambar.getHeight(),
            BufferedImage.TYPE_INT_ARGB);
    
    Graphics2D grap = bufferHasil.createGraphics();
    grap.drawImage(bufferGambar, 0, 0, null);
    grap.setColor(Color.RED);
    grap.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
    
    for(int i = 0; i < dataKordWajah.length; i++) {
      int x = dataKordWajah[i][0];
      int y = dataKordWajah[i][1];
      int lebar = dataKordWajah[i][2];
      int tinggi = dataKordWajah[i][3];
      
      ambilWajahDanSimpan(ui, berkasGambar, "Wajah " + (i+1), bufferGambar,
              x, y, lebar, tinggi);
      
      grap.draw(new Rectangle2D.Float(x, y, lebar, tinggi));
    }
    
    grap.dispose();
    
    Berkas output = new Berkas(ui,
            berkasGambar.getObjekFile().getParent() + "/." +
            berkasGambar.getObjekFile().getName() + ".png");
    ImageIO.write(bufferHasil, "png", output.getObjekFile());
    
    return output;
  }
}
