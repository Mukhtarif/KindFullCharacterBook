package am.production.kindfullcharacterpocketbook.siswa

import am.production.kindfullcharacterpocketbook.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import am.production.kindfullcharacterpocketbook.databinding.ActivityIsianKategoriSiswaBinding
import am.production.kindfullcharacterpocketbook.model.post.indikator.siswa.SiswaIndikatorPost
import am.production.kindfullcharacterpocketbook.model.post.indikator.siswa.SiswaLaporIndikator
import am.production.kindfullcharacterpocketbook.model.post.indikator.siswa.SiswaLaporPost
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

class IsianKategoriSiswaActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityIsianKategoriSiswaBinding
    private var isianKategoriSiswaAdapter: IsianKategoriSiswaAdapter? = null

    val service = ApiService.makeRetrofitService()

    val list = mutableListOf<SiswaLaporIndikator>()

    var tanggal = ""

    override fun onStart() {
        super.onStart()

        var id_kategori = 0
        var namaKategori = ""

        mBinding.loadingView.visibility = View.VISIBLE

        if(intent.extras != null)
        {
            val bundle = intent.extras

            id_kategori = bundle.getInt("id_kategori",0)
            namaKategori = bundle.getString("namaKategori","")
            tanggal = bundle.getString("tanggal","")
        }

        CoroutineScope(Dispatchers.IO).launch {

            val today =  Helper.getCurrentDateTime()

            val post = SiswaIndikatorPost(LocalService.getSiswa().id.toString(), tanggal)
            val request = service.getSiswaKategoriIndikator(id_kategori.toString(),post)

            withContext(Dispatchers.Main){
                val result = request.await()

                if (result.isSuccessful){

                    mBinding.loadingView.visibility = View.GONE

                    isianKategoriSiswaAdapter = result.body()?.data?.let { IsianKategoriSiswaAdapter(it, this@IsianKategoriSiswaActivity, tanggal, today.toString("yyyy-MM-dd")) }

                    if (::mBinding.isInitialized) {

                        isianKategoriSiswaAdapter?.insertData(object : IndikatorListener{
                            override fun OnChange(indikatorId: Int) {
                                val post = SiswaLaporIndikator(indikatorId)
                                list.add(post)
                            }
                        })

                        mBinding.tvNamaKategori.text = namaKategori

                        mBinding.rvListIsianKategori.apply {
                            layoutManager = LinearLayoutManager(this@IsianKategoriSiswaActivity)
                            adapter = isianKategoriSiswaAdapter
                        }

                    }

                } else {

                    mBinding.loadingView.visibility = View.GONE

                    Helper.showError(this@IsianKategoriSiswaActivity, result.message())
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_isian_kategori_siswa
        )

        mBinding.btnOk.setOnClickListener {
            onButtonSave()

        }

    }

    private fun onButtonSave() {
        mBinding.loadingView.visibility = View.VISIBLE

        if (list.size != 0){
            CoroutineScope(Dispatchers.IO).launch {
                val post = SiswaLaporPost(LocalService.getSiswa().id,list)

                val request = service.postSiswaLaporan(post)

                withContext(Dispatchers.Main){
                    val result = request.await()

                    if (result.isSuccessful){
                        mBinding.loadingView.visibility = View.GONE

                        goDashboard()
                    } else {
                        mBinding.loadingView.visibility = View.GONE

                        Helper.showError(this@IsianKategoriSiswaActivity,result.message())
                    }
                }
            }
        } else {
            mBinding.loadingView.visibility = View.GONE

            goDashboard()
        }
    }

    private fun goDashboard() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }
}
