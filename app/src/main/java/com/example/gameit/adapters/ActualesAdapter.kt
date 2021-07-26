package com.example.gameit.adapters

import android.R.attr.label
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.example.gameit.R
import com.example.gameit.databinding.ItemActualesBinding
import com.example.gameit.models.Partida
import com.example.gameit.ui.FindFragment
import com.squareup.picasso.Picasso


class ActualesAdapter(

    private val mDataSet: ArrayList<Partida>,
    var clickAction: (Partida) -> Unit
) :
    RecyclerView.Adapter<ActualesAdapter.MainViewHolder>() {

    var context: Context? = null

    private var TAG = "ActualesAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_actuales, parent, false)
        return MainViewHolder(v)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val data = mDataSet[position]
        data.let {
            holder.bindItems(it)

            holder.actualesCard.setOnClickListener {
                clickAction(data)

                Log.v(TAG, "CLick en la partida actual")

            }

            holder.actualesCard.setOnLongClickListener(View.OnLongClickListener { // obtenemos el texto del textView3

                Log.v(TAG, "CLick laaaaaargo")

                /*val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip: ClipData = ClipData.newPlainText("simple text", data.apuesta.toString())
                clipboard.setPrimaryClip(clip)*/

                false
            })
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

        val b = ItemActualesBinding.bind(v)

        val actualesCard = v.findViewById(R.id.card) as CardView

        fun bindItems(data: Partida) {
            Picasso.get().load(data.portada)
                .into(b.actualesImage)
            b.actualesName.text = data.nombre
            b.actualesCreator.text = data.creador
            b.actualesLevel.text = data.nivel

            when (data.nivel) {
                "PRO" -> {
                    b.actualesLevel.setTextColor(ContextCompat.getColor(context!!, R.color.red))
                }
                "AVERAGE" -> {
                    b.actualesLevel.setTextColor(ContextCompat.getColor(context!!, R.color.yellow))
                }
                "NOOB" -> {
                    b.actualesLevel.setTextColor(ContextCompat.getColor(context!!, R.color.green))
                }
            }

            b.actualesApuesta.text = "${data.apuesta} \uD83D\uDC8E"
            b.actualesCodigo.text = data.codigo
        }
    }
}