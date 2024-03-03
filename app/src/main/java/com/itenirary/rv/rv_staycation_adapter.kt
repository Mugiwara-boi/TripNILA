package com.itenirary.rv

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tripnila.R
import com.itenirary.fragments.IteniraryFragment


class rv_staycation_adapter(var iteniraryFragment: IteniraryFragment, var items: ArrayList<rv_staycation_data>) : RecyclerView.Adapter<rv_staycation_adapter.ViewHolder>() {
    private var selectedIndex: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_staycation,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.apply {
            tvHotelName.text = item.hotelName
            tvDisplayDate.text = item.displaySchedule

            crdRow.backgroundTintList = ColorStateList.valueOf(if (position == selectedIndex) {
                Color.parseColor("#F67E24")
            } else {
                Color.WHITE
            })

            crdRowIn.setOnClickListener {
                with(iteniraryFragment) {
                    tempSelectedHostId = item.hostId
                    tempCheckInDate = item.stayInDate
                    tempCheckOutDate = item.stayOutDate
                    tempSelectedHotelName = item.hotelName
                    tempSelectedPersonName = item.firstName
                    tempSelectedAddress = item.address
                    tempStaycationId = item.stayCationId
                    tempStaycationLat = item.staycationLat
                    tempStaycationLng = item.staycationLng
                }
                selectedIndex = adapterPosition
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val tvHotelName : TextView = itemView.findViewById(R.id.tvHotelName)
        val tvDisplayDate : TextView = itemView.findViewById(R.id.tvDisplayDate)
        val crdRow: CardView = itemView.findViewById(R.id.crdRow)
        val crdRowIn: CardView = itemView.findViewById(R.id.crdRowIn)
    }
}