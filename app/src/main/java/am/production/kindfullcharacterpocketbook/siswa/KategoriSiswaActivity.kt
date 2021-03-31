package am.production.kindfullcharacterpocketbook.siswa

import am.production.kindfullcharacterpocketbook.R
import am.production.kindfullcharacterpocketbook.databinding.ActivityKategoriSiswaBinding
import am.production.kindfullcharacterpocketbook.model.get.Kategori
import am.production.kindfullcharacterpocketbook.network.ApiService
import am.production.kindfullcharacterpocketbook.utils.Helper
import am.production.kindfullcharacterpocketbook.utils.LocalService
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import devs.mulham.horizontalcalendar.HorizontalCalendar
import java.util.*
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat


class KategoriSiswaActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityKategoriSiswaBinding
    private var kategoriSiswaAdapter: KategoriSiswaAdapter? = null
    lateinit var horizontalCalendar : HorizontalCalendar

    val service = ApiService.makeRetrofitService()

    override fun onStart() {
        super.onStart()


        CoroutineScope(Dispatchers.IO).launch {

            val request = service.getSiswaKategori(LocalService.getSiswa().id.toString())

            withContext(Dispatchers.Main){
                val result = request.await()

                val today =  Helper.getCurrentDateTime()

                if (result.isSuccessful){

                    kategoriSiswaAdapter = result.body()?.data?.let { KategoriSiswaAdapter(it, this@KategoriSiswaActivity) }
                    kategoriSiswaAdapter?.setTanggal(today.toString("yyyy-MM-dd"))
                    mBinding.rvListKategori.visibility = View.VISIBLE
                    mBinding.rvListKategori.apply {
                        layoutManager = LinearLayoutManager(this@KategoriSiswaActivity)
                        adapter = kategoriSiswaAdapter
                    }

                    horizontalCalendar.calendarListener = object : HorizontalCalendarListener() {
                        override fun onDateSelected(date: Calendar, position: Int) {
                            kategoriSiswaAdapter?.setTanggal(date.time.toString("yyyy-MM-dd"))
                        }
                    }
                } else {
                    Helper.showError(this@KategoriSiswaActivity,result.message())
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_kategori_siswa
        )

        var startDate: Calendar = Calendar.getInstance()
        var endDate: Calendar = Calendar.getInstance()

        startDate.add(Calendar.DAY_OF_MONTH, -30)
        endDate.add(Calendar.DAY_OF_MONTH, 30)

        horizontalCalendar = HorizontalCalendar
            .Builder(this@KategoriSiswaActivity, mBinding.calendarView.id)
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
