package com.example.ekidungmantram.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import com.example.ekidungmantram.R
import com.example.ekidungmantram.admin.fragment.HomeAdminFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_home_admin.*

class HomeAdminActivity : AppCompatActivity() {
    private var doubleBackToExitPressedOnce = false
    private lateinit var toggle  : ActionBarDrawerToggle
    private val fm: FragmentManager = supportFragmentManager
    private val homeFragment        = HomeAdminFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_admin)

        val drawerLayout : DrawerLayout = app_drawer_admin
        val navView : NavigationView = nav_view_admin
        val botView : BottomNavigationView = bottomnav_view_admin

        toggle = ActionBarDrawerToggle(this, drawerLayout,
            R.string.open, R.string.close
        )
        toggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
//                R.id.yadnya_admin
//                R.id.tari_bali_admin -> goToTari()
//                R.id.tabuh_admin -> goToTabuh()
//                R.id.gamelan_bali_admin -> goToGamelan()
//                R.id.kidung_admin -> goToKidung()
//                R.id.mantram_admin -> goToMantram()
//                R.id.prosesi_upacara_admin -> goToProsesi()
//                R.id.logout -> goToLogin()
//                R.id.about_admin -> goToAbout()
            }

            true
        }

        botView.setOnNavigationItemSelectedListener{
            when (it.itemId) {
                R.id.adminHome -> {
                    fm.beginTransaction().show(homeFragment).commit()
                    setTitleActionBar("Beranda Admin")
                }
            }
            true
        }
    }

    private fun setTitleActionBar(s: String) {
        supportActionBar!!.title = s
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