package com.mikail.chatme.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mikail.chatme.R
import com.mikail.chatme.models.User
import com.squareup.picasso.Picasso
//import kotlinx.android.synthetic.main.activity_chats.view.userImage
import kotlinx.android.synthetic.main.users.view.*
import kotlinx.android.synthetic.main.users.view.username

class UsersAdapter(private val ItemsList: List<User>, val listener: OnUserClick) :
    RecyclerView.Adapter<UsersAdapter.RecyclerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {

        val items = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.users, parent
                , false
            )

        return RecyclerViewHolder(items)

    }

    override fun getItemCount(): Int {
        return ItemsList.size
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {

        val currentItem = ItemsList[position]
        holder.initialise(ItemsList.get(position), listener)

    }

    class RecyclerViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        private val username: TextView = itemview.username
        private val image: ImageView = itemview.userImage
        private val stack: TextView = itemview.stack
        fun initialise(datamodel: User, listener: OnUserClick) {
            username.text = datamodel.username
            val mstack = datamodel.stack + "  Developer"
            stack.text = mstack

            if (datamodel.userImage == "default") {
                image.setImageResource(R.drawable.defaultimage)
            } else {
                Picasso.get().load(datamodel.userImage).into(image)
            }
            itemView.setOnClickListener {
                listener.onUserClick(datamodel, adapterPosition)
            }
//            itemView.setOnClickListener {
//                listener.onUserClick(datamodel, adapterPosition)
//            }

        }
    }
}

interface OnUserClick {
    fun onUserClick(datamodel: User, position: Int)
}
