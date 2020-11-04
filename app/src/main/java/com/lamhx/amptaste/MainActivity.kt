package com.lamhx.amptaste

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.lamhx.amptaste.databinding.ActivityMainBinding
import com.lamhx.amptaste.diskcache.DiskCacheController
import com.lamhx.amptaste.recyclerview.RecyclerWebViewActivity
import com.lamhx.amptaste.viewpager.ViewPagerActivity


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkAndRequestPermissions()

        configureViewPagerStartButton()
        configureRecyclerViewStartButton()
        configureToggleUseCache()
    }

    private fun configureViewPagerStartButton() {
        // Action
        binding.btnViewpager.setOnClickListener {
            if(!permissionCheckOK) {
                Toast.makeText(this, "Storage permission failed", Toast.LENGTH_SHORT).show()
             return@setOnClickListener
            }
            val intent = Intent(this, ViewPagerActivity::class.java).apply {
            }
            startActivity(intent)
        }
    }
    private fun configureRecyclerViewStartButton() {
        // Action
        binding.btnRecyclerview.setOnClickListener {
            if(!permissionCheckOK) {
                Toast.makeText(this, "Storage permission failed", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intent = Intent(this, RecyclerWebViewActivity::class.java).apply {
            }
            startActivity(intent)
        }
    }
    private fun configureToggleUseCache() {
        binding.switchCache.setOnCheckedChangeListener {_, isChecked ->
            DiskCacheController.useResCached = isChecked
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> {
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Granted.
                    permissionCheckOK = true
                } else {
                    // Denied
                    Toast.makeText(this, "Storage permission failed", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    val REQUEST_ID_MULTIPLE_PERMISSIONS = 1
    private var permissionCheckOK = false
    private fun checkAndRequestPermissions(): Boolean {
        val listPermissionsNeeded: ArrayList<String> = ArrayList()
        val storageWrite = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val storageRead = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        if(storageRead != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if(storageWrite != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }
        if (listPermissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissionsNeeded.toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        } else {
            permissionCheckOK = true
        }
        return true
    }
}