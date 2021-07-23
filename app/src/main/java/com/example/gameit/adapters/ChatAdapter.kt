package com.example.gameit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gameit.R
import com.example.gameit.databinding.ItemChatBinding
import com.example.gameit.models.Mensaje

class ChatAdapter(private val mDataSet: ArrayList<Mensaje>) :
    RecyclerView.Adapter<ChatAdapter.MainViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        return MainViewHolder(v)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val data = mDataSet[position]
        data.let {
            holder.bindItems(it)

        }
    }

    override fun getItemCount(): Int {
        return mDataSet.size ?: 0
    }

    inner class MainViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        val b = ItemChatBinding.bind(v)

        fun bindItems(data: Mensaje) {
            b.chatName.text = data.usuario
            b.chatMessage.text = data.mensaje
            b.chatDate.text = data.fecha?.toGMTString()
        }
    }
}
