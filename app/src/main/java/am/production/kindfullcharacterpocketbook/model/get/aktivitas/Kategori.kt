package am.production.kindfullcharacterpocketbook.model.get.aktivitas

data class Kategori(
    val created_at: String,
    val id: Int,
    val indikator: List<Indikator>,
    val kelas_id: Int,
    val nama: String,
    val sekolah_id: Int,
    val tipe: Int,
    val updated_at: String
)