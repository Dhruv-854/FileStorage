package com.dhruv.filestorage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.os.Environment
import android.widget.Toast
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.android.tools.build.jetifier.core.utils.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun generatePDF(context: Context, directory: File) {
    val pageHeight = 1120
    val pageWidth = 792
    val pdfDocument = PdfDocument()
    val paint = Paint()
    val title = Paint()
    val myPageInfo = PageInfo.Builder(pageWidth, pageHeight, 1).create()
    val myPage = pdfDocument.startPage(myPageInfo)
    val canvas: Canvas = myPage.canvas
    val bitmap: Bitmap? = drawableToBitmap(context.resources.getDrawable(R.drawable.ic_launcher_foreground))
    val scaleBitmap: Bitmap? = Bitmap.createScaledBitmap(bitmap!!, 120, 120, false)
    canvas.drawBitmap(scaleBitmap!!, 40f, 40f, paint)
    title.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
    title.textSize = 15f
    title.color = ContextCompat.getColor(context, R.color.purple_200)
    canvas.drawText("Jetpack Compose", 400f, 100f, title)
    canvas.drawText("Make it Easy", 400f, 80f, title)
    title.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
    title.color = ContextCompat.getColor(context, R.color.purple_200)
    title.textSize = 15f
    title.textAlign = Paint.Align.CENTER
    canvas.drawText("This is sample document which we have created.", 396f, 560f, title)
    pdfDocument.finishPage(myPage)
//    val file = File(directory, "sample.pdf")
    val file: File = File(Environment.getExternalStorageDirectory(), "sample.pdf")


    try {
        pdfDocument.writeTo(FileOutputStream(file))
        Toast.makeText(context, "PDF file generated successfylly", Toast.LENGTH_SHORT).show()
        Log.d("Path" , file.absolutePath)
    } catch (ex: IOException) {
        ex.printStackTrace()
    }
    pdfDocument.close()
}

fun drawableToBitmap(drawable: Drawable): Bitmap? {
    if (drawable is BitmapDrawable) {
        return drawable.bitmap
    }
    val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, 200, 200)
    drawable.draw(canvas)
    return bitmap
}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
