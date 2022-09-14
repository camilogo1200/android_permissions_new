package com.example.permissioncodelab

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.permissioncodelab.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import ironark.com.charge.utils.PermissionsManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var permissionManager: PermissionsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        permissionManager = PermissionsManager(this, activityResultRegistry)
        bindListeners()
    }

    private fun bindListeners() {
        binding.permissionsBtn.setOnClickListener {
            onClickRequestPermission(it)
        }
    }

    private fun onClickRequestPermission(view: View) {
        if (permissionManager.checkPermission(Manifest.permission.CAMERA)) {
            view.showSnackbar(
                view,
                getString(R.string.permission_granted),
                Snackbar.LENGTH_INDEFINITE,
                null
            )
        } else {
            permissionManager.requestPermission(Manifest.permission.CAMERA, {
                view.showSnackbar(
                    view,
                    getString(R.string.permission_required),
                    Snackbar.LENGTH_INDEFINITE,
                    "Ok"
                ) {
                    permissionManager.requestPermission(Manifest.permission.CAMERA)
                }
            }, {
                binding.root.showSnackbar(
                    binding.root,
                    getString(R.string.permission_granted),
                    Snackbar.LENGTH_INDEFINITE,
                    null
                )
            }, {
                binding.root.showSnackbar(
                    binding.root,
                    getString(R.string.permission_not_granted),
                    Snackbar.LENGTH_INDEFINITE,
                    null
                )
            })
        }
    }

    fun View.showSnackbar(
        view: View,
        msg: String,
        length: Int,
        actionMessage: CharSequence?,
        action: ((View) -> Unit)? = null
    ) {
        val snackbar = Snackbar.make(view, msg, length)
        if (actionMessage != null) {
            snackbar.setAction(actionMessage) {
                if (action != null) {
                    action(this)
                }
            }.show()
        } else {
            snackbar.show()
        }
    }
}