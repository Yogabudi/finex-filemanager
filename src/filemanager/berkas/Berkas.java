
package filemanager.berkas;

import filemanager.webviewui.WebViewUI;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Yoga Budi Yulianto
 */
public class Berkas {
  
  private File objekFile;
  private String icon;
  private WebViewUI ui;
  
  public Berkas(WebViewUI ui, String pathname) {
    this.objekFile = new File(pathname);
    this.icon = "";
    this.ui = ui;
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
    
    this.js_hapusSemuaBerkas();
    
    for(int i = 0; i < daftarBerkas.length; i++) {
      if(daftarBerkas[i].objekFile.isDirectory()) {
        daftarBerkas[i].js_buatBerkas();
      }
    }
    
    for(int i = 0; i < daftarBerkas.length; i++) {
      if(daftarBerkas[i].objekFile.isFile()) {
        daftarBerkas[i].js_buatBerkas();
      }
    }
  }
  
  public void js_buatBerkas() {
    String jenisBerkas = (objekFile.isDirectory()) ? "folder" : "file";
    
    String js = ""+
      "var berkas = new Berkas();"+
      "berkas.setNama('"+objekFile.getName()+"');"+
      "berkas.setJenis('"+jenisBerkas+"');"+
      "berkas.setIcon('"+icon+"');"+
      "berkas.setPathAbsolut('"+objekFile.getAbsolutePath()+"');"+
      "berkas.getContextMenu().tambahkanSemuaMenu(berkas.dataContextMenuBerkas);"+
      "berkas.pasangElemen($('.tempatBerkas'));";

      ui.eksekusiJavascript(js);
  }
  
  public void js_hapusSemuaBerkas() {
    ui.eksekusiJavascript("Berkas.hapusSemuaBerkas();");
  }
}
