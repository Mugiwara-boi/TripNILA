package com.itenirary.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tripnila.R
import com.itenirary.fragments.IteniraryFragment


class rv_steps_adapter(var items: ArrayList<rv_steps_data>) : RecyclerView.Adapter<rv_steps_adapter.ViewHolder>() {
    private var selectedIndex: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_steps,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.apply {
            tvSteps.text = item.step
        }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val tvSteps: TextView = itemView.findViewById(R.id.tvSteps)
    }
}