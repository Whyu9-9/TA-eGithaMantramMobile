package com.example.ekidungmantram.user

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.ekidungmantram.AboutAppActivity
import com.example.ekidungmantram.LoginActivity
import com.example.ekidungmantram.R
import com.example.ekidungmantram.databinding.ActivityMainBinding
import com.example.ekidungmantram.user.fragment.HomeFragment
import com.example.ekidungmantram.user.fragment.ListYadnyaFragment
import com.example.ekidungmantram.user.fragment.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var toggle  : ActionBarDrawerToggle
    private var doubleBackToExitPressedOnce = false
    private val homeFragment                = HomeFragment()
    private val searchFragment              = SearchFragment()
    private val listYadnya                  = ListYadnyaFragment()
    private val fm: FragmentManager         = supportFragmentManager
    private var active : Fragment           = homeFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitleActionBar("Beranda")

        fm.beginTransaction().add(R.id.fragment_container, listYadnya).hide(listYadnya).commit()
        fm.beginTransaction().add(R.id.fragment_container, searchFragment).hide(searchFragment).commit()
        fm.beginTransaction().add(R.id.fragment_container,homeFragment).commit()

        val drawerLayout : DrawerLayout    = binding.appDrawer
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
                R.id.tari_bali -> goToTari()
                R.id.tabuh -> goToTabuh()
                R.id.gamelan_bali -> goToGamelan()
                R.id.kidung -> goToKidung()
                R.id.mantram -> goToMantram()
                R.id.prosesi_upacara -> goToProsesi()
                R.id.login -> goToLogin()
                R.id.about -> goToAbout()
            }

            true
        }

        botView.setOnNavigationItemSelectedListener{
            when (it.itemId) {
                R.id.home -> {
                    fm.beginTransaction().hide(active).show(homeFragment).commit()
                    active = homeFragment
                    setTitleActionBar("Beranda")
                }
                R.id.cari -> {
                    fm.beginTransaction().hide(active).show(searchFragment).commit()
                    active = searchFragment
                    setTitleActionBar("Cari Yadnya")
                }
                R.id.list_yadnya -> {
                    fm.beginTransaction().hide(active).show(listYadnya).commit()
                    active = listYadnya
                    setTitleActionBar("Yadnya Ditandai")
                }
            }
            true
        }

    }

    private fun setTitleActionBar(s: String) {
        supportActionBar!!.title = s
    }

    private fun goToTari() {
        val intent = Intent(this, AllTariActivity::class.java)
        startActivity(intent)
    }

    private fun goToTabuh() {
        val intent = Intent(this, AllTabuhActivity::class.java)
        startActivity(intent)
    }

    private fun goToGamelan() {
        val intent = Intent(this, AllGamelanActivity::class.java)
        startActivity(intent)
    }

    private fun goToKidung() {
        val intent = Intent(this, AllKidungActivity::class.java)
        startActivity(intent)
    }

    private fun goToMantram() {
        val intent = Intent(this, AllMantramActivity::class.java)
        startActivity(intent)
    }

    private fun goToProsesi() {
        val intent = Intent(this, AllProsesiActivity::class.java)
        startActivity(intent)
    }

    private fun goToAbout() {
        val intent = Intent(this, AboutAppActivity::class.java)
        startActivity(intent)
    }

    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
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

        Handler(Looper.getMainLooper()).postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }
}