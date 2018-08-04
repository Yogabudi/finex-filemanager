package filemanager;

import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserCommandEvent;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserEvent;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.EventQueue;

import filemanager.webviewui.*;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Yoga Budi Yulianto
 */
public class FileManager extends JFrame
implements PendengarWebBrowser {

  public WebViewUI ui;
  public List<Berkas> jejakNav = new ArrayList<>();
  public NavMajuMundur nav;
  
  public FileManager() {
    super();

    this.setSize(1200, 650);
    this.setTitle("Yoga File Manager");
    this.setName("UI Utama");
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    this.ketengahkan();
//    this.setExtendedState(JFrame.MAXIMIZED_BOTH);

    ui = new WebViewUI(this);
    ui.setPendengarWebBrowser(this);
    ui.setURL(FileManager.class.getResource("/ui/index.html").toExternalForm());
    
    this.getContentPane().add(ui);
    
    nav = new NavMajuMundur(ui);
  }

  public void ketengahkan() {
    Dimension dimensiLayar = getToolkit().getScreenSize();
    int x = (int) (dimensiLayar.getWidth() - this.getWidth()) / 2;
    int y = (int) (dimensiLayar.getHeight() - this.getHeight()) / 2;

    this.setLocation(x, y);
  }

  public static void main(String[] args) {
    WebViewUI.inisialisasi();
    
    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        new FileManager().setVisible(true);
      }
    });
    
    WebViewUI.jalankanEventPump();
  }

  @Override
  public void saatSelesaiLoading(WebBrowserEvent wbe, JWebBrowser browser) {
    Berkas berkas = new Berkas(ui, "/");
    nav.majuKe(berkas).tampilkanListBerkas();
  }

  @Override
  public void saatPerintahDiterima(WebBrowserCommandEvent wbce, JWebBrowser browser) {
    String perintah = wbce.getCommand();
    Object[] param = wbce.getParameters();
    
    if(perintah.equals("tampilkanListBerkas")) {
      String pathAbsolut = (String)param[0];
      
      js_tampilkanLoadingCircle();
      
      Berkas berkasTerpilih = new Berkas(ui, pathAbsolut);
      nav.majuKe(berkasTerpilih).tampilkanListBerkas();
      
      js_sembunyikanLoadingCircle();
    }
    else if(perintah.equals("tampilkanBerkasSebelumnya")) {
      if(!nav.sampaiRoot()) {
        js_tampilkanLoadingCircle();
        nav.mundur().tampilkanListBerkas();
        js_sembunyikanLoadingCircle();
      }
    }
    else if(perintah.equals("tampilkanBerkasKedepan")) {
      js_tampilkanLoadingCircle();
      nav.maju().tampilkanListBerkas();
      js_sembunyikanLoadingCircle();
      
      System.out.println("ABS : " + nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
    }
  }

  public void tampilkanInfoBerkas(Berkas berkasTerpilih) {
    String namaBerkas = berkasTerpilih.getObjekFile().getName();
    String jenis = (berkasTerpilih.getObjekFile().isDirectory()) ? "folder" : "file";
    String path = berkasTerpilih.getObjekFile().getAbsolutePath();

    String info = "" +
    "NAMA BERKAS : " + namaBerkas + "\n" +
    "JENIS : " + jenis + "\n" +
    "PATH : " + path + "\n" +
    "PARENT : " + berkasTerpilih.getObjekFile().getParent();

    JOptionPane.showMessageDialog(this, info);
  }
  
  public void js_tampilkanLoadingCircle() {
    String js = ""+
    "$('#konten').hide();"+
    "$('#loadingCircle').show();";
    
    ui.eksekusiJavascript(js);
  }
  
  public void js_sembunyikanLoadingCircle() {
    String js = ""+
    "$('#konten').show();"+
    "$('#loadingCircle').hide();";
    
    ui.eksekusiJavascript(js);
  }
}
