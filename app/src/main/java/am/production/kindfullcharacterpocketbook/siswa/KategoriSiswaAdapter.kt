package am.production.kindfullcharacterpocketbook.siswa

import am.production.kindfullcharacterpocketbook.R
import am.production.kindfullcharacterpocketbook.databinding.ItemKategoriKarakterBinding
import am.production.kindfullcharacterpocketbook.model.get.Kategori
import am.production.kindfullcharacterpocketbook.model.get.kategori.siswa.Data
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView

class KategoriSiswaAdapter(
    private val items: List<Data>,
    private val context: Context
) : RecyclerView.Adapter<KategoriSiswaAdapter.ViewHolder>() {

    private lateinit var date : String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_kategori_karakter, parent, false))
        val binding = DataBindingUtil.inflate<ItemKategoriKarakterBinding>(
            LayoutInflater.from(parent.context), R.layout.item_kategori_karakter, parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: Data = items[position]
        holder.bindView(item)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, IsianKategoriSiswaActivity::class.java)
            intent.putExtra("id_kategori",item.id)
            intent.putExtra("namaKategori",item.nama)
            intent.putExtra("tanggal",date)
            context.startActivity(Intent(intent))
        }
    }

    fun setTanggal(date: String){
        this.date = date
    }

    class ViewHolder(
        private val binding: ItemKategoriKarakterBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bindView(item: Data) {
            binding.tvNamaKategori.text = item.nama
            binding.tvNamaKategoriButton.text = "Lengkapi " + item.nama + "mu"
            when (item.nama) {
                "Ibadah" -> binding.imgKategori.setBackgroundResource(R.drawable.ibadah_banner)
                "Sosial" -> binding.imgKategori.setBackgroundResource(R.drawable.sosial_banner)
                "Kepribadian" -> binding.imgKategori.setBackgroundResource(R.drawable.kepribadian_banner)
                else -> binding.imgKategori.setBackgroundResource(R.drawable.ic_launcher_background)
            }
        }
    }

}