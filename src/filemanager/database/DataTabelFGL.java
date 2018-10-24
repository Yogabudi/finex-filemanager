
package filemanager.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Yoga Budi Yulianto
 */
public class DataTabelFGL {
  private String strKoneksi = "jdbc:sqlite:yfileman.db";
  private String namaClassJDBC = "org.sqlite.JDBC";
  private String namaTabel = "TabelFolderGambarLain";
  private Connection koneksi;
  
  private String sqlBuatTabel;
  
  private String namaFolder;
  private String path;

  public DataTabelFGL(String namaFolder, String path) {
    this.namaFolder = namaFolder;
    this.path = path;
    this.koneksi = null;
    this.sqlBuatTabel = "";
  }
  
  public DataTabelFGL() {
    this.namaFolder = this.path = this.sqlBuatTabel = "";
    this.koneksi = null;
  }

  public String getStrKoneksi() {
    return strKoneksi;
  }

  public void setStrKoneksi(String strKoneksi) {
    this.strKoneksi = strKoneksi;
  }

  public String getNamaFolder() {
    return namaFolder;
  }

  public void setNamaFolder(String namaFolder) {
    this.namaFolder = namaFolder;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getNamaTabel() {
    return namaTabel;
  }

  public String getNamaClassJDBC() {
    return namaClassJDBC;
  }
  
  public Connection getKoneksi() throws Exception {
    if(koneksi == null) {
      Class.forName(namaClassJDBC);
      koneksi = DriverManager.getConnection(strKoneksi);
    }
    
    return koneksi;
  }
  
  public void tutupKoneksi() throws Exception {
    if(!koneksi.isClosed()) {
      koneksi.close();
    }
  }
  
  public void buatTabelJikaTidakAda() throws Exception {
    this.sqlBuatTabel = "create table if not exists "+namaTabel+
                        "(id integer primary key autoincrement,"+
                        "nama_folder_gambar text,"+
                        "path text);";
    
    Connection koneksi = getKoneksi();
    Statement stm = koneksi.createStatement();
    stm.execute(this.sqlBuatTabel);
    
    stm.close();
  }
  
  public void masukkanData() throws Exception {
    Connection koneksi = getKoneksi();
    String sql = "insert into "+namaTabel+" (nama_folder_gambar, path) values "+
                 "(?, ?);";
    
    PreparedStatement pst = koneksi.prepareStatement(sql);
    pst.setString(1, namaFolder);
    pst.setString(2, path);
    pst.executeUpdate();
    
    pst.close();
  }

  public DataTabelFGL[] dapatkanSemuaData() throws Exception {
    ArrayList<DataTabelFGL> data = new ArrayList<>();
    Connection koneksi = getKoneksi();
    
    String sql = "select * from "+namaTabel;
    Statement stm = koneksi.createStatement();
    
    ResultSet hasil = stm.executeQuery(sql);
    
    while(hasil.next()) {
      data.add(new DataTabelFGL(hasil.getString(2), hasil.getString(3)));
    }
    
    hasil.close();
    stm.close();
    
    return data.toArray(new DataTabelFGL[0]);
  }
  
  public DataTabelFGL dapatkanData(String namaFolder) throws Exception {
    DataTabelFGL data = null;
    
    Connection koneksi = getKoneksi();
    
    String sql = "select * from "+namaTabel+" where nama_folder_gambar = ?;";
    PreparedStatement ps = koneksi.prepareStatement(sql);
    
    ps.setString(1, namaFolder);
    ResultSet rs = ps.executeQuery();
    
    while(rs.next()) {
      data = new DataTabelFGL(rs.getString(2), rs.getString(3));
    }
    
    ps.close();
    
    return data;
  }
  
  public void updateData(String kritNamaFolder) throws Exception {
    Connection koneksi = getKoneksi();
    String sql = "update "+namaTabel+
                 " set nama_folder_gambar = ?,"+
                 "path = ? "+
                 "where nama_folder_gambar = ?;";
    
    PreparedStatement ps = koneksi.prepareStatement(sql);
    ps.setString(1, namaFolder);
    ps.setString(2, path);
    ps.setString(3, kritNamaFolder);
    ps.executeUpdate();
    
    ps.close();
  }
  
  public void hapusData(String kritNamaFolder) throws Exception {
    Connection koneksi = getKoneksi();
    String sql = "delete from "+namaTabel+ " where nama_folder_gambar = ?;";
    
    PreparedStatement ps = koneksi.prepareStatement(sql);
    ps.setString(1, kritNamaFolder);
    ps.executeUpdate();
    
    ps.close();
  }
  
  public void hapusSemuaData() throws Exception {
    Connection koneksi = getKoneksi();
    String sql = "delete from "+namaTabel+ ";";
    
    Statement stm = koneksi.createStatement();
    stm.executeUpdate(sql);
    
    stm.close();
  }
  
  public int getJumlahData() throws Exception {
    int jumlah = 0;
    Connection koneksi = getKoneksi();
    String sql = "select count(*) from "+namaTabel;
    
    Statement stm = koneksi.createStatement();
    ResultSet rs = stm.executeQuery(sql);
    
    while(rs.next()) {
      jumlah = rs.getInt(1);
    }
    
    rs.close();
    stm.close();
    
    return jumlah;
  }
}
