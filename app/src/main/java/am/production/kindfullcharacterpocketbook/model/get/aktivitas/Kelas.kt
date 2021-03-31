package am.production.kindfullcharacterpocketbook.model.get.aktivitas

data class Kelas(
    val created_at: String,
    val id: Int,
    val jumlah_siswa: Int,
    val kategori: List<Kategori>,
    val nama: String,
    val sekolah_id: Int,
    val tingkat: String,
    val updated_at: String
)