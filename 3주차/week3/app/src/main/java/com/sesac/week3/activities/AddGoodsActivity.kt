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
import com.sesac.week3.databinding.ActivityAddGoodsBinding
import com.sesac.week3.models.Goods

class AddGoodsActivity : AppCompatActivity() {
    private val binding by lazy { ActivityAddGoodsBinding.inflate(layoutInflater) }
    private var productID = Constant.IS_EMPTY_PRODUCT_ID
    private var update: Goods? = null
    private lateinit var launcher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        update = intent.getParcelableExtra(Constant.ADD_GOODS_EXTRA_KEY)
        createLauncher()
        setLightStatusBar()
        initViews()
    }

    private fun createLauncher() {
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == RESULT_OK) {
               val uris = it.data?.getStringArrayListExtra(Constant.GALLERY_EXTRA_KEY)
                binding.selectImageButton.text = "${uris?.size}/10"
                when(uris?.size) {
                    0 -> {}
                    1 -> {
                        binding.firstImageView.visibility = View.VISIBLE
                        binding.firstImageView.tag = uris[0]
                        Glide.with(binding.firstImageView.context)
                            .load(uris[0])
                            .into(binding.firstImageView)
                    }
                    2 -> {
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
                    else -> { }
                }
            }
        }
    }

    private fun initViews() {
        binding.firstImageView.visibility = View.GONE
        binding.secondImageView.visibility = View.GONE
        binding.thirdImageView.visibility = View.GONE

        update?.let {
            productID = it.productID
            binding.selectImageButton.text = "${it.imageURL.size}/10"
            binding.priceEditText.setText(it.price)
            binding.titleEditText.setText(it.explain)
            when(it.imageURL.size) {
                0 -> {}
                1 -> {
                    binding.firstImageView.visibility = View.VISIBLE
                    binding.firstImageView.tag = it.imageURL[0]
                    Glide.with(binding.firstImageView.context)
                        .load(it.imageURL[0])
                        .into(binding.firstImageView)
                }
                2 -> {
                    binding.firstImageView.visibility = View.VISIBLE
                    binding.firstImageView.tag = it.imageURL[0]

                    Glide.with(binding.firstImageView.context)
                        .load(it.imageURL[0])
                        .into(binding.firstImageView)

                    binding.secondImageView.visibility = View.VISIBLE
                    binding.secondImageView.tag = it.imageURL[1]

                    Glide.with(binding.secondImageView.context)
                        .load(it.imageURL[1])
                        .into(binding.secondImageView)
                }
                3 -> {
                    binding.firstImageView.visibility = View.VISIBLE
                    binding.firstImageView.tag = it.imageURL[0]

                    Glide.with(binding.firstImageView.context)
                        .load(it.imageURL[0])
                        .into(binding.firstImageView)

                    binding.secondImageView.visibility = View.VISIBLE
                    binding.secondImageView.tag = it.imageURL[1]

                    Glide.with(binding.secondImageView.context)
                        .load(it.imageURL[1])
                        .into(binding.secondImageView)

                    binding.thirdImageView.visibility = View.VISIBLE
                    binding.thirdImageView.tag = it.imageURL[2]

                    Glide.with(binding.thirdImageView.context)
                        .load(it.imageURL[2])
                        .into(binding.thirdImageView)
                }
                else -> { }
            }
        }

        binding.selectImageButton.setOnClickListener {
            launcher.launch(Intent(this, GalleryActivity::class.java))
        }

        binding.completeButton.setOnClickListener {
            val uris = mutableListOf<String>()
            if(binding.firstImageView.visibility == View.VISIBLE) {
                uris.add(binding.firstImageView.tag.toString())
            }
            if(binding.secondImageView.visibility == View.VISIBLE) {
                uris.add(binding.secondImageView.tag.toString())
            }
            if(binding.thirdImageView.visibility == View.VISIBLE) {
                uris.add(binding.thirdImageView.tag.toString())
            }

            val goods = Goods(
                uris,
                binding.titleEditText.text.toString(),
                "등촌제3동·30초 전",
                binding.priceEditText.text.toString(),
                productID
            )
            val intent = Intent()
            intent.putExtra(Constant.ADD_GOODS_EXTRA_KEY, goods)
            setResult(RESULT_OK, intent)
            finish()
        }
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