package com.example.githubusers.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.githubusers.databinding.FragmentFollowBinding
import com.example.githubusers.handler.RecyclerViewHelper
import com.example.githubusers.data.response.UserListItem
import com.example.githubusers.ui.viewmodels.FollowViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FollowFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FollowFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private var param1: String? = null
    private var param2: String? = null

    private var position: Int = 0
    private var username: String? = ""

    private lateinit var binding: FragmentFollowBinding

    private val followViewModel by viewModels<FollowViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        binding = FragmentFollowBinding.inflate(layoutInflater)

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username= it.getString(EXTRA_USERNAME)
        }

        username?.let {
            followViewModel.getFollower(it)
            followViewModel.getFollowing(it)
        }

        if (position == 1){
            followViewModel.userfollower.observe(viewLifecycleOwner){
                RecyclerViewHelper.setupRecyclerView(
                    requireActivity(),
                    binding.recyclerViewUser,
                    it.followResponse as ArrayList<UserListItem>
                )
            }

            followViewModel.isLoading.observe(viewLifecycleOwner){
                showLoading(it)
            }

        } else {
            followViewModel.userfollowing.observe(viewLifecycleOwner){
                RecyclerViewHelper.setupRecyclerView(
                    requireActivity(),
                    binding.recyclerViewUser,
                    it.followResponse as ArrayList<UserListItem>
                )
            }

            followViewModel.isLoading.observe(viewLifecycleOwner){
                showLoading(it)
            }
        }
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val ARG_POSITION = "arg_position"
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}