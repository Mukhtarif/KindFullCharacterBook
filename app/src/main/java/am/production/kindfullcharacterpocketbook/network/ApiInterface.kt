package am.production.kindfullcharacterpocketbook.network

import am.production.kindfullcharacterpocketbook.model.get.BaseModel
import am.production.kindfullcharacterpocketbook.model.get.aktivitas.AktivitasSiswa
import am.production.kindfullcharacterpocketbook.model.get.auth.guru.GuruModel
import am.production.kindfullcharacterpocketbook.model.get.auth.orangtua.OrangTuaModel
import am.production.kindfullcharacterpocketbook.model.get.auth.siswa.SiswaModel
import am.production.kindfullcharacterpocketbook.model.get.indikator.orangtua.OrangtuaIndikator
import am.production.kindfullcharacterpocketbook.model.get.indikator.siswa.SiswaIndikator
import am.production.kindfullcharacterpocketbook.model.get.kategori.orangtua.OrangtuaKategori
import am.production.kindfullcharacterpocketbook.model.get.kategori.siswa.SiswaKategori
import am.production.kindfullcharacterpocketbook.model.get.profile.orangtua.OrangtuaProfile
import am.production.kindfullcharacterpocketbook.model.get.profile.siswa.SiswaProfile
import am.production.kindfullcharacterpocketbook.model.post.auth.guru.GuruLoginPost
import am.production.kindfullcharacterpocketbook.model.post.auth.ortu.OrtuLoginPost
import am.production.kindfullcharacterpocketbook.model.post.auth.ortu.OrtuRegisterPost
import am.production.kindfullcharacterpocketbook.model.post.auth.siswa.SiswaLoginPost
import am.production.kindfullcharacterpocketbook.model.post.guru.CekGuruPost
import am.production.kindfullcharacterpocketbook.model.post.guru.GuruAktivitasSiswaPost
import am.production.kindfullcharacterpocketbook.model.post.indikator.orangtua.OrangtuaIndikatorPost
import am.production.kindfullcharacterpocketbook.model.post.indikator.orangtua.OrangtuaLaporPost
import am.production.kindfullcharacterpocketbook.model.post.indikator.siswa.SiswaIndikatorPost
import am.production.kindfullcharacterpocketbook.model.post.indikator.siswa.SiswaLaporPost
import am.production.kindfullcharacterpocketbook.model.post.profile.ortu.OrtuProfilePost
import am.production.kindfullcharacterpocketbook.model.post.profile.siswa.SiswaProfilePost
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*
import java.util.*

interface ApiInterface {

    //API Siswa
    @POST("siswa/login")
    fun loginSiswa(@Body siswaLoginPost: SiswaLoginPost): Deferred<Response<SiswaModel>>

    @POST("siswa/{id}")
    fun getSiswaProfile(@Path("id") id: String, @Body siswaProfilePost: SiswaProfilePost) : Deferred<Response<SiswaProfile>>

    @GET("siswa/kategori/{id}")
    fun getSiswaKategori(@Path("id") id: String): Deferred<Response<SiswaKategori>>

    @POST("siswa/kategori/{id}")
    fun getSiswaKategoriIndikator(@Path("id") id: String, @Body siswaIndikatorPost: SiswaIndikatorPost): Deferred<Response<SiswaIndikator>>

    @POST("siswa/kategori/indikator/laporkan")
    fun postSiswaLaporan(@Body siswaLaporPost: SiswaLaporPost): Deferred<Response<BaseModel>>

    //API Ortu
    @POST("orangtua/login")
    fun loginOrtu(@Body ortuLoginPost: OrtuLoginPost): Deferred<Response<OrangTuaModel>>

    @POST("orangtua/register")
    fun registerOrtu(@Body ortuRegisterPost: OrtuRegisterPost): Deferred<Response<OrangTuaModel>>

    @POST("orangtua/{id}")
    fun getOrtuProfile(@Path("id") id: String, @Body ortuProfilePost: OrtuProfilePost): Deferred<Response<OrangtuaProfile>>

    @GET("orangtua/kategori/{id}")
    fun getOrtuKategori(@Path("id") id: String): Deferred<Response<OrangtuaKategori>>

    @POST("orangtua/kategori/{id}")
    fun getOrtuKategoriIndikator(
        @Path("id") id: String, @Body orangtuaIndikatorPost: OrangtuaIndikatorPost): Deferred<Response<OrangtuaIndikator>>

    @POST("orangtua/kategori/indikator/laporkan")
    fun postOrtuLaporan(@Body orangtuaLaporPost: OrangtuaLaporPost): Deferred<Response<BaseModel>>

    //API Guru

    @POST("guru/login")
    fun loginGuru(@Body guruLoginPost: GuruLoginPost): Deferred<Response<GuruModel>>

    @GET("guru/{id}")
    fun getGuruProfile(@Path("id") id: String): Deferred<Response<GuruModel>>

    @POST("guru/laporan")
    fun getGuruAktivitasSiswa(@Body guruAktivitasSiswaPost: GuruAktivitasSiswaPost) : Deferred<Response<AktivitasSiswa>>

    @POST("guru/lapor")
    fun postConfirmGuru(@Body cekGuruPost: CekGuruPost): Deferred<Response<BaseModel>>

}