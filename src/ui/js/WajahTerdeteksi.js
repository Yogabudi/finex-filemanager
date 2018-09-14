
class WajahTerdeteksi extends KotakWajah {
  
  constructor() {
    super();
    
    this.ubahNama = function(nama) {
      $("div[id-wajah-terdeteksi='"+this.getNama()+"'] button span").text(nama);
      this.setNama(nama);
    };
    
    this.ubahFoto = function(foto) {
      $("div[id-wajah-terdeteksi='"+this.getNama()+"'] button img").attr("src", foto);
      this.setFoto(foto);
    };
    
    this.pasangElemen = function(elemenTempat) {
      var elemen = ""+
      "<div id-wajah-terdeteksi=\""+this.getNama()+"\" class=\"cont-wajah-terdeteksi\">"+
        "<button class=\"btn-flat waves-effect waves-light tombol-wajah-terdeteksi\">"+
          "<img src=\""+this.getFoto()+"\" width=\"150\" height=\"150\" />"+
          "<span>"+this.getNama()+"</span>"+
        "</button>"+
        "<div id=\"pop_"+this.getNama()+"\" class=\"webui-popover-content\">"+
          "<div class=\"row\">"+
            "<div class=\"col s12\">"+
              "<div class=\"input-field\">"+
                "<input type=\"text\" placeholder=\"Masukkan nama\" autofocus class=\"validate\">"+
              "</div>"+
              "<button class=\"btn waves-effect waves-light tombol-fluid\">"+
                "ok"+
              "</button>"+
            "</div>"+
          "</div>"+
        "</div>"+
      "</div>"+
      "<br class='br-tombol-terdeteksi'><br class='br-tombol-terdeteksi'>";
      
      elemenTempat.append(elemen);
      
      $("div[id-wajah-terdeteksi='"+this.getNama()+"'] .tombol-wajah-terdeteksi")
              .webuiPopover({
        container: "#panelGambar",
        title: "Beri Nama Wajah",
        width: 300,
        animation: "pop",
        url: "div[id-wajah-terdeteksi='"+this.getNama()+"'] .webui-popover-content",
        closeable: true,
        placement: "left"
      });
      
    };
  }
  
  static hapusSemua() {
    if($(".cont-wajah-terdeteksi").length) {
      $(".cont-wajah-terdeteksi").remove();
      $(".br-tombol-terdeteksi").remove();
    }
  }
  
  static tampilkanCircle() {
    $("#lcWajahTerdeteksi").show();
  }
  
  static sembunyikanCircle() {
    $("#lcWajahTerdeteksi").hide();
  }
  
  static tampilkanInfoWT() {
    $("#teksInfoWT").show();
  }
  
  static sembunyikanInfoWT() {
    $("#teksInfoWT").hide();
  }
  
  static ubahTeksInfoWT(teks) {
    $("#teksInfoWT h5").text(teks);
  }
};