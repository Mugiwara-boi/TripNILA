package com.itenirary.rv

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tripnila.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.itenirary.BusinessActivityDetails
import com.itenirary.firebaseclass.instruction
import com.itenirary.fragments.IteniraryFragment

class rv_itenirary_adapter(var iteniraryFragment: IteniraryFragment, var items: ArrayList<rv_itenirary_data>) : RecyclerView.Adapter<rv_itenirary_adapter.ViewHolder>(),
    OnMapReadyCallback {
    private var selectedIndex: Int = RecyclerView.NO_POSITION
    private lateinit var googleMap: GoogleMap


    interface OnRowUpdateListener {
        fun updateRow(position: Int, instructionList: ArrayList<instruction>)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_itenirary,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        var expectedStartLat: Double
        var expectedStartLng: Double
        var expectedPrevLocation:String
        var optionIndex: Int = 0

        if (position == 0) {
            expectedStartLat = item.selectedLat
            expectedStartLng = item.selectedLng
            expectedPrevLocation = " "
        } else {
            expectedStartLat = items[position - 1].businessLat
            expectedStartLng = items[position - 1].businessLng
            expectedPrevLocation = "Direction from ${items[position - 1].businessName}"
        }

        if (position != selectedIndex) {
            holder.lnStepsHolder.visibility = View.GONE
        }


        val mSpannableString = SpannableString(item.businessName)
        mSpannableString.setSpan(UnderlineSpan(), 0, mSpannableString.length, 0)


        holder.apply {
            tvBusiness.text = mSpannableString
            tvDistance.text = item.distance
            tvCategory.text = item.category
            tvAmount.text = item.amount
            //tvRoute.text = "${item.expectedLat}-${item.expectedLng}    ${item.nextBusinessLat}-${item.nextBusinessLng}"
            tvOpen.text = item.time
            tvDayOfWeek.text = item.day
            tvTime.text = item.arrivalTime.uppercase()

            if (item.instructionList != null) {
                item.instructionList?.forEach {
                    displaySteps(holder,it.instruction)
                }

                tvDirectionButton1.text = RETURN_TO_STAYCATION
                tvDirectionButton2.text = SEE_ROUTE
                lnStepsHolder.visibility = View.VISIBLE
            } else {
                tvDirectionButton1.text = CHECK_DIRECTIONS
                lnStepsHolder.visibility = View.GONE
            }

            tvDirectionButton1.text = CHECK_DIRECTIONS

            lnCheckDirections.setOnClickListener {
                if (expectedPrevLocation != " ") {
                    val optionChoices = arrayOf(
                        expectedPrevLocation,
                        "Direction from Staycation",
                        "Back to Staycation"
                    )


                    showSelectionDialog(itemView.context, optionChoices) { selectedItemIndex ->
                        lnStepsHolder.visibility = View.GONE
                        lnStepsHolder.removeAllViews()
                        iteniraryFragment.showProgress()
                        optionIndex = selectedItemIndex

                        when (selectedItemIndex) {
                            0 -> {
                                iteniraryFragment.getInstruction(
                                    position,
                                    LatLng(expectedStartLat, expectedStartLng),
                                    LatLng(item.businessLat, item.selectedLng)
                                )
                            }

                            1 -> {
                                iteniraryFragment.getInstruction(
                                    position,
                                    LatLng(item.selectedLat, item.selectedLng),
                                    LatLng(item.businessLat, item.businessLng)
                                )
                            }

                            2 -> {
                                iteniraryFragment.getInstruction(
                                    position,
                                    LatLng(item.businessLat, item.businessLng),
                                    LatLng(item.selectedLat, item.selectedLng)
                                )
                            }
                        }

                        lnStepsHolder.visibility = View.VISIBLE
                        tvDirectionButton1.text = optionChoices[selectedItemIndex]

                        selectedIndex = adapterPosition
                        notifyDataSetChanged()
                    }
                } else {
                    val optionChoices = arrayOf(
                        "Direction from Staycation",
                        "Back to Staycation"
                    )


                    showSelectionDialog(itemView.context, optionChoices) { selectedItemIndex ->
                        lnStepsHolder.visibility = View.GONE
                        lnStepsHolder.removeAllViews()
                        iteniraryFragment.showProgress()
                        optionIndex = selectedItemIndex

                        when (selectedItemIndex) {
                            0 -> {
                                iteniraryFragment.getInstruction(
                                    position,
                                    LatLng(item.selectedLat, item.selectedLng),
                                    LatLng(item.businessLat, item.businessLng)
                                )
                            }

                            1 -> {
                                iteniraryFragment.getInstruction(
                                    position,
                                    LatLng(item.businessLat, item.businessLng),
                                    LatLng(item.selectedLat, item.selectedLng)
                                )
                            }

                        }

                        lnStepsHolder.visibility = View.VISIBLE
                        tvDirectionButton1.text = optionChoices[selectedItemIndex]

                        selectedIndex = adapterPosition
                        notifyDataSetChanged()
                    }

                }
            }


            lnSeeRoute.setOnClickListener {
                iteniraryFragment.showProgress()

                when(optionIndex) {
                    0 -> {
                        iteniraryFragment.updateLocation(
                            LatLng(expectedStartLat,expectedStartLng),
                            LatLng(item.businessLat,item.selectedLng)
                        )
                    }
                    1 -> {
                        iteniraryFragment.updateLocation(
                            LatLng(item.selectedLat,item.selectedLng),
                            LatLng(item.businessLat,item.selectedLng)
                        )
                    }
                    2 -> {
                        iteniraryFragment.updateLocation(
                            LatLng(item.businessLat,item.businessLng),
                            LatLng(item.selectedLat,item.selectedLng)
                        )
                    }
                }

                lnStepsHolder.visibility = View.VISIBLE
            }

            tvBusiness.setOnClickListener {
                val context = it.context
                val intent = Intent(context, BusinessActivityDetails::class.java)
                intent.putExtra("businessId", item.businessId)
                intent.putExtra("touristId", iteniraryFragment.selectedHostId)
                context.startActivity(intent)
            }
        }
    }

    fun showSelectionDialog(context: Context, itemList: Array<String>, onItemSelected: (Int) -> Unit) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Select an Option")
        builder.setItems(itemList) { dialog, which ->
            onItemSelected(which)
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun displaySteps(holder:ViewHolder, plainInstruction: String) {
        val valueTV = TextView(holder.itemView.context)
        valueTV.text = "• $plainInstruction"
        valueTV.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        holder.lnStepsHolder.addView(valueTV)
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val tvTime : TextView = itemView.findViewById(R.id.tvTime)
        val tvBusiness : TextView = itemView.findViewById(R.id.tvBusiness)
        val tvDistance : TextView = itemView.findViewById(R.id.tvDistance)
        val tvCategory : TextView = itemView.findViewById(R.id.tvCategory)
        val imgExpand : ImageView = itemView.findViewById(R.id.imgExpand)
        val map : MapView = itemView.findViewById(R.id.map)
        val rvSteps: RecyclerView = itemView.findViewById(R.id.rvSteps)
        val lnRoute: LinearLayout = itemView.findViewById(R.id.lnRoute)
        val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
        val tvRoute: TextView = itemView.findViewById(R.id.tvRoute)
        val lnStepsHolder: LinearLayout = itemView.findViewById(R.id.lnStepsHolder)
        val lnCheckDirections: LinearLayout = itemView.findViewById(R.id.lnCheckDirections)
        val lnSeeRoute: LinearLayout = itemView.findViewById(R.id.lnSeeRoute)
        val tvDirectionButton1: TextView = itemView.findViewById(R.id.tvDirectionButton1)
        val tvDirectionButton2: TextView = itemView.findViewById(R.id.tvDirectionButton2)
        val loader: ProgressBar = itemView.findViewById(R.id.loader)

        val lnRouteReturn: LinearLayout = itemView.findViewById(R.id.lnRouteReturn)
        val lnStepsHolderReturn: LinearLayout = itemView.findViewById(R.id.lnStepsHolderReturn)
        val mapReturn: MapView = itemView.findViewById(R.id.mapReturn)
        val lnReturn: LinearLayout = itemView.findViewById(R.id.lnReturn)
        val tvOpen: TextView = itemView.findViewById(R.id.tvOpen)
        val tvReturn: TextView = itemView.findViewById(R.id.tvReturn)
        val tvDayOfWeek: TextView = itemView.findViewById(R.id.tvDayOfWeek)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap?.mapType = GoogleMap.MAP_TYPE_TERRAIN
    }

    companion object {
        public const val NOT_STARTED = "Not Started"
        public const val COMPLETED = "Completed"
        public const val ONGOING = "ONGOING"
        public const val FAILED = "FAILED"
        public const val CHECK_DIRECTIONS = "Check Directions"
        public const val HIDE_DIRECTIONS = "Hide Direction"
        public const val SEE_ROUTE = "See Route"
        public const val RETURN_TO_STAYCATION = "Return to Staycation"
    }
}