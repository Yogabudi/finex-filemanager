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
public class NavMajuMundur {
  
  private ArrayList<Berkas> jejak = new ArrayList<>();
  private Berkas berkasTerpilih;
  private WebViewUI ui;
  private int indexJejak = -1;
  
  public NavMajuMundur(WebViewUI ui) {
    this.ui = ui;
  }

  public Berkas getBerkasTerpilih() {
    return berkasTerpilih;
  }

  public Berkas majuKe(Berkas berkas) {
    jejak.clear();
    
    berkasTerpilih = berkas;
    
    return berkasTerpilih;
  }
  
  public Berkas maju() {
    if(indexJejak > -1) {
      berkasTerpilih = jejak.get(indexJejak--);
    }
    
    return berkasTerpilih;
  }
  
  public Berkas mundur() {
    if(berkasTerpilih.getObjekFile().getParent() != null) {
      jejak.add(berkasTerpilih);
      
      // isi index jejak, index jejak tidak dimulai dari 0, tapi dari jejak terbaru
      indexJejak = jejak.size() - 1;
      berkasTerpilih = new Berkas(ui, berkasTerpilih.getObjekFile().getParent());
    }
    
    return berkasTerpilih;
  }
  
  public boolean sampaiRoot() {
    return berkasTerpilih.getObjekFile().getAbsolutePath().equals("/");
  }
}
