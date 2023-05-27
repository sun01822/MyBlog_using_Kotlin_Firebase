package com.example.authentication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.authentication.databinding.FragmentPostBinding


class PostFragment : Fragment() {
    private lateinit var binding : FragmentPostBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostBinding.inflate(layoutInflater)
        refreshApp()
        return binding.root
    }
    private fun refreshApp() {
        val swipeToRefresh = binding.swipeToRefresh
        swipeToRefresh.setOnRefreshListener {
            swipeToRefresh.isRefreshing = false
        }

    }
}