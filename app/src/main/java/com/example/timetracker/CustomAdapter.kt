package com.example.timetracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(val categoryDataSet: ArrayList<CategoryDataModel>, val callback: MyIntentCallback):
    RecyclerView.Adapter<CustomAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View, private val listenerCallback: MyIntentCallback) :
        RecyclerView.ViewHolder(itemView) {
        var cardView: CardView? = null
        var textViewName: TextView? = null
        var textViewDescription: TextView? = null
        var imageViewIcon: ImageView? = null
        var cardId: TextView? = null

        init {
            cardView = itemView.findViewById(R.id.card_view)
            textViewName = itemView.findViewById(R.id.textViewName)
            textViewDescription = itemView.findViewById(R.id.textViewDescription)
            imageViewIcon = itemView.findViewById(R.id.imageView)
            cardId = itemView.findViewById(R.id.categoryId)

            itemView.setOnClickListener {
                val b = Bundle()
                b.putString("categoryId", cardId?.text.toString())
                b.putString("categoryName", textViewName?.text.toString())
                listenerCallback.onIntentCallback(b)
            }
        }
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.cards_layout, parent, false)
        return MyViewHolder(view, callback)
    }

    override fun onBindViewHolder(@NonNull holder: MyViewHolder, position: Int) {
        val textViewName: TextView? = holder.textViewName
        val textViewDescription: TextView? = holder.textViewDescription
        val imageView: ImageView? = holder.imageViewIcon
        val cardIdView: TextView? = holder.cardId

        textViewName?.text = categoryDataSet[position].name
        textViewDescription?.text = categoryDataSet[position].description
        imageView?.setImageResource(categoryDataSet[position].image)
        cardIdView?.text = categoryDataSet[position].id
    }

    override fun getItemCount(): Int {
        return categoryDataSet.size
    }


}