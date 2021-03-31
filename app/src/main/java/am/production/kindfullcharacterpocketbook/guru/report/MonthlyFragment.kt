package am.production.kindfullcharacterpocketbook.guru.report


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import am.production.kindfullcharacterpocketbook.R
import am.production.kindfullcharacterpocketbook.databinding.FragmentMonthlyBinding
import am.production.kindfullcharacterpocketbook.model.post.guru.CekGuruListId
import am.production.kindfullcharacterpocketbook.model.post.guru.GuruAktivitasSiswaPost
import am.production.kindfullcharacterpocketbook.network.ApiService
import am.production.kindfullcharacterpocketbook.utils.Helper
import am.production.kindfullcharacterpocketbook.utils.LocalService
import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class MonthlyFragment : Fragment() {
    private lateinit var mBinding: FragmentMonthlyBinding
    private val REQUEST_CODE_ASK_PERMISSIONS = 111
    private var datetime: String? = null

    val odds = listOf<String>("01", "03", "05", "07", "08", "10", "12")

    val even = listOf<String>("04", "06", "09", "11")

    private var dataLaporan = mutableListOf<CekGuruListId>()

    val service = ApiService.makeRetrofitService()

    private var pdfFile: File? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentMonthlyBinding.inflate(layoutInflater, container, false)

        mBinding.btnDownloadPdf.setOnClickListener {
            createPdfWrapper()
        }

        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        this.context?.let { Helper.init(it) }
    }

    override fun onResume() {
        super.onResume()

        mBinding.loadingView.visibility = View.VISIBLE

        CoroutineScope(Dispatchers.IO).launch {
            val post = GuruAktivitasSiswaPost(LocalService.getGuru().kelas_id.toString())
            val request = service.getGuruAktivitasSiswa(post)

            withContext(Dispatchers.Main) {
                val result = request.await()

                if (result.isSuccessful) {

                    mBinding.loadingView.visibility = View.GONE

                    val data = result.body()?.data
                    LocalService.saveDataAktivitasSiswa(data!!)
                }
                else {
                    mBinding.loadingView.visibility = View.GONE
                    Toast.makeText(activity,result.message(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @Throws(FileNotFoundException::class, DocumentException::class)
    private fun createPdfWrapper() {
        val hasWriteStoragePermission = ActivityCompat.checkSelfPermission(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                    showMessageOKCancel("You need to allow access to Storage",
                        DialogInterface.OnClickListener { _, _ ->
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(
                                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                    REQUEST_CODE_ASK_PERMISSIONS
                                )
                            }
                        })
                    return
                }


                requestPermissions(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_CODE_ASK_PERMISSIONS
                )
            }
            return
        } else {
            datetime = SimpleDateFormat("dd/MM/yy hh:mm", Locale.getDefault()).format(Date())
            try {
                if (LocalService.getDataAktivitasSiswa().isNotEmpty()) {
                    createPdf()
                }
                else {
                    Toast.makeText(activity, "Data tidak ditemukan", Toast.LENGTH_LONG).show()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    @Throws(IOException::class, DocumentException::class)
    fun createPdf() {
        val docsFolder = File(Environment.getExternalStorageDirectory().toString() + "/Documents")

        val data = LocalService.getDataAktivitasSiswa()

        val date = Helper.getCurrentDateTime()
        val dateInString = date.toString("yyyy-MM")
        val monthInString = date.toString("MM")

        if (!docsFolder.exists()) {
            docsFolder.mkdir()
        }

        pdfFile = File(docsFolder.absolutePath, "DataKelas${data[0].kelas.tingkat}${data[0].kelas.nama}.pdf")
        val output = FileOutputStream(pdfFile)
        val document = Document()
        document.pageSize = Rectangle(650f, 4000f)
        PdfWriter.getInstance(document, output)
        document.open()

        val judulAtas = Paragraph()
        judulAtas.add(Phrase("DATA KELAS ${data[0].kelas.tingkat} ${data[0].kelas.nama}"))
        judulAtas.alignment = Element.ALIGN_CENTER
        judulAtas.spacingBefore = 10f
        judulAtas.spacingAfter = 8f

        var jmlIndikator = 0
        for (i in 1..data[0].kelas.kategori.size) {
            jmlIndikator += data[0].kelas.kategori[i-1].indikator.size
        }

        val table = PdfPTable(jmlIndikator+1)
        var cell: PdfPCell

        cell = PdfPCell(Phrase("Nama Siswa"))
        table.addCell(cell)
        for (i in 1..data[0].kelas.kategori.size) {
            for (j in 1..data[0].kelas.kategori[i-1].indikator.size) {
                cell = PdfPCell(Phrase(data[0].kelas.kategori[i-1].indikator[j-1].nama))
                table.addCell(cell)
            }
        }

        for (i in 1..data.size) {
            cell = PdfPCell(Phrase(data[i-1].nama))
            var idSiswa = data[i-1].id
            table.addCell(cell)
            for (j in 1..data[i-1].kelas.kategori.size) {
                for (k in 1..data[i-1].kelas.kategori[j-1].indikator.size) {
                    var indikatorBool = false
                    var countChecklist = 0
                    var totalCount: String = if (odds.contains(monthInString)) "31"
                    else if (even.contains(monthInString)) "30"
                    else "28"
                    for (l in 1..data[i-1].kelas.kategori[j-1].indikator[k-1].laporan.size) {
                        var dateCreated = data[i-1].kelas.kategori[j-1].indikator[k-1].laporan[l-1].created_at.substring(0, 7)
                        if (dateCreated == dateInString && idSiswa == data[i-1].kelas.kategori[j-1].indikator[k-1].laporan[l-1].siswa_id) {
                            countChecklist++
                            dataLaporan.add(CekGuruListId(data[i-1].kelas.kategori[j-1].indikator[k-1].laporan[l-1].id))
                        }
                        Log.d("Bulan Sekarang", "Now : $dateInString + created at : $dateCreated")
                    }
                    cell = PdfPCell(Phrase("$countChecklist/$totalCount"))
                    table.addCell(cell)
                }
            }
        }

        document.add(judulAtas)
        document.add(table)

        document.close()
        previewPdf(pdfFile!!)
    }

    private fun previewPdf(file: File) {
        val packageManager = context!!.packageManager
        val testIntent = Intent(Intent.ACTION_VIEW)
        testIntent.type = "application/pdf"
        val list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY)
        if (list.size > 0) {
            val uri = FileProvider.getUriForFile(context!!, "com.mypackage.myprovider", File(file.absolutePath))
            context!!.grantUriPermission(
                context!!.packageName,
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.setDataAndType(uri, "application/pdf")

            startActivity(intent)
        } else {
            Toast.makeText(context!!, "Download a PDF Viewer to see the generated PDF", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(context!!)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    private fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }


}
