package com.team.app

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PermissionInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.example.compose.AppTheme
import com.team.app.ui.navigation.MouNavHost
import dagger.hilt.android.AndroidEntryPoint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.material3.Button
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.team.app.ui.permission.ActivityTextProv
import com.team.app.ui.permission.NotiPerTextProv
import com.team.app.ui.permission.PermissionDialog
import com.team.app.ui.permission.PermissionViewModel
import com.team.app.ui.permission.SensorTextProv
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.internal.wait


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val needPermission = arrayOf(
        Manifest.permission.POST_NOTIFICATIONS,
        Manifest.permission.BODY_SENSORS,
        Manifest.permission.ACTIVITY_RECOGNITION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                // A surface container using the 'background' color from the theme
                val viewModel = viewModel<PermissionViewModel>()
                val permissionQueue = viewModel.permissionQueue
                val multiplePermissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions(),
                    onResult = { perms ->
                        needPermission.forEach { permission ->
                            viewModel.onPermissionResult(
                                permission = permission,
                                isGranted = perms[permission] == true
                            )
                        }
                    }
                )
                permissionQueue.reversed().forEach {
                        permission,
                    ->
                    PermissionDialog(
                        permission = when (permission) {
                            Manifest.permission.POST_NOTIFICATIONS -> {
                                NotiPerTextProv()
                            }

                            Manifest.permission.BODY_SENSORS -> {
                                SensorTextProv()
                            }

                            Manifest.permission.ACTIVITY_RECOGNITION -> {
                                ActivityTextProv()
                            }

                            else -> return@forEach
                        },
                        isPermaDecline = !shouldShowRequestPermissionRationale(
                            permission
                        ),
                        onDismiss = {
                            viewModel::dismissDialog
                        },
                        onOk = {
                            viewModel.dismissDialog()
                            multiplePermissionLauncher.launch(arrayOf(permission))


                        },
                        onGoToSettings = ::openAppSettings


                    )
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {


                    LaunchedEffect(Unit) {
                        multiplePermissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.POST_NOTIFICATIONS,
                                Manifest.permission.BODY_SENSORS,
                                Manifest.permission.ACTIVITY_RECOGNITION
                            )
                        )
                    }


                    val navController = rememberNavController()
                    MouNavHost(navController = navController)
                }
            }

        }
    }
}

fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}