package com.team.app.ui.permission

import androidx.lifecycle.ViewModel


class PermissionViewModel (): ViewModel(){
    val permissionQueue = mutableListOf<String>()

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