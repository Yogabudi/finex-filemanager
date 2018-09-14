
package filemanager;

import filemanager.webviewui.WebViewUI;

/**
 *
 * @author Yoga Budi Yulianto
 */
public class WajahTerdeteksi {
  
  private String namaWajah;
  private Berkas fotoWajah;
  private Berkas fotoAsal;
  
  private WebViewUI ui;
  
  public WajahTerdeteksi(WebViewUI ui, String namaWajah) {
    this.namaWajah = namaWajah;
    this.ui = ui;
    this.fotoWajah = this.fotoAsal = null;
  }

  public String getNamaWajah() {
    return namaWajah;
  }

  public void setNamaWajah(String namaWajah) {
    this.namaWajah = namaWajah;
  }

  public Berkas getFotoWajah() {
    return fotoWajah;
  }

  public void setFotoWajah(Berkas fotoWajah) {
    this.fotoWajah = fotoWajah;
  }

  public Berkas getFotoAsal() {
    return fotoAsal;
  }

  public void setFotoAsal(Berkas fotoAsal) {
    this.fotoAsal = fotoAsal;
  }
  
  public static void hapusSemuaPadaJS(WebViewUI ui) {
    ui.eksekusiJavascript("WajahTerdeteksi.hapusSemua();");
  }
  
  public static void tampilkanCirclePadaJS(WebViewUI ui) {
    ui.eksekusiJavascript("WajahTerdeteksi.tampilkanCircle();");
  }
  
  public static void sembunyikanCirclePadaJS(WebViewUI ui) {
    ui.eksekusiJavascript("WajahTerdeteksi.sembunyikanCircle();");
  }
  
  public static void tampilkanInfoWTPadaJS(WebViewUI ui) {
    ui.eksekusiJavascript("WajahTerdeteksi.tampilkanInfoWT();");
  }
  
  public static void sembunyikanInfoWTPadaJS(WebViewUI ui) {
    ui.eksekusiJavascript("WajahTerdeteksi.sembunyikanInfoWT();");
  }
  
  public static void ubahTeksWTPadaJS(WebViewUI ui, String teks) {
    ui.eksekusiJavascript("WajahTerdeteksi.ubahTeksInfoWT(\""+teks+"\");");
  }
  
  public void buatWajahTerdeteksiPadaJS() {
    String js = ""+
    "var wajahTerdeteksi = new WajahTerdeteksi();"+
    "wajahTerdeteksi.setNama(\""+namaWajah+"\");"+
    "wajahTerdeteksi.setFoto(\""+fotoWajah.getObjekFile().getAbsolutePath()+"\");"+
    "wajahTerdeteksi.pasangElemen($('#tempatWajahTerdeteksi'));";
    
    ui.eksekusiJavascript(js);
  }
}
