/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filemanager;

import filemanager.webviewui.WebViewUI;

/**
 *
 * @author Yoga Budi Yulianto
 */
public class BreadcrumbBerkas extends Berkas {
  
  public BreadcrumbBerkas(WebViewUI ui, String pathname) {    
    super(ui, pathname);
  }
  
  public void tampilkan() {
    String label = (this.getObjekFile().getAbsolutePath().equals("/"))
                    ? "/" : this.getObjekFile().getName();
    
    String js = ""+
    "var bc = new BreadcrumbBerkas('"+label+"',"+
              "'"+this.getObjekFile().getAbsolutePath()+"');"+
    "bc.pasangElemen($('#panelBreadcrumb'));";
        
    this.getUi().eksekusiJavascript(js);
  }
  
  public void tandaiPadaJS() {
    String js = "BreadcrumbBerkas.tandai('"+this.getObjekFile().getName()+"');";
    this.getUi().eksekusiJavascript(js);
  }
  
  public void hilangkanTandaPadaJS() {
    String js = "BreadcrumbBerkas.hilangkanTanda('"+this.getObjekFile().getName()+"');";
    this.getUi().eksekusiJavascript(js);
  }
  
  public static void hapusSemuaPadaJS(WebViewUI ui) {
    ui.eksekusiJavascript("BreadcrumbBerkas.hapusSemua();");
  }
  
  public static void tandaiBreadcrumbPadaJS(BreadcrumbBerkas bc) {
    bc.tandaiPadaJS();
  }
  
  public static void hilangkanTandaBreadcrumbPadaJS(BreadcrumbBerkas bc) {
    bc.hilangkanTandaPadaJS();
  }
}
