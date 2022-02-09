package com.example.ekidungmantram.user

import android.content.Intent
import android.graphics.Color
import android.icu.text.CaseMap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.ekidungmantram.AboutAppActivity
import com.example.ekidungmantram.LoginActivity
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.CardSliderAdapter
import com.example.ekidungmantram.data.CardSliderData
import com.example.ekidungmantram.databinding.ActivityMainBinding
import com.example.ekidungmantram.user.fragment.HomeFragment
import com.example.ekidungmantram.user.fragment.ListYadnyaFragment
import com.example.ekidungmantram.user.fragment.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle  : ActionBarDrawerToggle
    private var doubleBackToExitPressedOnce = false
    private val homeFragment                = HomeFragment()
    private val searchFragment              = SearchFragment()
    private val listYadnya                  = ListYadnyaFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.setTitle("")
        replaceFragment(homeFragment, "Beranda")

        val drawerLayout : DrawerLayout     = binding.appDrawer
        val navView : NavigationView       = binding.navView
        val botView : BottomNavigationView = binding.bottomnavView

        toggle = ActionBarDrawerToggle(this, drawerLayout,
            R.string.open, R.string.close
        )
        toggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.tari_bali -> Toast.makeText(applicationContext,"Menu Tari", Toast.LENGTH_SHORT).show()
                R.id.login -> startActivity(Intent(this, LoginActivity::class.java))
                R.id.about -> startActivity(Intent(this, AboutAppActivity::class.java))
            }

            true
        }

        botView.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.home -> replaceFragment(homeFragment, "Beranda")
                R.id.cari -> replaceFragment(searchFragment, "Pencarian")
                R.id.list_yadnya -> replaceFragment(listYadnya, "Daftar Yadnya")
            }
            true
        }

    }

    private fun replaceFragment(fragment: Fragment, title: String) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
        supportActionBar!!.setTitle(title)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Tekan sekali lagi untuk keluar", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }
}