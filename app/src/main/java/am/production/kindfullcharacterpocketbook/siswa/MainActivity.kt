package am.production.kindfullcharacterpocketbook.siswa

import am.production.kindfullcharacterpocketbook.R
import am.production.kindfullcharacterpocketbook.auth.LoginActivity
import am.production.kindfullcharacterpocketbook.databinding.ActivityMainBinding
import am.production.kindfullcharacterpocketbook.model.post.profile.siswa.SiswaProfilePost
import am.production.kindfullcharacterpocketbook.network.ApiService
import am.production.kindfullcharacterpocketbook.utils.Helper.Companion.getCurrentDateTime
import am.production.kindfullcharacterpocketbook.utils.LocalService
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding

    val service = ApiService.makeRetrofitService()

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()

        mBinding.loadingView.visibility = View.VISIBLE

        CoroutineScope(Dispatchers.IO).launch {

            val date = getCurrentDateTime()
            val dateInString = date.toString("yyyy-MM-dd")

            val post = SiswaProfilePost(dateInString)
            val request = service.getSiswaProfile(LocalService.getSiswa().id.toString(),post)

            withContext(Dispatchers.Main){
                val result = request.await()

                if (result.isSuccessful){

                    mBinding.loadingView.visibility = View.GONE

                    val data = result.body()?.data
                    mBinding.tvNamaSiswa.text = data?.nama
                    mBinding.tvNisnSiswa.text = data?.nisn.toString()
                    mBinding.tvSekolahSiswa.text = data?.sekolah
                    mBinding.tvKelasSiswa.text = "Kelas " + data?.tingkat + " " + data?.kelas
                    mBinding.tvCodeSiswa.text = data?.kode.toString()
                    mBinding.pbPersenanSiswa.max = data?.jumlah_indikator!!
                    mBinding.pbPersenanSiswa.progress = data.jumlah_laporan
                } else {

                    mBinding.loadingView.visibility = View.GONE

                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView<ActivityMainBinding>(
            this, R.layout.activity_main
        )

        mBinding.btnMainGoKategori.setOnClickListener {
            goKategori()
        }

        mBinding.btnKeluar.setOnClickListener {
            goLogin()
            finishAffinity()
            LocalService.deleteSiswa()
            LocalService.deleteRole()
        }
    }

    private fun goKategori() {
        startActivity(Intent(this, KategoriSiswaActivity::class.java))
    }

    private fun goLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }
}
