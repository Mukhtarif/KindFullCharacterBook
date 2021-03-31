package am.production.kindfullcharacterpocketbook.model.get.indikator.orangtua

data class Laporan(
    val check_guru: Int,
    val check_ortu: Int,
    val created_at: String,
    val id: Int,
    val indikator_id: Int,
    val siswa_id: Int,
    val updated_at: String
)