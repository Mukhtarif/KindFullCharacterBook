package am.production.kindfullcharacterpocketbook.auth

import am.production.kindfullcharacterpocketbook.R
import am.production.kindfullcharacterpocketbook.databinding.ActivityLoginBinding
import am.production.kindfullcharacterpocketbook.model.post.auth.siswa.SiswaLoginPost
import am.production.kindfullcharacterpocketbook.network.ApiInterface
import am.production.kindfullcharacterpocketbook.network.ApiService
import am.production.kindfullcharacterpocketbook.siswa.MainActivity
import am.production.kindfullcharacterpocketbook.utils.LocalService
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

class LoginActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityLoginBinding
    private lateinit var service: ApiInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_login
        )

        service = ApiService.makeRetrofitService()

        mBinding.btnLoginMasuk.setOnClickListener {
            login(mBinding.inputLoginUsername.text.toString())
        }
        mBinding.btnLoginGuru.setOnClickListener {
            goLoginGuru()
        }
        mBinding.btnLoginOrtu.setOnClickListener {
            goLoginOrtu()
        }
    }

    private fun login(nisn: String) {
        if(mBinding.inputLoginUsername.text.isNotEmpty()){
            mBinding.loadingView.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                val loginPost = SiswaLoginPost(mBinding.inputLoginUsername.text.toString())
                val request = service.loginSiswa(loginPost)

                withContext(Dispatchers.Main){
                    val result = request.await()

                    if (result.isSuccessful){

                        mBinding.loadingView.visibility = View.GONE

                        if (result.body()?.status == 200){
                            result.body()?.data?.let { it1 -> LocalService.saveSiswa(it1) }
                            LocalService.saveRole("siswa")
                            goDashboard()
                        } else {
                            Toast.makeText(this@LoginActivity, result.body()?.message, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        mBinding.loadingView.visibility = View.GONE

                        Toast.makeText(this@LoginActivity,result.body()?.message,Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            Toast.makeText(this,"Harap NISN diisi", Toast.LENGTH_SHORT).show()
        }
    }

    private fun goLoginOrtu() {
        startActivity(Intent(this, LoginOrtuActivity::class.java))
    }

    private fun goLoginGuru() {
        startActivity(Intent(this, LoginGuruActivity::class.java))
    }

    private fun goDashboard() {
        startActivity(Intent(this, MainActivity::class.java))
    }
}
