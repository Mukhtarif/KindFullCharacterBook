package am.production.kindfullcharacterpocketbook.wali

import am.production.kindfullcharacterpocketbook.R
import am.production.kindfullcharacterpocketbook.databinding.ItemKategoriWaliMuridBinding
import am.production.kindfullcharacterpocketbook.model.get.Kategori
import am.production.kindfullcharacterpocketbook.model.get.kategori.orangtua.Data
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class KategoriWaliMuridAdapter(
    private val items: List<Data>,
    private val context: Context
) : RecyclerView.Adapter<KategoriWaliMuridAdapter.ViewHolder>() {

    private lateinit var date: String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ItemKategoriWaliMuridBinding>(
            LayoutInflater.from(parent.context), R.layout.item_kategori_wali_murid, parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setTanggal(date: String){
        this.date = date
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bindView(item)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, IsianKategoriWaliMuridActivity::class.java)
            intent.putExtra("id_kategori",item.id)
            intent.putExtra("nama_kategori",item.nama)
            intent.putExtra("tanggal",date)
            context.startActivity(Intent(intent))
        }
    }

    class ViewHolder(
        private val binding: ItemKategoriWaliMuridBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(item: Data) {
            binding.tvNamaKategori.text = item.nama
            binding.tvNamaKategoriButton.text = "Lihat progres ${item.nama} anak anda"
        }
    }

}