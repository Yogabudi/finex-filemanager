
package filemanager;

import filemanager.webviewui.WebViewUI;

/**
 *
 * @author Yoga Budi Yulianto
 */
public class KotakWajah {
  
  private String namaWajah;
  private Berkas fotoAsal;
  private Berkas fotoWajah;
  
  private WebViewUI ui;

  public KotakWajah(WebViewUI ui, String namaWajah) {
    this.namaWajah = namaWajah;
    this.ui = ui;
  }

  public String getNamaWajah() {
    return namaWajah;
  }

  public void setNamaWajah(String namaWajah) {
    this.namaWajah = namaWajah;
  }

  public Berkas getFotoAsal() {
    return fotoAsal;
  }

  public void setFotoAsal(Berkas fotoAsal) {
    this.fotoAsal = fotoAsal;
  }

  public Berkas getFotoWajah() {
    return fotoWajah;
  }

  public void setFotoWajah(Berkas fotoWajah) {
    this.fotoWajah = fotoWajah;
  }

  public WebViewUI getUi() {
    return ui;
  }

  public void setUi(WebViewUI ui) {
    this.ui = ui;
  }
  
  public void buatKotakWajahPadaJS() {
    String js = ""+
    "new KotakWajah()"+
    ".setNama(\""+namaWajah+"\")"+
    ".setFoto(\""+fotoWajah.getObjekFile().getAbsolutePath()+"\")"+
    ".setFotoAsal(\""+fotoAsal.getObjekFile().getAbsolutePath()+"\")"+
    ".pasangElemen($('#tempatKotakWajah'));";
    
    ui.eksekusiJavascript(js);
  }
  
  public static void hapusSemuaPadaJS(WebViewUI ui) {
    ui.eksekusiJavascript("KotakWajah.hapusSemua()");
  }
}
