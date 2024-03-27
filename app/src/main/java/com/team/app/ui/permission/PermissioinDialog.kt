package com.team.app.ui.permission

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.team.app.R
import com.team.app.utils.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionDialog(
    permission: perTextProv,
    isPermaDecline: Boolean,
    onDismiss: () -> Unit,
    onOk: () -> Unit,
    onGoToSettings: () -> Unit
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
                            stringResource(id = R.string.grant_permission)
                        } else {
                            stringResource(id = R.string.ok)
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
            Constants.PERMISSION_DECLINED
        } else {
            Constants.NOTIFICATION_INFO
        }
    }
}

class SensorTextProv : perTextProv {
    override fun getDesc(isPermaDecline: Boolean): String {
        return if (isPermaDecline) {
            Constants.PERMISSION_DECLINED
        } else {
            Constants.TRACK_STEPS_INFO
        }
    }
}

class ActivityTextProv : perTextProv {
    override fun getDesc(isPermaDecline: Boolean): String {
        return if (isPermaDecline) {
            Constants.PERMISSION_DECLINED
        } else {
            Constants.MOVEMENT_INFO
        }
    }
}
