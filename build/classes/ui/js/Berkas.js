
class Berkas {
  
  constructor() {
    // member variabel
    var jenis = "";
    var nama = "";
    var pathAbsolut = "";
    var icon = "assets/Icons/64/101-folder-5.png";
    var lokasiIcon = "assets/Icons/";
    
    // getter & setter
    
    this.setIcon = function(iconBerkas) {
      icon = lokasiIcon + iconBerkas;
    };
    
    this.setNama = function(namaBerkas) {
      nama = namaBerkas;
    };
    
    this.setJenis = function(jenisBerkas) {
      jenis = jenisBerkas;
    };
    
    this.setPathAbsolut = function(pathAbsolutBerkas) {
      pathAbsolut = pathAbsolutBerkas;
    };
    
    this.getNama = function() {
      return nama;
    };
    
    this.getIcon = function() {
      return icon;
    };
    
    this.getJenis = function() {
      return jenis;
    };
    
    this.getPathAbsolut = function() {
      return pathAbsolut;
    };
    
    ///////////////////////
    
    this.tandai = function() {
      var id = nama.split(" ").join("");
      $("#berkas_" + id).addClass("ds-selected");
    };
    
    this.hilangkanTanda = function() {
      var id = nama.split(" ").join("");
      $("#berkas_" + id).removeClass("ds-selected");
    };
    
    this.ubahKeFormatParam = function() {
      var param = nama + "," + jenis + "," + pathAbsolut + "," + icon;
      
      return param;
    };
    
    ///////////////////////////////////////////
    //
    // event dengan body default
    
    this.eventSaatTerpilihSatu = function(elements) {
      $("#tabEdit").show();
      $("#ribbon").tabs("select", "edit");
      $("#ribbon").tabs("updateTabIndicator");

      $("#btnUbahNama").show();
      $("#btnDuplikat").show();
      $("#btnInfoBerkas").show();
      
//      Jembatan.kirimInfoBerkas(Berkas.dapatkanBerkasTerpilih());
    };
    
    this.eventSaatTerpilihBanyak = function(elements) {
      $("#tabEdit").show();
      $("#ribbon").tabs("select", "edit");
      $("#ribbon").tabs("updateTabIndicator");

      $("#btnUbahNama").hide();
      $("#btnDuplikat").hide();
      $("#btnInfoBerkas").hide();
    };
    
    this.eventSaatTidakTerpilih = function() {
      $("#ribbon").tabs("select", "berkas");
      $("#ribbon").tabs("updateTabIndicator");
      $("#tabEdit").hide();
    };
    
    ////////////////////////////////////////////////
    
    // panggil method ini setelah property yang diperlukan telah di atur
    this.pasangElemen = function(elemenTempat) {
      var id = nama.split(" ").join("");
      
      var berkas =
        "<button id='berkas_"+id+"' "+
                  "class='button button-3d button-box button-jumbo berkas' "+
                  "path-absolut='"+pathAbsolut+"' jenis='"+jenis+"'>" +
          "<span class='row'>" +
            "<span class='center col s12 icon-berkas'>" +
              "<img src='"+icon+"'/>"+
            "</span>"+
            "<span class='center col s12 nama-berkas'>"+
              nama +
            "</span>"+
          "</span>"+
        "</button>";

      elemenTempat.append(berkas);
      
      Berkas.terapkanEventBerkas(this.eventSaatTerpilihBanyak,
                          this.eventSaatTerpilihSatu,
                          this.eventSaatTidakTerpilih);
    };
    
    this.tampilkanInfo = function() {
      console.log("NAMA : " + nama);
      console.log("JENIS : " + jenis);
      console.log("PATH ABSOLUT : " + pathAbsolut);
      console.log("ICON : " + icon);
    };
  }
  
  static terapkanEventBerkas(evtSaatTerpilihBanyak, evtSaatTerpilihSatu, evtSaatTidakTerpilih) {      
    Berkas.terapkanEventBerkas.ds = new DragSelect({
      area: document.getElementById("konten"),
      selectables: document.getElementsByClassName("berkas"),
      callback: function(elements) {
        if(elements.length > 1) {
          // saat terpilih banyak
          evtSaatTerpilihBanyak(elements);
        }
        else {
          if(!$(document.activeElement).hasClass("berkas")) {
            // saat tidak terpilih (unselect)
            evtSaatTidakTerpilih();
          }
          else {
            // saat terpilih satu
            evtSaatTerpilihSatu(elements);
          }
        }
      },
      onElementSelect: function(element) {
        element.focus();
      }
    });
  };
  
  static dapatkanBerkasTerpilih() {
    var namaBerkas = $.trim($(".ds-selected span .nama-berkas").text());
    var iconBerkas = $(".ds-selected span .icon-berkas img").attr("src");
    var pathAbsolut = $(".ds-selected").attr("path-absolut");
    var jenis = $(".ds-selected").attr("jenis");
    
    var berkas = new Berkas();
    berkas.setNama(namaBerkas);
    berkas.setJenis(jenis);
    berkas.setPathAbsolut(pathAbsolut);
    berkas.setIcon(iconBerkas);
    
    return berkas;
  }
};

// testing
$(document).ready(function() {
  for(var i = 0; i < 10; i++) {
    var berkas = new Berkas();
    berkas.setNama("Ini Folder " + i);
    berkas.setPathAbsolut("/home/" + berkas.getNama());
    berkas.setJenis("folder");
    berkas.pasangElemen($(".tempatBerkas"));
  }
  
//  var berkas = new Berkas();
//  berkas.setNama("Ini Folder");
//  berkas.setPathAbsolut("/home/" + berkas.getNama());
//  berkas.setJenis("folder");
//  berkas.pasangElemen($(".tempatBerkas"));
});