package com.secretsound.hvoicerecorder

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayout
import com.secretsound.hvoicerecorder.databinding.ActivityMainBinding
import com.secretsound.hvoicerecorder.adapter.TabAdapter
import com.secretsound.hvoicerecorder.list.ListFragment
import com.secretsound.hvoicerecorder.record.RecordFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var tabAdapter: TabAdapter

    private lateinit var binding: ActivityMainBinding

    private val permList: Array<String> = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    companion object {
        private const val PERMISSION_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // init tab layout and tab views

        initTabLayout()
        if (!checkForPermission(Manifest.permission.RECORD_AUDIO)) {
            askPermission(permList)

        }
        if (!checkForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            askPermission(permList)
        }
    }

    private fun initTabLayout() {
        // view Pager adapter
        val viewPager = binding.viewPager
        tabAdapter.addFragment(RecordFragment())
        tabAdapter.addFragment(ListFragment())
        viewPager.adapter = tabAdapter
        viewPager.currentItem = 0
        binding.tabLayout.setupWithViewPager(viewPager)
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout))
    }

    private fun checkForPermission(permission: String): Boolean {
        ActivityCompat.checkSelfPermission(this, permission)
        return ContextCompat.checkSelfPermission(
            applicationContext,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun onPermissionResult(granted: Boolean) {
        if (granted) {
            // cool
        } else {
            // again ask for permission
//            Toast.makeText(this, "App Won't Work without Permissions!", Toast.LENGTH_SHORT).show()
            askPermission(permList)
        }
    }

    private fun askPermission(permissions: Array<String>) {
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty()) {
            val bool = grantResults[0] == PackageManager.PERMISSION_GRANTED
            onPermissionResult(bool)
            val bool1 = grantResults[1] == PackageManager.PERMISSION_GRANTED
            onPermissionResult(bool1)
        }
    }
}