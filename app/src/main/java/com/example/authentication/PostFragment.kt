package com.example.authentication

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.authentication.databinding.FragmentPostBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class PostFragment : Fragment() {
    val db = Firebase.firestore
    private lateinit var binding : FragmentPostBinding
    private var selectImage : Uri?= null
    private lateinit var imageLink : String
    private lateinit var email : String
    private var checker : Int = 0
    val formatter = SimpleDateFormat("yyyy-MM-dd hh:mm:ss a")
    val time = formatter.format(Date())
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostBinding.inflate(layoutInflater)
        refreshApp()
        val sharedPreferences = requireActivity().getSharedPreferences("secretDB", Context.MODE_PRIVATE)
        email = sharedPreferences.getString("email", null).toString()


        binding.setImage.setOnClickListener {
            resultLauncher.launch("image/*")
            checker = 1
        }

        binding.uploadBlog.setOnClickListener {
            binding.uploadBlog.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
            if(checker == 0){
                db.collection("Profile").document(email).get().addOnCompleteListener {
                    val data = it.result.toObject(ProfileModel::class.java)
                    if(data != null){
                        val task : Task<AuthResult>
                        val profileEmail = email
                        val reactChecker = "0"
                        val profileName = data?.name.toString()
                        val profileImage = data?.image.toString()
                        val postTime = time
                        val postDescription = binding.description.text.toString()
                        val postImage = ""
                        val postLikes = "0"
                        val postLoves = "0"
                        val postUnlikes = "0"
                        val model = PostModel(it.toString(), profileEmail, profileImage, profileName, postTime, postDescription, postImage, postLikes, postLoves, postUnlikes, reactChecker)
                        db.collection(email).document(it.toString()).set(model).addOnCompleteListener {
                            Toast.makeText(requireContext(), "Blog uploaded successfully", Toast.LENGTH_LONG).show()
                            loadingFragment()
                        }.addOnFailureListener {
                            Toast.makeText(requireContext(),it.localizedMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
                }.addOnFailureListener {
                    Toast.makeText(requireContext(),it.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
            else{
                val storage = FirebaseStorage.getInstance().getReference("blogimages/${selectImage!!.path}")
                storage.putFile(selectImage!!).addOnCompleteListener{
                    storage.downloadUrl.addOnSuccessListener {uri ->
                        var map = HashMap<String, Any>()
                        map["pic"] = uri.toString()
                        imageLink = map["pic"].toString()
                        db.collection("Profile").document(email).get().addOnCompleteListener {
                            val data = it.result.toObject(ProfileModel::class.java)
                            if(data != null){
                                val profileEmail = email
                                val reactChecker = "0"
                                val profileName = data?.name.toString()
                                val profileImage = data?.image.toString()
                                val postTime = time
                                val postDescription = binding.description.text.toString()
                                val postImage = imageLink
                                val postLikes = "0"
                                val postLoves = "0"
                                val postUnlikes = "0"
                                val model = PostModel(it.toString(), profileEmail, profileImage, profileName, postTime, postDescription, postImage, postLikes, postLoves, postUnlikes, reactChecker)
                                db.collection(email).document(it.toString()).set(model).addOnCompleteListener {
                                    Toast.makeText(requireContext(), "Blog uploaded successfully", Toast.LENGTH_LONG).show()
                                    loadingFragment()
                                }.addOnFailureListener {
                                    Toast.makeText(requireContext(),it.localizedMessage, Toast.LENGTH_SHORT).show()
                                }
                            }
                        }.addOnFailureListener {
                            Toast.makeText(requireContext(),it.localizedMessage, Toast.LENGTH_SHORT).show()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener{
                    Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_SHORT).show()
                }
                checker = 0
            }
        }


        return binding.root
    }
    private fun refreshApp() {
        val swipeToRefresh = binding.swipeToRefresh
        swipeToRefresh.setOnRefreshListener {
            swipeToRefresh.isRefreshing = false
        }

    }


    private fun loadingFragment(){
        Handler(Looper.getMainLooper()).postDelayed({
            val fragment = HomeFragment()
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
        },300)
    }


    private var resultLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ){
        selectImage = it
        binding.setImage.setImageURI(selectImage)
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

