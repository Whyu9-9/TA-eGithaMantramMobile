package com.example.ekidungmantram.admin

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.ekidungmantram.AboutAppActivity
import com.example.ekidungmantram.R
import com.example.ekidungmantram.admin.adminmanager.AllAdminActivity
import com.example.ekidungmantram.admin.fragment.HomeAdminFragment
import com.example.ekidungmantram.admin.gamelan.AllGamelanAdminActivity
import com.example.ekidungmantram.admin.kidung.AllKidungAdminActivity
import com.example.ekidungmantram.admin.mantram.AllMantramAdminActivity
import com.example.ekidungmantram.admin.prosesiupacara.AllProsesiAdminActivity
import com.example.ekidungmantram.admin.tabuh.AllTabuhAdminActivity
import com.example.ekidungmantram.admin.tari.AllTariAdminActivity
import com.example.ekidungmantram.admin.upacarayadnya.AllYadnyaAdminActivity
import com.example.ekidungmantram.user.MainActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_home_admin.*


class HomeAdminActivity : AppCompatActivity() {
    private var doubleBackToExitPressedOnce = false
    private lateinit var toggle  : ActionBarDrawerToggle
    private val fm: FragmentManager = supportFragmentManager
    private val homeFragment        = HomeAdminFragment()
    private var active : Fragment   = homeFragment
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_admin)
        setTitleActionBar("Beranda Admin")

        fm.beginTransaction().add(R.id.fragment_container_admin,homeFragment).commit()

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

        sharedPreferences = this.getSharedPreferences("is_logged", Context.MODE_PRIVATE)
        val role          = sharedPreferences.getString("ROLE", null)

        if(role != "1") {
            val nav_Menu: Menu = navView.getMenu()
            nav_Menu.findItem(R.id.approval).setVisible(false)
            nav_Menu.findItem(R.id.kelola_admin).setVisible(false)
        }

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.yadnya_admin -> goToYadnya()
                R.id.tari_bali_admin -> goToTari()
                R.id.tabuh_admin -> goToTabuh()
                R.id.gamelan_bali_admin -> goToGamelan()
                R.id.kidung_admin -> goToKidung()
                R.id.mantram_admin -> goToMantram()
//                R.id.approval -> goToKajiMantram()
                R.id.prosesi_upacara_admin -> goToProsesi()
                R.id.kelola_admin -> goToAdmin()
                R.id.logout -> goToLogout()
                R.id.about_admin -> goToAbout()
            }

            true
        }
        botView.selectedItemId = R.id.adminHome
        botView.setOnNavigationItemSelectedListener{
            when (it.itemId) {
                R.id.adminHome -> {
                    fm.beginTransaction().show(homeFragment).commit()
                    active = homeFragment
                    setTitleActionBar("Beranda Admin")
                }
            }
            true
        }
    }
    private fun goToYadnya() {
        val intent = Intent(this, AllYadnyaAdminActivity::class.java)
        startActivity(intent)
    }

    private fun goToProsesi() {
        val intent = Intent(this, AllProsesiAdminActivity::class.java)
        startActivity(intent)
    }

    private fun goToTari() {
        val intent = Intent(this, AllTariAdminActivity::class.java)
        startActivity(intent)
    }

    private fun goToGamelan() {
        val intent = Intent(this, AllGamelanAdminActivity::class.java)
        startActivity(intent)
    }

    private fun goToMantram() {
        val intent = Intent(this, AllMantramAdminActivity::class.java)
        startActivity(intent)
    }

    private fun goToAdmin() {
        val intent = Intent(this, AllAdminActivity::class.java)
        startActivity(intent)
    }

    private fun goToTabuh() {
        val intent = Intent(this, AllTabuhAdminActivity::class.java)
        startActivity(intent)
    }

    private fun goToKidung() {
        val intent = Intent(this, AllKidungAdminActivity::class.java)
        startActivity(intent)
    }

    private fun goToLogout() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Log Out")
            .setMessage("Apakah anda yakin ingin keluar dari halaman admin?")
            .setCancelable(true)
            .setPositiveButton("Iya") { _, _ ->
                sharedPreferences = getSharedPreferences("is_logged", Context.MODE_PRIVATE)
                sharedPreferences.edit().remove("ID_ADMIN").apply()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }.setNegativeButton("Batal") { dialogInterface, _ ->
                dialogInterface.cancel()
            }.show()
    }

    private fun goToAbout() {
        val intent = Intent(this, AboutAppActivity::class.java)
        startActivity(intent)
    }

    private fun setTitleActionBar(s: String) {
        supportActionBar!!.title = s
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