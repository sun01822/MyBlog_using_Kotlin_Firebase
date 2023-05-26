package com.example.authentication


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.authentication.databinding.FragmentProfileBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ProfileFragment : Fragment() {
    val db = Firebase.firestore
    private lateinit var binding: FragmentProfileBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        val sharedPreferences = requireActivity().getSharedPreferences("secretDB", Context.MODE_PRIVATE)
        val myValue = sharedPreferences.getString("email", null)
        if (myValue != null){
            getAllProfileData(myValue.toString())
        }
        return binding.root
    }
    private fun getAllProfileData(myValue: String){
        db.collection("Profile").document(myValue).get().addOnCompleteListener {
            val data = it.result.toObject(ProfileModel::class.java)
            binding.name.text = data?.name
            binding.email.text = data?.email
            binding.address.text = data?.address
            binding.time.text = data?.time
        }.addOnFailureListener {
            Toast.makeText(requireContext(),it.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }
}