package com.team.app.ui.permission

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionDialog(
    permission: perTextProv,
    isPermaDecline: Boolean,
    onDismiss: () -> Unit,
    onOk: () -> Unit,
    onGoToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    BasicAlertDialog(
        content = {
            Card {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = permission.getDesc(isPermaDecline),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                    Text(
                        text = if (isPermaDecline) {
                            "Grant permission"
                        } else {
                            "OK"
                        },
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (isPermaDecline) {
                                    onGoToSettings()
                                } else {
                                    onOk()
                                }
                            }
                            .padding(16.dp)
                            .background(Color.Red)


                    )
                }
            }

        },
        onDismissRequest = onDismiss,
        modifier = Modifier
    )
}


interface perTextProv {
    fun getDesc(isPermaDecline: Boolean): String
}

class NotiPerTextProv : perTextProv {
    override fun getDesc(isPermaDecline: Boolean): String {
        return if (isPermaDecline) {
            "You declinded the permission permanent, got to your settings and turn it on manually"
        } else {
            "This app wants to send you notifications for new coins and when your mini-me needs you"
        }
    }
}

class SensorTextProv : perTextProv {
    override fun getDesc(isPermaDecline: Boolean): String {
        return if (isPermaDecline) {
            "You declinded the permission permanent, got to your settings and turn it on manually"
        } else {
            "This app wants to track how many steps you take inorder to reward coins to you"
        }
    }
}

class ActivityTextProv : perTextProv {
    override fun getDesc(isPermaDecline: Boolean): String {
        return if (isPermaDecline) {
            "You declinded the permission permanent, got to your settings and turn it on manually"
        } else {
            "This app wants to track your movement to award coins to you"
        }
    }
}
