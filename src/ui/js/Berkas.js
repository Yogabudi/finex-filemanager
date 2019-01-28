
class Berkas {
  
  constructor() {
    // member variabel
    var jenis = "";
    var nama = "";
    var pathAbsolut = "";
    var icon = "assets/Icons/64/101-folder-5.png";
    var jumlahBerkas = 0;
    var ukuranFile = 0;
    var tersembunyi = false;
    var pakeThumbnail = false;
    var contextMenu = new ContextMenu();
    
    ////////////////////////////////////////////
    //
    // getter & setter
    
    this.apakahPakeThumbnail = function() {
      return pakeThumbnail;
    };
    
    this.setPakeThumbnail = function(pake) {
      pakeThumbnail = pake;
    };
    
    this.apakahTersembunyi = function() {
      return tersembunyi;
    };
    
    this.setTersembunyi = function(sembunyikan) {
      tersembunyi = sembunyikan;
    };
    
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
    
    this.ubahTersembunyi = function(sembunyikan) {
      tersembunyi = sembunyikan;
      $("div[id-berkas='"+nama+"']").attr("tersembunyi", tersembunyi);
      
      if(tersembunyi) {
        $("div[id-berkas='"+nama+"'] .card-panel")
                .removeClass("white")
                .addClass("grey");
      }
      else {
        $("div[id-berkas='"+nama+"'] .card-panel")
                .removeClass("grey")
                .addClass("white");
      }
    };
    
    this.ubahNama = function(n) {
      $("div[id-berkas='"+nama+"']")
              .attr("id-berkas", n)
              .find(".nama-berkas b")
              .text(n);
      nama = n;
    };
    
    this.ubahPathAbsolut = function(p) {
      pathAbsolut = p;
      $("div[id-berkas='"+nama+"']").attr("path-absolut", pathAbsolut);
    };
        
    this.dataContextMenuBerkas = [
      new ObjekMenu("Cut", "assets/Icons/24/scissors.png", "menu_cut()").buatMenu(),
      new ObjekMenu("Salin", "assets/Icons/24/papers.png", "menu_salin()").buatMenu(),
      new ObjekMenu("Paste disini", "assets/Icons/24/clipboard-paste-button.png", "menu_paste()").buatMenu(),
      new ObjekMenu("Info berkas", "assets/Icons/24/info.png", "menu_infoBerkas()").buatMenu(),
      new ObjekMenu("Duplikat", "assets/Icons/24/duplicate-file.png", "menu_duplikat()").buatMenu(),
      new ObjekMenu("Hapus ke trash", "assets/Icons/24/garbage.png", "menu_hapusKeTrash()").buatMenu(),
      new ObjekMenu("Hapus", "assets/Icons/24/delete.png", "menu_hapusPermanen()").buatMenu()
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
    
    this.hilangkanSemuaTanda = function() {
      $(".cont-berkas .card-panel").removeClass("ds-selected");
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
      else if(berkasTerpilih.getJenis() === "file") {
        sendNSCommand("bukaFile", pathAbsolut);
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
      var classImg = (pakeThumbnail)
                    ? "gambar-thumbnail" : "card-image center-align icon-berkas";
      icon = (pakeThumbnail) ? pathAbsolut : icon;
      
      var berkas = ""+
      "<div id-berkas='"+nama+"' "+
            "class='col cont-berkas' "+
            "path-absolut='"+pathAbsolut+"' "+
            "jenis='"+jenis+"' "+
            "tersembunyi='"+tersembunyi+"' "+
            "diklik-kanan='false'>"+
        "<div class='card-panel white berkas'>"+
          "<div class='"+classImg+"'>"+
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
      
      if(tersembunyi) {
        $("div[id-berkas='"+nama+"'] .card-panel")
                .removeClass("white")
                .addClass("grey");
        
        $("div[id-berkas='"+nama+"']").hide();
      }
      
      $("#cbTampilkanTersembunyi").prop("checked", false);
      
      $("div[id-berkas='"+nama+"']").on("dblclick", this.eventSaatDblKlik);
      
      $("div[id-berkas='"+nama+"']").on("contextmenu", function(e) {
        $(".cont-berkas .card-panel").removeClass("ds-selected");
        $("div[diklik-kanan='true']").attr("diklik-kanan", "false");
        
        $(this).attr("diklik-kanan", "true")
                .children(".card-panel")
                .addClass("ds-selected");
      });
      
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
    
    if(Berkas.dapatkanBerkasTerpilih().getJenis() === "file") {
      $("#btnAddtoBookmark").hide();
      $("#btnAddtoFGL").hide();
    }
    else if(Berkas.dapatkanBerkasTerpilih().getJenis() === "folder") {
      $("#btnAddtoBookmark").show();
      $("#btnAddtoFGL").show();
    }
  }

  static eventSaatTerpilihBanyak(elements) {
    $("#tabEdit").show();
    $("#ribbon").tabs("select", "edit");
    $("#ribbon").tabs("updateTabIndicator");

    $("#btnUbahNama").hide();
    $("#btnDuplikat").hide();
    $("#btnInfoBerkas").hide();
    
    if(Berkas.dapatkanBerkasTerpilih().getJenis() === "file") {
      $("#btnAddtoBookmark").hide();
      $("#btnAddtoFGL").hide();
    }
    else if(Berkas.dapatkanBerkasTerpilih().getJenis() === "folder") {
      $("#btnAddtoBookmark").show();
      $("#btnAddtoFGL").show();
    }
  }

  static eventSaatTidakTerpilih() {
    $("#ribbon").tabs("select", kontenTabTerpilih.substring(1));
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
            $(elements[0]).removeClass("ds-selected");
            
            // saat tidak terpilih (unselect)
            evtSaatTidakTerpilih();
          }
          else {
            $(".cont-berkas").attr("diklik-kanan", "false")
                .children(".card-panel")
                .removeClass("ds-selected");
            $(elements[0]).addClass("ds-selected");
        
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
    var tersembunyi = $(".ds-selected").parent().attr("tersembunyi");
    
    var berkas = new Berkas();
    berkas.setNama(namaBerkas);
    berkas.setJenis(jenis);
    berkas.setPathAbsolut(pathAbsolut);
    berkas.setIcon(iconBerkas);
    berkas.setTersembunyi((tersembunyi === "true"));
    
    return berkas;
  }
  
  static dapatkanBanyakBerkasTerpilih() {
    var dataBerkas = [];
    
    for(var i = 0; i < $(".ds-selected").length; i++) {
      var namaBerkas = $($(".ds-selected")[i]).parent().attr("id-berkas");
      var iconBerkas = $($(".ds-selected .card-image img")[i]).attr("src");
      var pathAbsolut = $($(".ds-selected")[i]).parent().attr("path-absolut");
      var jenis = $($(".ds-selected")[i]).parent().attr("jenis");
      var tersembunyi = $($(".ds-selected")[i]).parent().attr("tersembunyi");
      
      dataBerkas[i] = new Berkas();
      dataBerkas[i].setNama(namaBerkas);
      dataBerkas[i].setJenis(jenis);
      dataBerkas[i].setPathAbsolut(pathAbsolut);
      dataBerkas[i].setIcon(iconBerkas);
      dataBerkas[i].setTersembunyi((tersembunyi === "true"));
    }
    
    return dataBerkas;
  }
  
  static dapatkanBerkasBerdasarNama(nama) {    
    var namaBerkas = $("div[id-berkas='"+nama+"']").attr("id-berkas");
    var iconBerkas = $("div[id-berkas='"+nama+"'] .card-panel .card-image img").attr("src");
    var pathAbsolut = $("div[id-berkas='"+nama+"']").attr("path-absolut");
    var jenis = $("div[id-berkas='"+nama+"']").attr("jenis");
    var tersembunyi = $("div[id-berkas='"+nama+"']").attr("tersembunyi");
    
    var berkas = new Berkas();
    berkas.setNama(namaBerkas);
    berkas.setJenis(jenis);
    berkas.setPathAbsolut(pathAbsolut);
    berkas.setIcon(iconBerkas);
    berkas.setTersembunyi((tersembunyi === "true"));
    
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
    var tersembunyi = $(elementButton).attr("tersembunyi");
    
    var berkas = new Berkas();
    berkas.setNama(namaBerkas);
    berkas.setJenis(jenis);
    berkas.setPathAbsolut(pathAbsolut);
    berkas.setIcon(iconBerkas);
    berkas.setTersembunyi((tersembunyi === "true"));
    
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
  
  static tandai(nama) {
    $("div[id-berkas='"+nama+"'] .card-panel").addClass("ds-selected");
    Berkas.eventSaatTerpilihSatu(null);
  };

  static hilangkanTanda(nama) {
    $("div[id-berkas='"+nama+"'] .card-panel").removeClass("ds-selected");
    Berkas.eventSaatTidakTerpilih();
  };

  static hilangkanSemuaTanda() {
    $(".cont-berkas .card-panel").removeClass("ds-selected");
    Berkas.eventSaatTidakTerpilih();
  }
  
  static janganTampilkanBerkasTersembunyi() {
    $("div[tersembunyi='true']").hide();
  }
  
  static tampilkanBerkasTersembunyi() {
    $("div[tersembunyi='true']").show();
  }
  
  static bukaFileGambar(pathAbsolut) {
    $("#imgPanelGambar").attr("src", pathAbsolut);
  }
  
  static bukaPanelGambar() {
    $("#panelGambar").modal("open");
  }
};