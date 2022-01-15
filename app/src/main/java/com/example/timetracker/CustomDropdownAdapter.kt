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
        val vh: ItemHolder
        if (convertView == null) {
            view = inflater.inflate(R.layout.dropdown_item, parent, false)
            vh = ItemHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemHolder
        }
        vh.label.text = dataSet[position]

//        val id = context.resources.getIdentifier(dataSet.get(position).url, "drawable", context.packageName)
        val id = CATEGORIES_ICONS_MAPPING[dataSet[position]] ?: R.drawable.ic_launcher
        vh.img.setBackgroundResource(id)

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