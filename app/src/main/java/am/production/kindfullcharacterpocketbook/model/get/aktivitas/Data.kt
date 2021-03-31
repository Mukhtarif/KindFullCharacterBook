package am.production.kindfullcharacterpocketbook.model.get.aktivitas

data class Data(
    val created_at: String,
    val id: Int,
    val kelas: Kelas,
    val nama: String,
    val nisn: Long,
    val siswa_code: Int,
    val updated_at: String
)