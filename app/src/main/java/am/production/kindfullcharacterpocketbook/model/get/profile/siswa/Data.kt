package am.production.kindfullcharacterpocketbook.model.get.profile.siswa

data class Data(
    val jumlah_indikator: Int,
    val jumlah_laporan: Int,
    val kelas: String,
    val kode: Int,
    val nama: String,
    val nisn: String,
    val sekolah: String,
    val tingkat: String
)