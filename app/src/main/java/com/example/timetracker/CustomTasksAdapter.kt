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

class CustomTasksAdapter(val dataSet: ArrayList<TaskDataModel>) :
    RecyclerView.Adapter<CustomTasksAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var taskCardView: CardView? = null
        var taskTextViewName: TextView? = null
        var taskTextViewDescription: TextView? = null
        var taskImageViewIcon: ImageView? = null
        var taskIdView: TextView? = null

        init {
            taskCardView = itemView.findViewById(R.id.task_card_view)
            taskTextViewName = itemView.findViewById(R.id.taskTextViewName)
            taskTextViewDescription = itemView.findViewById(R.id.taskTextViewDescription)
            taskImageViewIcon = itemView.findViewById(R.id.taskImageView)
            taskIdView = itemView.findViewById(R.id.taskId)

//            itemView.setOnClickListener {
//                val b = Bundle()
//                b.putString("categoryName", textViewName?.text.toString())
//                listenerCallback.onIntentCallback(b)
//            }
        }
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.tasks_cards_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(@NonNull holder: MyViewHolder, position: Int) {
        val textViewName: TextView? = holder.taskTextViewName
        val textViewDescription: TextView? = holder.taskTextViewDescription
        val imageView: ImageView? = holder.taskImageViewIcon
        val taskIdView: TextView? = holder.taskIdView

        textViewName?.text = dataSet[position].name
        textViewDescription?.text = dataSet[position].description
        imageView?.setImageResource(dataSet[position].image)
        taskIdView?.text = dataSet[position].id
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }


}