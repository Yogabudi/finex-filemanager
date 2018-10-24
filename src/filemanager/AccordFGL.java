
package filemanager;

import filemanager.webviewui.WebViewUI;

/**
 *
 * @author Yoga Budi Yulianto
 */
public class AccordFGL {
  
  private Berkas berkasFolder;
  private WebViewUI ui;

  public AccordFGL(WebViewUI ui, Berkas berkasFolder) {
    this.berkasFolder = berkasFolder;
    this.ui = ui;
  }

  public Berkas getBerkasFolder() {
    return berkasFolder;
  }

  public WebViewUI getUi() {
    return ui;
  }

  public AccordFGL setUi(WebViewUI ui) {
    this.ui = ui;
    
    return this;
  }
  
  public AccordFGL setBerkasFolder(Berkas berkasFolder) {
    this.berkasFolder = berkasFolder;
    
    return this;
  }
  
  public AccordFGL buatElemenPadaJS() {
    ui.eksekusiJavascript("new AccordFGL()" +
      ".setNamaFolder(\""+berkasFolder.getObjekFile().getName()+"\")" +
      ".setPath(\""+berkasFolder.getObjekFile().getAbsolutePath()+"\")" +
      ".pasangElemen($(\"#accordFolderGambarLain\"));");
    
    return this;
  }
  
  public static void hapusSemuaPadaJS(WebViewUI ui) {
    ui.eksekusiJavascript("AccordFGL.hapusSemua();");
  }
  
  public static void hapusPadaJS(WebViewUI ui, String namaFolder) {
    ui.eksekusiJavascript("AccordFGL.get(\""+namaFolder+"\").hapus();");
  }
}
