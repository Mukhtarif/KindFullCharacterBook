package am.production.kindfullcharacterpocketbook.wali

import am.production.kindfullcharacterpocketbook.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import am.production.kindfullcharacterpocketbook.databinding.ActivityIsianKategoriWaliMuridBinding
import am.production.kindfullcharacterpocketbook.model.post.indikator.orangtua.OrangtuaIndikatorPost
import am.production.kindfullcharacterpocketbook.model.post.indikator.orangtua.OrangtuaLaporIndikator
import am.production.kindfullcharacterpocketbook.model.post.indikator.orangtua.OrangtuaLaporPost
import am.production.kindfullcharacterpocketbook.network.ApiService
import am.production.kindfullcharacterpocketbook.utils.Helper
import am.production.kindfullcharacterpocketbook.utils.IndikatorListener
import am.production.kindfullcharacterpocketbook.utils.LocalService
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class IsianKategoriWaliMuridActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityIsianKategoriWaliMuridBinding
    private var isianKategoriWaliMuridAdapter: IsianKategoriWaliMuridAdapter? = null

    val service = ApiService.makeRetrofitService()

    val list = mutableListOf<OrangtuaLaporIndikator>()

    override fun onStart() {
        super.onStart()

        Helper.init(this)

        var id_kategori = 0
        var namakategori = ""
        var tanggal = ""

        mBinding.loadingView.visibility = View.VISIBLE

        if(intent.extras != null)
        {
            val bundle = intent.extras

            id_kategori = bundle.getInt("id_kategori")
            namakategori = bundle.getString("nama_kategori")
            tanggal = bundle.getString("tanggal")
        }

        CoroutineScope(Dispatchers.IO).launch {
            val post = OrangtuaIndikatorPost(tanggal,LocalService.getOrtu().siswa_id)
            val request = service.getOrtuKategoriIndikator(id_kategori.toString(),post)

            withContext(Dispatchers.Main){
                val result = request.await()

                if (result.isSuccessful){

                    mBinding.loadingView.visibility = View.GONE

                    isianKategoriWaliMuridAdapter = result.body()?.data?.let { IsianKategoriWaliMuridAdapter(it,this@IsianKategoriWaliMuridActivity) }

                    if (::mBinding.isInitialized){

                            mBinding.tvNamaKategori.text = namakategori

                            mBinding.rvListIsianKategori.apply {
                                layoutManager = LinearLayoutManager(this@IsianKategoriWaliMuridActivity)
                                adapter = isianKategoriWaliMuridAdapter
                            }

                            isianKategoriWaliMuridAdapter?.insertData(object : IndikatorListener{
                                override fun OnChange(indikatorId: Int) {
                                    val post = OrangtuaLaporIndikator(indikatorId, LocalService.getOrtu().siswa_id, tanggal)
                                    list.add(post)
                                }

                            })
                    }

                } else {
                    mBinding.loadingView.visibility = View.GONE

                    Helper.showError(this@IsianKategoriWaliMuridActivity, result.message())
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_isian_kategori_wali_murid
        )

        mBinding.btnOk.setOnClickListener {
            onButtonSave()
        }
    }

    private fun onButtonSave() {
        mBinding.loadingView.visibility = View.VISIBLE

        if (list.size != 0){
            CoroutineScope(Dispatchers.IO).launch {
                val post = OrangtuaLaporPost(list)

                val request = service.postOrtuLaporan(post)

                withContext(Dispatchers.Main){
                    val result = request.await()

                    if (result.isSuccessful){
                        mBinding.loadingView.visibility = View.GONE

                        goProfileOrangtua()
                    } else {
                        mBinding.loadingView.visibility = View.GONE

                        Helper.showError(this@IsianKategoriWaliMuridActivity,result.message())
                    }
                }
            }
        } else {
            mBinding.loadingView.visibility = View.GONE

            goProfileOrangtua()
        }
    }

    private fun goProfileOrangtua() {
        startActivity(Intent(this, ProfileWaliMuridActivity::class.java))
    }

    private fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }
}
