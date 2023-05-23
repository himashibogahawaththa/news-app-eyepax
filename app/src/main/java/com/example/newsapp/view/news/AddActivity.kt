package com.example.newsapp.view.news

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.newsapp.database.AppDatabase
import com.example.newsapp.model.News
import com.example.newsapp.databinding.ActivityAddBinding
import kotlinx.coroutines.launch

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    private var news: News? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        news = intent.getSerializableExtra("Data") as News?

        if (news == null) binding.btnAddOrUpdate.text = "Add News"
        else {
            binding.btnAddOrUpdate.text = "Update"
            binding.edHeadline.setText(news?.heading.toString())
            binding.edContent.setText(news?.content.toString())
            binding.edDate.setText(news?.date.toString())
            binding.edPublisher.setText(news?.publisher.toString())

        }

        binding.btnAddOrUpdate.setOnClickListener { addNews() }
    }

    private fun addNews() {
        //val titleImage = binding.edImage.text.toString()
        val heading = binding.edHeadline.text.toString()
        val content = binding.edContent.text.toString()
        val publisher = binding.edPublisher.text.toString()
        val date = binding.edDate.text.toString()

        lifecycleScope.launch{
            if (news == null){
                val news = News(heading = heading, content = content , publisher = publisher, date = date, titleImage = "", description = "", contentUrl = "")
                AppDatabase(this@AddActivity).getNewsDao().addNews(news)
                finish()
            }
            else{
                val n = News(heading, content, date, publisher, "", "", "")
                n.id = news?.id ?: 0
                AppDatabase(this@AddActivity).getNewsDao().updateNews(n)
                finish()
            }
        }
    }
}