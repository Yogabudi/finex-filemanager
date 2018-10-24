
package filemanager;

import filemanager.webviewui.WebViewUI;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 *
 * @author Yoga Budi Yulianto
 */
public class VisitorFaceRecog extends SimpleFileVisitor<Path> {
  
  private WebViewUI ui;
  
  private Berkas berkasWajah;

  public VisitorFaceRecog(WebViewUI ui, Berkas berkasWajah) {
    this.ui = ui;
    this.berkasWajah = berkasWajah;
  }
  
  public WebViewUI getUi() {
    return ui;
  }

  public void setUi(WebViewUI ui) {
    this.ui = ui;
  }

  public Berkas getBerkasWajah() {
    return berkasWajah;
  }

  public void setBerkasWajah(Berkas berkasWajah) {
    this.berkasWajah = berkasWajah;
  }
  
  public FileVisitResult cariFotoBerdasarWajah(Berkas berkasDikunjungi) {
    if(FileManager.berhentiMencariWajah) {
      return FileVisitResult.TERMINATE;
    }
    
    try {
      if(!berkasDikunjungi.getObjekFile().getName().startsWith(".")) {
        if(berkasDikunjungi.getObjekFile().getName().endsWith(".jpg") ||
          berkasDikunjungi.getObjekFile().getName().endsWith(".JPG") ||
          berkasDikunjungi.getObjekFile().getName().endsWith(".png") ||
          berkasDikunjungi.getObjekFile().getName().endsWith(".PNG") ||
          berkasDikunjungi.getObjekFile().getName().endsWith(".jpeg")) {
          
          System.out.println("Mendeteksi dan membandingkan wajah pada file gambar...");
          
          String laporanWajah =
                FaceComparator.bandingkanWajah(berkasWajah, berkasDikunjungi);
          
          if(laporanWajah.equals(FaceComparator.WAJAH_DIKENAL)) {
            berkasDikunjungi.setPakeThumbnail(true);
            berkasDikunjungi.buatBerkasPadaJS();
            System.out.println("WAJAH DIKENAL");
          }
          else {
            System.out.println("WAJAH TIDAK DIKENAL");
          }
        }
      }
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
    
    return FileVisitResult.CONTINUE;
  }
  
  @Override
  public FileVisitResult visitFile(Path path, BasicFileAttributes attr) {
    Berkas berkas = new Berkas(ui, path.toFile().getAbsolutePath());
    
    return cariFotoBerdasarWajah(berkas);
  }
  
   @Override
  public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes attr) {
    Berkas berkas = new Berkas(ui, path.toFile().getAbsolutePath());
    
    return FileVisitResult.CONTINUE;
  }
  
  @Override
  public FileVisitResult postVisitDirectory(Path path, IOException ex) {
    return FileVisitResult.CONTINUE;
  }
  
  @Override
  public FileVisitResult visitFileFailed(Path path, IOException ex) {
    return FileVisitResult.CONTINUE;
  }
}
