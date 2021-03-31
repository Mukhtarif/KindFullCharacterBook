package am.production.kindfullcharacterpocketbook.auth

import am.production.kindfullcharacterpocketbook.R
import am.production.kindfullcharacterpocketbook.databinding.ActivityLoginOrtuBinding
import am.production.kindfullcharacterpocketbook.model.post.auth.ortu.OrtuLoginPost
import am.production.kindfullcharacterpocketbook.network.ApiService
import am.production.kindfullcharacterpocketbook.utils.Helper
import am.production.kindfullcharacterpocketbook.utils.LocalService
import am.production.kindfullcharacterpocketbook.wali.ProfileWaliMuridActivity
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

class LoginOrtuActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityLoginOrtuBinding

    override fun onStart() {
        super.onStart()
        Helper.init(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_login_ortu
        )

        val service = ApiService.makeRetrofitService()

        mBinding.btnLoginDaftar.setOnClickListener {
            goDaftarOrtu()
        }
        mBinding.btnLoginGuru.setOnClickListener {
            goLoginGuru()
        }
        mBinding.btnLoginSiswa.setOnClickListener {
            goLoginSiswa()
        }
        mBinding.btnLoginMasuk.setOnClickListener {
            if (mBinding.inputLoginUsername.text.isNotEmpty() && mBinding.inputLoginPassword.text.isNotEmpty()) {

                mBinding.loadingView.visibility = View.VISIBLE

                CoroutineScope(Dispatchers.IO).launch {
                    val ortuLoginPost = OrtuLoginPost(
                        mBinding.inputLoginUsername.text.toString(),
                        mBinding.inputLoginPassword.text.toString()
                    )

                    val request = service.loginOrtu(ortuLoginPost)

                    withContext(Dispatchers.Main) {
                        val result = request.await()

                        if (result.isSuccessful) {

                            mBinding.loadingView.visibility = View.GONE

                            if (result.body()?.status == 200){
                                result.body()?.data?.let { it1 -> LocalService.saveOrtu(it1) }
                                LocalService.saveRole("orangtua")
                                goDashboard()
                            } else {
                                Helper.showError(this@LoginOrtuActivity,"email atau password salah")
                            }
                        }
                        else {

                            mBinding.loadingView.visibility = View.GONE

                            Toast.makeText(this@LoginOrtuActivity, result.body()?.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
            else {
                Toast.makeText(this@LoginOrtuActivity, "Harap isi username dan password dengan benar", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun goDashboard() {
        startActivity(Intent(this, ProfileWaliMuridActivity::class.java))
    }

    private fun goLoginSiswa() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun goLoginGuru() {
        startActivity(Intent(this, LoginGuruActivity::class.java))
    }

    private fun goDaftarOrtu() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }
}
