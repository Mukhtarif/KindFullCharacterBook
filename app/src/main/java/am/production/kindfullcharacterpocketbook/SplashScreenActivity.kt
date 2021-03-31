package am.production.kindfullcharacterpocketbook

import am.production.kindfullcharacterpocketbook.auth.LoginActivity
import am.production.kindfullcharacterpocketbook.guru.ProfileGuruActivity
import am.production.kindfullcharacterpocketbook.siswa.MainActivity
import am.production.kindfullcharacterpocketbook.utils.Helper
import am.production.kindfullcharacterpocketbook.utils.LocalService
import am.production.kindfullcharacterpocketbook.wali.ProfileWaliMuridActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.orhanobut.hawk.Hawk

class SplashScreenActivity : AppCompatActivity() {

    override fun onStart() {
        super.onStart()
        Helper.init(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
    }

    override fun onResume() {
        super.onResume()

        Handler().postDelayed({
            if (LocalService.getRole() != ""){
                when {
                    LocalService.getRole() == "siswa" && LocalService.getSiswa().nama != "" -> goProfileSiswa()
                    LocalService.getRole() == "orangtua" && LocalService.getOrtu().nama != "" -> goProfileOrtu()
                    LocalService.getRole() == "guru" && LocalService.getGuru().nama != "" -> goProfileGuru()
                    else -> goLogin()
                }
            } else {
                goLogin()
            }
        }, 2000)
    }

    private fun goLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun goProfileSiswa() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun goProfileOrtu() {
        startActivity(Intent(this, ProfileWaliMuridActivity::class.java))
    }

    private fun goProfileGuru() {
        startActivity(Intent(this, ProfileGuruActivity::class.java))
    }
}
