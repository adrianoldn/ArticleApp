package com.example.articleapp.util

import android.R.attr
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat


object UtilArticle {
    val api_key = "b1176f48d6294070bf654ebae69b1312"
    val domain = "wsj.com"

    suspend fun saveImage(image: Bitmap, id: Long, context: Context) {
        withContext(Dispatchers.Default) {
            var savedImagePath: String? = null
            val imageFileName = "JPEG_" + "IMAGE${id}" + ".jpg"
            val storageDir = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .toString()
            )
            var success = true
            if (!storageDir.exists()) {
                success = storageDir.mkdirs()
            }
            if (success) {
                showMessage(context)
                val imageFile = File(storageDir, imageFileName)
                try {
                    val fOut: OutputStream = FileOutputStream(imageFile)
                    image.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                    fOut.close()


                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }

    }

    suspend fun showMessage(context: Context){
        withContext(Dispatchers.Main){
            Toast.makeText(context, "image saved in Download", Toast.LENGTH_LONG).show()
        }
    }

    fun formatDate(data: String): String {
        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val date =
            SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(inputDateFormat.parse(data))
        return date
    }


    fun shareImage(context: Context, link: String) {
        Glide.with(context)
            .asBitmap()
            .load(link)
            .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(object : SimpleTarget<Bitmap>(250, 250){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.putExtra(Intent.EXTRA_TEXT, "Hey view/download this image")
                    val path: String = MediaStore.Images.Media.insertImage(
                        context.getContentResolver(),
                        resource,
                        "",
                        null
                    )
                    val screenshotUri: Uri = Uri.parse(path)

                    intent.putExtra(Intent.EXTRA_STREAM, screenshotUri)
                    intent.type = "image/*"

                    startActivity(context, Intent.createChooser(intent, "Share image via..."), null)
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                }

            })
    }
}