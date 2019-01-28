
class NavPintasan {
  
  constructor() {
    // member variable
    var namaFolder;
    var path;
    
    // setter & getter
    this.getNamaFolder = function() {
      return namaFolder;
    };
    
    this.getPath = function() {
      return path;
    };
    
    this.setNamaFolder = function(n) {
      namaFolder = n;
      return this;
    };
    
    this.setPath = function(p) {
      path = p;
      return this;
    };
    
    this.pasangElemen = function(elemenTempat) {
      var nav = 
      "<li nama-folder=\""+namaFolder+"\" path=\""+path+"\" class=\"nav-pintasan\">"+
        "<a href=\"#!\" class=\"waves-effect\">"+
          "<div class=\"row iconPintasan\" "+
               "style=\"background-image: url(assets/Icons/32/107-folder-2.png)\">"+
            "<div class=\"col s6 offset-s2\">"+namaFolder+"</div>"+
          "</div>"+
        "</a>"+
      "</li>";
      
      elemenTempat.append(nav);
      
      $("li[nama-folder='"+namaFolder+"']").click(function() {
        sendNSCommand("bukaMelaluiPintasan", path);
      });
      
      return this;
    };
  }
  
  static hapusPintasan(namaFolderPintasan) {
    if($("li[nama-folder='"+namaFolderPintasan+"']").length) {
      $("li[nama-folder='"+namaFolderPintasan+"']").remove();
    }
  }
  
  static hapusSemuaPintasan() {
    if($(".nav-pintasan").length) {
      $(".nav-pintasan").remove();
    }
  }
}

$(document).ready(function () {
//  new NavPintasan()
//          .setNamaFolder("downloads")
//          .setPath("/home/ini_laptop")
//          .pasangElemen($("#panelBookmark"));
//  
//  new NavPintasan()
//          .setNamaFolder("ini_laptop")
//          .setPath("/home/ini_laptop")
//          .pasangElemen($("#panelBookmark"));
});