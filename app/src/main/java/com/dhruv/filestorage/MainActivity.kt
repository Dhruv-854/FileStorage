package com.dhruv.filestorage

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.dhruv.filestorage.ui.theme.FileStorageTheme
import java.io.File
import java.io.FileOutputStream
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.core.app.ActivityCompat

//
//private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34
//
//private fun foregroundPermissionApproved(context: Context): Boolean {
//    val writePermissionFlag = PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
//        context, Manifest.permission.WRITE_EXTERNAL_STORAGE
//    )
//    val readPermissionFlag = PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
//        context, Manifest.permission.READ_EXTERNAL_STORAGE
//    )
//
//    return writePermissionFlag && readPermissionFlag
//}
//
//private fun requestForegroundPermission(context: Context) {
//    val provideRationale = foregroundPermissionApproved(context = context)
//    if (provideRationale) {
//        ActivityCompat.requestPermissions(
//            context as Activity,
//            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
//            REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
//        )
//    } else {
//        ActivityCompat.requestPermissions(
//            context as Activity,
//            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
//            REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
//        )
//    }
//}


class MainActivity : ComponentActivity() {

    private val pageHeight = 1120
    private val pageWidth = 792
    private lateinit var bmp: Bitmap
    private lateinit var scaledbmp: Bitmap
    private val PERMISSION_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)

        bmp = BitmapFactory.decodeResource(resources, R.drawable.ic_launcher_foreground)
        scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false)
        enableEdgeToEdge()
        setContent {
            FileStorageTheme {
                PDFGeneratorApp(
                    onGeneratePDF = { generatePDF() },
                    checkPermissions = { checkPermissions() },
                    requestPermission = { requestPermission() }
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun generatePDF() {
        val pdfDocument = PdfDocument()
        val paint = Paint()
        val title = Paint()
        val myPageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
        val myPage = pdfDocument.startPage(myPageInfo)
        val canvas = myPage.canvas

        canvas.drawBitmap(scaledbmp, 56F, 40F, paint)

        title.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        title.textSize = 15F
        title.color = ContextCompat.getColor(this, R.color.purple_200)
        canvas.drawText("A portal for IT professionals.", 209F, 100F, title)
        canvas.drawText("Geeks for Geeks", 209F, 80F, title)
        title.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
        title.color = ContextCompat.getColor(this, R.color.purple_200)
        title.textSize = 15F
        title.textAlign = Paint.Align.CENTER
        canvas.drawText("This is sample document which we have created.", 396F, 560F, title)

        pdfDocument.finishPage(myPage)

        val file = File(Environment.getExternalStorageDirectory(), "GFG.pdf")
        try {
            pdfDocument.writeTo(FileOutputStream(file))
            Toast.makeText(applicationContext, "PDF file generated..", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(applicationContext, "Fail to generate PDF file..", Toast.LENGTH_SHORT).show()
        }
        pdfDocument.close()
    }

    private fun checkPermissions(): Boolean {
        val writeStoragePermission = ContextCompat.checkSelfPermission(applicationContext, WRITE_EXTERNAL_STORAGE)
        val readStoragePermission = ContextCompat.checkSelfPermission(applicationContext, READ_EXTERNAL_STORAGE)
        return writeStoragePermission == PackageManager.PERMISSION_GRANTED &&
                readStoragePermission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE), PERMISSION_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission Denied..", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}

@Composable
fun PDFGeneratorApp(onGeneratePDF: () -> Unit, checkPermissions: () -> Boolean, requestPermission: () -> Unit) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        if (checkPermissions()) {
            Toast.makeText(context, "Permissions Granted..", Toast.LENGTH_SHORT).show()
        } else {
            requestPermission()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "PDF Generator",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        Button(
            onClick = onGeneratePDF,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Generate PDF", fontSize = 16.sp)
        }
    }
}
