package com.example.authentication



import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.authentication.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView
import java.security.AccessController.getContext


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding : ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var navigationView: NavigationView
    private lateinit var email : String
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        drawerLayout = findViewById(R.id.drawer_layout)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val sharedPreference =  getSharedPreferences("secret", Context.MODE_PRIVATE)
        email = sharedPreference.getString("email", null).toString()

        relaodAllData(email)

        binding.toolbar.setOnClickListener {
            relaodAllData(email)
        }

        val sharedPreference2 =  getSharedPreferences("secretDB", Context.MODE_PRIVATE)
        var editor = sharedPreference2.edit()
        editor.putString("email", email)
        editor.commit()
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        setFragment(HomeFragment())
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun relaodAllData(email : String){
        db.collection("Profile").document(email.toString()).get().addOnCompleteListener {
            val data = it.result.toObject(ProfileModel::class.java)
            navigationView = findViewById(R.id.nav_view)
            val headerLayout = navigationView.getHeaderView(0)
            val profileName = headerLayout.findViewById<TextView>(R.id.profile_name)
            val profileEmail = headerLayout.findViewById<TextView>(R.id.profile_email)
            val profileImage = headerLayout.findViewById<CircleImageView>(R.id.profile_image)
            if(data?.image?.isEmpty() == false){
                Glide.with(this).load(data?.image).into(profileImage)
            }
            else{
                Glide.with(this).load(R.drawable.profile_default).into(profileImage)
            }
            profileName.text = data?.name
            profileEmail.text = data?.email
        }.addOnFailureListener {
            Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> setFragment(HomeFragment())
            R.id.nav_profile -> setFragment(ProfileFragment())
            R.id.nav_settings -> setFragment(SettingsFragment())
            R.id.nav_post ->setFragment(PostFragment())
            R.id.nav_about -> setFragment(AboutFragment())
            R.id.nav_logout -> {
                firebaseAuth.signOut()
                this.getSharedPreferences("secret", 0).edit().clear().apply();
                this.getSharedPreferences("secretDB", 0).edit().clear().apply();
                Toast.makeText(this, "Logout successfully", Toast.LENGTH_SHORT).show();
                startActivity(Intent(this, DemoActivity::class.java))
                finish()
            }
        }
        relaodAllData(email)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    private fun setFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
    }
}