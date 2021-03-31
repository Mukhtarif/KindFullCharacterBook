package am.production.kindfullcharacterpocketbook.wali

import am.production.kindfullcharacterpocketbook.R
import am.production.kindfullcharacterpocketbook.databinding.ItemIsianKategoriWaliMuridBinding
import am.production.kindfullcharacterpocketbook.model.get.indikator.orangtua.Data
import am.production.kindfullcharacterpocketbook.model.get.indikator.orangtua.Indikator
import am.production.kindfullcharacterpocketbook.utils.IndikatorListener
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView

class IsianKategoriWaliMuridAdapter (
    private val items: Data,
    private val context: Context
) : RecyclerView.Adapter<IsianKategoriWaliMuridAdapter.ViewHolder>() {

    private lateinit var indikatorListener : IndikatorListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = DataBindingUtil.inflate<ItemIsianKategoriWaliMuridBinding>(
            LayoutInflater.from(parent.context), R.layout.item_isian_kategori_wali_murid, parent, false
        )

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.indikator.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item: Indikator = items.indikator[position]
        var stateSiswa : Boolean = items.laporan.any { laporan -> laporan.indikator_id == item.id }
        var stateOrtu : Boolean = checkOrtu(item.id)

        holder.binding.switchIsianIndikatorOrtu.setOnClickListener {
            holder.binding.switchIsianIndikatorOrtu.isChecked = true
            holder.binding.switchIsianIndikatorOrtu.isClickable = false
            indikatorListener.OnChange(item.id)
        }

        holder.bindView(item,stateOrtu, stateSiswa)
    }

    fun checkOrtu(id : Int) : Boolean{
        for (i in 1..items.laporan.size){
            if (items.laporan[i-1].indikator_id == id){
                if(items.laporan[i-1].check_ortu == 1){
                    return true
                }
            }
        }

        return false
    }



    fun insertData(indikatorListener: IndikatorListener){
        this.indikatorListener = indikatorListener
    }

    class ViewHolder(
        val binding : ItemIsianKategoriWaliMuridBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindView (
            item: Indikator,
            stateOrtu: Boolean,
            stateSiswa: Boolean
        ) {
            binding.switchIsianIndikatorSiswa.isClickable = false

            binding.textIsianIndikator.text = item.nama

            if (stateOrtu){
                binding.switchIsianIndikatorOrtu.isChecked = true
                binding.switchIsianIndikatorOrtu.isClickable = false
            }

            if (stateSiswa){
                binding.switchIsianIndikatorSiswa.isChecked = true
            }
        }
    }
}