package filemanager;

import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserCommandEvent;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserEvent;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import filemanager.webviewui.*;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Yoga Budi Yulianto
 */
public class FileManager extends JFrame
implements PendengarWebBrowser {

  public WebViewUI ui;
  public List<Berkas> jejakNav = new ArrayList<>();
  public NavMajuMundur nav;
  public ListBreadcrumbBerkas bcBerkas = new ListBreadcrumbBerkas();
  
  public FileManager() {
    super();

    this.setSize(1200, 650);
    this.setTitle("Yoga File Manager");
    this.setName("UI Utama");
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    this.ketengahkan();
    this.setExtendedState(JFrame.MAXIMIZED_BOTH);

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
    
    SwingUtilities.invokeLater(new Runnable() {
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
    
    bcBerkas.masukkanDanTampilkan(new BreadcrumbBerkas(ui, "/"));
    bcBerkas.tandaiYangTerakhir();
    
    setTextPathPadaJS(berkas.getObjekFile().getAbsolutePath());
    System.out.println(nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
  }

  @Override
  public void saatPerintahDiterima(WebBrowserCommandEvent wbce, JWebBrowser browser) {
    String perintah = wbce.getCommand();
    Object[] param = wbce.getParameters();
    
    if(perintah.equals("tampilkanListBerkas")) {
      String pathAbsolut = (String)param[0];
      
      try {
        Berkas berkasTerpilih = new Berkas(ui, pathAbsolut);
        
        tampilkanCirclePadaJS();
        nav.majuKe(berkasTerpilih).tampilkanListBerkas();
        bcBerkas.isiDariPath(berkasTerpilih.pecahPathAbsolut(), ui);
        sembunyikanCirclePadaJS();

        setTextPathPadaJS(berkasTerpilih.getObjekFile().getAbsolutePath());
        System.out.println(nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
      }
      catch(NullPointerException nullex) {
        JOptionPane.showMessageDialog(this,
                  "Maaf, anda tidak diizinkan membuka folder ini",
                  "Anda Bukan Root!",
                  JOptionPane.ERROR_MESSAGE);
        
        sembunyikanCirclePadaJS();
          
        if(!nav.sampaiRoot()) {
          nav.mundur().tampilkanListBerkas();
          bcBerkas.isiDariPath(nav.getBerkasTerpilih().pecahPathAbsolut(), ui);

          nav.majuKe(nav.getBerkasTerpilih());
        }
        else {
          Berkas berkas = new Berkas(ui, "/");
          nav.majuKe(berkas).tampilkanListBerkas();
        }
      }
    }
    else if(perintah.equals("tampilkanListBerkasDariBreadcrumb")) {
      String pathAbsolut = (String)param[0];
      String labelBc = (String)param[1];
      
      Berkas berkasTerpilih = new Berkas(ui, pathAbsolut);
      
      tampilkanCirclePadaJS();
      nav.mundurKe(berkasTerpilih).tampilkanListBerkas();
      bcBerkas.getBreadcrumb(labelBc).tandaiPadaJS();
      sembunyikanCirclePadaJS();
      
      setTextPathPadaJS(berkasTerpilih.getObjekFile().getAbsolutePath());
      System.out.println(nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
    }
    else if(perintah.equals("tampilkanBerkasSebelumnya")) {
      if(!nav.sampaiRoot()) {
        tampilkanCirclePadaJS();
        nav.mundur().tampilkanListBerkas();
        bcBerkas.isiDariPath(nav.getBerkasTerpilih().pecahPathAbsolut(), ui);
        sembunyikanCirclePadaJS();
        
        setTextPathPadaJS(nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
        System.out.println(nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
      }
    }
    else if(perintah.equals("tampilkanBerkasKedepan")) {
      tampilkanCirclePadaJS();
      nav.maju().tampilkanListBerkas();
      bcBerkas.isiDariPath(nav.getBerkasTerpilih().pecahPathAbsolut(), ui);
      sembunyikanCirclePadaJS();
      
      setTextPathPadaJS(nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
      System.out.println(nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
    }
    else if(perintah.equals("keyEnterDitekan")) {
      String namaKomponen = (String)param[0];
      String pathAbsolut = (String)param[1];
      
      if(namaKomponen.equals("#txPath")) {
        if(!pathAbsolut.equals("")) {
          Berkas berkasTerpilih = new Berkas(ui, pathAbsolut);

          if(berkasTerpilih.berkasTersedia()) {
            tampilkanCirclePadaJS();
            nav.majuKe(berkasTerpilih).tampilkanListBerkas();
            bcBerkas.isiDariPath(berkasTerpilih.pecahPathAbsolut(), ui);
            sembunyikanCirclePadaJS();
            
            System.out.println(nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
          }
          else {
            JOptionPane.showMessageDialog(this,
                "Path yang anda masukkan tidak valid!\n" +
                "Pastikan folder tujuan anda tersedia",
                "Terjadi Kesalahan!",
                JOptionPane.ERROR_MESSAGE);
          }
        }
        else {
          JOptionPane.showMessageDialog(this,
                "Anda belum memasukkan path folder tujuan anda!\n" +
                "Mohon masukkan path folder tujuan anda",
                "Terjadi Kesalahan!",
                JOptionPane.ERROR_MESSAGE);
        }
      }
    }
    else if(perintah.equals("loncatKeDokumen")) {
      String path = System.getProperty("user.home") + "/Documents";
      Berkas berkas = new Berkas(ui, path);
      
      if(berkas.berkasTersedia()) {
        tampilkanCirclePadaJS();
        nav.majuKe(berkas).tampilkanListBerkas();
        bcBerkas.isiDariPath(nav.getBerkasTerpilih().pecahPathAbsolut(), ui);
        sembunyikanCirclePadaJS();
        
        setTextPathPadaJS(nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
        System.out.println(nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
      }
      else {
        JOptionPane.showMessageDialog(this,
            "Path yang anda masukkan tidak valid!\n" +
            "Pastikan folder tujuan anda tersedia",
            "Terjadi Kesalahan!",
            JOptionPane.ERROR_MESSAGE);
      }
    }
    else if(perintah.equals("loncatKeFoto")) {
      String path = System.getProperty("user.home") + "/Pictures";
      Berkas berkas = new Berkas(ui, path);
      
      if(berkas.berkasTersedia()) {
        tampilkanCirclePadaJS();
        nav.majuKe(berkas).tampilkanListBerkas();
        bcBerkas.isiDariPath(nav.getBerkasTerpilih().pecahPathAbsolut(), ui);
        sembunyikanCirclePadaJS();
        
        setTextPathPadaJS(nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
        System.out.println(nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
      }
      else {
        JOptionPane.showMessageDialog(this,
            "Path yang anda masukkan tidak valid!\n" +
            "Pastikan folder tujuan anda tersedia",
            "Terjadi Kesalahan!",
            JOptionPane.ERROR_MESSAGE);
      }
    }
    else if(perintah.equals("loncatKeMusik")) {
      String path = System.getProperty("user.home") + "/Music";
      Berkas berkas = new Berkas(ui, path);
      
      if(berkas.berkasTersedia()) {
        tampilkanCirclePadaJS();
        nav.majuKe(berkas).tampilkanListBerkas();
        bcBerkas.isiDariPath(nav.getBerkasTerpilih().pecahPathAbsolut(), ui);
        sembunyikanCirclePadaJS();
        
        setTextPathPadaJS(nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
        System.out.println(nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
      }
      else {
        JOptionPane.showMessageDialog(this,
            "Path yang anda masukkan tidak valid!\n" +
            "Pastikan folder tujuan anda tersedia",
            "Terjadi Kesalahan!",
            JOptionPane.ERROR_MESSAGE);
      }
    }
    else if(perintah.equals("loncatKeVideo")) {
      String path = System.getProperty("user.home") + "/Videos";
      Berkas berkas = new Berkas(ui, path);
      
      if(berkas.berkasTersedia()) {
        tampilkanCirclePadaJS();
        nav.majuKe(berkas).tampilkanListBerkas();
        bcBerkas.isiDariPath(nav.getBerkasTerpilih().pecahPathAbsolut(), ui);
        sembunyikanCirclePadaJS();
        
        setTextPathPadaJS(nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
        System.out.println(nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
      }
      else {
        JOptionPane.showMessageDialog(this,
            "Path yang anda masukkan tidak valid!\n" +
            "Pastikan folder tujuan anda tersedia",
            "Terjadi Kesalahan!",
            JOptionPane.ERROR_MESSAGE);
      }
    }
    else if(perintah.equals("loncatKeUnduhan")) {
      String path = System.getProperty("user.home") + "/Downloads";
      Berkas berkas = new Berkas(ui, path);
      
      if(berkas.berkasTersedia()) {
        tampilkanCirclePadaJS();
        nav.majuKe(berkas).tampilkanListBerkas();
        bcBerkas.isiDariPath(nav.getBerkasTerpilih().pecahPathAbsolut(), ui);
        sembunyikanCirclePadaJS();
        
        setTextPathPadaJS(nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
        System.out.println(nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
      }
      else {
        JOptionPane.showMessageDialog(this,
            "Path yang anda masukkan tidak valid!\n" +
            "Pastikan folder tujuan anda tersedia",
            "Terjadi Kesalahan!",
            JOptionPane.ERROR_MESSAGE);
      }
    }
    else if(perintah.equals("loncatKeRoot")) {
      String path = "/";
      Berkas berkas = new Berkas(ui, path);
      
      if(berkas.berkasTersedia()) {
        tampilkanCirclePadaJS();
        nav.majuKe(berkas).tampilkanListBerkas();
        bcBerkas.isiDariPath(nav.getBerkasTerpilih().pecahPathAbsolut(), ui);
        sembunyikanCirclePadaJS();
        
        setTextPathPadaJS(nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
        System.out.println(nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
      }
      else {
        JOptionPane.showMessageDialog(this,
            "Path yang anda masukkan tidak valid!\n" +
            "Pastikan folder tujuan anda tersedia",
            "Terjadi Kesalahan!",
            JOptionPane.ERROR_MESSAGE);
      }
    }
    else if(perintah.equals("buatFolderBaru")) {
      String namaFolder = (String)param[0];
      boolean tersembunyi = (boolean)param[1];
      String lokasiSekarang =
              nav.getBerkasTerpilih().getObjekFile().getAbsolutePath();
      
      try {
        if(!namaFolder.equals("")) {
          if(!namaFolder.contains("/")) {
            if(!tersembunyi) {
              Berkas.buatFolderBaru(lokasiSekarang + "/" + namaFolder, ui);
              Berkas.tandaiBerkasPadaJS(namaFolder, ui);
            }
            else {
              Berkas.buatFolderBaru(lokasiSekarang + "/." + namaFolder, ui);
              Berkas.tandaiBerkasPadaJS("." + namaFolder, ui);
            }   
          }
          else {
            JOptionPane.showMessageDialog(this,
              "Nama folder yang anda masukkan tidak valid!\n" +
              "Disarankan menggunakan karakter huruf dan angka",
              "Terjadi Kesalahan!",
              JOptionPane.ERROR_MESSAGE);
          }
        }
        else {
          JOptionPane.showMessageDialog(this,
              "Anda belum memasukkan nama folder baru!\n" +
              "Mohon masukkan nama folder baru",
              "Terjadi Kesalahan!",
              JOptionPane.ERROR_MESSAGE);
        }
      }
      catch(Exception ex) {
        JOptionPane.showMessageDialog(this,
              "Maaf, anda tidak diizinkan membuat folder disini!\n",
              "Anda Bukan Root!",
              JOptionPane.ERROR_MESSAGE);
      }
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
  
  public void setTextPathPadaJS(String path) {
    String js = ""+
    "$('#txPath').val('"+path+"');";
    
    ui.eksekusiJavascript(js);
  }
}
