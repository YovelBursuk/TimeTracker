package com.example.timetracker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class CustomDropdownAdapter(val context: Context, var dataSet: List<String>): BaseAdapter() {
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ItemHolder
        if (convertView == null) {
            view = inflater.inflate(R.layout.dropdown_item, parent, false)
            holder = ItemHolder(view)
            view?.tag = holder
        } else {
            view = convertView
            holder = view.tag as ItemHolder
        }
        holder.label.text = dataSet[position]

        val id = CATEGORIES_ICONS_MAPPING[dataSet[position]] ?: R.drawable.ic_launcher
        holder.img.setBackgroundResource(id)

        return view
    }

    override fun getItem(position: Int): Any {
        return dataSet[position];
    }

    override fun getCount(): Int {
        return dataSet.size;
    }

    override fun getItemId(position: Int): Long {
        return position.toLong();
    }

    private class ItemHolder(row: View?) {
        val label: TextView
        val img: ImageView

        init {
            label = row?.findViewById(R.id.dropdown_item_text) as TextView
            img = row.findViewById(R.id.dropdown_item_image) as ImageView
        }
    }
}