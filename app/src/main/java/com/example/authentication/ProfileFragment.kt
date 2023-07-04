package com.example.authentication


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.authentication.databinding.FragmentProfileBinding
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView


class ProfileFragment : Fragment() {
    val db = Firebase.firestore
    private lateinit var binding: FragmentProfileBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        val sharedPreferences = requireActivity().getSharedPreferences("secretDB", Context.MODE_PRIVATE)
        val myValue = sharedPreferences.getString("email", null)
        if (myValue != null){
            refreshApp()
            getAllProfileData(myValue.toString())
        }

        binding.update.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("email", myValue) // Replace "key" with an appropriate key and "data" with the actual data
            val fragment = UpdateProfileFragment()
            fragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
        }

        return binding.root
    }

    private fun refreshApp() {
        val swipeToRefresh = binding.swipeToRefresh
        swipeToRefresh.setOnRefreshListener {
            swipeToRefresh.isRefreshing = false
        }

    }
    private fun getAllProfileData(myValue: String){
        db.collection("Profile").document(myValue).get().addOnCompleteListener {
            val data = it.result.toObject(ProfileModel::class.java)
            if(data?.image?.isEmpty() == false){
                Glide.with(this).load(data?.image).into(binding.profileImage)
            }
            else{
                Glide.with(this).load(R.drawable.profile_default).into(binding.profileImage)
            }
            binding.name.text = data?.name
            binding.email.text = data?.email
            binding.address.text = data?.address
            binding.time.text = data?.time
        }.addOnFailureListener {
            Toast.makeText(requireContext(),it.localizedMessage, Toast.LENGTH_SHORT).show()
        }
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

