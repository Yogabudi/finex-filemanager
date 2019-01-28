package filemanager;

import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserCommandEvent;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserEvent;
import filemanager.database.DataTabelFGL;
import filemanager.database.DataTabelPintasan;
import filemanager.database.DataTabelWajah;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import filemanager.webviewui.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Yoga Budi Yulianto
 */
public class FileManager extends JFrame
        implements PendengarWebBrowser, WindowFocusListener, WindowListener {

  public WebViewUI ui;
  public NavMajuMundur nav;
  public ListBreadcrumbBerkas bcBerkas = new ListBreadcrumbBerkas();
  
  public List<Berkas> jejakNav = new ArrayList<>();
  public ArrayList<Berkas> holderBerkas = new ArrayList<>();
  
  public Berkas gambarSebelumnya;
  public ArrayList<Berkas> folderTempatWajah = new ArrayList<>();
  
  public ExecutorService servDeteksi;
  public ExecutorService servCariTglDibuat;
  
  final String FOLDER_WAJAH_TERSIMPAN = ".wajah_tersimpan";
  
  public static boolean berhentiMencariWajah = true;
  public String operasiBerkasDiinginkan = "SALIN";
  
  public FileManager() {
    super();

    this.setSize(1200, 650);
    this.setTitle("Yoga File Manager");
    this.setName("UI Utama");
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    this.ketengahkan();
    //this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    this.addWindowFocusListener(this);
    this.addWindowListener(this);

    ui = new WebViewUI(this);
    ui.setPendengarWebBrowser(this);
    ui.setURL(getClass().getResource("/ui/index.html").toExternalForm());
//    ui.setURL(WebServer.getDefaultWebServer().getClassPathResourceURL(
//                  FileManager.class.getName(), "/ui/index.html"));
    this.getContentPane().add(ui);
    
    nav = new NavMajuMundur(ui);
    
    servDeteksi = Executors.newFixedThreadPool(1);
    
    // log data wajah
    try {
      DataTabelWajah dat = new DataTabelWajah();
      dat.buatTabelJikaTidakAda();
      
      DataTabelWajah[] semuaData = dat.dapatkanSemuaData();
      
      for(int i = 0; i < semuaData.length; i++) {
        System.out.println("nama wajah : " + semuaData[i].getNamaWajah());
        System.out.println("path foto : " + semuaData[i].getPathFoto());
      }

      dat.tutupKoneksi();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
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
    Berkas berkas = new Berkas(ui, System.getProperty("user.home"));
    nav.majuKe(berkas).tampilkanListBerkas();
    
    bcBerkas.isiDariPath(berkas.pecahPathAbsolut(), ui);
    bcBerkas.tandaiYangTerakhir();
    
    Berkas.setTextPathPadaJS(ui, berkas.getObjekFile().getAbsolutePath());
    System.out.println(nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
    
    try {
      // load data wajah
      DataTabelWajah dat = new DataTabelWajah();
      dat.buatTabelJikaTidakAda();
      tampilkanSemuaWajah(dat);
      dat.tutupKoneksi();
      
      // load data FGL
      DataTabelFGL datFgl = new DataTabelFGL();
      datFgl.buatTabelJikaTidakAda();
      tampilkanSemuaFGL(datFgl);
      datFgl.tutupKoneksi();
      
      // load data pintasan
      DataTabelPintasan datPint = new DataTabelPintasan();
      datPint.buatTabelJikaTidakAda();
      tampilkanSemuaPintasan(datPint);
      datPint.tutupKoneksi();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }    
  }

  @Override
  public void saatPerintahDiterima(WebBrowserCommandEvent wbce, JWebBrowser browser) {
    String perintah = wbce.getCommand();
    Object[] param = wbce.getParameters();
    
    if(perintah.equals("log")) {
      System.out.println(param[0].toString().toUpperCase() + " : " + param[1]);
    }
//    else if(perintah.equals("tesArray")) {
//      for(int i = 0; i < param.length; i++) {
//        System.out.println((String)param[i]);
//      }
//    }
    else if(perintah.equals("tampilkanListBerkas")) {
      String pathAbsolut = param[0].toString();
      
      try {
        Berkas berkasTerpilih = new Berkas(ui, pathAbsolut);
        
        Berkas.tampilkanCirclePadaJS(ui);
        nav.majuKe(berkasTerpilih).tampilkanListBerkas();
        bcBerkas.isiDariPath(berkasTerpilih.pecahPathAbsolut(), ui);
        Berkas.sembunyikanCirclePadaJS(ui);

        Berkas.setTextPathPadaJS(ui, berkasTerpilih.getObjekFile().getAbsolutePath());
        System.out.println(nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
      }
      catch(NullPointerException nullex) {
        JOptionPane.showMessageDialog(this,
                  "Maaf, anda tidak diizinkan membuka folder ini",
                  "Anda Bukan Root!",
                  JOptionPane.ERROR_MESSAGE);
        
        Berkas.sembunyikanCirclePadaJS(ui);
          
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
      
      berhentiMencariWajah = true;
      
      Berkas berkasTerpilih = new Berkas(ui, pathAbsolut);
      
      Berkas.tampilkanCirclePadaJS(ui);
      nav.mundurKe(berkasTerpilih).tampilkanListBerkas();
      bcBerkas.getBreadcrumb(labelBc).tandaiPadaJS();
      Berkas.sembunyikanCirclePadaJS(ui);
      
      Berkas.setTextPathPadaJS(ui, berkasTerpilih.getObjekFile().getAbsolutePath());
      System.out.println(nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
    }
    else if(perintah.equals("tampilkanBerkasSebelumnya")) {
      if(!nav.sampaiRoot()) {
        Berkas.tampilkanCirclePadaJS(ui);
        nav.mundur().tampilkanListBerkas();
        bcBerkas.isiDariPath(nav.getBerkasTerpilih().pecahPathAbsolut(), ui);
        Berkas.sembunyikanCirclePadaJS(ui);
        
        Berkas.setTextPathPadaJS(ui, nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
        System.out.println(nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
      }
    }
    else if(perintah.equals("tampilkanBerkasKedepan")) {
      Berkas.tampilkanCirclePadaJS(ui);
      nav.maju().tampilkanListBerkas();
      bcBerkas.isiDariPath(nav.getBerkasTerpilih().pecahPathAbsolut(), ui);
      Berkas.sembunyikanCirclePadaJS(ui);
      
      Berkas.setTextPathPadaJS(ui, nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
      System.out.println(nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
    }
    else if(perintah.equals("keyEnterDitekan")) {
      String namaKomponen = (String)param[0];
      String pathAbsolut = (String)param[1];
      
      if(namaKomponen.equals("#txPath")) {
        if(!pathAbsolut.equals("")) {
          Berkas berkasTerpilih = new Berkas(ui, pathAbsolut);

          if(berkasTerpilih.berkasTersedia()) {
            Berkas.tampilkanCirclePadaJS(ui);
            nav.majuKe(berkasTerpilih).tampilkanListBerkas();
            bcBerkas.isiDariPath(berkasTerpilih.pecahPathAbsolut(), ui);
            Berkas.sembunyikanCirclePadaJS(ui);
            
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
        Berkas.tampilkanCirclePadaJS(ui);
        nav.majuKe(berkas).tampilkanListBerkas();
        bcBerkas.isiDariPath(nav.getBerkasTerpilih().pecahPathAbsolut(), ui);
        Berkas.sembunyikanCirclePadaJS(ui);
        
        Berkas.setTextPathPadaJS(ui, nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
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
        Berkas.tampilkanCirclePadaJS(ui);
        nav.majuKe(berkas).tampilkanListBerkas();
        bcBerkas.isiDariPath(nav.getBerkasTerpilih().pecahPathAbsolut(), ui);
        Berkas.sembunyikanCirclePadaJS(ui);
        
        Berkas.setTextPathPadaJS(ui, nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
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
        Berkas.tampilkanCirclePadaJS(ui);
        nav.majuKe(berkas).tampilkanListBerkas();
        bcBerkas.isiDariPath(nav.getBerkasTerpilih().pecahPathAbsolut(), ui);
        Berkas.sembunyikanCirclePadaJS(ui);
        
        Berkas.setTextPathPadaJS(ui, nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
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
        Berkas.tampilkanCirclePadaJS(ui);
        nav.majuKe(berkas).tampilkanListBerkas();
        bcBerkas.isiDariPath(nav.getBerkasTerpilih().pecahPathAbsolut(), ui);
        Berkas.sembunyikanCirclePadaJS(ui);
        
        Berkas.setTextPathPadaJS(ui, nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
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
        Berkas.tampilkanCirclePadaJS(ui);
        nav.majuKe(berkas).tampilkanListBerkas();
        bcBerkas.isiDariPath(nav.getBerkasTerpilih().pecahPathAbsolut(), ui);
        Berkas.sembunyikanCirclePadaJS(ui);
        
        Berkas.setTextPathPadaJS(ui, nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
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
        Berkas.tampilkanCirclePadaJS(ui);
        nav.majuKe(berkas).tampilkanListBerkas();
        bcBerkas.isiDariPath(nav.getBerkasTerpilih().pecahPathAbsolut(), ui);
        Berkas.sembunyikanCirclePadaJS(ui);
        
        Berkas.setTextPathPadaJS(ui, nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
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
    else if(perintah.equals("loncatKeHome")) {
      String path = System.getProperty("user.home");
      Berkas berkas = new Berkas(ui, path);
      
      if(berkas.berkasTersedia()) {
        Berkas.tampilkanCirclePadaJS(ui);
        nav.majuKe(berkas).tampilkanListBerkas();
        bcBerkas.isiDariPath(nav.getBerkasTerpilih().pecahPathAbsolut(), ui);
        Berkas.sembunyikanCirclePadaJS(ui);
        
        Berkas.setTextPathPadaJS(ui, nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
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
              Berkas.scrollKeBawahPadaJS(ui);
            }
            else {
              Berkas.buatFolderBaru(lokasiSekarang + "/." + namaFolder, ui);
              Berkas.tandaiBerkasPadaJS("." + namaFolder, ui);
              Berkas.scrollKeBawahPadaJS(ui);
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
              Berkas.scrollKeBawahPadaJS(ui);
            }
            else {
              Berkas.buatFileBaru(lokasiSekarang + "/." + namaFile, ui);
              Berkas.tandaiBerkasPadaJS("." + namaFile, ui);
              Berkas.scrollKeBawahPadaJS(ui);
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
      String fileSh = "src/filemanager/tools/bukaterminal.sh";
      
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
      holderBerkas.clear();
      operasiBerkasDiinginkan = "CUT";
      
      for(int i = 0; i < param.length; i++) {
        String namaBerkas = (String)param[i];
        Berkas berkas = new Berkas(ui, namaBerkas);

        holderBerkas.add(berkas);
      }
    }
    else if(perintah.equals("salinBerkas")) {
      holderBerkas.clear();
      operasiBerkasDiinginkan = "SALIN";
      
      for(int i = 0; i < param.length; i++) {
        String namaBerkas = (String)param[i];
        Berkas berkas = new Berkas(ui, namaBerkas);

        holderBerkas.add(berkas);
      }
    }
    else if(perintah.equals("tempelBerkas")) {
      String lokasiSekarang =
          nav.getBerkasTerpilih().getObjekFile().getAbsolutePath();

      ExecutorService proses = Executors.newCachedThreadPool();
      
      for(int i = 0; i < holderBerkas.size(); i++) {
        final Berkas berkas = holderBerkas.get(i);
        
        proses.execute(new Runnable() {
          @Override
          public void run() {
            if(operasiBerkasDiinginkan.equals("CUT")) {
              try {
                Berkas berkasTujuan = new Berkas(ui,
                      lokasiSekarang + "/" + berkas.getObjekFile().getName());

                Berkas.buatPanelOpPadaJS(ui,
                        "cut_" + berkas.getObjekFile().getName(),
                        "pemindahan", berkas, berkasTujuan);

                Berkas berkasHasil =
                        Berkas.pindahkanBerkas(ui, berkas, berkasTujuan);

                Berkas.hapusPanelOpPadaJS(ui,
                        "cut_" + berkas.getObjekFile().getName());
                Berkas.sembunyikanTeksNoOp(ui);

                berkasHasil.buatBerkasPadaJS();
                Berkas.scrollKeBawahPadaJS(ui);
                Berkas.tandaiBerkasPadaJS(berkasHasil.getObjekFile().getName(), ui);
                Berkas.hilangkanEfekPulse(ui);
              }
              catch(IOException ex) {
                JOptionPane.showMessageDialog(FileManager.this,
                    "Terjadi Kesalahan saat memindahkan berkas " +
                    berkas.getObjekFile().getName() + "\n",
                    "Terjadi Kesalahan!",
                    JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
              }
            }
            else if(operasiBerkasDiinginkan.equals("SALIN")) {
              try {
                Berkas berkasTujuan = new Berkas(ui,
                      lokasiSekarang + "/" + berkas.getObjekFile().getName());

                Berkas.buatPanelOpPadaJS(ui,
                        "salin_" + berkas.getObjekFile().getName(),
                        "penyalinan", berkas, berkasTujuan);

                Berkas berkasHasil = Berkas.salinBerkas(ui, berkas, berkasTujuan);

                Berkas.hapusPanelOpPadaJS(ui,
                        "salin_" + berkas.getObjekFile().getName());
                Berkas.sembunyikanTeksNoOp(ui);

                berkasHasil.buatBerkasPadaJS();
                Berkas.scrollKeBawahPadaJS(ui);
                Berkas.tandaiBerkasPadaJS(berkasHasil.getObjekFile().getName(), ui);
                Berkas.hilangkanEfekPulse(ui);
              }
              catch(IOException ex) {
                JOptionPane.showMessageDialog(FileManager.this,
                    "Terjadi Kesalahan saat menyalin berkas " +
                    berkas.getObjekFile().getName() + "\n",
                    "Terjadi Kesalahan!",
                    JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
              }
            }
          }
        });
        
      }
      
      proses.shutdown();
    }
    else if(perintah.equals("hideBerkas")) {
      for(int i = 0; i < param.length; i++) {
        String pathAbsolut = (String)param[i];
        
        Berkas berkas = new Berkas(ui, pathAbsolut);
        berkas.hideBerkas();
      }
    }
    else if(perintah.equals("unhideBerkas")) {
      for(int i = 0; i < param.length; i++) {
        String pathAbsolut = (String)param[i];

        Berkas berkas = new Berkas(ui, pathAbsolut);
        berkas.unhideBerkas();
      }
    }
    else if(perintah.equals("duplikatBerkas")) {
      String pathAbsolut = (String)param[0];
      
      Berkas berkas = new Berkas(ui, pathAbsolut);
      
      try {
        Berkas duplikatan = berkas.duplikatDanTampilkan();
        
      }
      catch(IOException ex) {
        ex.printStackTrace();
      }
    }
    else if(perintah.equals("ubahNama")) {
      String pathAbsolut = (String)param[0];
      String namaBaru = (String)param[1];
      
      Berkas berkas = new Berkas(ui, pathAbsolut);
      berkas.ubahNamaDanTampilkan(namaBaru);
    }
    else if(perintah.equals("hapusKeTrash")) {
      for(int i = 0; i < param.length; i++) {
        String pathAbsolut = (String)param[i];
        Berkas berkas = new Berkas(ui, pathAbsolut);

        try {
          berkas.hapusKeTrash();
        }
        catch(IOException ioex) {
          JOptionPane.showMessageDialog(this,
                "Terjadi Kesalahan saat menghapus berkas, silahkan coba lagi!\n",
                "Terjadi Kesalahan!",
                JOptionPane.ERROR_MESSAGE);
          ioex.printStackTrace();
        }
      }
    }
    else if(perintah.equals("hapusBerkasPermanen")) {
      for(int i = 0; i < param.length; i++) {
        String pathAbsolut = (String)param[i];
        Berkas berkas = new Berkas(ui, pathAbsolut);

        try {
          berkas.hapusPermanen();

        }
        catch(IOException ex) {
          JOptionPane.showMessageDialog(this,
                "Terjadi Kesalahan saat menghapus berkas, silahkan coba lagi!\n",
                "Terjadi Kesalahan!",
                JOptionPane.ERROR_MESSAGE);
          ex.printStackTrace();
        }
      }
    }
    else if(perintah.equals("tampilkanInfoBerkas")) {
      String pathAbsolut = (String)param[0];
      Berkas berkas = new Berkas(ui, pathAbsolut);
      
      try {
        berkas.tampilkanInfoBerkas();
      }
      catch(IOException ex) {
        JOptionPane.showMessageDialog(this,
              "Maaf, anda tidak diizinkan melihat detail berkas ini!\n",
              "Anda Bukan Root!",
              JOptionPane.ERROR_MESSAGE);
      }
      catch(NullPointerException ex) {
        JOptionPane.showMessageDialog(this,
              "Maaf, anda tidak diizinkan melihat detail berkas ini!\n",
              "Anda Bukan Root!",
              JOptionPane.ERROR_MESSAGE);
      }
    }
    else if(perintah.equals("cariBerkas")) {
      String teks = (String)param[0];
      String lokasiSekarang = nav.getBerkasTerpilih().getObjekFile().getAbsolutePath();
      Berkas berkasTempat = new Berkas(ui, lokasiSekarang);
      
      Berkas.hapusSemuaBerkasPadaJS(ui);
      
      ExecutorService execService = Executors.newCachedThreadPool();
      execService.execute(new Runnable() {
        @Override
        public void run() {
          try {
            ArrayList<Berkas> dataCari =
               Berkas.cariBerkas(ui, Berkas.BERDASAR_NAMA, teks, berkasTempat);
            
//            for(int i = 0; i < dataCari.size(); i++) {
//              if(i < 100) {
//                dataCari.get(i).buatBerkasPadaJS();
//              }
//              else {
//                break;
//              }
//            }
          }
          catch (IOException ex){
            ex.printStackTrace();
          }
        }
      });

      execService.shutdown();
    }
    else if(perintah.equals("cariBerkasBerdasarTglDibuat")) {
      String tgl = (String)param[0];
      String lokasiSekarang = nav.getBerkasTerpilih().getObjekFile().getAbsolutePath();
      Berkas berkasTempat = new Berkas(ui, lokasiSekarang);
      
      Berkas.tampilkanCirclePadaJS(ui);
      Berkas.sembunyikanTeksInfo(ui);
      Berkas.hapusSemuaBerkasPadaJS(ui);
      
      if(servCariTglDibuat != null) {
        servCariTglDibuat.shutdownNow();
      }
      
      servCariTglDibuat = Executors.newFixedThreadPool(1);
      servCariTglDibuat.execute(new Runnable() {
        @Override
        public void run() {
          try {
            ArrayList<Berkas> dataCari =
                  Berkas.cariBerkas(ui, Berkas.BERDASAR_TGL_DIBUAT_PAST_TO_PRESENT,
                          tgl, berkasTempat);
            
//            for(int i = 0; i < dataCari.size(); i++) {
//              dataCari.get(i).buatBerkasPadaJS();
//            }
//            
//            if(dataCari.isEmpty()) {
//              Berkas.tampilkanTeksInfo(ui);
//            }
//            else {
//              Berkas.sembunyikanTeksInfo(ui);
//            }
//            
//            Berkas.sembunyikanCirclePadaJS(ui);
          }
          catch (IOException ex){
            ex.printStackTrace();
          }
        }
      });
      
    }
    else if(perintah.equals("cariBerkasBerdasarTglModif")) {
      String tgl = (String)param[0];
      String lokasiSekarang = nav.getBerkasTerpilih().getObjekFile().getAbsolutePath();
      Berkas berkasTempat = new Berkas(ui, lokasiSekarang);
      
      Berkas.tampilkanCirclePadaJS(ui);
      Berkas.sembunyikanTeksInfo(ui);
      Berkas.hapusSemuaBerkasPadaJS(ui);
      
      ExecutorService execService = Executors.newFixedThreadPool(1);
      execService.execute(new Runnable() {
        @Override
        public void run() {
          try {
            ArrayList<Berkas> dataCari =
                  Berkas.cariBerkas(ui, Berkas.BERDASAR_TGL_MODIFIKASI_PAST_TO_PRESENT,
                          tgl, berkasTempat);
            
//            for(int i = 0; i < dataCari.size(); i++) {
//              dataCari.get(i).buatBerkasPadaJS();
//            }
//            
//            if(dataCari.isEmpty()) {
//              Berkas.tampilkanTeksInfo(ui);
//            }
//            else {
//              Berkas.sembunyikanTeksInfo(ui);
//            }
//            
//            Berkas.sembunyikanCirclePadaJS(ui);
          }
          catch (IOException ex){
            ex.printStackTrace();
          }
        }
      });

      execService.shutdown();
    }
    else if(perintah.equals("cariBerkasBerdasarTglAkses")) {
      String tgl = (String)param[0];
      String lokasiSekarang = nav.getBerkasTerpilih().getObjekFile().getAbsolutePath();
      Berkas berkasTempat = new Berkas(ui, lokasiSekarang);
      
      Berkas.tampilkanCirclePadaJS(ui);
      Berkas.sembunyikanTeksInfo(ui);
      Berkas.hapusSemuaBerkasPadaJS(ui);
      
      ExecutorService execService = Executors.newFixedThreadPool(1);
      execService.execute(new Runnable() {
        @Override
        public void run() {
          try {
            ArrayList<Berkas> dataCari =
                  Berkas.cariBerkas(ui, Berkas.BERDASAR_TGL_AKSES_PAST_TO_PRESENT,
                          tgl, berkasTempat);
            
//            for(int i = 0; i < dataCari.size(); i++) {
//              dataCari.get(i).buatBerkasPadaJS();
//            }
//            
//            if(dataCari.isEmpty()) {
//              Berkas.tampilkanTeksInfo(ui);
//            }
//            else {
//              Berkas.sembunyikanTeksInfo(ui);
//            }
//            
//            Berkas.sembunyikanCirclePadaJS(ui);
          }
          catch (IOException ex){
            ex.printStackTrace();
          }
        }
      });

      execService.shutdown();
    }
    else if(perintah.equals("urutkanBerdasarATime")) {
      String mode = (String)param[0];
      String lokasiSekarang = nav.getBerkasTerpilih().getObjekFile().getAbsolutePath();
      Berkas berkasTempat = new Berkas(ui, lokasiSekarang);
      
      Berkas.tampilkanCirclePadaJS(ui);
      Berkas.hapusSemuaBerkasPadaJS(ui);
      
      ExecutorService execService = Executors.newFixedThreadPool(1);
      execService.execute(new Runnable() {
        @Override
        public void run() {
          try {
            Berkas[] dataBerkas = null;
            
            if(mode.equals("past_to_present")) {
              dataBerkas = berkasTempat.urutkan(Berkas.BERDASAR_TGL_AKSES_PAST_TO_PRESENT, "");
            }
            else if(mode.equals("present_to_past")) {
              dataBerkas = berkasTempat.urutkan(Berkas.BERDASAR_TGL_AKSES_PRESENT_TO_PAST, "");
            }
            
            for(int i = 0; i < dataBerkas.length; i++) {
              if(dataBerkas[i].getObjekFile().getName().endsWith(".jpg") ||
                  dataBerkas[i].getObjekFile().getName().endsWith(".JPG") ||
                  dataBerkas[i].getObjekFile().getName().endsWith(".png") ||
                  dataBerkas[i].getObjekFile().getName().endsWith(".PNG") ||
                  dataBerkas[i].getObjekFile().getName().endsWith(".jpeg")) {

                  dataBerkas[i].setPakeThumbnail(true);
              }
              
              dataBerkas[i].buatBerkasPadaJS();
            }
            
            Berkas.sembunyikanCirclePadaJS(ui);
            
          } catch (IOException ex) {
            ex.printStackTrace();
          }
        }
      });
      
      execService.shutdown();
    }
    else if(perintah.equals("urutkanBerdasarMTime")) {
      String mode = (String)param[0];
      String lokasiSekarang = nav.getBerkasTerpilih().getObjekFile().getAbsolutePath();
      Berkas berkasTempat = new Berkas(ui, lokasiSekarang);
      
      Berkas.tampilkanCirclePadaJS(ui);
      Berkas.hapusSemuaBerkasPadaJS(ui);
      
      ExecutorService execService = Executors.newFixedThreadPool(1);
      execService.execute(new Runnable() {
        @Override
        public void run() {
          try {
            Berkas[] dataBerkas = null;
            
            if(mode.equals("past_to_present")) {
              dataBerkas =
              berkasTempat.urutkan(Berkas.BERDASAR_TGL_MODIFIKASI_PAST_TO_PRESENT, "");
            }
            else if(mode.equals("present_to_past")) {
              dataBerkas =
              berkasTempat.urutkan(Berkas.BERDASAR_TGL_MODIFIKASI_PRESENT_TO_PAST, "");
            }
            
            for(int i = 0; i < dataBerkas.length; i++) {
              if(dataBerkas[i].getObjekFile().getName().endsWith(".jpg") ||
                  dataBerkas[i].getObjekFile().getName().endsWith(".JPG") ||
                  dataBerkas[i].getObjekFile().getName().endsWith(".png") ||
                  dataBerkas[i].getObjekFile().getName().endsWith(".PNG") ||
                  dataBerkas[i].getObjekFile().getName().endsWith(".jpeg")) {

                  dataBerkas[i].setPakeThumbnail(true);
              }
              
              dataBerkas[i].buatBerkasPadaJS();
            }
            
            Berkas.sembunyikanCirclePadaJS(ui);
            
          } catch (IOException ex) {
            ex.printStackTrace();
          }
        }
      });
      
      execService.shutdown();
    }
    else if(perintah.equals("urutkanBerdasarCTime")) {
      String mode = (String)param[0];
      String lokasiSekarang = nav.getBerkasTerpilih().getObjekFile().getAbsolutePath();
      Berkas berkasTempat = new Berkas(ui, lokasiSekarang);
      
      Berkas.tampilkanCirclePadaJS(ui);
      Berkas.hapusSemuaBerkasPadaJS(ui);
      
      ExecutorService execService = Executors.newFixedThreadPool(1);
      execService.execute(new Runnable() {
        @Override
        public void run() {
          try {
            urutkanBerdasarCTime(mode, berkasTempat);
          }
          catch (Exception ex) {
            try {
              urutkanBerdasarCTime(mode, berkasTempat);
            }
            catch(Exception ex2) {
              ex2.printStackTrace();
            }
            
            ex.printStackTrace();
          }
        }
      });
      
      execService.shutdown();
    }
    else if(perintah.equals("filterEkstensi")) {
      String ekstensi = (String)param[0];
      String lokasiSekarang = nav.getBerkasTerpilih().getObjekFile().getAbsolutePath();
      Berkas berkasTempat = new Berkas(ui, lokasiSekarang);
      
      Berkas.tampilkanCirclePadaJS(ui);
      Berkas.hapusSemuaBerkasPadaJS(ui);
      
      ExecutorService execService = Executors.newFixedThreadPool(1);
      execService.execute(new Runnable() {
        @Override
        public void run() {
          try {
            Berkas[] dataBerkas =
                    berkasTempat.urutkan(Berkas.BERDASAR_EKSTENSI,
                            ekstensi.toLowerCase());
            
            for(int i = 0; i < dataBerkas.length; i++) {
              dataBerkas[i].buatBerkasPadaJS();
            }
            
            Berkas.sembunyikanCirclePadaJS(ui);
            
          } catch (IOException ex) {
            ex.printStackTrace();
          }
        }
      });
      
      execService.shutdown();
    }
    else if(perintah.equals("bukaFile")) {
      String pathAbsolut = (String)param[0];
      
      Berkas berkas = new Berkas(ui, pathAbsolut);
      
      if(berkas.getObjekFile().getName().endsWith(".png") ||
         berkas.getObjekFile().getName().endsWith(".PNG") ||
         berkas.getObjekFile().getName().endsWith(".jpg") ||
         berkas.getObjekFile().getName().endsWith(".JPG") ||
         berkas.getObjekFile().getName().endsWith(".jpeg")) {
        
        berkas.bukaFileGambar();
        
        WajahTerdeteksi.tampilkanCirclePadaJS(ui);
        WajahTerdeteksi.tampilkanInfoWTPadaJS(ui);
        WajahTerdeteksi.ubahTeksWTPadaJS(ui, "Mencari wajah...");
        WajahTerdeteksi.hapusSemuaPadaJS(ui);
        
        Berkas.bukaPanelGambarPadaJS(ui);
        
        servDeteksi.execute(new Runnable() {
          @Override
          public void run() {
            try {
              if(gambarSebelumnya != null && gambarSebelumnya.getObjekFile().exists()) {
                gambarSebelumnya.getObjekFile().delete();
              }
              
              Berkas folderWajah = new Berkas(ui,
                        berkas.getObjekFile().getParent() + "/.wajah_" +
                                berkas.getObjekFile().getName());
              
              if(!folderWajah.getObjekFile().exists()) {
                int[][] dataWajah = FaceDetector.deteksiWajah(berkas);
                Berkas hasil = FaceDetector.prosesGambar(ui, berkas, dataWajah);
                
                gambarSebelumnya = hasil;
                
                Berkas.bukaFileGambar(ui, hasil);
                
                if(dataWajah.length == 0) {
                  WajahTerdeteksi.sembunyikanCirclePadaJS(ui);
                  WajahTerdeteksi.ubahTeksWTPadaJS(ui, "Tidak ada wajah terdeteksi");
                }
              }
              
              Berkas[] fotoWajah = folderWajah.listBerkas();

              for(int i = 0; i < fotoWajah.length; i++) {
                String namaFileWajah = fotoWajah[i].getObjekFile().getName();
                String namaWajah = namaFileWajah.substring(0, namaFileWajah.length() - 4);

                WajahTerdeteksi wajah = new WajahTerdeteksi(ui, namaWajah);
                wajah.setFotoWajah(fotoWajah[i]);
                wajah.setFotoAsal(berkas);
                wajah.buatWajahTerdeteksiPadaJS();
              }

              WajahTerdeteksi.sembunyikanCirclePadaJS(ui);
              WajahTerdeteksi.sembunyikanInfoWTPadaJS(ui);

              folderTempatWajah.add(folderWajah);
              
              System.out.println("Proses Deteksi Selesai!");
            }
            catch(IOException | InterruptedException ex) {
              ex.printStackTrace();
            }
          }
        });

      }
    }
    else if(perintah.equals("hapusGambarBertanda")) {
      if(gambarSebelumnya != null && gambarSebelumnya.getObjekFile().exists()) {
        gambarSebelumnya.getObjekFile().delete();
      }
      
      servDeteksi.shutdownNow();
      servDeteksi = Executors.newFixedThreadPool(1);
    }
    else if(perintah.equals("simpanWajahTerdeteksi")) {
      String namaWajah = (String)param[0];
      String namaWajahSebelumnya = (String)param[1];
      String pathFotoSumber = (String)param[2];
      
      Berkas berkasFotoSumber = new Berkas(ui, pathFotoSumber);
      
      try {
        DataTabelWajah data = new DataTabelWajah(namaWajah, pathFotoSumber);
        data.buatTabelJikaTidakAda();
        data.masukkanData();
        System.out.println("Wajah berhasil masuk ke tabel wajah");
        
        if(!Files.exists(Paths.get(FOLDER_WAJAH_TERSIMPAN),
                LinkOption.NOFOLLOW_LINKS)) {
          Files.createDirectory(Paths.get(FOLDER_WAJAH_TERSIMPAN));
        }
        
        String pathFolderWajah = berkasFotoSumber.getObjekFile().getParent() +
                "/.wajah_" + berkasFotoSumber.getObjekFile().getName();

        Berkas berkasWajah = new Berkas(ui, pathFolderWajah + "/" +
                namaWajahSebelumnya + ".png");
        Berkas berkasWajahRename = new Berkas(ui,
                berkasWajah.getObjekFile().getParent() + "/" + namaWajah + ".png");
        Berkas berkasWajahTujuan = new Berkas(ui,
                FOLDER_WAJAH_TERSIMPAN + "/" + namaWajah + ".png");
        
        // rename berkas
        Files.move(Paths.get(berkasWajah.getObjekFile().getPath()),
                   Paths.get(berkasWajahRename.getObjekFile().getPath()),
                   StandardCopyOption.REPLACE_EXISTING);
        
        Files.copy(Paths.get(berkasWajahRename.getObjekFile().getPath()),
                    Paths.get(berkasWajahTujuan.getObjekFile().getPath()),
                    StandardCopyOption.REPLACE_EXISTING);
        
        ui.eksekusiJavascript("WajahTerdeteksi"+
                ".dapatkanWajahTerpilih().ubahNama(\""+namaWajah+"\");");
        ui.eksekusiJavascript("WajahTerdeteksi.sembunyikanPopover();");
        
        tampilkanSemuaWajah(data);
      }
      catch(Exception ex) {
        ex.printStackTrace();
      }
    }
    else if(perintah.equals("ubahNamaWajah")) {
      String namaLama = (String)param[0];
      String namaBaru = (String)param[1];
      
      Berkas berkasWajah = new Berkas(ui,
              FOLDER_WAJAH_TERSIMPAN + "/" + namaLama + ".png");
      Berkas berkasBaru = new Berkas(ui,
              FOLDER_WAJAH_TERSIMPAN + "/" + namaBaru + ".png");
      
      try {
        DataTabelWajah data = new DataTabelWajah();
        data.buatTabelJikaTidakAda();
        data.setNamaWajah(namaBaru);
        data.setPathFoto(data.dapatkanData(namaLama).getPathFoto());
        data.updateData(namaLama);
        
        DataTabelWajah[] semuaData = data.dapatkanSemuaData();
        for(int i = 0; i < semuaData.length; i++) {
          System.out.println("nama wajah : " + semuaData[i].getNamaWajah());
          System.out.println("path foto : " + semuaData[i].getPathFoto());
        }
        
        Files.move(Paths.get(berkasWajah.getObjekFile().getPath()),
                  Paths.get(berkasBaru.getObjekFile().getPath()),
                  StandardCopyOption.REPLACE_EXISTING);
      }
      catch(Exception ex) {
        ex.printStackTrace();
      }
    }
    else if(perintah.equals("hapusWajah")) {
      String namaWajah = (String)param[0];
      
      try {
        Berkas wajah = new Berkas(ui,
              FOLDER_WAJAH_TERSIMPAN + "/" + namaWajah + ".png");
        Files.delete(wajah.getObjekFile().toPath());
        
        DataTabelWajah data = new DataTabelWajah();
        data.buatTabelJikaTidakAda();
        data.hapusData(namaWajah);
        
        DataTabelWajah[] semuaData = data.dapatkanSemuaData();
        for(int i = 0; i < semuaData.length; i++) {
          System.out.println("nama wajah : " + semuaData[i].getNamaWajah());
          System.out.println("path foto : " + semuaData[i].getPathFoto());
        }
      }
      catch(Exception ex) {
        ex.printStackTrace();
      }
    }
    else if(perintah.equals("masukkanKeFGL")) {
      for(int i = 0; i < param.length; i++) {
        String pathAbsolut = (String)param[i];
        
        Berkas berkas = new Berkas(ui, pathAbsolut);
        
        try {
          DataTabelFGL data = new DataTabelFGL(
                  berkas.getObjekFile().getName(),
                  berkas.getObjekFile().getAbsolutePath());
          data.buatTabelJikaTidakAda();
          data.masukkanData();
          
          new AccordFGL(ui, berkas).buatElemenPadaJS();
          
          data.tutupKoneksi();
        }
        catch(Exception ex) {
          ex.printStackTrace();
        }
      }
    }
    else if(perintah.equals("hapusFGL")) {
      String pathAbsolut = (String)param[0];
        
      Berkas berkas = new Berkas(ui, pathAbsolut);

      try {
        DataTabelFGL data = new DataTabelFGL(
                berkas.getObjekFile().getName(),
                berkas.getObjekFile().getAbsolutePath());
        data.buatTabelJikaTidakAda();
        data.hapusData(berkas.getObjekFile().getName());

        AccordFGL.hapusPadaJS(ui, berkas.getObjekFile().getName());

        data.tutupKoneksi();
      }
      catch(Exception ex) {
        ex.printStackTrace();
      }
    }
    else if(perintah.equals("masukkanKePintasan")) {
      for(int i = 0; i < param.length; i++) {
        String pathAbsolut = (String)param[i];
        
        Berkas berkas = new Berkas(ui, pathAbsolut);
        
        try {
          DataTabelPintasan data = new DataTabelPintasan(
                  berkas.getObjekFile().getName(),
                  berkas.getObjekFile().getAbsolutePath());
          data.buatTabelJikaTidakAda();
          data.masukkanData();
          
          new AccordPintasan(ui, berkas).buatElemenPadaJS();
          buatPintasan(berkas.getObjekFile().getName(),
                  berkas.getObjekFile().getAbsolutePath());
          
          data.tutupKoneksi();
        }
        catch(Exception ex) {
          ex.printStackTrace();
        }
      }
    }
    else if(perintah.equals("hapusPintasan")) {
      String pathAbsolut = (String)param[0];
        
      Berkas berkas = new Berkas(ui, pathAbsolut);

      try {
        DataTabelPintasan data = new DataTabelPintasan(
                berkas.getObjekFile().getName(),
                berkas.getObjekFile().getAbsolutePath());
        data.buatTabelJikaTidakAda();
        data.hapusData(berkas.getObjekFile().getName());

        AccordPintasan.hapusPadaJS(ui, berkas.getObjekFile().getName());
        hapusPintasan(berkas.getObjekFile().getName());

        data.tutupKoneksi();
      }
      catch(Exception ex) {
        ex.printStackTrace();
      }
    }
    else if(perintah.equals("bukaMelaluiPintasan")) {
      String pathAbsolut = (String)param[0];
      
      Berkas berkas = new Berkas(ui, pathAbsolut);
      
      if(berkas.berkasTersedia()) {
        Berkas.tampilkanCirclePadaJS(ui);
        nav.majuKe(berkas).tampilkanListBerkas();
        bcBerkas.isiDariPath(nav.getBerkasTerpilih().pecahPathAbsolut(), ui);
        Berkas.sembunyikanCirclePadaJS(ui);
        
        Berkas.setTextPathPadaJS(ui, nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
        System.out.println(nav.getBerkasTerpilih().getObjekFile().getAbsolutePath());
      }
      else {
        JOptionPane.showMessageDialog(this,
            "Pintasan tidak valid!\n" +
            "Pastikan pintasan mengarah pada folder yang tersedia",
            "Terjadi Kesalahan!",
            JOptionPane.ERROR_MESSAGE);
      }
    }
    else if(perintah.equals("cariFotoBerdasarWajah")) {
      String nama = (String)param[0];
      String pathWajah = (String)param[1];
      Berkas berkasWajah = new Berkas(ui, pathWajah);
      
      Berkas.hapusSemuaBerkasPadaJS(ui);
      Berkas.tampilkanFloatingCirclePadaJS(ui);
      Berkas.ubahTeksCircle(ui, "Mencari " + nama + "...");
      Berkas.tampilkanTeksInfo(ui);
      
      berhentiMencariWajah = false;
      
      ExecutorService execService = Executors.newCachedThreadPool();
      execService.execute(new Runnable() {
        @Override
        public void run() {
          try {
            DataTabelFGL data = new DataTabelFGL();
            data.buatTabelJikaTidakAda();

            DataTabelFGL[] semuaData = data.dapatkanSemuaData();

            // cari di daftar folder gambar lain
            for(int i = 0; i < semuaData.length; i++) {
              Berkas tempatCari = new Berkas(ui, semuaData[i].getPath());

              VisitorFaceRecog visitor = new VisitorFaceRecog(ui, berkasWajah);
              Files.walkFileTree(tempatCari.getObjekFile().toPath(), visitor);
            }
            
            data.tutupKoneksi();
            
//            // cari di folder Pictures
//            String folderPict = System.getProperty("user.home") + "/Pictures";
//            Berkas tempatCari = new Berkas(ui, folderPict);
//            VisitorFaceRecog visitor = new VisitorFaceRecog(ui, berkasWajah);
//            Files.walkFileTree(tempatCari.getObjekFile().toPath(), visitor);
//            
//            // terakhir, cari dari root
//            Berkas root = new Berkas(ui, "/");
//            visitor = new VisitorFaceRecog(ui, berkasWajah);
//            Files.walkFileTree(root.getObjekFile().toPath(), visitor);
            
            Berkas.resetTeksCircle(ui);
            Berkas.sembunyikanTeksInfo(ui);
            Berkas.sembunyikanFloatingCirclePadaJS(ui);
            
            berhentiMencariWajah = true;
          }
          catch (Exception ex){
            ex.printStackTrace();
          }
        }
      });

      execService.shutdown();
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
  
  public void tampilkanSemuaWajah(DataTabelWajah data) throws Exception {
    KotakWajah.hapusSemuaPadaJS(ui);
      
    DataTabelWajah[] semuaData = data.dapatkanSemuaData();
    for(int i = 0; i < semuaData.length; i++) {
      KotakWajah wajah = new KotakWajah(ui, semuaData[i].getNamaWajah());
      wajah.setFotoWajah(new Berkas(ui,
              FOLDER_WAJAH_TERSIMPAN + "/" + semuaData[i].getNamaWajah() + ".png"));
      wajah.setFotoAsal(new Berkas(ui, semuaData[i].getPathFoto()));
      wajah.buatKotakWajahPadaJS();
    }
  }
  
  public void tampilkanSemuaFGL(DataTabelFGL data) throws Exception {
    AccordFGL.hapusSemuaPadaJS(ui);
    
    DataTabelFGL[] semuaData = data.dapatkanSemuaData();
    for(int i = 0; i < semuaData.length; i++) {
      new AccordFGL(ui, new Berkas(ui, semuaData[i].getPath()))
              .buatElemenPadaJS();
    }
  }
  
  public void tampilkanSemuaPintasan(DataTabelPintasan data) throws Exception {
    AccordPintasan.hapusSemuaPadaJS(ui);
    hapusSemuaPintasan();
    
    DataTabelPintasan[] semuaData = data.dapatkanSemuaData();
    for(int i = 0; i < semuaData.length; i++) {
      new AccordPintasan(ui, new Berkas(ui, semuaData[i].getPath()))
              .buatElemenPadaJS();
      buatPintasan(semuaData[i].getNamaFolder(), semuaData[i].getPath());
    }
  }
  
  public void urutkanBerdasarCTime(String mode, Berkas berkasTempat) throws Exception {
    Berkas[] dataBerkas = null;
            
    if(mode.equals("past_to_present")) {
      dataBerkas = berkasTempat.urutkan(Berkas.BERDASAR_TGL_DIBUAT_PAST_TO_PRESENT, "");
    }
    else if(mode.equals("present_to_past")) {
      dataBerkas = berkasTempat.urutkan(Berkas.BERDASAR_TGL_DIBUAT_PRESENT_TO_PAST, "");
    }

    for(int i = 0; i < dataBerkas.length; i++) {
      if(dataBerkas[i].getObjekFile().getName().endsWith(".jpg") ||
          dataBerkas[i].getObjekFile().getName().endsWith(".JPG") ||
          dataBerkas[i].getObjekFile().getName().endsWith(".png") ||
          dataBerkas[i].getObjekFile().getName().endsWith(".PNG") ||
          dataBerkas[i].getObjekFile().getName().endsWith(".jpeg")) {

          dataBerkas[i].setPakeThumbnail(true);
      }

      dataBerkas[i].buatBerkasPadaJS();
    }

    Berkas.sembunyikanCirclePadaJS(ui);
  }
  
  public void buatPintasan(String namaFolder, String path) {
    String pint =
    "new NavPintasan()"
            + ".setNamaFolder('"+namaFolder+"')"
            + ".setPath('"+path+"')"
            + ".pasangElemen($('#panelBookmark'));";
    ui.eksekusiJavascript(pint);
  }
  
  public void hapusPintasan(String namaFolder) {
    String pint =
    "NavPintasan.hapusPintasan('"+namaFolder+"');";
    ui.eksekusiJavascript(pint);
  }
  
  public void hapusSemuaPintasan() {
    String pint =
    "NavPintasan.hapusSemuaPintasan();";
    ui.eksekusiJavascript(pint);
  }

  @Override
  public void windowGainedFocus(WindowEvent e) {
  }

  @Override
  public void windowLostFocus(WindowEvent e) {
    
  }

  @Override
  public void windowOpened(WindowEvent e) {
  }

  @Override
  public void windowClosing(WindowEvent e) {
    if(servDeteksi != null) {
      servDeteksi.shutdownNow();
    }
    
    if(servCariTglDibuat != null) {
      servCariTglDibuat.shutdownNow();
    }
    
    if(gambarSebelumnya != null && gambarSebelumnya.getObjekFile().exists()) {
      gambarSebelumnya.getObjekFile().delete();
    }
  }

  @Override
  public void windowClosed(WindowEvent e) {
  }

  @Override
  public void windowIconified(WindowEvent e) {
  }

  @Override
  public void windowDeiconified(WindowEvent e) {
  }

  @Override
  public void windowActivated(WindowEvent e) {
  }

  @Override
  public void windowDeactivated(WindowEvent e) {
  }
  
}
