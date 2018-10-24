
package filemanager;

import filemanager.webviewui.WebViewUI;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 *
 * @author Yoga Budi Yulianto
 */
public class VisitorPencarianBerkas extends SimpleFileVisitor<Path> {
  
  private String namaBerkas;
  private String tanggal;
  private String kriteria;
  private WebViewUI ui;
  
  private ArrayList<Berkas> dataHasil = new ArrayList<>();
  
  // kriteria pencarian
  public static final String BERDASAR_NAMA = "berdasarnama";
  public static final String BERDASAR_TGL_DIBUAT = "berdasartgldibuat";
  public static final String BERDASAR_TGL_AKSES = "berdasartglakses";
  public static final String BERDASAR_TGL_MODIFIKASI = "berdasartglmodif";

  public VisitorPencarianBerkas(WebViewUI ui) {
    this.ui = ui;
  }
  
  public String getNamaBerkas() {
    return namaBerkas;
  }

  public void setNamaBerkas(String namaBerkas) {
    this.namaBerkas = namaBerkas;
  }

  public String getKriteria() {
    return kriteria;
  }

  public void setKriteria(String kriteria) {
    this.kriteria = kriteria;
  }

  public ArrayList<Berkas> getDataHasil() {
    return dataHasil;
  }

  public void setDataHasil(ArrayList<Berkas> dataHasil) {
    this.dataHasil = dataHasil;
  }

  public String getTanggal() {
    return tanggal;
  }

  public void setTanggal(String tanggal) {
    this.tanggal = tanggal;
  }
  
  public FileVisitResult cariBerkas(Berkas berkasDikunjungi,
          BasicFileAttributes attr) {
    
    if(kriteria.equals(BERDASAR_NAMA)) {
      String nama = berkasDikunjungi.getObjekFile().getName();
      
      if(nama.toLowerCase().contains(namaBerkas.toLowerCase())) {
        dataHasil.add(berkasDikunjungi);
        
        if(berkasDikunjungi.getObjekFile().getName().endsWith(".jpg") ||
          berkasDikunjungi.getObjekFile().getName().endsWith(".JPG") ||
          berkasDikunjungi.getObjekFile().getName().endsWith(".png") ||
          berkasDikunjungi.getObjekFile().getName().endsWith(".PNG") ||
          berkasDikunjungi.getObjekFile().getName().endsWith(".jpeg")) {

          berkasDikunjungi.setPakeThumbnail(true);
        }
        
        Berkas.sembunyikanCirclePadaJS(ui);
        berkasDikunjungi.buatBerkasPadaJS();
      }
      
      System.out.println(berkasDikunjungi.getObjekFile().getAbsolutePath());
      
      if(dataHasil.size() >= 100) {
        return FileVisitResult.TERMINATE;
      }
    }
    else if(kriteria.equals(BERDASAR_TGL_DIBUAT)) {
      try {
        LocalDate tglDibuat = berkasDikunjungi.dapatkanInfoTgl("computer")[2];
        String strTgl = tglDibuat.format(DateTimeFormatter.ofPattern("d MMM yyyy"));
        
        if(strTgl.toLowerCase().contains(tanggal.toLowerCase())) {
          dataHasil.add(berkasDikunjungi);
          
          if(berkasDikunjungi.getObjekFile().getName().endsWith(".jpg") ||
            berkasDikunjungi.getObjekFile().getName().endsWith(".JPG") ||
            berkasDikunjungi.getObjekFile().getName().endsWith(".png") ||
            berkasDikunjungi.getObjekFile().getName().endsWith(".PNG") ||
            berkasDikunjungi.getObjekFile().getName().endsWith(".jpeg")) {

            berkasDikunjungi.setPakeThumbnail(true);
          }
          
          Berkas.sembunyikanCirclePadaJS(ui);
          berkasDikunjungi.buatBerkasPadaJS();
        }
        
        System.out.println(strTgl);
        
        if(dataHasil.size() >= 100) {
          return FileVisitResult.TERMINATE;
        }
      }
      catch(Exception ex) {
        ex.printStackTrace();
      }
    }
    else if(kriteria.equals(BERDASAR_TGL_AKSES)) {
      DateFormat df = new SimpleDateFormat("d MMM yyyy");
      String strTgl = df.format(attr.lastAccessTime().toMillis());
      
      if(strTgl.toLowerCase().contains(tanggal.toLowerCase())) {
        dataHasil.add(berkasDikunjungi);
          
        if(berkasDikunjungi.getObjekFile().getName().endsWith(".jpg") ||
          berkasDikunjungi.getObjekFile().getName().endsWith(".JPG") ||
          berkasDikunjungi.getObjekFile().getName().endsWith(".png") ||
          berkasDikunjungi.getObjekFile().getName().endsWith(".PNG") ||
          berkasDikunjungi.getObjekFile().getName().endsWith(".jpeg")) {

          berkasDikunjungi.setPakeThumbnail(true);
        }

        Berkas.sembunyikanCirclePadaJS(ui);
        berkasDikunjungi.buatBerkasPadaJS();
      }
      
      System.out.println(strTgl);
        
      if(dataHasil.size() >= 100) {
        return FileVisitResult.TERMINATE;
      }
    }
    else if(kriteria.equals(BERDASAR_TGL_MODIFIKASI)) {
      DateFormat df = new SimpleDateFormat("d MMM yyyy");
      String strTgl = df.format(attr.lastModifiedTime().toMillis());
      
      if(strTgl.toLowerCase().contains(tanggal.toLowerCase())) {
        dataHasil.add(berkasDikunjungi);
          
        if(berkasDikunjungi.getObjekFile().getName().endsWith(".jpg") ||
          berkasDikunjungi.getObjekFile().getName().endsWith(".JPG") ||
          berkasDikunjungi.getObjekFile().getName().endsWith(".png") ||
          berkasDikunjungi.getObjekFile().getName().endsWith(".PNG") ||
          berkasDikunjungi.getObjekFile().getName().endsWith(".jpeg")) {

          berkasDikunjungi.setPakeThumbnail(true);
        }

        Berkas.sembunyikanCirclePadaJS(ui);
        berkasDikunjungi.buatBerkasPadaJS();
      }
      
      System.out.println(strTgl);
        
      if(dataHasil.size() >= 100) {
        return FileVisitResult.TERMINATE;
      }
    }
    
    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult visitFile(Path path, BasicFileAttributes attr) {
    Berkas berkas = new Berkas(ui, path.toFile().getAbsolutePath());
    
    return cariBerkas(berkas, attr);
  }
  
   @Override
  public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes attr) {
    Berkas berkas = new Berkas(ui, path.toFile().getAbsolutePath());
    
    return cariBerkas(berkas, attr);
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
