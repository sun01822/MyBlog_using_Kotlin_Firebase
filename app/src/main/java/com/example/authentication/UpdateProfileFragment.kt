package com.example.authentication

import android.app.Instrumentation.ActivityResult
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.authentication.databinding.FragmentUpdateProfileBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage


class UpdateProfileFragment : Fragment() {
    val db = Firebase.firestore
    private lateinit var binding : FragmentUpdateProfileBinding
    private var selectImage : Uri? = null
    private lateinit var imageLink : String
    private lateinit var email: String
    private var checker : Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateProfileBinding.inflate(layoutInflater)
        val bundle = arguments
        if (bundle != null) {
            val data = bundle.getString("email").toString()
            email = data.toString()
            setAllProfileData(data)
        }
        refreshApp()
        binding.setImage.setOnClickListener {
            resultLauncher.launch("image/*")
            checker = 1
        }

        binding.update.setOnClickListener {
            binding.update.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
            if(checker == 0){
                updateAllDataToDatabaseWithoutPhoto()
                loadingFragment()
            }
            else{
                val storage = FirebaseStorage.getInstance().getReference("images/${selectImage!!.path}")
                storage.putFile(selectImage!!).addOnCompleteListener{
                    storage.downloadUrl.addOnSuccessListener {uri ->
                        var map = HashMap<String, Any>()
                        map["pic"] = uri.toString()
                        imageLink = map["pic"].toString()
                        updateAllDataToDatabase(imageLink)
                        loadingFragment()
                    }.addOnFailureListener {

                    }
                    Toast.makeText(requireContext(), "Image upload successfully", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener{
                    Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
            checker = 0
        }

        return binding.root
    }

    private fun loadingFragment(){
        Handler(Looper.getMainLooper()).postDelayed({
            val fragment = ProfileFragment()
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
        },200)
    }

    private fun updateAllDataToDatabaseWithoutPhoto(){
        val updates = hashMapOf<String, Any>(
            "name" to binding.setName.text.toString(),
            "address" to binding.setAddress.text.toString(),
        )
        db.collection("Profile").document(email).update(updates)
    }
    private fun updateAllDataToDatabase(imageLink: String) {
        val updates = hashMapOf<String, Any>(
                "name" to binding.setName.text.toString(),
                "address" to binding.setAddress.text.toString(),
                "image" to imageLink
        )
        db.collection("Profile").document(email).update(updates)
    }

    private fun refreshApp() {
        val swipeToRefresh = binding.swipeToRefresh
        swipeToRefresh.setOnRefreshListener {
            swipeToRefresh.isRefreshing = false
        }

    }
    private fun setAllProfileData(data: String) {
        db.collection("Profile").document(data).get().addOnCompleteListener {
            val data = it.result.toObject(ProfileModel::class.java)
            if(data?.image?.isEmpty() == false){
                Glide.with(this).load(data?.image).into(binding.setImage)
            }
            else{
                Glide.with(this).load(R.drawable.upload_bg).into(binding.setImage)
            }
            binding.setName.setText(data?.name)
            binding.setAddress.setText(data?.address)
        }.addOnFailureListener {
            Toast.makeText(requireContext(),it.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private var resultLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ){
        selectImage = it
        binding.setImage.setImageURI(selectImage)
    }


}