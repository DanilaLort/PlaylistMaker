package com.example.playlistmaker.data.impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import com.example.playlistmaker.domain.api.TrackCoverRepository
import java.io.File
import java.io.FileOutputStream

class TrackCoverRepositoryImpl(private val context: Context) : TrackCoverRepository {
    override fun saveTrackCover(uri: Uri): Uri {
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        if (!filePath.exists()){
            filePath.mkdirs()
        }
        val file = File(filePath, "${uri.lastPathSegment}.jpg")
        val inputStream = context.contentResolver?.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        return file.toUri()
    }
}