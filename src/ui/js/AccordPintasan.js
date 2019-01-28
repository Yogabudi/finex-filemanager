
class AccordPintasan {
  
  constructor() {
    // member variable
    var namaFolder = "";
    var path = "";
    
    // getter dan setter
    this.getPath = function() {
      return path;
    };
    
    this.setPath = function(p) {
      path = p;
      return this;
    };
    
    this.getNamaFolder = function() {
      return namaFolder;
    };
    
    this.setNamaFolder = function(n) {
      namaFolder = n;
      return this;
    };
    
    this.pasangElemen = function(elemenTempat) {
      var accord = "" +
      "<li id-apint=\""+namaFolder+"\" class=\"apint\" >"+
        "<div class=\"collapsible-header\">"+
          "<img src=\"assets/Icons/32/143-right-arrow-3.png\" class=\"icon-accord-kecil\" />"+
          "<span>"+namaFolder+"</span>"+
        "</div>"+
        "<div class=\"collapsible-body\">"+
          "<span>"+path+"</span>"+
          "<button class=\"btn waves-effect waves-light tombol-fluid\" "+
            "style=\"margin-top: 15px;\">"+
            "Hilangkan"+
          "</button>"+
        "</div>"+
      "</li>";
      
      elemenTempat.append(accord);
      
      $("li[id-apint='"+namaFolder+"'] .collapsible-body button").click(function() {
        sendNSCommand("hapusPintasan", path);
      });
      
      return this;
    };
    
    this.ubahNamaFolder = function(n) {
      $("li[id-apint='"+namaFolder+"'] .collapsible-header span").text(n);
      $("li[id-apint='"+namaFolder+"']").attr("id-apint", n);
      namaFolder = n;
      
      return this;
    };
    
    this.ubahPath = function(p) {
      $("li[id-apint='"+namaFolder+"'] .collapsible-body span").text(p);
      path = p;
      
      return this;
    };
    
    this.hapus = function() {
      if($("li[id-apint='"+namaFolder+"']").length) {
        $("li[id-apint='"+namaFolder+"']").remove();
      }
    };
    
  };
  
  static hapusSemua() {
    if($(".apint").length) {
      $(".apint").remove();
    }
  }
  
  static get(namaFolder) {
    var folder = $("li[id-apint='"+namaFolder+"'] .collapsible-header span").text();
    var path = $("li[id-apint='"+namaFolder+"'] .collapsible-body span").text();
    
    return new AccordPintasan().setNamaFolder(folder).setPath(path);
  }
}

$(document).ready(function() {
  new AccordPintasan()
      .setNamaFolder("This is picture")
      .setPath("/home/this")
      .pasangElemen($("#accordPintasan"));
      
  new AccordPintasan()
      .setNamaFolder("Punk")
      .setPath("/home/Punk")
      .pasangElemen($("#accordPintasan"));
});