
package filemanager;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 *
 * @author Yoga Budi Yulianto
 */
public class FaceComparator {
  
  public static final String WAJAH_DIKENAL = "wajahdikenal";
  public static final String WAJAH_TIDAK_DIKENAL = "wajahtidakdikenal";
  
  public static String bandingkanWajah(Berkas wajahDikenal, Berkas foto)
          throws Exception {
    
    String hasil = WAJAH_TIDAK_DIKENAL;
    
    ProcessBuilder pb = new ProcessBuilder(
            "python3",
            "src/filemanager/tools/facerecog.py",
            wajahDikenal.getObjekFile().getAbsolutePath(),
            foto.getObjekFile().getAbsolutePath());
    pb.redirectErrorStream(true);
    Process proc = pb.start();
    
    BufferedReader buffer = new BufferedReader(
            new InputStreamReader(proc.getInputStream()));
    
    String sebarisOutput;
    while((sebarisOutput = buffer.readLine()) != null) {
      if(sebarisOutput.equals("ADA WAJAH DIKENAL")) {
        hasil = WAJAH_DIKENAL;
      }
    }
    
    return hasil;
  }
}
