package com.pooja.invoiceapplication.util

import android.os.Environment
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileOutputStream

class PdfService {
    lateinit var pdf: PdfWriter
    val TITLE_FONT = Font(Font.FontFamily.TIMES_ROMAN, 16f, Font.BOLD)
    val BODY_FONT = Font(Font.FontFamily.TIMES_ROMAN, 12f, Font.NORMAL)
    fun createFile(): File {
        //Prepare file
        //val title = "Pdf to export.pdf"
       // val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val path =Environment.getExternalStorageDirectory().path+"/invoice.pdf"
        val file = File(path)
        if (!file.exists()) file.createNewFile()
        return file
    }
    private fun createDocument(): Document {
        //Create Document
        val document = Document()
        document.setMargins(24f, 24f, 32f, 32f)
        document.pageSize = PageSize.A4
        return document
    }

    private fun setupPdfWriter(document: Document, file: File) {
        pdf = PdfWriter.getInstance(document, FileOutputStream(file))
        pdf.setFullCompression()
        //Open the document
        document.open()
    }

    private fun createParagraph(content: String): Paragraph {
        val paragraph = Paragraph(content, BODY_FONT)
        paragraph.firstLineIndent = 25f
        paragraph.alignment = Element.ALIGN_JUSTIFIED
        return paragraph
    }

    private fun createTable(column: Int, columnWidth: FloatArray): PdfPTable {
        val table = PdfPTable(column)
        table.widthPercentage = 100f
        table.setWidths(columnWidth)
        table.headerRows = 1
        table.defaultCell.verticalAlignment = Element.ALIGN_CENTER
        table.defaultCell.horizontalAlignment = Element.ALIGN_CENTER
        return table
    }
    private fun createCell(content: String): PdfPCell {
        val cell = PdfPCell(Phrase(content))
        cell.horizontalAlignment = Element.ALIGN_CENTER
        cell.verticalAlignment = Element.ALIGN_MIDDLE
        //setup padding
        cell.setPadding(8f)
        cell.isUseAscender = true
        cell.paddingLeft = 4f
        cell.paddingRight = 4f
        cell.paddingTop = 8f
        cell.paddingBottom = 8f
        return cell
    }
    fun createUserTable(
        data: List<CartItem>,
        paragraphList: List<String>,
        onFinish: (file: File) -> Unit,
        onError: (Exception) -> Unit
    ) {
        //Define the document
        val file = createFile()
        val document = createDocument()

        //Setup PDF Writer
        setupPdfWriter(document, file)

        //Add Title
        document.add(Paragraph("Invoice", TITLE_FONT))
        //Add Empty Line as necessary


        //Add paragraph
        paragraphList.forEach {content->
            val paragraph = createParagraph(content)
            document.add(paragraph)
        }

        //Add Empty Line as necessary

        //Add table title
        document.add(Paragraph("Product", TITLE_FONT))

        //Define Table
        val userIdWidth = 0.2f
        val firstNameWidth = 1f
        val middleNameWidth = 1f
        val lastNameWidth = 1f
        val totalPriceWidth=1F
        val columnWidth = floatArrayOf(firstNameWidth, middleNameWidth, lastNameWidth,totalPriceWidth)
        val table = createTable(4, columnWidth)
        //Table header (first row)
        val tableHeaderContent = listOf(" Name", " Quantity", " Price"," TotalPrice")
        //write table header into table
        tableHeaderContent.forEach {
            //define a cell
            val cell = createCell(it)
            //add our cell into our table
            table.addCell(cell)
        }
        //write user data into table
        data.forEach {
            //Write Each User Id
            //val idCell = createCell(it.id.toString())
            //table.addCell(idCell)
            //Write Each First Name
            val firstNameCell = createCell(it.name.toString())
            table.addCell(firstNameCell)
            //Write Each Middle Name
            val middleNameCell = createCell(it.quantity.toString())
            table.addCell(middleNameCell)
            //Write Each Last Name
            val lastNameCell = createCell(it.price.toString())
            table.addCell(lastNameCell)
            val totalPrice = createCell(it.totalItemPrice.toString())
            table.addCell(totalPrice)
        }
        document.add(table)
        document.close()

        try {
            pdf.close()
        } catch (ex: Exception) {
            onError(ex)
        } finally {
            onFinish(file)
        }
    }
}