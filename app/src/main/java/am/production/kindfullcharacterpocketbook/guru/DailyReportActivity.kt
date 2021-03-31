package am.production.kindfullcharacterpocketbook.guru

import am.production.kindfullcharacterpocketbook.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import am.production.kindfullcharacterpocketbook.databinding.ActivityDailyReportBinding
import am.production.kindfullcharacterpocketbook.guru.report.DailyFragment
import am.production.kindfullcharacterpocketbook.guru.report.MonthlyFragment
import am.production.kindfullcharacterpocketbook.guru.report.WeeklyFragment

class DailyReportActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityDailyReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_daily_report
        )

        if (::mBinding.isInitialized) {
            val adapter = ReportViewPagerAdapter(supportFragmentManager)
            adapter.addFragment(DailyFragment(), "Harian")
            adapter.addFragment(MonthlyFragment(), "Bulanan")
//            adapter.addFragment(MonthlyFragment(), "Semester")
            adapter.notifyDataSetChanged()
            mBinding.pager.adapter = adapter
            mBinding.tabs.setupWithViewPager(mBinding.pager)
        }

        mBinding.pager.offscreenPageLimit = 1
    }
}
