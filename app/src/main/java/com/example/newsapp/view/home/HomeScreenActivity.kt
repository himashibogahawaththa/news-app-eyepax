package com.example.newsapp.view.home

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import com.example.newsapp.view.adapters.MyAdapter
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.newsapp.*
import com.example.newsapp.adapters.ButtonListAdapter
import com.example.newsapp.view.fragments.UserProfileFragment
import com.example.newsapp.view.adapters.HorizontalRecyclerViewAdapter
import com.example.newsapp.view.news.AddActivity
import com.example.newsapp.view.news.NewsActivity
import com.example.newsapp.api.ApiInterface
import com.example.newsapp.api.Articles
import com.example.newsapp.api.RetrofitClient
import com.example.newsapp.database.AppDatabase
import com.example.newsapp.database.PrefManager
import com.example.newsapp.databinding.ActivityHomePageBinding
import com.example.newsapp.model.CategoryButton
import com.example.newsapp.model.LatestNews
import com.example.newsapp.model.News
import com.example.newsapp.view.news.FavoriteNewsActivity
import com.example.newsapp.view.auth.LoginActivity
import com.example.newsapp.view.news.LatestNewsActivity
import com.example.newsapp.view.splash.MainActivity
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class HomeScreenActivity : AppCompatActivity() {
    private lateinit var prefManager: PrefManager
    private lateinit var username: String
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnRecyclerView: RecyclerView
    private lateinit var hrRecyclerView: RecyclerView
    private lateinit var binding: ActivityHomePageBinding
    private lateinit var buttonArrayList: ArrayList<CategoryButton>
    private lateinit var categoryList: Array<String>
    private lateinit var logoutBtn: LottieAnimationView
    private lateinit var searchBar: SearchView
    private var mAdapter: MyAdapter? = null
    private var hrAdapter: HorizontalRecyclerViewAdapter? = null
    private var currentItem: Int = 0
    private var totalItem: Int = 0
    private var scrollOutItem: Int = 0
    private var count: Int = 0
    private var isScrolling: Boolean = false
    private lateinit var manager: LinearLayoutManager
    lateinit var handler: Handler
    private lateinit var news: News
    private lateinit var favNewsArray: MutableList<News>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createNotificationChannel()

        if (!isNetworkAvailable) {
            AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Internet Connection Alert")
                .setMessage("Please Check Your Internet Connection")
                .setPositiveButton(
                    "Close"
                ) { dialogInterface, i -> finish() }.show()
        } else if (isNetworkAvailable) {
            Toast.makeText(
                this@HomeScreenActivity,
                "Welcome", Toast.LENGTH_LONG
            ).show()
        }

//        searchBar = findViewById(R.id.search_bar)
//        searchBar.setOnQueryTextListener{
//
//        }

        init()

        logoutBtn.setOnClickListener{
            clickLogout(it)
        }

        checkLogin()
        getLatestNewsList()
        getNewsList()

        hrRecyclerView = findViewById((R.id.recyclerViewHorizontal))
        hrRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        hrAdapter = HorizontalRecyclerViewAdapter(this)
        hrRecyclerView.adapter = hrAdapter

        hrAdapter?.setOnClickItem {
            viewLatestNews(it)
        }

        categoryList = arrayOf(
            "All",
            "General",
            "Business",
            "Entertainment",
            "Health",
            "Science",
            "Sports",
            "Technology"
        )

        btnRecyclerView = findViewById(R.id.recyclerViewButtons)
        btnRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        btnRecyclerView.setHasFixedSize(true)
        buttonArrayList = arrayListOf<CategoryButton>()

        getButtonTitle()

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter = MyAdapter(this)
        recyclerView.adapter = mAdapter

        recyclerView.startLayoutAnimation()

        manager = LinearLayoutManager(this)
        recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true
                    Log.d(TAG, "onScrollStateChanged: scrolling true")
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                currentItem = recyclerView.layoutManager!!.childCount
                scrollOutItem = manager.findFirstVisibleItemPosition()
                totalItem = recyclerView.adapter!!.itemCount

                Log.d(TAG, "onScrolled currentItem: $currentItem")
                Log.d(TAG, "onScrolled scrollOutItem: $scrollOutItem")
                Log.d(TAG, "onScrolled totalItem: $totalItem")

                super.onScrolled(recyclerView, dx, dy)
                if (isScrolling && currentItem + scrollOutItem == totalItem){
                    isScrolling = false
                    Log.d(TAG, "onScrollStateChanged: scrolling false")
                    fetchData()
                }
            }
        })

        mAdapter?.setOnClickItem {
            viewNews(it)
        }

        binding.btnAdd.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }

        binding.button2.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, UserProfileFragment())
                .commit()
        }
    }

    private fun fetchData() {
        getNewsList()
    }

    private val isNetworkAvailable: Boolean
        get() {
            val connectivityManager =
                getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivityManager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val capabilities =
                        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                    if (capabilities != null) {
                        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                            return true
                        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                            return true
                        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                            return true
                        }
                    }
                }
            }
            return false
        }

    private fun init(){
        logoutBtn = findViewById(R.id.logoutBtn)
        prefManager = PrefManager(this)
        username = prefManager.getUsername()
    }

    private fun checkLogin(){
        if (prefManager.isLogin() == false){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun clickLogout(view: View){
        prefManager.removeData()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun getLatestNewsList() {
        val retrofit = RetrofitClient.getInstance()
        val apiInterface = retrofit.create(ApiInterface::class.java)
        lifecycleScope.launchWhenCreated {
            try {
                val latestNewsResponse = apiInterface.getAPILatestNews("q","publishedAt")

                if (latestNewsResponse.isSuccessful) {
                    getLatestNews(latestNewsResponse.body()?.articles)
                }else {
                    Toast.makeText(
                        this@HomeScreenActivity,
                        latestNewsResponse.errorBody().toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (Ex: Exception) {
                Log.e("Error", Ex.localizedMessage)
            }
        }
    }

    private fun getLatestNews(articles: List<Articles>?) {
        lifecycleScope.launch {
            articles?.forEach {
                val latest = LatestNews(
                    titleImage = it.urlToImage,
                    heading = it.title,
                    publisher = it.author,
                    date = it.publishedAt,
                    content = it.content,
                    description = it.description,
                    contentUrl = it.url,
                    category = it.category
                )
                AppDatabase(this@HomeScreenActivity).getLatestNewsDao().addLatestNews(latest)
            }
        }
    }

    private fun getButtonTitle() {
        for (i in categoryList.indices){
            val buttonTitle = CategoryButton(categoryList[i])
            buttonArrayList.add(buttonTitle)
        }

        val adapterData = ButtonListAdapter(buttonArrayList)
        btnRecyclerView.adapter = adapterData

        adapterData.setOnItemClickListener(
            object: ButtonListAdapter.OnClickItemListener{
                override fun onItemClick(position: Int) {
                    val item = adapterData.getData(position)
                    setButtonName(item.button_title)

                    buttonArrayList.forEach{
                        if (it.button_title == item.button_title){
                            it.isChecked = true
                            adapterData.notifyDataSetChanged()
                        }
                        else{
                            it.isChecked = false
                        }
                    }
                }
            }
        )
    }

    private fun setButtonName(data: String){
        var filteredResult : List<News>

        if (data == "All"){
            lifecycleScope.launchWhenCreated {
                filteredResult = AppDatabase(this@HomeScreenActivity).getNewsDao().getDatabaseAllNews()
                binding.recyclerView.apply {
                    layoutManager = LinearLayoutManager(this@HomeScreenActivity)
                    adapter = mAdapter
                    setAdapter(filteredResult)
                }
            }
        }
        else{
            filteredResult = AppDatabase(this@HomeScreenActivity).getNewsDao().getDatabaseCategoryNews(data)
            binding.recyclerView.apply {
                layoutManager = LinearLayoutManager(this@HomeScreenActivity)
                adapter = mAdapter
                setAdapter(filteredResult)
            }
            getCategoryNews(data)
        }
    }

    private fun getCategoryNews(category: String) {
        prefManager.setCategory(category)
        val retrofit = RetrofitClient.getInstance()
        val apiInterface = retrofit.create(ApiInterface::class.java)
        lifecycleScope.launchWhenCreated {
            try {
                val response = apiInterface.getAPINews(category)

                if (response.isSuccessful) {
                    insertAllDatabase(response.body()?.articles, category)

                } else {
                    Toast.makeText(
                        this@HomeScreenActivity,
                        response.errorBody().toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (Ex: Exception) {
                Log.e("Error", Ex.localizedMessage)
            }
        }
    }

    //Getting data from the API
    private fun getNewsList() {
        val retrofit = RetrofitClient.getInstance()
        val apiInterface = retrofit.create(ApiInterface::class.java)
        lifecycleScope.launchWhenCreated {
            try {
                val response = apiInterface.getAPINews("a")

                if (response.isSuccessful) {
                    insertAllDatabase(response.body()?.articles, "")

                } else {
                    Toast.makeText(
                        this@HomeScreenActivity,
                        response.errorBody().toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (Ex: Exception) {
                Log.e("Error", Ex.localizedMessage)
            }
        }
    }

    //Save news to the database
    private fun insertAllDatabase(articles: List<Articles>?, category: String) {
        val currentDate = Calendar.getInstance().time

        prefManager.setCategory(category)
        lifecycleScope.launch {
            articles?.forEach {
                val news = News(
                    titleImage = it.urlToImage,
                    heading = it.title,
                    publisher = it.author,
                    date = it.publishedAt,
                    content = it.content,
                    description = it.description,
                    contentUrl = it.url
                )

                if (currentDate.toString() == news.date || prefManager.getCategory() == prefManager.getFavoriteCategory()){
                    showNotification(news)
                }

                AppDatabase(this@HomeScreenActivity).getNewsDao().addNews(news)
            }

//            if (currentDate.toString() == news.date || prefManager.getCategory() == prefManager.getFavoriteCategory()){
//                showNotification(news.heading, news.description.toString(),news)
//            }

        }
    }

    private fun viewNews(it: News){
        val intent = Intent(this@HomeScreenActivity, NewsActivity::class.java)
        intent.putExtra("News",it)
        startActivity(intent)
    }

    private fun viewLatestNews(it: LatestNews){
        val intent = Intent(this@HomeScreenActivity, LatestNewsActivity::class.java)
        intent.putExtra("LatestNews",it)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        recyclerView.startLayoutAnimation()

        lifecycleScope.launch {
            val newsList = AppDatabase(this@HomeScreenActivity).getNewsDao().getDatabaseAllNews()
            val latestNewsList = AppDatabase(this@HomeScreenActivity).getLatestNewsDao().getLatestNews()

            binding.recyclerView.apply {
                layoutManager = LinearLayoutManager(this@HomeScreenActivity)
                adapter = mAdapter
                setAdapter(newsList)
                mAdapter?.setOnActionEditListener {
                    val intent = Intent(this@HomeScreenActivity, AddActivity::class.java)
                    intent.putExtra("Data", it)
                    startActivity(intent)
                }

                mAdapter?.setOnActionDeleteListener {
                    val builder = AlertDialog.Builder(this@HomeScreenActivity)
                    builder.setMessage("Are you sure you want to delete?")
                    builder.setPositiveButton("YES") { p0, p1 ->
                        lifecycleScope.launch{
                            AppDatabase(this@HomeScreenActivity).getNewsDao().deleteNews(it)
                            val list = AppDatabase(this@HomeScreenActivity).getNewsDao().getDatabaseAllNews()
                            setAdapter(list)
                        }
                        p0.dismiss()
                    }

                    builder.setNegativeButton("NO"){ p0, p1 ->
                        p0.dismiss()
                    }

                    val dialog = builder.create()
                    dialog.show()
                }
            }

            binding.recyclerViewHorizontal.apply {
                layoutManager = LinearLayoutManager(this@HomeScreenActivity, LinearLayoutManager.HORIZONTAL, false)
                adapter = hrAdapter
                setHrAdapter(latestNewsList)
            }
        }
    }

    private fun setAdapter(list: List<News>){
        mAdapter?.setData(list)
    }

    private fun setHrAdapter(list: List<LatestNews>){
        hrAdapter?.setData(list)
    }

    //Home Button
    fun onHomeButtonClick(view: View) {
        recyclerView.startLayoutAnimation()
        val intent = Intent(this, HomeScreenActivity::class.java)
        startActivity(intent)
    }

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = NotificationService.channel_name
            val descriptionText = NotificationService.channel_description
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(NotificationService.CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

    }

    private fun showNotification(it: News){

//        val intent = Intent(this@HomeScreenActivity, FavoriteNewsActivity::class.java)
//        intent.putExtra("News",news)
//        startActivity(intent)

        count += 1
        favNewsArray [count] = it

        prefManager.setHeading(it.heading)

        val intent = Intent(this, FavoriteNewsActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(this, NotificationService.CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle(it.heading)
            .setContentText(it.description)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this@HomeScreenActivity)) {
            // notificationId is a unique int for each notification that you must define
            notify(1, builder.build())
        }

    }
}
