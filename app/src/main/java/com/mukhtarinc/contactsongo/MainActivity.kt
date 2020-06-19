package com.mukhtarinc.contactsongo

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.mukhtarinc.contactsongo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED){

            Toast.makeText(this,"Permission",Toast.LENGTH_SHORT).show()
            ActivityCompat.requestPermissions(this,Array(1){Manifest.permission.READ_CONTACTS},101)

        }



        binding.navView.setOnNavigationItemReselectedListener {

            when(it.itemId){

                R.id.contacts -> Toast.makeText(this,"Contacts",Toast.LENGTH_LONG).show()
                R.id.settings -> Toast.makeText(this,"Settings",Toast.LENGTH_LONG).show()

            }
        }

        binding.btnSync.setOnClickListener {
           // Toast.makeText(this, "Sync",Toast.LENGTH_LONG).show()
            val scanFragment : Fragment = ScanFragment()

            val fragmentManager: FragmentManager = supportFragmentManager

            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

            fragmentTransaction.replace(R.id.frame_container,scanFragment)

            fragmentTransaction.commit()

        }
    }


}