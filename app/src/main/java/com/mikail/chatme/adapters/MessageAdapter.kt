package com.mikail.chatme.adapters

import com.mikail.chatme.models.MessageModel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.mikail.chatme.R
import com.squareup.picasso.Picasso

class MessageAdapter(private val messageList: List<MessageModel>) :
    RecyclerView.Adapter<MessageAdapter.RecyclerViewHolder>() {
    lateinit var fUser: FirebaseUser
    private val messageLeft = 0
    private val messageRight = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        if (viewType == messageRight)
        {
            val items = LayoutInflater.from(parent.context)
                .inflate(R.layout.chat_item_right, parent, false)
            return RecyclerViewHolder(items)

        }
        else {
            val items = LayoutInflater.from(parent.context)
                .inflate(R.layout.chat_item_left, parent, false)
            return RecyclerViewHolder(items)
        }


    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {

        val currentItem = messageList[position]
      holder.showMessage.text = currentItem.message

//        if (image.equals("default"))
//        {
//
//            holder.image.setImageResource(R.drawable.person)
//        }
//        else{
//            Picasso.get().load(image).into(holder.image)
//        }

    }

    class RecyclerViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val showMessage: TextView = itemview.findViewById(R.id.message)
         val image: ImageView = itemview.findViewById(R.id.profile_image)


    }

    override fun getItemViewType(position: Int): Int {
        fUser = FirebaseAuth.getInstance().currentUser!!
        if (messageList[position].sender.equals(fUser)) {
            return messageRight
        } else {
            return messageLeft
        }

//        return super.getItemViewType(position)
    }
}

