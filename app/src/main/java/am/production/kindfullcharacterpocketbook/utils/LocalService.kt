package am.production.kindfullcharacterpocketbook.utils

import am.production.kindfullcharacterpocketbook.model.get.auth.guru.Data
import am.production.kindfullcharacterpocketbook.model.get.auth.guru.GuruModel
import am.production.kindfullcharacterpocketbook.model.get.auth.orangtua.OrangTuaModel
import am.production.kindfullcharacterpocketbook.model.get.auth.siswa.SiswaModel
import com.orhanobut.hawk.Hawk

class LocalService {

    companion object {

        private const val USER : String = "Key.User"
        private const val ORTU : String = "Key.Ortu"
        private const val GURU : String = "Key.Guru"
        private const val ROLE : String = "key.Role"
        private const val AKTIVITAS : String = "Key.Aktivitas"
        private const val SAVED_INDICATOR : String = "Key.SavedIndicator"

        fun saveIndicator(){
            deleteIndicator()
        }

        fun deleteIndicator() {

        }

        fun saveGuru(guruModel: am.production.kindfullcharacterpocketbook.model.get.auth.guru.Data){
            deleteSiswa()
            deleteOrtu()
            deleteGuru()
            saveRole("Guru")
            Hawk.put(GURU,guruModel)
        }

        fun deleteGuru() {
            Hawk.delete(GURU)
        }

        fun getGuru() : am.production.kindfullcharacterpocketbook.model.get.auth.guru.Data{
            val data = am.production.kindfullcharacterpocketbook.model.get.auth.guru.Data(0,"",0,"","","")
            return Hawk.get(GURU,data)
        }

        fun saveSiswa(siswaModel: am.production.kindfullcharacterpocketbook.model.get.auth.siswa.Data){
            deleteSiswa()
            deleteOrtu()
            deleteGuru()
            saveRole("Siswa")
            Hawk.put(USER,siswaModel)
        }

        fun saveDataAktivitasSiswa(aktivitasModel: List<am.production.kindfullcharacterpocketbook.model.get.aktivitas.Data>) {
            Hawk.put(AKTIVITAS, aktivitasModel)
        }

        fun getDataAktivitasSiswa() : List<am.production.kindfullcharacterpocketbook.model.get.aktivitas.Data> {
            return Hawk.get(AKTIVITAS)
        }

        fun saveOrtu(orangTuaModel: am.production.kindfullcharacterpocketbook.model.get.auth.orangtua.Data) {
            deleteSiswa()
            deleteOrtu()
            deleteGuru()
            saveRole("Ortu")
            Hawk.put(ORTU, orangTuaModel)
        }

        fun deleteSiswa() {
            Hawk.delete(USER)
        }

        fun deleteOrtu() {
            Hawk.delete(ORTU)
        }

        fun getSiswa() : am.production.kindfullcharacterpocketbook.model.get.auth.siswa.Data {
            val data = am.production.kindfullcharacterpocketbook.model.get.auth.siswa.Data("",0,"","",0,"")
            return Hawk.get(USER,data)
        }

        fun getOrtu() : am.production.kindfullcharacterpocketbook.model.get.auth.orangtua.Data {
            val data = am.production.kindfullcharacterpocketbook.model.get.auth.orangtua.Data("",0,"",0,"",0)
            return Hawk.get(ORTU,data)
        }

        fun saveRole(role : String){
            deleteRole()
            Hawk.put(ROLE,role)
        }

        fun deleteRole(){
            Hawk.delete(ROLE)
        }

        fun getRole() : String{
            return Hawk.get(ROLE,"")
        }
    }
}