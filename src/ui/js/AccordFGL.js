
class AccordFGL {
  
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
      "<li id-afgl=\""+namaFolder+"\" class=\"afgl\" >"+
        "<div class=\"collapsible-header\">"+
          "<img src=\"assets/Icons/32/106-folder-3.png\" class=\"icon-accord-kecil\" />"+
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
      
      $("li[id-afgl='"+namaFolder+"'] .collapsible-body button").click(function() {
        sendNSCommand("hapusFGL", path);
      });
      
      return this;
    };
    
    this.ubahNamaFolder = function(n) {
      $("li[id-afgl='"+namaFolder+"'] .collapsible-header span").text(n);
      $("li[id-afgl='"+namaFolder+"']").attr("id-afgl", n);
      namaFolder = n;
      
      return this;
    };
    
    this.ubahPath = function(p) {
      $("li[id-afgl='"+namaFolder+"'] .collapsible-body span").text(p);
      path = p;
      
      return this;
    };
    
    this.hapus = function() {
      if($("li[id-afgl='"+namaFolder+"']").length) {
        $("li[id-afgl='"+namaFolder+"']").remove();
      }
    };
    
  };
  
  static hapusSemua() {
    if($(".afgl").length) {
      $(".afgl").remove();
    }
  }
  
  static get(namaFolder) {
    var folder = $("li[id-afgl='"+namaFolder+"'] .collapsible-header span").text();
    var path = $("li[id-afgl='"+namaFolder+"'] .collapsible-body span").text();
    
    return new AccordFGL().setNamaFolder(folder).setPath(path);
  }
}

$(document).ready(function() {
//  new AccordFGL()
//      .setNamaFolder("This is picture")
//      .setPath("/home/this")
//      .pasangElemen($("#accordFolderGambarLain"));
//      
//  new AccordFGL()
//      .setNamaFolder("Punk")
//      .setPath("/home/Punk")
//      .pasangElemen($("#accordFolderGambarLain"));
});