package com.example.newsapp.view.news

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.database.PrefManager
import com.example.newsapp.databinding.ActivityFavoriteNewsBinding
import com.example.newsapp.model.News
import com.example.newsapp.view.adapters.FavoriteNewsAdapter
import com.example.newsapp.view.home.HomeScreenActivity

class FavoriteNewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteNewsBinding
    private lateinit var backBtn: ImageButton
    private lateinit var prefManager: PrefManager
    private lateinit var favRecycler: RecyclerView
    private var fAdapter: FavoriteNewsAdapter? = null
    private var news: News? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PrefManager(this)

        backBtn = findViewById(R.id.back_btn)
        backBtn.setOnClickListener {
            val intent = Intent(this@FavoriteNewsActivity, HomeScreenActivity::class.java)
            startActivity(intent)
        }

        //val heading = prefManager.getHeading()

        val pHeading = binding.tvHeading
//        val pNews = binding.tvNews
//        val pDate = binding.tvDate
//        val pPublisher = binding.tvPublisher
//        val pImage = binding.imageHeading

        pHeading.text = prefManager.getHeading()
//        pDate.text = news?.date
//        pPublisher.text = news?.publisher
//
//        pNews.apply {
//            news?.let { loadUrl(it.contentUrl) }
//        }
//
//        Glide.with(this@FavoriteNewsActivity).load(news?.titleImage).into(pImage);


//        favNews = intent.getSerializableExtra("FavNews") as News?
//
//        favRecycler = findViewById(R.id.recyclerViewFavorite)
//        favRecycler.layoutManager = LinearLayoutManager(this)
//        fAdapter = favNews?.let { FavoriteNewsAdapter(it) }
//        favRecycler.adapter = fAdapter
//
//        val pHeading = favNews?.heading
//        val pNews = favNews?.content
//        val pDate = favNews?.date
//        val pPublisher = favNews?.publisher
//        val pImage = favNews?.titleImage
//
//        println("$pHeading , $pNews , $pDate , $pPublisher , $pImage")

    }
}