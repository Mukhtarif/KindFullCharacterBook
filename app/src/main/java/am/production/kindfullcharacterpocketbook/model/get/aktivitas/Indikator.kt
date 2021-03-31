package am.production.kindfullcharacterpocketbook.model.get.aktivitas

data class Indikator(
    val created_at: String,
    val id: Int,
    val kategori_id: Int,
    val laporan: List<Laporan>,
    val nama: String,
    val updated_at: String
)