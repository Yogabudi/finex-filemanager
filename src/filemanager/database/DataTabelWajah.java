
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
public class DataTabelWajah {
  
  private String strKoneksi = "jdbc:sqlite:yfileman.db";
  private String namaClassJDBC = "org.sqlite.JDBC";
  private String namaTabel = "TabelWajah";
  private Connection koneksi;
  
  private String sqlBuatTabel;
  
  private String namaWajah;
  private String pathFoto;

  public DataTabelWajah(String namaWajah, String pathFoto) {
    this.namaWajah = namaWajah;
    this.pathFoto = pathFoto;
    this.koneksi = null;
    this.sqlBuatTabel = "";
  }
  
  public DataTabelWajah() {
    this.namaWajah = this.pathFoto = this.sqlBuatTabel = "";
    this.koneksi = null;
  }

  public String getStrKoneksi() {
    return strKoneksi;
  }

  public void setStrKoneksi(String strKoneksi) {
    this.strKoneksi = strKoneksi;
  }

  public String getNamaWajah() {
    return namaWajah;
  }

  public void setNamaWajah(String namaWajah) {
    this.namaWajah = namaWajah;
  }

  public String getPathFoto() {
    return pathFoto;
  }

  public void setPathFoto(String pathFoto) {
    this.pathFoto = pathFoto;
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
                        "nama_wajah varchar(10),"+
                        "path_foto text);";
    
    Connection koneksi = getKoneksi();
    Statement stm = koneksi.createStatement();
    stm.execute(this.sqlBuatTabel);
    
    stm.close();
  }
  
  public void masukkanData() throws Exception {
    Connection koneksi = getKoneksi();
    String sql = "insert into "+namaTabel+" (nama_wajah, path_foto) values "+
                 "(?, ?);";
    
    PreparedStatement pst = koneksi.prepareStatement(sql);
    pst.setString(1, namaWajah);
    pst.setString(2, pathFoto);
    pst.executeUpdate();
    
    pst.close();
  }

  public DataTabelWajah[] dapatkanSemuaData() throws Exception {
    ArrayList<DataTabelWajah> data = new ArrayList<>();
    Connection koneksi = getKoneksi();
    
    String sql = "select * from "+namaTabel;
    Statement stm = koneksi.createStatement();
    
    ResultSet hasil = stm.executeQuery(sql);
    
    while(hasil.next()) {
      data.add(new DataTabelWajah(hasil.getString(2), hasil.getString(3)));
    }
    
    hasil.close();
    stm.close();
    
    return data.toArray(new DataTabelWajah[0]);
  }
  
  public DataTabelWajah dapatkanData(String namaWajah) throws Exception {
    DataTabelWajah data = null;
    
    Connection koneksi = getKoneksi();
    
    String sql = "select * from "+namaTabel+" where nama_wajah = ?;";
    PreparedStatement ps = koneksi.prepareStatement(sql);
    
    ps.setString(1, namaWajah);
    ResultSet rs = ps.executeQuery();
    
    while(rs.next()) {
      data = new DataTabelWajah(rs.getString(2), rs.getString(3));
    }
    
    ps.close();
    
    return data;
  }
  
  public void updateData(String kritNamaWajah) throws Exception {
    Connection koneksi = getKoneksi();
    String sql = "update "+namaTabel+
                 " set nama_wajah = ?,"+
                 "path_foto = ? "+
                 "where nama_wajah = ?;";
    
    PreparedStatement ps = koneksi.prepareStatement(sql);
    ps.setString(1, namaWajah);
    ps.setString(2, pathFoto);
    ps.setString(3, kritNamaWajah);
    ps.executeUpdate();
    
    ps.close();
  }
  
  public void hapusData(String kritNamaWajah) throws Exception {
    Connection koneksi = getKoneksi();
    String sql = "delete from "+namaTabel+ " where nama_wajah = ?;";
    
    PreparedStatement ps = koneksi.prepareStatement(sql);
    ps.setString(1, kritNamaWajah);
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
