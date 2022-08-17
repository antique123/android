package com.sesac.week3.activities

import android.Manifest
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.view.WindowInsetsController
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.sesac.week3.Constant
import com.sesac.week3.adapters.GalleryAdapter
import com.sesac.week3.databinding.ActivityGalleryBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class GalleryActivity : AppCompatActivity() {
    private val binding by lazy { ActivityGalleryBinding.inflate(layoutInflater) }
    private val uris = mutableListOf<String>()
    private var adapter: GalleryAdapter? = null
    private lateinit var currentPhotoPath: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setLightStatusBar()

        checkPermissions()
        initViews()
    }

    private fun initViews() {
        binding.completeButton.setOnClickListener {
            adapter?.let {
                val resultIntent = Intent()
                resultIntent.putStringArrayListExtra(
                    Constant.GALLERY_EXTRA_KEY,
                    ArrayList(it.getSelectedUris())
                )
                setResult(RESULT_OK, resultIntent)
                finish()
            } ?: emptyResult()
        }
        binding.cameraButton.setOnClickListener {
            dispatchTakePictureIntent()
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",   // prefix
            ".jpg",              // suffix
                storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            //Intent 를 처리할 카메라 Activity 가 존재하는지 확인
            takePictureIntent.resolveActivity(packageManager)?.also {
                //사진이 있어야할 파일을 생성한다
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) { null }
                //사진이 저장될 파일이 정상적으로 생성된 경우에만 계속하여 진행
                photoFile?.also {
                    //생성한 파일의 URI 를 획득
                    val photoURI = FileProvider.getUriForFile(
                        this,
                        "com.sesac.week3.fileprovider",
                        it
                    )
                    //카메라 액티비티에게 사진을 저장할 URI 를 Extra Data 로 전달
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, Constant.REQUEST_TAKE_PHOTO)
                }
            }
        }
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            loadPhotos()
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                AlertDialog.Builder(this)
                    .setMessage("사진 정보를 얻기위해 외부 저장소 접근 권한이 필요합니다.")
                    .setPositiveButton("동의하기") { _, _ ->
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                            1000
                        )
                    }
                    .setNegativeButton("거부하기") { _, _ ->
                        emptyResult()
                    }
                    .setCancelable(false)
                    .create()
                    .show()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constant.READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == Constant.READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                loadPhotos()
            } else {
                emptyResult()
            }
        }
    }

    private fun loadPhotos() {
        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val projection = arrayOf(MediaStore.Images.Media._ID)
        val cursor = contentResolver.query(
            collection,
            projection,
            null,
            null,
            MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC"
        )
        cursor?.let {
            val columnIndexId = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext()) {
                val uri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    cursor.getLong(columnIndexId)
                )

                uris.add(uri.toString())
            }
            cursor.close()
        }

        adapter = GalleryAdapter(uris, this)
        binding.galleryRecyclerView.layoutManager = GridLayoutManager(this, 3)
        binding.galleryRecyclerView.adapter = adapter


        //TODO Adapter 에 uris 추가
    }

    private fun emptyResult() {
        val resultIntent = Intent()
        resultIntent.putStringArrayListExtra(Constant.GALLERY_EXTRA_KEY, null)
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    private fun setLightStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val wic = window.decorView.windowInsetsController
            wic?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT
    }
}