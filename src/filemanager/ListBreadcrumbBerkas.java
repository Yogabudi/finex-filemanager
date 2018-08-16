/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filemanager;

import filemanager.webviewui.WebViewUI;
import java.util.ArrayList;

/**
 *
 * @author Yoga Budi Yulianto
 */
public class ListBreadcrumbBerkas extends ArrayList<BreadcrumbBerkas> {
  
  public ListBreadcrumbBerkas() {
    super();
  }
  
  public void masukkanDanTampilkan(BreadcrumbBerkas bc) {
    this.add(bc);
    bc.tampilkan();
  }
  
  public void masukkan(BreadcrumbBerkas bc) {
    this.add(bc);
  }
  
  public BreadcrumbBerkas getBreadcrumb(String label) {
    BreadcrumbBerkas bc = null;
    
    for(int i = 0; i < this.size(); i++) {
      if(this.get(i).getObjekFile().getName().equals(label) ||
         this.get(i).getObjekFile().getAbsolutePath().equals(label)) {
        bc = this.get(i);
      }
    }
    
    return bc;
  }
  
  // method ini untuk mengisi list dengan pecahan path dan menampilkannya
  // method ini menghapus semua data pada list !!!
  public void isiDariPath(Berkas[] pecahanPath, WebViewUI ui) {
    this.clear();
    BreadcrumbBerkas.hapusSemuaPadaJS(ui);

    for(int i = 0; i < pecahanPath.length; i++) {
      this.masukkanDanTampilkan(new BreadcrumbBerkas(ui,
                          pecahanPath[i].getObjekFile().getAbsolutePath()));
    }
    
    tandaiYangTerakhir();
  }
  
  public void tandaiYangTerakhir() {
    this.get(this.size() - 1).tandaiPadaJS();
  }
}
