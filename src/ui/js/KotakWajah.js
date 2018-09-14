
class KotakWajah {
  
  constructor() {
    // member variabel
    var nama = "";
    var foto = "";
    var eventKlikUbahNama = null;
    var eventKlikHapus = null;
    
    // setter & getter
    this.setNama = function(namaWajah) {
      nama = namaWajah;
    };
    
    this.setFoto = function(fotoWajah) {
      foto = fotoWajah;
    };
    
    this.setEventKlikUbahNama = function(fungsiEvent) {
      eventKlikUbahNama = fungsiEvent;
    };
    
    this.setEventKlikHapus = function(fungsiEvent) {
      eventKlikHapus = fungsiEvent;
    };
    
    this.getEventKlikUbahNama = function() {
      return eventKlikUbahNama;
    };
    
    this.getEventKlikHapus = function() {
      return eventKlikHapus;
    };
    
    this.getNama = function() {
      return nama;
    };
    
    this.getFoto = function() {
      return foto;
    };
    
    /////////////////
    
    this.ubahKeFormatParam = function() {
      var param = nama + "," + foto;
      
      return param;
    };
    
    this.pasangElemen = function(elemenTempat) {
      var id = nama.split(" ").join("");
      
      var kotakWajah =
      "<div class='col s5'>"+
        "<div id='card_"+id+"' class='card hoverable kotak-wajah'>"+
          "<div class='card-image waves-effect waves-block waves-light'>"+
            "<img class='activator' src='"+foto+"' />"+
          "</div>"+
          "<div class='card-content'>"+
            "<span class='card-title'>"+nama+"</span>"+
          "</div>"+
          "<div class='card-reveal'>"+
            "<span class='card-title'>"+
              "<span class='right card-title' style='font-weight: bolder;'>X</span>"+
              nama+
            "</span>"+
            "<div class='row'>"+
              "<div class='col s12'>"+
                "<div class='input-field'>"+
                  "<input id='inputNamaWajah_"+id+"' type='text' class='validate' autofocus>"+
                "</div>"+
              "</div>"+
              "<div class='col s12'>"+
                "<button id='btnUbahNama_"+id+"' class='btn waves-effect waves-light' style='width: 100%;'>"+
                  "ubah nama"+
                "</button>"+
                "<button id='btnHapus_"+id+"' class='btn waves-effect waves-light' style='width: 100%;'>"+
                  "hapus"+
                "</button>"+
              "</div>"+
            "</div>"+
          "</div>"+
        "</div>"+
      "</div>";
      
      elemenTempat.append(kotakWajah);
      
      $("#inputNamaWajah_" + id).val(nama);
      $("#inputNamaWajah_" + id).select();
      
      // jika card diklik maka tandai card
      $("#card_" + id).click(function() {
        $(".kotak-wajah").removeClass("kotak-terpilih");
        $(this).addClass("kotak-terpilih");
      });
      
      $("#btnUbahNama_" + id).click(function() {
        if(eventKlikUbahNama !== null) {
          eventKlikUbahNama(nama);
        }
      });
      
      $("#btnHapus_" + id).click(function() {
        if(eventKlikHapus !== null) {
          eventKlikHapus(nama);
        }
      });
    };
    
    this.tampilkanInfo = function() {
      console.log("NAMA : " + nama);
      console.log("FOTO : " + foto);
    };
  }
  
  static dapatkanWajahTerpilih() {
    var fotoTerpilih = $(".kotak-terpilih div img").attr("src");
    var namaTerpilih = $.trim($(".kotak-terpilih .card-content span").text());
    
    var wajah = new KotakWajah();
    wajah.setFoto(fotoTerpilih);
    wajah.setNama(namaTerpilih);
    
    wajah.tampilkanInfo();
    
    return wajah;
  }
};

// testing
$(document).ready(function() {
//  var punk = new KotakWajah();
//  punk.setNama("Nicholas Flamel");
//  punk.setFoto("file:///home/ini_laptop/Pictures/punk.jpg");
//  
//  punk.setEventKlikUbahNama(function(nama) {
//    console.log("HALO : " + nama);
//  });
//  
//  var punk2 = new KotakWajah();
//  punk2.setNama("Sarah Phoenix");
//  punk2.setFoto("file:///home/ini_laptop/Pictures/punk_jumping.jpg");
//  
//  punk2.setEventKlikUbahNama(function(nama) {
//    console.log("HALO : " + nama);
//  });
//  
//  punk.pasangElemen($("#tempatKotakWajah"));
//  punk2.pasangElemen($("#tempatKotakWajah"));
});