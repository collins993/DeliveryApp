package io.github.collins993.deliveryapp.driversdashboard

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import io.github.collins993.deliveryapp.R
import io.github.collins993.deliveryapp.authentication.LoginActivity
import io.github.collins993.deliveryapp.databinding.ActivityDashBoardBinding
import io.github.collins993.deliveryapp.databinding.ActivityDriversDashboardBinding
import io.github.collins993.deliveryapp.viewmodel.MyViewModel
import io.github.collins993.deliveryapp.viewmodel.MyViewModelFactory


class DriversDashboardActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityDriversDashboardBinding
    private lateinit var viewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDriversDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarDashBoard2.toolbar)

        val viewModelProviderFactory = MyViewModelFactory(Application())
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(MyViewModel::class.java)


        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_dash_board2)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home1,
                R.id.driverScheduledOrderFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.driver_dash_board, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_settings -> {
                viewModel.signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_dash_board2)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}