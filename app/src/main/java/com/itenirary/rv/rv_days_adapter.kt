package com.itenirary.rv

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tripnila.R
import com.itenirary.fragments.IteniraryFragment


class rv_days_adapter(var iteniraryFragment: IteniraryFragment, var items: ArrayList<rv_days_data>) : RecyclerView.Adapter<rv_days_adapter.ViewHolder>() {
    private var selectedIndex: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_days,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.apply {
            tvDaySelected.text = item.title
            tvDayOfWeekSelected.text = ("${item.day}")
            tvDayUnSelected.text = item.title
            tvDayOfWeekUnSelected.text = ("${item.day}")

            if (iteniraryFragment.isNewDaySelected) {
                if (position == selectedIndex) {
                    lnDaySelected.visibility = View.VISIBLE
                    lnDayUnSelected.visibility = View.GONE
                } else {
                    lnDaySelected.visibility = View.GONE
                    lnDayUnSelected.visibility = View.VISIBLE
                }
            } else {
                if (item.show) {
                    lnDaySelected.visibility = View.VISIBLE
                    lnDayUnSelected.visibility = View.GONE
                } else {
                    lnDaySelected.visibility = View.GONE
                    lnDayUnSelected.visibility = View.VISIBLE
                }
            }

            lnSelect.setOnClickListener {
                iteniraryFragment.isNewDaySelected = true
                iteniraryFragment.selectedDay = item.day
                selectedIndex = adapterPosition
                notifyDataSetChanged()

                iteniraryFragment.showProgress()
                iteniraryFragment.recalibrateSettings()

                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        iteniraryFragment.clearMap()
                        iteniraryFragment.displayItenirary()
                    },
                    1000 // value in milliseconds
                )
            }
        }
    }


    override fun getItemCount(): Int = items.size

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val lnSelect : LinearLayout = itemView.findViewById(R.id.lnSelect)
        val lnDaySelected : LinearLayout = itemView.findViewById(R.id.lnDaySelected)
        val lnDayUnSelected : LinearLayout = itemView.findViewById(R.id.lnDayUnSelected)
        val tvDayUnSelected: TextView = itemView.findViewById(R.id.tvDayUnSelected)
        val tvDayOfWeekUnSelected: TextView = itemView.findViewById(R.id.tvDayOfWeekUnSelected)
        val tvDaySelected: TextView = itemView.findViewById(R.id.tvDaySelected)
        val tvDayOfWeekSelected: TextView = itemView.findViewById(R.id.tvDayOfWeekSelected)
    }
}