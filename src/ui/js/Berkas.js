
class Berkas {
  
  constructor() {
    // member variabel
    var jenis = "";
    var nama = "";
    var pathAbsolut = "";
    var icon = "assets/Icons/64/101-folder-5.png";
    var jumlahBerkas = 0;
    var ukuranFile = 0;
    var contextMenu = new ContextMenu();
    
    ////////////////////////////////////////////
    //
    // getter & setter
    
    this.setUkuranFile = function(ukuran) {
      ukuranFile = ukuran;
    };
    
    this.getUkuranFile = function() {
      return ukuranFile;
    };
    
    this.setIcon = function(iconBerkas) {
      icon = iconBerkas;
    };
    
    this.setNama = function(namaBerkas) {
//      nama = namaBerkas.replace("#", " ")
//                        .replace("&", " ")
//                        .replace("/", " ")
//                        .replace("\\", " ")
//                        .replace("\"", " ")
//                        .replace("\'", " ")
//                        .replace("#", " ")
//                        .replace(",", " ")
//                        .replace("(", " ")
//                        .replace(")", " ");
      nama = namaBerkas;
    };
    
    this.setJenis = function(jenisBerkas) {
      jenis = jenisBerkas;
    };
    
    this.setPathAbsolut = function(pathAbsolutBerkas) {
      pathAbsolut = pathAbsolutBerkas;
    };
    
    this.setJumlahBerkas = function(jml) {
      jumlahBerkas = jml;
    };
    
    this.getJumlahBerkas = function() {
      return jumlahBerkas;
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
    
    this.getContextMenu = function() {
      return contextMenu;
    };
    
    ////////////////////////////////////
    
    this.dataContextMenuBerkas = [
      new ObjekMenu("Cut", "assets/Icons/24/scissors.png", "").buatMenu(),
      new ObjekMenu("Salin", "assets/Icons/24/papers.png", "").buatMenu(),
      new ObjekMenu("Paste kedalam folder", "assets/Icons/24/clipboard-paste-button.png", "").buatMenu(),
      new ObjekMenu("Paste disini", "assets/Icons/24/clipboard-paste-button.png", "").buatMenu(),
      new ObjekMenu("Info berkas", "assets/Icons/24/info.png", "").buatMenu(),
      new ObjekMenu("Duplikat", "assets/Icons/24/duplicate-file.png", "").buatMenu(),
      new ObjekMenu("Hapus ke trash", "assets/Icons/24/garbage.png", "").buatMenu(),
      new ObjekMenu("Hapus", "assets/Icons/24/delete.png", "").buatMenu()
    ];
    
    this.tambahkanMenu = function(labelMenu, iconMenu, evtSaatDipilih) {
      var objMenu = new ObjekMenu(labelMenu, iconMenu, evtSaatDipilih).buatMenu();
      contextMenu.masukkan(objMenu);
    };
    
    this.tandai = function() {
      $("div[id-berkas='"+nama+"'] .card-panel").addClass("ds-selected");
    };
    
    this.hilangkanTanda = function() {
      $("div[id-berkas='"+nama+"'] .card-panel").removeClass("ds-selected");
    };
    
    this.ubahKeFormatParam = function() {
      var param = nama + "," + jenis + "," + pathAbsolut + "," + icon;
      
      return param;
    };
    
    this.eventSaatDblKlik = function() {
      var berkasTerpilih = Berkas.dapatkanBerkasTerpilih();
      var pathAbsolut = berkasTerpilih.getPathAbsolut();
      
      if(berkasTerpilih.getJenis() === "folder") {
        sendNSCommand("tampilkanListBerkas", pathAbsolut);
      }
      else {
        
      }
    };
    
    // panggil method ini setelah property yang diperlukan telah di atur
    this.pasangElemen = function(elemenTempat) {      
//      var berkas =
//        "<button id-berkas='"+nama+"' "+
//                  "class='button button-3d button-box button-jumbo berkas' "+
//                  "path-absolut='"+pathAbsolut+"' jenis='"+jenis+"'>" +
//          "<span class='row'>" +
//            "<span class='center col s12 icon-berkas'>" +
//              "<img src='"+icon+"'/>"+
//            "</span>"+
//            "<span class='center col s12 nama-berkas'>"+
//              "<span class='truncate'>"+nama+"</span>" +
//            "</span>"+
//          "</span>"+
//        "</button>";
      
      var iconInfo = (jenis === "folder")
                   ? "assets/Icons/24/folder.png" : "assets/Icons/24/file.png";
      var labelInfo = (jenis === "folder")
                    ? (jumlahBerkas + " Berkas") : (ukuranFile + " KB");
      
      var berkas = ""+
      "<div id-berkas='"+nama+"' class='col cont-berkas' "+
            "path-absolut='"+pathAbsolut+"' jenis='"+jenis+"' >"+
        "<div class='card-panel white berkas'>"+
          "<div class='card-image center-align icon-berkas'>"+
            "<img src='"+icon+"'/>"+
          "</div>"+
          "<div class='divider garis-card-berkas'></div>"+
          "<div class='card-content'>"+
            "<span class='nama-berkas truncate'><b>"+nama+"</b></span>"+
            "<span class='keterangan-jumlah'>"+
              "<img src='"+iconInfo+"'/>"+
              "<span>"+labelInfo+"</span>"+
            "</span>"+
          "</div>"+
        "</div>"+
      "</div>";

      elemenTempat.append(berkas);
      
      if(contextMenu.getJumlah() > 0) {
        contextMenu.pasang("berkas");
      }
      
      $("div[id-berkas='"+nama+"']").on("dblclick", this.eventSaatDblKlik);
      
      Berkas.pasangEventSeleksi(Berkas.eventSaatTerpilihBanyak,
                                  Berkas.eventSaatTerpilihSatu,
                                  Berkas.eventSaatTidakTerpilih);
    };
    
    this.tampilkanInfo = function() {
      console.log("NAMA : " + nama);
      console.log("JENIS : " + jenis);
      console.log("PATH ABSOLUT : " + pathAbsolut);
      console.log("ICON : " + icon);
    };
  }
  
  ///////////////////////////////////////////
  //
  // Method static

  static eventSaatTerpilihSatu(elements) {
    $("#tabEdit").show();
    $("#ribbon").tabs("select", "edit");
    $("#ribbon").tabs("updateTabIndicator");

    $("#btnUbahNama").show();
    $("#btnDuplikat").show();
    $("#btnInfoBerkas").show();
  }

  static eventSaatTerpilihBanyak(elements) {
    $("#tabEdit").show();
    $("#ribbon").tabs("select", "edit");
    $("#ribbon").tabs("updateTabIndicator");

    $("#btnUbahNama").hide();
    $("#btnDuplikat").hide();
    $("#btnInfoBerkas").hide();
  }

  static eventSaatTidakTerpilih() {
    $("#ribbon").tabs("select", "berkas");
    $("#ribbon").tabs("updateTabIndicator");
    $("#tabEdit").hide();
  }
  
  static pasangEventSeleksi(evtSaatTerpilihBanyak, evtSaatTerpilihSatu, evtSaatTidakTerpilih) {
    // jika sebelumnya sudah ada elemen DragSelect, maka hapus dan buat lagi
    if($(".ds-selector").length) {
      $(".ds-selector").remove();
    }
    
    if(typeof Berkas.pasangEventSeleksi.ds === "undefined") {
      Berkas.pasangEventSeleksi.ds = null;
    }
    else {
      Berkas.pasangEventSeleksi.ds.area = null;
      Berkas.pasangEventSeleksi.ds.selectables = [];
    }
    
    Berkas.pasangEventSeleksi.ds = new DragSelect({
      area: document.getElementById("konten"),
      selectables: document.getElementsByClassName("berkas"),
      callback: function(elements) {
        if(elements.length > 1) {
          // saat terpilih banyak
          evtSaatTerpilihBanyak(elements);
        }
        else {
          if(!$(elements[0]).hasClass("berkas")) {
            // saat tidak terpilih (unselect)
            evtSaatTidakTerpilih();
          }
          else {
            // saat terpilih satu
            evtSaatTerpilihSatu(elements);
          }
        }
      }
    });
  }
  
  static dapatkanBerkasTerpilih() {
    var namaBerkas = $(".ds-selected").parent().attr("id-berkas");
    var iconBerkas = $(".ds-selected .card-image img").attr("src");
    var pathAbsolut = $(".ds-selected").parent().attr("path-absolut");
    var jenis = $(".ds-selected").parent().attr("jenis");
    
    var berkas = new Berkas();
    berkas.setNama(namaBerkas);
    berkas.setJenis(jenis);
    berkas.setPathAbsolut(pathAbsolut);
    berkas.setIcon(iconBerkas);
    
    return berkas;
  }
  
  static dapatkanBerkasBerdasarNama(nama) {
    var id = this.getId();
    
    var namaBerkas = $("div[id-berkas='"+nama+"']").attr("id-berkas");
    var iconBerkas = $("div[id-berkas='"+nama+"'] .card-panel .card-image img").attr("src");
    var pathAbsolut = $("div[id-berkas='"+nama+"']").attr("path-absolut");
    var jenis = $("div[id-berkas='"+nama+"']").attr("jenis");
    
    var berkas = new Berkas();
    berkas.setNama(namaBerkas);
    berkas.setJenis(jenis);
    berkas.setPathAbsolut(pathAbsolut);
    berkas.setIcon(iconBerkas);
    
    return berkas;
  }
  
  static hapusBerkasBerdasarNama(nama) {    
    if($("div[id-berkas='"+nama+"']").length) {
      $("div[id-berkas='"+nama+"']").remove();
    }
    
    Berkas.eventSaatTidakTerpilih();
  }
  
  static hapusSemuaBerkas() {
    var elemenBerkas = $(".tempatBerkas").children(".cont-berkas");
    
    if(elemenBerkas.length) {
      elemenBerkas.remove();
    }
    
    Berkas.eventSaatTidakTerpilih();
  }
  
  static keBerkas(elementButton) {
    var namaBerkas = $(elementButton).attr("id-berkas");
    var iconBerkas = $(elementButton).find(".card-panel .card-image img").attr("src");
    var pathAbsolut = $(elementButton).attr("path-absolut");
    var jenis = $(elementButton).attr("jenis");
    
    var berkas = new Berkas();
    berkas.setNama(namaBerkas);
    berkas.setJenis(jenis);
    berkas.setPathAbsolut(pathAbsolut);
    berkas.setIcon(iconBerkas);
    
    return berkas;
  }
  
  static dapatkanSemuaBerkas() {
    var elemenBerkas = $(".tempatBerkas").children(".cont-berkas");
    var berkas = [];
    
    for(var i = 0; i < elemenBerkas.length; i++) {
      berkas[i] = Berkas.keBerkas(elemenBerkas[i]);
    }
    
    return berkas;
  }
  
  static tampilkanLoadingCircle() {
    $("#konten").hide();
    $("#loadingCircle").show();
  }
  
  static sembunyikanLoadingCircle() {
    $("#konten").show();
    $("#loadingCircle").hide();
  }
};