package com.suraj.weathersnap.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream

data class CompressionResult(
    val compressedFile: File,
    val originalSizeKb: Int,
    val compressedSizeKb: Int
)

fun compressImage(context: Context, originalFile: File, onDone: () -> Unit, ): CompressionResult {
    val originalSizeKb = (originalFile.length() / 1024).toInt()
    val bitmap = BitmapFactory.decodeFile(originalFile.absolutePath)
    val compressed = File(context.cacheDir, "compressed_${originalFile.name}")
    FileOutputStream(compressed).use { out ->
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out)
    }
    originalFile.delete()
    onDone()
    return CompressionResult(
        compressedFile = compressed,
        originalSizeKb = originalSizeKb,
        compressedSizeKb = (compressed.length() / 1024).toInt()
    )
}