package com.example.newsapp.view.news

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.database.PrefManager
import com.example.newsapp.databinding.ActivityNewsBinding
import com.example.newsapp.model.News
import com.example.newsapp.view.home.HomeScreenActivity


class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding
    private lateinit var backBtn: ImageButton
    private lateinit var favBtn: CheckBox
    private lateinit var prefManager: PrefManager
    private var news: News? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PrefManager(this)

        backBtn = findViewById(R.id.back_btn)
        backBtn.setOnClickListener {
            val intent = Intent(this@NewsActivity, HomeScreenActivity::class.java)
            startActivity(intent)
        }

        news = intent.getSerializableExtra("News") as News?

        val pHeading = binding.tvHeading
        val pNews = binding.tvNews
        val pDate = binding.tvDate
        val pPublisher = binding.tvPublisher
        val pImage = binding.imageHeading

        pHeading.text = news?.heading
        pDate.text = news?.date
        pPublisher.text = news?.publisher

        pNews.apply {
            news?.let { loadUrl(it.contentUrl) }
        }

        Glide.with(this@NewsActivity).load(news?.titleImage).into(pImage);

        favBtn = findViewById(R.id.favBtn)
        favBtn.setOnCheckedChangeListener{checkBox, isChecked ->
            if(isChecked){
                val category = prefManager.getCategory()
                prefManager.setFavoriteCategory(category)

                Toast.makeText(this, "Item added to favorites", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, "Item removed from favorites", Toast.LENGTH_SHORT).show()
            }
        }
    }
}