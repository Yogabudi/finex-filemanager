
class KotakWajah {
  
  constructor() {
    // member variabel
    var nama = "";
    var foto = "";
    var fotoAsal = "";
    var eventKlikUbahNama = null;
    var eventKlikHapus = null;
    
    // setter & getter
    this.setNama = function(namaWajah) {
      nama = namaWajah;
      
      return this;
    };
    
    this.setFoto = function(fotoWajah) {
      foto = fotoWajah;
      
      return this;
    };
    
    this.setFotoAsal = function(f) {
      fotoAsal = f;
      
      return this;
    };
    
    this.getFotoAsal = function() {
      return fotoAsal;
    };
    
    this.setEventKlikUbahNama = function(fungsiEvent) {
      eventKlikUbahNama = fungsiEvent;
      
      return this;
    };
    
    this.setEventKlikHapus = function(fungsiEvent) {
      eventKlikHapus = fungsiEvent;
      
      return this;
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
    
    this.ubahNama = function(namaBaru) {
      var id = nama.split(" ").join("");
      
      $("#card_"+id).find(".nama-wajah").text(namaBaru);
      $("#card_"+id).find(".nama-wajah-didalam").text(namaBaru);
      $("#card_"+id).find("#inputNamaWajah_"+id).attr("id", namaBaru.split(" ").join(""));
      $("#card_"+id).find("#btnUbahNama_"+id).attr("id", namaBaru.split(" ").join(""));
      $("#card_"+id).find("#btnHapus_"+id).attr("id", namaBaru.split(" ").join(""));
      $("#card_"+id).attr("id", namaBaru.split(" ").join(""));
      
      nama = namaBaru;
      
      return this;
    };
    
    this.ubahFoto = function(fotoBaru) {
      var id = nama.split(" ").join("");
      $("#card_"+id+" div img").attr("src", fotoBaru);
      
      foto = fotoBaru;
      
      return this;
    };
    
    this.ubahFotoAsal = function(f) {
      var id = nama.split(" ").join("");
      $("#card_"+id+"").attr("foto-asal", f);
      fotoAsal = f;
      
      return this;
    };
    
    this.pasangElemen = function(elemenTempat) {
      var id = nama.split(" ").join("");
      
      var kotakWajah =
      "<div class='col s4'>"+
        "<div id='card_"+id+"' foto-asal=\""+fotoAsal+"\" class='card hoverable kotak-wajah'>"+
          "<div class='card-image waves-effect waves-block waves-light'>"+
            "<img class='activator' src='"+foto+"' />"+
          "</div>"+
          "<div class='card-content'>"+
            "<span class='card-title nama-wajah truncate'>"+nama+"</span>"+
            "<button id='btnCari_"+id+"' class='btn waves-effect waves-light' style='width: 100%;'>"+
              "cari foto"+
            "</button>"+
          "</div>"+
          "<div class='card-reveal'>"+
            "<span class='card-title'>"+
              "<span class='right card-title' style='font-weight: bolder;'>X</span>"+
              "<span class='nama-wajah-didalam'>"+nama+"</span>"+
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
        var id = nama.split(" ").join("");
        var namaBaru = $("#inputNamaWajah_"+id).val();
        
        $("#card_"+id).find(".nama-wajah").text(namaBaru);
        $("#card_"+id).find(".nama-wajah-didalam").text(namaBaru);
        $("#card_"+id).find("#inputNamaWajah_"+id)
                .attr("id", "inputNamaWajah_"+namaBaru.split(" ").join(""));
        $("#card_"+id).find("#btnUbahNama_"+id)
                .attr("id", "btnUbahNama_"+namaBaru.split(" ").join(""));
        $("#card_"+id).find("#btnHapus_"+id)
                .attr("id", "btnHapus_"+namaBaru.split(" ").join(""));
        $("#card_"+id).attr("id", "card_"+namaBaru.split(" ").join(""));

        sendNSCommand("ubahNamaWajah", nama, namaBaru);
        
        nama = namaBaru;
      });
      
      $("#btnHapus_" + id).click(function() {
        var id = nama.split(" ").join("");
        
        if($("#card_" + id).length) {
          $("#card_" + id).parent().remove();
        }
        
        sendNSCommand("hapusWajah", nama);
      });
      
      $("#btnCari_" + id).click(function() {
        var id = nama.split(" ").join("");
        
        $("#panelWajah").sidenav("close");
        
        sendNSCommand("cariFotoBerdasarWajah", nama, foto);
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
  
  static hapusSemua() {
    if($(".kotak-wajah").parent().length) {
      $(".kotak-wajah").parent().remove();
    }
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