
package filemanager;

import filemanager.webviewui.WebViewUI;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Yoga Budi Yulianto
 */
public class Berkas {
  
  private File objekFile;
  private String icon;
  private int jumlahBerkas = 0;
  private long ukuranFile = 0;
  
  private WebViewUI ui;
  
  public Berkas(WebViewUI ui, String pathname) {
    this.objekFile = new File(pathname);
    this.jumlahBerkas = (objekFile.listFiles() != null)
                          ? objekFile.listFiles().length : 0;
    
    // konversikan ukuran file dari bytes ke KB
    this.ukuranFile = objekFile.length() / 1024;
    
    this.icon = "";
    this.ui = ui;
  }

  public long getUkuranFile() {
    return ukuranFile;
  }

  public void setUkuranFile(long ukuranFile) {
    this.ukuranFile = ukuranFile;
  }

  public int getJumlahBerkas() {
    return jumlahBerkas;
  }

  public void setJumlahBerkas(int jumlahBerkas) {
    this.jumlahBerkas = jumlahBerkas;
  }

  public String getIcon() {
    return icon;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }

  public WebViewUI getUi() {
    return ui;
  }

  public void setUi(WebViewUI ui) {
    this.ui = ui;
  }

  public File getObjekFile() {
    return objekFile;
  }
  
  public boolean berkasTersedia() {
    return objekFile.exists();
  }

  public Berkas[] listBerkas() {
    List<Berkas> daftarBerkas = new ArrayList<>();
    File[] daftarFile = objekFile.listFiles();
    
    for(int i = 0; i < daftarFile.length; i++) {
      Berkas berkas = new Berkas(ui, daftarFile[i].getAbsolutePath());
      
      if(berkas.getObjekFile().isDirectory()) {
        berkas.setIcon("assets/Icons/64/101-folder-5.png");
      }
      else {
        berkas.setIcon("assets/Icons/64/053-document-7.png");
      }
      
      daftarBerkas.add(berkas);
    }
    
    return daftarBerkas.toArray(new Berkas[0]);
  }
  
  public void tampilkanListBerkas() {    
    Berkas[] daftarBerkas = this.listBerkas();
    
    this.hapusSemuaBerkasPadaJS();
    for(int i = 0; i < daftarBerkas.length; i++) {
      if(daftarBerkas[i].objekFile.isDirectory()) {
        daftarBerkas[i].buatBerkasPadaJS();
      }
    }

    for(int i = 0; i < daftarBerkas.length; i++) {
      if(daftarBerkas[i].objekFile.isFile()) {
        daftarBerkas[i].buatBerkasPadaJS();
      }
    }
  }
  
  public void buatBerkasPadaJS() {
    String jenisBerkas = (objekFile.isDirectory()) ? "folder" : "file";
    
    String js = ""+
      "var berkas = new Berkas();"+
      "berkas.setNama('"+objekFile.getName()+"');"+
      "berkas.setJenis('"+jenisBerkas+"');"+
      "berkas.setIcon('"+icon+"');"+
      "berkas.setPathAbsolut('"+objekFile.getAbsolutePath()+"');"+
      "berkas.setJumlahBerkas("+jumlahBerkas+");"+
      "berkas.setUkuranFile("+ukuranFile+");"+
      "berkas.getContextMenu().tambahkanSemuaMenu(berkas.dataContextMenuBerkas);"+
      "berkas.pasangElemen($('.tempatBerkas'));";

      ui.eksekusiJavascript(js);
  }
  
  public Berkas[] pecahPathAbsolut() {
    ArrayList<Berkas> hasil = new ArrayList<>();
    Path pathAbsolut = objekFile.toPath();
    String path = "/";
    
    hasil.add(new Berkas(ui, "/"));
    
    for(int i = 0; i < pathAbsolut.getNameCount(); i++) {
      path += pathAbsolut.getName(i).toString() + "/";
      
      hasil.add(new Berkas(ui, path));
    }
    
    return hasil.toArray(new Berkas[0]);
  }
  
  public void hapusSemuaBerkasPadaJS() {
    ui.eksekusiJavascript("Berkas.hapusSemuaBerkas();");
  }
  
  public void tampilkanCirclePadaJS() {
    String js = ""+
    "$('#konten').hide();"+
    "$('#loadingCircle').show();";
    
    ui.eksekusiJavascript(js);
  }
  
  public void sembunyikanCirclePadaJS() {
    String js = ""+
    "$('#konten').show();"+
    "$('#loadingCircle').hide();";
    
    ui.eksekusiJavascript(js);
  }
}
