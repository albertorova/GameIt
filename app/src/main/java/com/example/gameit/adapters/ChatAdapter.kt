package com.example.gameit.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.gameit.R
import com.example.gameit.databinding.ItemChatBinding
import com.example.gameit.models.Mensaje

class ChatAdapter(private val mDataSet: ArrayList<Mensaje>) :
    RecyclerView.Adapter<ChatAdapter.MainViewHolder>() {

    var context: Context? = null

    private var TAG = "ChatAdapter"

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

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
    }

    override fun getItemCount(): Int {
        return mDataSet.size ?: 0
    }

    inner class MainViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        //val card = v.findViewById(R.id.chatCard) as CardView

        val b = ItemChatBinding.bind(v)

        fun bindItems(data: Mensaje) {

       /*     when (data.juego) {
                "Fortnite" -> {
                    b.chatCard.setCardBackgroundColor(ContextCompat.getColor(context!!, R.color.y))
                }
                "Brawl Stars" -> {
                    b.chatCard.setCardBackgroundColor(ContextCompat.getColor(context!!, R.color.y))
                }
                "Mario Kart" -> {
                    b.chatCard.setCardBackgroundColor(ContextCompat.getColor(context!!, R.color.y))
                }
                "Garena FF" -> {
                    b.chatCard.setCardBackgroundColor(ContextCompat.getColor(context!!, R.color.y))
                }
                "Clash of Clans" -> {
                    b.chatCard.setCardBackgroundColor(ContextCompat.getColor(context!!, R.color.y))
                }
                "Clash Royale" -> {
                    b.chatCard.setCardBackgroundColor(ContextCompat.getColor(context!!, R.color.y))
                }
                "Hearthstone" -> {
                    b.chatCard.setCardBackgroundColor(ContextCompat.getColor(context!!, R.color.y))
                }
                "FIFA Mobile" -> {
                    b.chatCard.setCardBackgroundColor(ContextCompat.getColor(context!!, R.color.y))
                }
                "Call of Duty" -> {
                    b.chatCard.setCardBackgroundColor(ContextCompat.getColor(context!!, R.color.y))
                }
                "PUBG MOBILE" -> {
                    b.chatCard.setCardBackgroundColor(ContextCompat.getColor(context!!, R.color.y))
                }
            }*/

            b.chatName.text = data.usuario
            b.chatMessage.text = data.mensaje
            b.chatDate.text = data.fecha?.toGMTString()


        }
    }
}
