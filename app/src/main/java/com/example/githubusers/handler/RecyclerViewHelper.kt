package com.example.githubusers.handler

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubusers.ui.activity.DetailActivity
import com.example.githubusers.ui.activity.DetailActivity.Companion.EXTRA_USERNAME
import com.example.githubusers.ui.adapters.UserAdapter
import com.example.githubusers.data.response.UserListItem

class RecyclerViewHelper {
    companion object {
        fun setupRecyclerView(context: Context, recyclerView: RecyclerView, itemList: ArrayList<UserListItem>) {
            val userListAdapter = UserAdapter(itemList)
            recyclerView.adapter = userListAdapter
            recyclerView.layoutManager = LinearLayoutManager(context)

            userListAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
                override fun onItemClicked(username: String) {
                    val detailIntent = Intent(context, DetailActivity::class.java)
                    detailIntent.putExtra(EXTRA_USERNAME, username)
                    context.startActivity(detailIntent)
                }
            })
        }
    }
}
