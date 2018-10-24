
import face_recognition as fr
import sys

if len(sys.argv) > 2:
    gambarWajahDikenal = fr.load_image_file(sys.argv[1])
    wajahDikenal = fr.face_encodings(gambarWajahDikenal)[0]

    gambarLain = fr.load_image_file(sys.argv[2])
    wajahTidakDikenal = fr.face_encodings(gambarLain)

    status = "TIDAK ADA WAJAH DIKENAL"

    for wajah in wajahTidakDikenal:
        hasil = fr.compare_faces([wajahDikenal], wajah)

        if hasil[0] == True:
            status = "ADA WAJAH DIKENAL"
            print(status)
            break

    if status == "TIDAK ADA WAJAH DIKENAL":
        print(status)
else:
    print("mohon masukkan gambar pada argumen program!")
