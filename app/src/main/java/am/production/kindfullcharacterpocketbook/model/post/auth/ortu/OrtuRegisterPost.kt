package am.production.kindfullcharacterpocketbook.model.post.auth.ortu

data class OrtuRegisterPost (
    val siswa_code: String,
    val nama: String,
    val email: String,
    val password: String,
    val role: String
)