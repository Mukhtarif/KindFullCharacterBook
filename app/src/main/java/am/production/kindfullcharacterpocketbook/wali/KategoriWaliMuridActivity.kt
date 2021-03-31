package am.production.kindfullcharacterpocketbook.wali

import am.production.kindfullcharacterpocketbook.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import am.production.kindfullcharacterpocketbook.databinding.ActivityKategoriWaliMuridBinding
import am.production.kindfullcharacterpocketbook.model.get.Kategori
import am.production.kindfullcharacterpocketbook.network.ApiService
import am.production.kindfullcharacterpocketbook.utils.Helper
import am.production.kindfullcharacterpocketbook.utils.LocalService
import android.os.Debug
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class KategoriWaliMuridActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityKategoriWaliMuridBinding
    private var kategoriWaliMuridAdapter: KategoriWaliMuridAdapter? = null

    lateinit var horizontalCalendar: HorizontalCalendar

    val service = ApiService.makeRetrofitService()

    override fun onStart() {
        super.onStart()
        Helper.init(this)
    }

    override fun onResume() {
        super.onResume()

        CoroutineScope(Dispatchers.IO).launch {

            val request = service.getOrtuKategori(LocalService.getOrtu().id.toString())

            withContext(Dispatchers.Main){
                val result = request.await()

                val today =  Helper.getCurrentDateTime()

                if (result.isSuccessful){

                    kategoriWaliMuridAdapter = result.body()?.data?.let { KategoriWaliMuridAdapter(it, this@KategoriWaliMuridActivity) }
                    kategoriWaliMuridAdapter?.setTanggal(today.toString("yyyy-MM-dd"))
                    mBinding.rvListKategori.visibility = View.VISIBLE
                    mBinding.rvListKategori.apply {
                        layoutManager = LinearLayoutManager(this@KategoriWaliMuridActivity)
                        adapter = kategoriWaliMuridAdapter
                    }

                    horizontalCalendar.calendarListener = object : HorizontalCalendarListener() {
                        override fun onDateSelected(date: Calendar, position: Int) {

//                            Helper.showError(this@KategoriWaliMuridActivity, date.time.toString("yyyy-MM-dd"))
//                            kategoriWaliMuridAdapter = result.body()?.data?.let { KategoriWaliMuridAdapter(it, this@KategoriWaliMuridActivity) }
                            kategoriWaliMuridAdapter?.setTanggal(date.time.toString("yyyy-MM-dd"))
                        }
                    }
                } else {
                    Helper.showError(this@KategoriWaliMuridActivity,result.message())
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_kategori_wali_murid
        )

        var startDate: Calendar = Calendar.getInstance()
        var endDate: Calendar = Calendar.getInstance()

        startDate.add(Calendar.DAY_OF_MONTH, -30)
        endDate.add(Calendar.DAY_OF_MONTH, 30)

        horizontalCalendar = HorizontalCalendar
            .Builder(this@KategoriWaliMuridActivity, mBinding.calendarView.id)
            .range(startDate, endDate)
            .datesNumberOnScreen(5)
            .build()

        horizontalCalendar.goToday(false)
    }

    private fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }
}
