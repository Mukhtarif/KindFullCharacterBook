package am.production.kindfullcharacterpocketbook.siswa

import am.production.kindfullcharacterpocketbook.R
import am.production.kindfullcharacterpocketbook.databinding.ItemIsianKategoriBinding
import am.production.kindfullcharacterpocketbook.model.get.indikator.siswa.Data
import am.production.kindfullcharacterpocketbook.model.get.indikator.siswa.Indikator
import am.production.kindfullcharacterpocketbook.utils.IndikatorListener
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView

class IsianKategoriSiswaAdapter (
    private val items: Data,
    private val context: Context,
    private val tanggalMasukan: String,
    private val tanggalHariIni: String
) : RecyclerView.Adapter<IsianKategoriSiswaAdapter.ViewHolder>() {

    private lateinit var indikatorListener : IndikatorListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ItemIsianKategoriBinding>(
            LayoutInflater.from(parent.context), R.layout.item_isian_kategori, parent, false
        )

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.indikator.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: Indikator = items.indikator[position]
        var state : Boolean = items.laporan.any { laporan -> laporan.indikator_id == item.id }

        holder.bindView(item, state)

        if (tanggalMasukan != tanggalHariIni){
            holder.binding.switchIsianIndikator.isClickable = false
        } else {
            holder.binding.switchIsianIndikator.setOnClickListener {
                indikatorListener.OnChange(item.id)
                holder.binding.switchIsianIndikator.isClickable = false
            }
        }
    }

    fun insertData(indikatorListener: IndikatorListener){
        this.indikatorListener = indikatorListener
    }

    class ViewHolder (
        val binding: ItemIsianKategoriBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(
            item: Indikator,
            state: Boolean
        ) {
            binding.textIsianIndikator.text = item.nama

            if (state){
                binding.switchIsianIndikator.isChecked = true
                binding.switchIsianIndikator.isClickable = false
            }



        }
    }

}