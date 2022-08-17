package com.sesac.week3.activities

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.WindowCompat
import com.bumptech.glide.Glide
import com.sesac.week3.Constant
import com.sesac.week3.databinding.ActivityAddPostBinding
import com.sesac.week3.models.Post

class AddPostActivity : AppCompatActivity() {
    private val binding by lazy { ActivityAddPostBinding.inflate(layoutInflater) }
    private var update: Post? = null
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private var size = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        update = intent.getParcelableExtra(Constant.ADD_POST_EXTRA_KEY)
        initialize()
    }

    private fun initialize() {
        setLightStatusBar()
        createLauncher()
        initViews()
    }

    private fun createLauncher() {
        launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {

                    val uris = result.data?.getStringArrayListExtra(Constant.GALLERY_EXTRA_KEY)
                    uris?.let {
                        bindImages(it)
                    }

                }
            }
    }

    private fun initViews() {


        update?.let {
            binding.bodyEditText.setText(it.body)
            bindImages(it.uris)
        }

        binding.galleryButton.setOnClickListener {
            launcher.launch(Intent(this, GalleryActivity::class.java))
        }

        binding.completeButton.setOnClickListener {

            if(binding.bodyEditText.text.isNotEmpty()) {
                val uris = arrayListOf<String>()
                when (size) {
                    0 -> {
                    }
                    1 -> {
                        uris.add(binding.firstImageView.tag.toString())
                    }
                    2 -> {
                        uris.add(binding.firstImageView.tag.toString())
                        uris.add(binding.secondImageView.tag.toString())
                    }
                    3 -> {
                        uris.add(binding.firstImageView.tag.toString())
                        uris.add(binding.secondImageView.tag.toString())
                        uris.add(binding.thirdImageView.tag.toString())
                    }
                    else -> {
                    }
                }
                val post = Post(
                    "동네질문",
                    "Q.",
                    binding.bodyEditText.text.toString(),
                    uris
                )

                val intent = Intent()
                intent.putExtra(Constant.ADD_POST_EXTRA_KEY, post)
                setResult(RESULT_OK, intent)
                finish()
            } else {
                setResult(RESULT_CANCELED)
                finish()
            }
        }
    }

    private fun bindImages(uris: MutableList<String>) {
        binding.firstImageView.visibility = View.GONE
        binding.secondImageView.visibility = View.GONE
        binding.thirdImageView.visibility = View.GONE

        when (uris.size) {
            0 -> { size = 0 }

            1 -> {
                size = 1
                binding.firstImageView.visibility = View.VISIBLE
                binding.firstImageView.tag = uris[0]

                Glide.with(binding.firstImageView.context)
                    .load(uris[0])
                    .into(binding.firstImageView)
            }

            2 -> {
                size = 2
                binding.firstImageView.visibility = View.VISIBLE
                binding.firstImageView.tag = uris[0]

                Glide.with(binding.firstImageView.context)
                    .load(uris[0])
                    .into(binding.firstImageView)

                binding.secondImageView.visibility = View.VISIBLE
                binding.secondImageView.tag = uris[1]

                Glide.with(binding.secondImageView.context)
                    .load(uris[1])
                    .into(binding.secondImageView)
            }

            3 -> {
                size = 3
                binding.firstImageView.visibility = View.VISIBLE
                binding.firstImageView.tag = uris[0]

                Glide.with(binding.firstImageView.context)
                    .load(uris[0])
                    .into(binding.firstImageView)

                binding.secondImageView.visibility = View.VISIBLE
                binding.secondImageView.tag = uris[1]

                Glide.with(binding.secondImageView.context)
                    .load(uris[1])
                    .into(binding.secondImageView)

                binding.thirdImageView.visibility = View.VISIBLE
                binding.thirdImageView.tag = uris[2]

                Glide.with(binding.thirdImageView.context)
                    .load(uris[2])
                    .into(binding.thirdImageView)
            }
            else -> { size = 0 }
        }

        binding.galleryButton.setText("${size}/3")
    }

    private fun setLightStatusBar() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
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