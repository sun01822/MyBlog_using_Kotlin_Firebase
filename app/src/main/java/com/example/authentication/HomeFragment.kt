package com.example.authentication

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.authentication.databinding.FragmentHomeBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    val db = Firebase.firestore
    lateinit var dataList : ArrayList<PostModel>
    private lateinit var adapter: BlogAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        refreshApp()
        showAllBlog()
        return binding.root
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun refreshApp() {
        val swipeToRefresh = binding.swipeToRefresh
        swipeToRefresh.setOnRefreshListener {
            showAllBlog()
            binding.blogRecyclerView.adapter?.notifyDataSetChanged()
            swipeToRefresh.isRefreshing = false
        }

    }

    private fun showAllBlog(){
        dataList = arrayListOf()
        db.collection("Post").get().addOnSuccessListener {
            if(it.isEmpty){

            }
            else{
                it.documents.forEach{singleData->
                    val model = singleData.toObject(PostModel::class.java)
                    if(model != null){
                        dataList.add(model)
                    }
                }
                initRecyclerView()
            }
        }.addOnFailureListener {

        }

    }

    private fun initRecyclerView() {
        binding.blogRecyclerView.hasFixedSize()
        binding.blogRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        dataList.shuffle()
        adapter = BlogAdapter(dataList)
        binding.blogRecyclerView.adapter = adapter

        adapter.setOnClickListener(object : BlogAdapter.OnClickListener{
            override fun onClick(position: Int, post: PostModel) {
                val bundle = Bundle()
                bundle.putString("postDescription", post.postDescription)
                bundle.putString("postImage", post.postImage)
                val fragment = DetailScreenFragment()
                fragment.arguments = bundle
                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
            }

            override fun onLikeClick(position: Int, post: PostModel) {
                var like = post.postLikes?.toInt()!!
                like++

            }

            override fun onLoveClick(position: Int, post: PostModel) {

            }


            override fun onUnlikeClick(position: Int, post: PostModel) {

            }

        })

    }

}