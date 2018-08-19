package filemanager;

import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserCommandEvent;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserEvent;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import filemanager.webviewui.*;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Yoga Budi Yulianto
 */
public class FileManager extends JFrame implements PendengarWebBrowser {

  public WebViewUI ui;
  public NavMajuMundur nav;
  public ListBreadcrumbBerkas bcBerkas = new ListBreadcrumbBerkas();
  
  public List<Berkas> jejakNav = new ArrayList<>();
  public ArrayList<Berkas> holderBerkas = new ArrayList<>();
  
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
    ui.setURL(getClass().getResource("/ui/index.html").toExternalForm());
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
    
    if(perintah.equals("log")) {
      System.out.println(param[0].toString().toUpperCase() + " : " + param[1]);
    }
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
              scrollKeBawahPadaJS();
            }
            else {
              Berkas.buatFolderBaru(lokasiSekarang + "/." + namaFolder, ui);
              Berkas.tandaiBerkasPadaJS("." + namaFolder, ui);
              scrollKeBawahPadaJS();
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
      catch(AccessDeniedException accdenex) {
        JOptionPane.showMessageDialog(this,
              "Maaf, anda tidak diizinkan membuat folder disini!\n",
              "Anda Bukan Root!",
              JOptionPane.ERROR_MESSAGE);
      }
      catch(FileAlreadyExistsException faee) {
        JOptionPane.showMessageDialog(this,
              "Maaf, nama folder yang anda masukkan sudah ada!\n",
              "Terjadi Kesalahan!",
              JOptionPane.ERROR_MESSAGE);
      }
      catch(IOException ioe) {
        JOptionPane.showMessageDialog(this,
              "Terjadi Kesalahan saat membuat folder, silahkan coba lagi!\n",
              "Terjadi Kesalahan!",
              JOptionPane.ERROR_MESSAGE);
      }
    }
    else if(perintah.equals("buatFileBaru")) {
      String namaFile = (String)param[0];
      boolean tersembunyi = (boolean)param[1];
      String lokasiSekarang =
              nav.getBerkasTerpilih().getObjekFile().getAbsolutePath();
      
      try {
        if(!namaFile.equals("")) {
          if(!namaFile.contains("/")) {
            if(!tersembunyi) {
              Berkas.buatFileBaru(lokasiSekarang + "/" + namaFile, ui);
              Berkas.tandaiBerkasPadaJS(namaFile, ui);
              scrollKeBawahPadaJS();
            }
            else {
              Berkas.buatFileBaru(lokasiSekarang + "/." + namaFile, ui);
              Berkas.tandaiBerkasPadaJS("." + namaFile, ui);
              scrollKeBawahPadaJS();
            }   
          }
          else {
            JOptionPane.showMessageDialog(this,
              "Nama file yang anda masukkan tidak valid!\n" +
              "Disarankan menggunakan karakter huruf dan angka",
              "Terjadi Kesalahan!",
              JOptionPane.ERROR_MESSAGE);
          }
        }
        else {
          JOptionPane.showMessageDialog(this,
              "Anda belum memasukkan nama file baru!\n" +
              "Mohon masukkan nama file baru",
              "Terjadi Kesalahan!",
              JOptionPane.ERROR_MESSAGE);
        }
      }
      catch(AccessDeniedException accdenex) {
        JOptionPane.showMessageDialog(this,
              "Maaf, anda tidak diizinkan membuat file disini!\n",
              "Anda Bukan Root!",
              JOptionPane.ERROR_MESSAGE);
      }
      catch(FileAlreadyExistsException faee) {
        JOptionPane.showMessageDialog(this,
              "Maaf, nama file yang anda masukkan sudah ada!\n",
              "Terjadi Kesalahan!",
              JOptionPane.ERROR_MESSAGE);
      }
      catch(IOException ioe) {
        JOptionPane.showMessageDialog(this,
              "Terjadi Kesalahan saat membuat file, silahkan coba lagi!\n",
              "Terjadi Kesalahan!",
              JOptionPane.ERROR_MESSAGE);
      }
    }
    else if(perintah.equals("bukaTerminal")) {
      String lokasiSekarang =
              nav.getBerkasTerpilih().getObjekFile().getAbsolutePath();
      String fileSh = "src/filemanager/programsh/bukaterminal.sh";
      
      try {
        ProcessBuilder pb = new ProcessBuilder(fileSh, lokasiSekarang);
        Process proc = pb.start();
      }
      catch (IOException ex) {
        JOptionPane.showMessageDialog(this,
              "Terjadi Kesalahan saat membuka terminal, silahkan coba lagi!\n",
              "Terjadi Kesalahan!",
              JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
      }
      
    }
    else if(perintah.equals("kosongkanTrash")) {
      try {
        ProcessBuilder pb = new ProcessBuilder("gio", "trash", "--empty");
        Process proc = pb.start();
      }
      catch(IOException ioex) {
        JOptionPane.showMessageDialog(this,
              "Terjadi Kesalahan saat mengosongkan trash, silahkan coba lagi!\n",
              "Terjadi Kesalahan!",
              JOptionPane.ERROR_MESSAGE);
        ioex.printStackTrace();
      }
      
    }
    else if(perintah.equals("cutBerkas")) {
      String namaBerkas = (String)param[0];
      Berkas berkas = new Berkas(ui, namaBerkas);
      
      holderBerkas.clear();
      holderBerkas.add(berkas);
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
  
  public void scrollKeBawahPadaJS() {
    String js = ""+
    "$('#konten').animate({ scrollTop: $(document).height() }, 500);";
    
    ui.eksekusiJavascript(js);
  }
}
