package com.example.githubusers.ui.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.githubusers.R
import com.example.githubusers.databinding.ActivityMainBinding
import com.example.githubusers.handler.RecyclerViewHelper
import com.example.githubusers.data.response.UserListItem
import com.example.githubusers.ui.viewmodels.UserViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val userViewModel by viewModels<UserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel.users.observe(this){
            RecyclerViewHelper.setupRecyclerView(this@MainActivity,
                binding.recyclerView,
                it as ArrayList<UserListItem>)
        }

        userViewModel.isLoading.observe(this){
            showLoading(it)
        }

        userViewModel.emptyResult.observe(this) { toastText ->
            toastText.getContentIfNotHandled()?.let {
                Toast.makeText(
                    this@MainActivity,
                    it,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    // Set up the search action bar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)

        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val favoritePage = menu.findItem(R.id.favorite)
        val settingsPage = menu.findItem(R.id.settings)

        favoritePage.setOnMenuItemClickListener {
            val favoritePageIntent = Intent(this@MainActivity, FavoriteUserActivity::class.java)
            startActivity(favoritePageIntent)
            true
        }

        settingsPage.setOnMenuItemClickListener {
            val settingsPageIntent = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(settingsPageIntent)
            true
        }

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    userViewModel.searchUsers(query)
                    searchView.clearFocus()
                } else {
                    Toast.makeText(this@MainActivity,
                        "Enter a login to search.",
                        Toast.LENGTH_SHORT).show()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return true
    }

}
