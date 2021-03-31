package am.production.kindfullcharacterpocketbook.auth

import am.production.kindfullcharacterpocketbook.R
import am.production.kindfullcharacterpocketbook.databinding.ActivityLoginGuruBinding
import am.production.kindfullcharacterpocketbook.guru.ProfileGuruActivity
import am.production.kindfullcharacterpocketbook.model.get.auth.guru.Data
import am.production.kindfullcharacterpocketbook.model.get.auth.guru.GuruModel
import am.production.kindfullcharacterpocketbook.model.post.auth.guru.GuruLoginPost
import am.production.kindfullcharacterpocketbook.network.ApiService
import am.production.kindfullcharacterpocketbook.utils.Helper
import am.production.kindfullcharacterpocketbook.utils.LocalService
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginGuruActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityLoginGuruBinding

    private val service = ApiService.makeRetrofitService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_login_guru
        )

        mBinding.btnLoginMasuk.setOnClickListener {
            if (mBinding.inputLoginUsername.text.isNotEmpty() && mBinding.inputLoginPassword.text.isNotEmpty()){

                mBinding.loadingView.visibility = View.VISIBLE

                CoroutineScope(Dispatchers.IO).launch {
                    val post = GuruLoginPost(mBinding.inputLoginUsername.text.toString(), mBinding.inputLoginPassword.text.toString())
                    val request = service.loginGuru(post)

                    withContext(Dispatchers.Main){
                        val result = request.await()

                        if (result.isSuccessful){

                            mBinding.loadingView.visibility = View.GONE

                            if (result.body()?.status == 200){
                                result.body()?.data?.let { it1 -> LocalService.saveGuru(it1) }
                                goDashboard()
                            } else {
                                result.body()?.message?.let { it1 -> Helper.showError(this@LoginGuruActivity, it1) }
                            }
                        } else {

                            mBinding.loadingView.visibility = View.GONE

                            Helper.showError(this@LoginGuruActivity,result.message())
                        }
                    }
                }

            } else {
                Helper.showError(this,"Ada yang masih kosong")
            }
        }
        mBinding.btnLoginOrtu.setOnClickListener {
            goLoginOrtu()
        }
        mBinding.btnLoginSiswa.setOnClickListener {
            goLoginSiswa()
        }
    }

    private fun goLoginSiswa() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun goLoginOrtu() {
        startActivity(Intent(this, LoginOrtuActivity::class.java))
    }

    private fun goDashboard() {
        val intent = Intent(this, ProfileGuruActivity::class.java)
        startActivity(intent)
    }
}
