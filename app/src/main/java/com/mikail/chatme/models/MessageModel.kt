package com.mikail.chatme.models

import android.content.BroadcastReceiver

data class MessageModel(var sender:String = "", var receiver:String = "", var message:String = "")
