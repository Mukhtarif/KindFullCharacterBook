package am.production.kindfullcharacterpocketbook.utils

import android.content.Context
import android.widget.Toast
import com.orhanobut.hawk.Hawk
import java.text.SimpleDateFormat
import java.util.*

class Helper {
    companion object {

        fun init(context: Context){
            if (!Hawk.isBuilt()){
                Hawk.init(context).build()
            }
        }

        fun getCurrentDateTime(): Date {
            return Calendar.getInstance().time
        }

        fun showError(context: Context, message : String){
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
}