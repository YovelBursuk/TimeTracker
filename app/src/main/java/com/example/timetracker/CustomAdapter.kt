package com.example.timetracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(val dataSet: ArrayList<DataModel>):
    RecyclerView.Adapter<CustomAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cardView: CardView? = null
        var textViewName: TextView? = null
        var textViewDescription: TextView? = null
        var imageViewIcon: ImageView? = null

        init {
            cardView = itemView.findViewById(R.id.card_view)
            textViewName = itemView.findViewById(R.id.textViewName)
            textViewDescription = itemView.findViewById(R.id.textViewDescription)
            imageViewIcon = itemView.findViewById(R.id.imageView)

//            itemView.setOnClickListener {
//
//            }
        }
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.cards_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(@NonNull holder: MyViewHolder, position: Int) {
        val textViewName: TextView? = holder.textViewName
        val textViewDescription: TextView? = holder.textViewDescription
        val imageView: ImageView? = holder.imageViewIcon

        textViewName?.text = dataSet[position].name
        textViewDescription?.text = dataSet[position].description
        imageView?.setImageResource(dataSet[position].image)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }


}