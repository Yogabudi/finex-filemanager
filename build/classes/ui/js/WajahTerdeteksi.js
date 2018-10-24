
class WajahTerdeteksi extends KotakWajah {
  
  constructor() {
    super();
    
    var fotoSumber = "";
    
    this.getFotoSumber = function() {
      return fotoSumber;
    };
    
    this.setFotoSumber = function(f) {
      fotoSumber = f;
      
      return this;
    };
    
    this.ubahFotoSumber = function(f) {
      $("div[id-wajah-terdeteksi='"+this.getNama()+"']")
              .find("button[foto-sumber='"+fotoSumber+"']")
              .attr("foto-sumber", f);
      $("div[id-wajah-terdeteksi='"+this.getNama()+"']").attr("foto-sumber", f);
      fotoSumber = f;
    };
    
    this.ubahNama = function(nama) {
      $("div[id-wajah-terdeteksi='"+this.getNama()+"']")
              .find(".tombol-ok")
              .attr("nama-wajah", nama);
      $("div[id-wajah-terdeteksi='"+this.getNama()+"'] button span").text(nama);
      $("input[id-wajah-terdeteksi='"+this.getNama()+"']").attr("id-wajah-terdeteksi", nama);
      $("div[id-wajah-terdeteksi='"+this.getNama()+"']").attr("id-wajah-terdeteksi", nama);
      this.setNama(nama);
      
      return this;
    };
    
    this.ubahFoto = function(foto) {
      $("div[id-wajah-terdeteksi='"+this.getNama()+"']")
              .find(".tombol-ok")
              .attr("path-foto", foto);
      $("div[id-wajah-terdeteksi='"+this.getNama()+"'] button img").attr("src", foto);
      this.setFoto(foto);
      
      return this;
    };
    
    this.pasangElemen = function(elemenTempat) {
      var elemen = ""+
      "<div id-wajah-terdeteksi=\""+this.getNama()+"\" "+
            "foto-sumber=\""+fotoSumber+"\" "+
            "class=\"cont-wajah-terdeteksi\">"+
        "<button class=\"btn-flat waves-effect waves-light tombol-wajah-terdeteksi\">"+
          "<img src=\""+this.getFoto()+"\" width=\"150\" height=\"150\" />"+
          "<span>"+this.getNama()+"</span>"+
        "</button>"+
        "<div class=\"webui-popover-content\">"+
          "<div class=\"row\">"+
            "<div class=\"col s12\">"+
              "<div class=\"input-field\">"+
                "<input id-wajah-terdeteksi=\""+this.getNama()+"\" "+
                        "type=\"text\" "+
                        "placeholder=\"Masukkan nama\" "+
                        "autofocus "+
                        "class=\"tx-nama\">"+
              "</div>"+
              "<button nama-wajah=\""+this.getNama()+"\" "+
                      "path-foto=\""+this.getFoto()+"\" "+
                      "foto-sumber=\""+fotoSumber+"\" "+
                      "class=\"btn waves-effect waves-light tombol-fluid tombol-ok\">"+
                "ok"+
              "</button>"+
            "</div>"+
          "</div>"+
        "</div>"+
      "</div>"+
      "<br class='br-tombol-terdeteksi'><br class='br-tombol-terdeteksi'>";
      
      elemenTempat.append(elemen);
      
      $("div[id-wajah-terdeteksi='"+this.getNama()+"'] .tombol-wajah-terdeteksi").webuiPopover({
        container: "#panelGambar",
        title: "Beri Nama Wajah",
        width: 300,
        animation: "pop",
        url: "div[id-wajah-terdeteksi='"+this.getNama()+"'] .webui-popover-content",
        closeable: true,
        placement: "left"
      });
      
      $("div[id-wajah-terdeteksi='"+this.getNama()+"'] .tombol-wajah-terdeteksi")
              .click(function(e) {
        $(".cont-wajah-terdeteksi").removeClass("wajah-terpilih");
        $(this).parent().addClass("wajah-terpilih");
      });
      
      $("div[id-wajah-terdeteksi='"+this.getNama()+"']")
              .find(".tombol-ok").click(function(e) {
        var namaWajahSebelumnya = $(this).attr("nama-wajah");
        var pathFotoSumber = $(this).attr("foto-sumber");
        var namaWajahBaru = $("input[id-wajah-terdeteksi='"+namaWajahSebelumnya+"']").val();
        
        sendNSCommand("simpanWajahTerdeteksi", namaWajahBaru, namaWajahSebelumnya,
                      pathFotoSumber);
      });
      
      $("input[id-wajah-terdeteksi='"+this.getNama()+"']").keypress(function(e) {
        if(e.which === 13) {
          var namaWajahSebelumnya = $(this).attr("id-wajah-terdeteksi");
          var pathFotoSumber = $("div[id-wajah-terdeteksi='"+namaWajahSebelumnya+"']")
                                .attr("foto-sumber");
          var namaWajahBaru = this.value;
          
          sendNSCommand("simpanWajahTerdeteksi", namaWajahBaru, namaWajahSebelumnya,
                        pathFotoSumber);
        }
      });
    };
  }
  
  static sembunyikanPopover() {
    $(".wajah-terpilih .tombol-wajah-terdeteksi").webuiPopover("hide");
  }
  
  static dapatkanWajahTerpilih() {
    var div = $(".wajah-terpilih");
    var namaWajah = div.attr("id-wajah-terdeteksi");
    var pathFotoSumber = div.attr("foto-sumber");
    var fotoWajah = div.find(".tombol-wajah-terdeteksi img").attr("src");
    
    var wajahTerpilih = new WajahTerdeteksi();
    wajahTerpilih.setNama(namaWajah);
    wajahTerpilih.setFoto(fotoWajah);
    wajahTerpilih.setFotoSumber(pathFotoSumber);
    
    return wajahTerpilih;
  }
  
  static hapusSemua() {
    if($(".cont-wajah-terdeteksi").length) {
      $(".cont-wajah-terdeteksi").remove();
      $(".br-tombol-terdeteksi").remove();
      $(".webui-popover").remove();
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