
package filemanager;

import filemanager.webviewui.WebViewUI;

/**
 *
 * @author Yoga Budi Yulianto
 */
public class AccordPintasan {
  
  private Berkas berkasFolder;
  private WebViewUI ui;

  public AccordPintasan(WebViewUI ui, Berkas berkasFolder) {
    this.berkasFolder = berkasFolder;
    this.ui = ui;
  }

  public Berkas getBerkasFolder() {
    return berkasFolder;
  }

  public WebViewUI getUi() {
    return ui;
  }

  public AccordPintasan setUi(WebViewUI ui) {
    this.ui = ui;
    
    return this;
  }
  
  public AccordPintasan setBerkasFolder(Berkas berkasFolder) {
    this.berkasFolder = berkasFolder;
    
    return this;
  }
  
  public AccordPintasan buatElemenPadaJS() {
    ui.eksekusiJavascript("new AccordPintasan()" +
      ".setNamaFolder(\""+berkasFolder.getObjekFile().getName()+"\")" +
      ".setPath(\""+berkasFolder.getObjekFile().getAbsolutePath()+"\")" +
      ".pasangElemen($(\"#accordPintasan\"));");
    
    return this;
  }
  
  public static void hapusSemuaPadaJS(WebViewUI ui) {
    ui.eksekusiJavascript("AccordPintasan.hapusSemua();");
  }
  
  public static void hapusPadaJS(WebViewUI ui, String namaFolder) {
    ui.eksekusiJavascript("AccordPintasan.get(\""+namaFolder+"\").hapus();");
  }
}
