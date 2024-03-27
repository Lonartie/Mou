package com.team.app.ui.permission

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel


class PermissionViewModel (): ViewModel(){
    val permissionQueue = mutableStateListOf<String>()

    fun dismissDialog(){
        permissionQueue.removeFirst()
    }
    fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ){
        if(!isGranted && !permissionQueue.contains(permission)) {
           permissionQueue.add(permission)
        }
    }
}