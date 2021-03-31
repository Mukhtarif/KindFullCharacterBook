package am.production.kindfullcharacterpocketbook.auth

import am.production.kindfullcharacterpocketbook.R
import am.production.kindfullcharacterpocketbook.databinding.ActivityRegisterBinding
import am.production.kindfullcharacterpocketbook.model.post.auth.ortu.OrtuRegisterPost
import am.production.kindfullcharacterpocketbook.network.ApiService
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

class RegisterActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityRegisterBinding

    val service = ApiService.makeRetrofitService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_register
        )

        var cbTermsStatus = false
        mBinding.cbRegisterTerms.setOnCheckedChangeListener { _, isChecked ->
            cbTermsStatus = isChecked
        }

        mBinding.btnDaftarOrtu.setOnClickListener {
            register(mBinding.inputRegisterEmail.toString())
        }
    }

    private fun register(userData: String) {
        if (mBinding.inputRegisterUsername.text.isNotEmpty()
            && mBinding.inputRegisterEmail.text.isNotEmpty()
            && mBinding.inputRegisterPassword.text.isNotEmpty()
            && mBinding.inputRegisterReferral.text.isNotEmpty()) {

            mBinding.loadingView.visibility = View.VISIBLE

            CoroutineScope(Dispatchers.IO).launch {
                val ortuRegisterPost = OrtuRegisterPost(
                    mBinding.inputRegisterReferral.text.toString(),
                    mBinding.inputRegisterUsername.text.toString(),
                    mBinding.inputRegisterEmail.text.toString(),
                    mBinding.inputRegisterPassword.text.toString(),
                    "orangtua"
                )

                val request = service.registerOrtu(ortuRegisterPost)

                withContext(Dispatchers.Main) {
                    val result = request.await()

                    if (result.isSuccessful) {

                        mBinding.loadingView.visibility = View.GONE

                        if (result.body()?.status == 200){
                            result.body()?.data?.let { it1 -> LocalService.saveOrtu(it1) }
                            LocalService.saveRole("orangtua")
                            goDashboard()
                        } else {
                            Toast.makeText(this@RegisterActivity, result.body()?.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                    else {

                        mBinding.loadingView.visibility = View.GONE

                        Toast.makeText(this@RegisterActivity, result.body()?.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
        else {
            Toast.makeText(this@RegisterActivity, "Harap isi semua data dengan benar", Toast.LENGTH_LONG).show()
        }
    }

    private fun goDashboard() {
        startActivity(Intent(this, ProfileWaliMuridActivity::class.java))
    }
}
