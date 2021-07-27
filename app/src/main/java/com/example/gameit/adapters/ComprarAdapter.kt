package com.example.gameit.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.gameit.R
import com.example.gameit.databinding.ItemFindBinding
import com.example.gameit.databinding.ItemPremioBinding
import com.example.gameit.models.Premio
import com.squareup.picasso.Picasso

class ComprarAdapter(

    private val mDataSet: ArrayList<Premio>,
    val activity: FragmentActivity?,
    var clickAction: (Premio) -> Unit
) :
    RecyclerView.Adapter<ComprarAdapter.MainViewHolder>() {

    var context: Context? = null

    private var TAG = "ComprarAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_premio, parent, false)
        return MainViewHolder(v)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val data = mDataSet[position]
        data.let {
            holder.bindItems(it)

            holder.b.comprarCard.setOnClickListener {
                clickAction(data)

                Log.v(TAG, "CLick en la partida del find")

            }
        }
    }

    override fun getItemCount(): Int {
        return mDataSet.size ?: 0
    }

    inner class MainViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        val b = ItemPremioBinding.bind(v)

        fun bindItems(data: Premio) {

            Picasso.get().load(data.imagen)
                .into(b.premioImagen)

            b.premioNombre.text = data.nombre

            b.premioPrecio.text = "${data.precio.toString()} \uD83E\uDE99"

        }
    }
}
