
class Berkas {
  
  constructor(jenis, nama) {
    // member
    this.jenis = jenis;
    this.nama = nama;
    this.pathAbsolut = "-";
    
    // jangan diakses
    this.lokasiIcon = "assets/Icons/";
    
    if(this.jenis === "folder") {
      this.icon = this.lokasiIcon + "64/101-folder-5.png";
    }
    else if(this.jenis === "file") {
      this.icon = this.lokasiIcon + "64/093-file-17.png";
    }
  }
  
  // gunakan method ini untuk set icon
  // jangan gunakan this.icon
  setIcon(icon) {
    this.icon = this.lokasiIcon + icon;
  }
  
  tandai() {
    $("#berkas_" + this.nama).addClass("ds-selected");
  }
  
  hilangkanTanda() {
    $("#berkas_" + this.nama).removeClass("ds-selected");
  }
  
  static dapatkanBerkasTerpilih() {
    var namaBerkas = $.trim($(".ds-selected span .nama-berkas").text());
    var iconBerkas = $(".ds-selected span .icon-berkas img").attr("src");
    var pathAbsolut = $(".ds-selected").attr("path-absolut");
    var jenis = $(".ds-selected").attr("jenis");
    
    var berkas = new Berkas(jenis, namaBerkas);
    berkas.pathAbsolut = pathAbsolut;
    berkas.icon = iconBerkas;
    
    return berkas;
  }
  
  pasangElemen(elemenTempat) {
    var berkas =
      "<button id='berkas_"+this.nama+"' "+
                "class='button button-3d button-box button-jumbo berkas' "+
                "path-absolut='"+this.pathAbsolut+"' jenis='"+this.jenis+"'>" +
        "<span class='row'>" +
          "<span class='center col s12 icon-berkas'>" +
            "<img src='"+this.icon+"'/>"+
          "</span>"+
          "<span class='center col s12 nama-berkas'>"+
            this.nama +
          "</span>"+
        "</span>"+
      "</button>";
    
    elemenTempat.append(berkas);
  }
}