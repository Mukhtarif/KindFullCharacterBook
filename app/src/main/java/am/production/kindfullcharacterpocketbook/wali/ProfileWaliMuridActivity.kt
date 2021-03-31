package am.production.kindfullcharacterpocketbook.wali

import am.production.kindfullcharacterpocketbook.R
import am.production.kindfullcharacterpocketbook.auth.LoginActivity
import am.production.kindfullcharacterpocketbook.auth.LoginOrtuActivity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import am.production.kindfullcharacterpocketbook.databinding.ActivityProfileWaliMuridBinding
import am.production.kindfullcharacterpocketbook.model.post.profile.ortu.OrtuProfilePost
import am.production.kindfullcharacterpocketbook.network.ApiService
import am.production.kindfullcharacterpocketbook.utils.Helper
import am.production.kindfullcharacterpocketbook.utils.Helper.Companion.getCurrentDateTime
import am.production.kindfullcharacterpocketbook.utils.LocalService
import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class ProfileWaliMuridActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityProfileWaliMuridBinding

    val service = ApiService.makeRetrofitService()

    override fun onStart() {
        super.onStart()
        Helper.init(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_profile_wali_murid
        )

        mBinding.btnMainGoKategori.setOnClickListener {
            goKategori()
        }

        mBinding.btnKeluar.setOnClickListener {
            goLogin()
            LocalService.deleteRole()
            LocalService.deleteOrtu()
            finishAffinity()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()

        mBinding.loadingView.visibility = View.VISIBLE

        CoroutineScope(Dispatchers.IO).launch {

            val date = getCurrentDateTime()
            val dateInString = date.toString("yyyy-MM-dd")

            val post = OrtuProfilePost(dateInString)

            val request = service.getOrtuProfile(LocalService.getOrtu().id.toString(), post)

            withContext(Dispatchers.Main) {
                val result = request.await()

                if (result.isSuccessful) {
                    mBinding.loadingView.visibility = View.GONE

                    val data = result.body()?.data
                    mBinding.tvNamaOrtu.text = data?.ortu_nama
                    mBinding.tvNamaAnak.text = data?.siswa_nama
                    mBinding.tvSekolahAnak.text = data?.siswa_sekolah
                    mBinding.tvKelasAnak.text = data?.siswa_tingkat + " " + data?.siswa_kelas
                    mBinding.pbPersenanAnak.max = data?.jumlah_indikator!!
                    mBinding.pbPersenanAnak.progress = data.jumlah_laporan
                }
                else {
                    mBinding.loadingView.visibility = View.GONE

                    Helper.showError(this@ProfileWaliMuridActivity, result.message())
                }
            }
        }

    }

    private fun goKategori() {
        startActivity(Intent(this, KategoriWaliMuridActivity::class.java))
    }

    private fun goLogin() {
        startActivity(Intent(this, LoginOrtuActivity::class.java))
    }

    private fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }
}
