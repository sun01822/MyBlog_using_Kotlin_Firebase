package com.example.authentication


import android.R
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.authentication.databinding.FragmentDetailScreenBinding


class DetailScreenFragment : Fragment() {
    private lateinit var binding:FragmentDetailScreenBinding
    private lateinit var postDescription : String
    private lateinit var postImage : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailScreenBinding.inflate(layoutInflater)
        val bundle = arguments
        if (bundle != null) {
            postDescription = bundle.getString("postDescription").toString()
            postImage = bundle.getString("postImage").toString()

            binding.postDescription.text = postDescription.toString()
            Glide.with(requireContext()).load(postImage).into(binding.postImage)
        }

        return binding.root
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true)
            {
                override fun handleOnBackPressed() {
                    requireActivity().supportFragmentManager.beginTransaction().replace(com.example.authentication.R.id.fragment_container, HomeFragment()).commit()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            this.requireActivity(),
            callback
        )
    }
}