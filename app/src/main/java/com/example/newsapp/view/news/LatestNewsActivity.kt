package com.example.newsapp.view.news

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.database.PrefManager
import com.example.newsapp.model.LatestNews
import com.example.newsapp.databinding.ActivityLatestNewsBinding
import com.example.newsapp.view.home.HomeScreenActivity

class LatestNewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLatestNewsBinding
    private lateinit var backBtn: ImageButton
    private var latestNews: LatestNews? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLatestNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        backBtn = findViewById(R.id.back_btn)
        backBtn.setOnClickListener {
            val intent = Intent(this@LatestNewsActivity, HomeScreenActivity::class.java)
            startActivity(intent)
        }

        latestNews = intent.getSerializableExtra("LatestNews") as LatestNews?

        val pHeading = binding.tvHeading
        val pNews = binding.tvNews
        val pDate = binding.tvDate
        val pPublisher = binding.tvPublisher
        val pImage = binding.imageHeading

        pHeading.text = latestNews?.heading
        pDate.text = latestNews?.date
        pPublisher.text = latestNews?.publisher

        pNews.apply {
            latestNews?.let { loadUrl(it.contentUrl) }
        }

        Glide.with(this@LatestNewsActivity).load(latestNews?.titleImage).into(pImage);
    }
}