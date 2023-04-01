package com.example.githubusers.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubusers.ui.adapters.MainPagerAdapter
import com.example.githubusers.R
import com.example.githubusers.ui.viewmodels.ViewModelFactory
import com.example.githubusers.databinding.ActivityDetailBinding
import com.example.githubusers.data.response.DetailUserResponse
import com.example.githubusers.ui.viewmodels.DetailUserViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailUserViewModel by viewModels<DetailUserViewModel>(){
        ViewModelFactory.getInstance(application)
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val TAG = "DetailActivity"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Get Intent Extra
        val username = intent.getStringExtra(EXTRA_USERNAME)
        detailUserViewModel.getUserDetails(username!!)

        detailUserViewModel.userdetail.observe(this){
            setUserDetails(it)
        }

        detailUserViewModel.isLoading.observe(this){
            showLoading(it)
        }

        detailUserViewModel.emptyResult.observe(this) { toastText ->
            toastText.getContentIfNotHandled()?.let {
                Toast.makeText(
                    this@DetailActivity,
                    it,
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        //Tab Layout for Follower and Following
        showTabLayout(username)

        binding.fabFavorite.setOnClickListener {
            detailUserViewModel.toggleFavorite()
        }
    }

    private fun showTabLayout(username : String) {
        val mainPagerAdapter = MainPagerAdapter(this@DetailActivity, username)
        val viewPager: ViewPager2 = binding.viewPager

        viewPager.adapter = mainPagerAdapter
        val tabs: TabLayout = binding.tabLayout
        TabLayoutMediator(tabs, viewPager){ tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setUserDetails(details: DetailUserResponse?) {
        binding.displayName.text = details?.name
        binding.username.text = details?.login
        binding.followersCount.text = resources.getString(R.string.follower, details?.followers)
        binding.followingCount.text = resources.getString(R.string.following, details?.following)

        Glide.with(this@DetailActivity)
            .load(details?.avatarUrl)
            .into(binding.profileImage)

        //Add observer to isFavorite LiveData to update FAB when the value of isFavorite changes
        detailUserViewModel.isFavorite.observe(this) { isFavorite ->
            val fabImageResId = if (isFavorite) {
                R.drawable.baseline_favorite_24
            } else {
                R.drawable.baseline_favorite_border_24
            }

            Log.d(TAG, "USER : ${details?.login}, FAVORITE? : $isFavorite")

            binding.fabFavorite.setImageResource(fabImageResId)
        }

    }

}