package am.production.kindfullcharacterpocketbook.guru

import am.production.kindfullcharacterpocketbook.R
import am.production.kindfullcharacterpocketbook.auth.LoginActivity
import am.production.kindfullcharacterpocketbook.databinding.ActivityProfileGuruBinding
import am.production.kindfullcharacterpocketbook.utils.Helper
import am.production.kindfullcharacterpocketbook.utils.LocalService
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil

class ProfileGuruActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityProfileGuruBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Helper.init(this)

        mBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_profile_guru
        )

        mBinding.tvNamaGuru.text = LocalService.getGuru().nama
        mBinding.tvNamaSekolah.text = LocalService.getGuru().sekolah
        mBinding.tvNamaKelas.text = LocalService.getGuru().tingkat + " " + LocalService.getGuru().kelas

        mBinding.btnDailyReport.setOnClickListener {
            rekapData()
        }

//        mBinding.btnCetakRaport.setOnClickListener {
//            goCetakRaport()
//        }

        mBinding.btnKeluar.setOnClickListener {
            goLogout()
            finishAffinity()
            LocalService.deleteGuru()
            LocalService.deleteRole()
        }
    }

    private fun goLogout() {
        startActivity(Intent(this,LoginActivity::class.java))
    }


    private fun goCetakRaport() {
        startActivity(Intent(this, CetakRaportActivity::class.java))
    }

    private fun rekapData() {
        startActivity(Intent(this, DailyReportActivity::class.java))
    }
}
