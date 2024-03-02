package com.itenirary.fragments

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.icu.text.DecimalFormat
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tripnila.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.model.TransitMode
import com.google.maps.model.TravelMode
import com.google.maps.model.VehicleType
import com.itenirary.firebaseclass.business
import com.itenirary.firebaseclass.business_availability
import com.itenirary.firebaseclass.filter_data
import com.itenirary.firebaseclass.instruction
import com.itenirary.firebaseclass.predefined_business_id
import com.itenirary.firebaseclass.servicetag
import com.itenirary.firebaseclass.touristtag
import com.itenirary.rv.rv_days_adapter
import com.itenirary.rv.rv_days_data
import com.itenirary.rv.rv_itenirary_adapter
import com.itenirary.rv.rv_itenirary_data
import com.itenirary.rv.rv_staycation_adapter
import com.itenirary.rv.rv_staycation_data
import com.itenirary.utils.utils
import java.lang.Math.atan2
import java.lang.Math.cos
import java.lang.Math.sin
import java.lang.Math.sqrt
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Date
import java.util.Locale


class IteniraryFragment : Fragment(), OnMapReadyCallback {
    private lateinit var itineraryView: View
    private lateinit var etStartTime: EditText
    private lateinit var etEndTime: EditText
    private lateinit var rlEnterTime: RelativeLayout
    private lateinit var etHour: EditText
    private lateinit var lnAM: LinearLayout
    private lateinit var lnPM: LinearLayout
    private lateinit var lnDisplayResult: LinearLayout
    private lateinit var tvCancel: TextView
    private lateinit var tvOk: TextView
    private lateinit var etBudget:EditText
    private lateinit var crdChoose:CardView
    private lateinit var rvStaycation: RecyclerView
    private lateinit var rlStayCation: RelativeLayout
    private lateinit var lnCancelBook: LinearLayout
    private lateinit var lnConfirmBook: LinearLayout
    private lateinit var llSelectedStaycation: LinearLayout
    private lateinit var tvHotelName: TextView
    private lateinit var tvHost: TextView
    private lateinit var tvAddress: TextView
    private lateinit var imgStaycation: ImageView
    private lateinit var tvSelect: TextView
    private lateinit var lnDetailsPlaceholder: LinearLayout
    private lateinit var lnIteniraryOutput: LinearLayout
    private lateinit var rvDays: RecyclerView
    private lateinit var map: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var rvItenirary: RecyclerView
    private lateinit var chkEntrance: CheckBox

    var isStartTime: Boolean = false
    var isAM: Boolean = false
    var selectedStaycation: Boolean = false
    var selectedAMPM: Int = Color.parseColor("#F67E24")
    var unSelectedAMPM: Int = Color.parseColor("#DFDEDE")
    var touristId: String = ""
    var rvStaycationAdapter: rv_staycation_adapter? = null
    var rvDaysAdapter: rv_days_adapter? = null
    var rvIteniraryAdapter: rv_itenirary_adapter? = null
    var selectedHostId: String = ""
    var tempSelectedHostId: String = ""
    var tempCheckInDate: Date? = null
    var tempCheckOutDate: Date? = null
    var selectedCheckInDate: Date? = null
    var selectedCheckOutDate: Date? = null
    var tempSelectedHotelName: String = ""
    var tempSelectedPersonName: String = ""
    var tempSelectedAddress: String = ""
    var selectedHotelName: String = ""
    var selectedPersonName: String = ""
    var selectedAddress: String = ""
    var tempStaycationId: String = ""
    var selectedStaycationId: String = ""
    var tempStaycationLat: Double? = null
    var tempStaycationLng: Double? = null
    var staycationLat: Double? = null
    var staycationLng: Double? = null
    var selectedDay: String = ""
    var isNewDaySelected: Boolean = false
    val utils = utils()
    var expectedStartLat: Double = 0.0
    var expectedStartLng: Double = 0.0
    val markerList: MutableList<Marker> = mutableListOf()

    var db = FirebaseFirestore.getInstance()
    var staycationList = ArrayList<rv_staycation_data>()
    var daysList = ArrayList<rv_days_data>()
    var iteniraryList = ArrayList<rv_itenirary_data>()
    var finalCurrentTagList = ArrayList<servicetag>()
    var businessList = ArrayList<business>()
    var serviceTagList = ArrayList<servicetag>()
    var touristTagList = ArrayList<touristtag>()
    var busAvailabilityList = ArrayList<business_availability>()

    var iteniraryBankList = ArrayList<rv_itenirary_data>()
    var iteniraryBankList_tempInit = ArrayList<rv_itenirary_data>()
    var iteniraryBankList_temp = ArrayList<rv_itenirary_data>()
    var selectedBusinessIdBasedOnPref = ArrayList<filter_data>()
    var selectedBusinessIdBasedOnAvailability = ArrayList<filter_data>()
    var availableBusinessIdBasedPrefAndSchedule = ArrayList<filter_data>()
    var selectedBusinessIdBasedOnNearestLocation = ArrayList<filter_data>()
    var selectedBusinessIdBasedOn_Pref_Avail_Sched_Nearest = ArrayList<filter_data>()
    var selectedBusinessIdBasedOnBudget = ArrayList<filter_data>()

    var totalAmount:Double = 0.0
    var finalAvailabilityList = ArrayList<servicetag>()
    var finalBusinessList = ArrayList<servicetag>()
    var preIteniraryList = ArrayList<rv_itenirary_data>()
    var tempServiceTagList = ArrayList<servicetag>()
    var predefinedList = ArrayList<predefined_business_id>()

    var dialog: Dialog? = null
    var rowCount: Int = 0

    val instructionList = ArrayList<instruction>()
    val instructionReturnList = ArrayList<instruction>()
    var startTime = ""
    var endTime = ""
    var numberOfIntervals = 0
    var currentTime = ""
    var arrivalTime = ""
    var currentCount = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val utils = utils()

        itineraryView = inflater.inflate(R.layout.fragment_itenirary, container, false)
        touristId = arguments?.getString("touristId").toString()

        itineraryView.apply {
            etStartTime = findViewById(R.id.etStartTime)
            etEndTime = findViewById(R.id.etEndTime)
            rlEnterTime = findViewById(R.id.rlEnterTime)
            etHour = findViewById(R.id.etHour)
            lnAM = findViewById(R.id.lnAM)
            lnPM = findViewById(R.id.lnPM)
            tvCancel = findViewById(R.id.tvCancel)
            tvOk = findViewById(R.id.tvOk)
            lnDisplayResult = findViewById(R.id.lnDisplayResult)
            etBudget = findViewById(R.id.etBudget)
            crdChoose = findViewById(R.id.crdChoose)
            rvStaycation = findViewById(R.id.rvStaycation)
            rlStayCation = findViewById(R.id.rlStayCation)
            lnCancelBook = findViewById(R.id.lnCancelBook)
            lnConfirmBook = findViewById(R.id.lnConfirmBook)
            llSelectedStaycation = findViewById(R.id.llSelectedStaycation)
            tvHotelName = findViewById(R.id.tvHotelName)
            tvHost = findViewById(R.id.tvHost)
            tvAddress = findViewById(R.id.tvAddress)
            imgStaycation = findViewById(R.id.imgStaycation)
            tvSelect = findViewById(R.id.tvSelect)
            lnDetailsPlaceholder = findViewById(R.id.lnDetailsPlaceholder)
            rvDays = findViewById(R.id.rvDays)
            lnIteniraryOutput = findViewById(R.id.lnIteniraryOutput)
            map = findViewById(R.id.map)
            rvItenirary = findViewById(R.id.rvItenirary)
            chkEntrance = findViewById(R.id.chkEntrance)
        }

        map.onCreate(savedInstanceState)
        map.getMapAsync(this)

        etStartTime.setOnClickListener {
            isStartTime = true
            displayTimeDialog()
        }

        etEndTime.setOnClickListener {
            isStartTime = false
            displayTimeDialog()
        }

        lnAM.setOnClickListener {
            isAM = true
            changeColorOfAMPM()
        }

        lnPM.setOnClickListener {
            isAM = false
            changeColorOfAMPM()
        }

        tvOk.setOnClickListener {
            if (etHour.text.toString().toIntOrNull() == null) {
                Toast.makeText(requireContext(),"Please provide a proper hour",Toast.LENGTH_LONG).show()
            } else {
                if (isStartTime) {
                    etStartTime.setText("${if (etHour.text.toString().length == 1) "0" else ""}${etHour.text}:00 ${if (isAM) "AM" else "PM"}")
                } else {
                    etEndTime.setText("${if (etHour.text.toString().length == 1) "0" else ""}${etHour.text}:00 ${if (isAM) "AM" else "PM"}")
                }

                rlEnterTime.visibility = View.GONE
                hideKeyboard(requireActivity())
            }
        }

        tvCancel.setOnClickListener {
            rlEnterTime.visibility = View.GONE
        }

        etHour.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val typedText = s.toString()
                if (typedText.length > 1 && typedText.startsWith("0")) {
                    etHour.setText(typedText.substring(1))
                    etHour.setSelection(etHour.text.length)
                }

                if (etHour.text.toString() != "") {
                    var inputtedHour = etHour.text.toString().toIntOrNull()!!

                    if (inputtedHour <= 0 || inputtedHour > 12) {
                        etHour.setText("")
                    }
                }
            }
        })

        crdChoose.setOnClickListener {
            selectedStaycation = true
            tempSelectedHostId = ""
            loadStaycation()
            rlStayCation.visibility = View.VISIBLE
        }

        lnConfirmBook.setOnClickListener {
            if (tempSelectedHostId.isNullOrEmpty()) {
                Toast.makeText(requireContext(),"Select booked staycation first",Toast.LENGTH_LONG).show()
            } else {
                selectedHostId = tempSelectedHostId
                selectedCheckInDate = tempCheckInDate
                selectedCheckOutDate = tempCheckOutDate
                selectedHotelName = tempSelectedHotelName
                selectedPersonName = tempSelectedPersonName
                selectedAddress = tempSelectedAddress
                selectedStaycationId = tempStaycationId
                staycationLat = tempStaycationLat
                staycationLng = tempStaycationLng
                rlStayCation.visibility = View.GONE
                tvSelect.visibility = View.GONE
                llSelectedStaycation.visibility = View.VISIBLE
                lnDetailsPlaceholder.visibility = View.VISIBLE
                lnIteniraryOutput.visibility = View.GONE
                etBudget.setText("")
                etStartTime.setText("")
                etEndTime.setText("")

                tvHotelName.text = selectedHotelName
                tvHost.text = selectedPersonName
                tvAddress.text = selectedAddress


                db.collection("service_photo")
                    .whereEqualTo("serviceId", selectedStaycationId)
                    .whereEqualTo("photoType", "Cover")
                    .get()
                    .addOnSuccessListener { photoDocument ->
                        for (document in photoDocument) {
                            val photoUrl = document.getString("photoUrl")

                            Glide.with(requireContext())
                                .load(photoUrl)
                                .placeholder(R.drawable.image_placeholder)
                                .into(imgStaycation)
                        }
                    }
                    .addOnFailureListener { exception ->
                        // Handle failure to fetch service photos
                        println("Error fetching service photos: $exception")
                    }

                loadMap(staycationLat!!,staycationLng!!)
            }
        }

        lnCancelBook.setOnClickListener {
            rlStayCation.visibility = View.GONE
        }

        lnDetailsPlaceholder.visibility = View.VISIBLE
        lnIteniraryOutput.visibility = View.GONE

        lnDisplayResult.setOnClickListener {
            if (rlEnterTime.visibility == View.VISIBLE || rlStayCation.visibility == View.VISIBLE) {
                return@setOnClickListener
            }

            if (!selectedStaycation ||
                etStartTime.text.toString().isEmpty() ||
                etEndTime.text.toString().isEmpty() ||
                etBudget.text.toString().isEmpty()) {
                Toast.makeText(requireContext(),"Please fill in required fields",Toast.LENGTH_LONG).show()
            } else if (!utils.isEndTimeGreaterThanStartTime(utils.convert12HourTo24Hour(etStartTime.text.toString()),utils.convert12HourTo24Hour(etEndTime.text.toString()))) {
                Toast.makeText(
                    requireContext(),
                    "Please provide proper start and end time or at least 1 hr schedule",
                    Toast.LENGTH_LONG
                ).show()
            } else if (etStartTime.text.toString() == etEndTime.text.toString()) {
                Toast.makeText(
                    requireContext(),
                    "Please provide at least 1 hr schedule",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                showProgress()
                totalAmount = 0.0
                finalAvailabilityList.clear()
                finalBusinessList.clear()
                preIteniraryList.clear()
                tempServiceTagList.clear()
                predefinedList.clear()

                var dayIndex = 0
                lnDetailsPlaceholder.visibility = View.GONE
                lnIteniraryOutput.visibility = View.VISIBLE
                isNewDaySelected = false
                daysList.clear()
                rvDays?.adapter?.notifyDataSetChanged()
                val startLocalDate = LocalDateTime.ofInstant(selectedCheckInDate!!.toInstant(), ZoneId.of("Asia/Manila")).toLocalDate()

                for (i in 0 until utils.countDaysBetween(selectedCheckInDate!!,selectedCheckOutDate!!)) {
                    val currentLocalDate = startLocalDate.plusDays(i)
                    var day = i + 1
                    val dayOfWeek = currentLocalDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())

                    if (dayIndex == 0) {
                        selectedDay = dayOfWeek
                    }

                    daysList.add(rv_days_data("Day $day",i,dayOfWeek,dayIndex == 0))

                    dayIndex++
                }

                rvDaysAdapter = rv_days_adapter(this,daysList)
                rvDays.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                rvDays.adapter = rvDaysAdapter

                loadItenirary()
            }
        }

        return itineraryView
    }

    fun showProgress() {
        var builder = AlertDialog.Builder(requireContext())
        builder.setView(R.layout.progress)
        dialog = builder.create()
        (dialog as AlertDialog).setCancelable(false)
        (dialog as AlertDialog).show()
    }


    fun hideKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
    }

    fun hideProgress() {
        dialog?.dismiss()
    }

    fun loadItenirary() {
        businessList.clear()
        serviceTagList.clear()
        touristTagList.clear()
        busAvailabilityList.clear()
        rvItenirary?.adapter?.notifyDataSetChanged()

        db.collection("business")
            .get()
            .addOnSuccessListener { businessDocs ->
                for (businessDoc in businessDocs) {
                    if (businessDoc.contains("businessLat")) {
                        if (businessDoc.getDouble("businessLat")!! != 0.0) {
                            if (chkEntrance.isChecked) {
                                if (businessDoc.contains("entranceFee")) {
                                    if (businessDoc.getDouble("entranceFee")!! == 0.0) {
                                        businessList.add(
                                            business(
                                                businessId = businessDoc.id,
                                                businessName = businessDoc.getString("businessTitle")!!,
                                                businessLat = businessDoc.getDouble("businessLat")!!,
                                                businessLng = businessDoc.getDouble("businessLng")!!,
                                                amount = businessDoc.getDouble("minSpend")!!
                                            )
                                        )
                                    }
                                }
                            } else {
                                if (businessDoc.contains("entranceFee")) {
                                    businessList.add(
                                        business(
                                            businessId = businessDoc.id,
                                            businessName = businessDoc.getString("businessTitle")!!,
                                            businessLat = businessDoc.getDouble("businessLat")!!,
                                            businessLng = businessDoc.getDouble("businessLng")!!,
                                            amount = businessDoc.getDouble("minSpend")!!
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                db.collection("service_tag")
                    .get()
                    .addOnSuccessListener { serviceTagDocs ->
                        for (serviceTagDoc in serviceTagDocs) {
                            serviceTagList.add(servicetag(
                                businessId = serviceTagDoc.getString("serviceId")!!,
                                tagName = serviceTagDoc.getString("tagName")!!
                            ))
                        }

                        db.collection("tourist_preference")
                            .get()
                            .addOnSuccessListener { userPrefDocs ->
                                for (userPrefDoc in userPrefDocs) {
                                    if (userPrefDoc.getString("touristId")!! == touristId) {
                                        touristTagList.add(touristtag(
                                            preference = userPrefDoc.getString("preference")!!
                                        ))
                                    }
                                }

                                db.collection("business_availability")
                                    .get()
                                    .addOnSuccessListener { businessAvailabilityDocs ->
                                        for (businessAvailabilityDoc in businessAvailabilityDocs) {
                                            if (businessAvailabilityDoc.contains("day")) {
                                                /*if (businessAvailabilityDoc.getString("day").equals(selectedDay)) {

                                                }*/

                                                busAvailabilityList.add(
                                                    business_availability(
                                                        businessAvailabilityDoc.getString("businessId")!!,
                                                        businessAvailabilityDoc.getString("closingTime")!!,
                                                        businessAvailabilityDoc.getString("day")!!,
                                                        businessAvailabilityDoc.getString("openingTime")!!
                                                    )
                                                )
                                            }
                                        }

                                        recalibrateItenirary()
                                    }
                                    .addOnFailureListener { exception ->
                                        // Handle failure to fetch service photos
                                        println("Error fetching service photos: $exception")
                                    }
                            }
                            .addOnFailureListener { exception ->
                                // Handle failure to fetch service photos
                                println("Error fetching service photos: $exception")
                            }
                    }
                    .addOnFailureListener { exception ->
                        // Handle failure to fetch service photos
                        println("Error fetching service photos: $exception")
                    }
            }
            .addOnFailureListener { exception ->
                // Handle failure to fetch service photos
                println("Error fetching service photos: $exception")
            }
    }

    fun recalibrateItenirary() {
        // Filter out of my preference based on tags of business
        touristTagList.forEach { ttl ->
            serviceTagList.forEach {  stl ->
                if (stl.tagName == ttl.preference) {
                    var isAlreadyAdded = false

                    selectedBusinessIdBasedOnPref.forEach {sbbp ->
                        if (sbbp.businessId == stl.businessId) {
                            isAlreadyAdded = true
                        }
                    }

                    if (!isAlreadyAdded) {
                        selectedBusinessIdBasedOnPref.add(filter_data(stl.businessId,ttl.preference,"",""))
                    }
                }
            }
        }

        println("My Pref : $touristTagList")
        println("Available Business ID based on pref : $selectedBusinessIdBasedOnPref")

        // Filter out business based on available days of week
        busAvailabilityList.forEach { bal ->
            var isAlreadyAdded = false

            daysList.forEach { dl ->
                if (dl.day == bal.day) {
                    val storeOpeningTime = LocalTime.of(utils.convertTo24HourFormat(bal.openingTime.uppercase()), 0) // Store opening time
                    val storeClosingTime = LocalTime.of(utils.convertTo24HourFormat(bal.closingTime.uppercase()), 0) // Store closing time
                    val yourScheduledStartTime = LocalTime.of(utils.convertTo24HourFormat(etStartTime.text.toString()), 0) // Your scheduled start time
                    val yourScheduledEndTime = LocalTime.of(utils.convertTo24HourFormat(etEndTime.text.toString()), 0) // Your scheduled end time

                    if (utils.isStoreOpenDuringYourSchedule(storeOpeningTime, storeClosingTime, yourScheduledStartTime, yourScheduledEndTime)) {
                        selectedBusinessIdBasedOnAvailability.forEach {sbba ->
                            if (sbba.businessId == bal.businessId) {
                                isAlreadyAdded = true
                            }
                        }
                    }
                }
            }

            if (!isAlreadyAdded) {
                selectedBusinessIdBasedOnAvailability.add(filter_data(bal.businessId,"",bal.day,bal.openingTime))
            }
        }

        println("Available Business ID based on sched : $selectedBusinessIdBasedOnAvailability")

        selectedBusinessIdBasedOnPref.forEach {sbbp ->
            var isAlreadyAdded = false

            selectedBusinessIdBasedOnAvailability.forEach { sbba ->
                if (sbba.businessId == sbbp.businessId) {
                    isAlreadyAdded = true
                }
            }

            if (!isAlreadyAdded) {
                availableBusinessIdBasedPrefAndSchedule.add(filter_data(sbbp.businessId,"","",""))
            }
        }

        println("Available Business ID based on available sched and pref : $availableBusinessIdBasedPrefAndSchedule")

        businessList.forEach {bl ->
            if (CalculationByDistance(LatLng(staycationLat!!,staycationLng!!), LatLng(bl.businessLat,bl.businessLng))!! < 10f) {
                selectedBusinessIdBasedOnNearestLocation.add(filter_data(bl.businessId,"","",""))
            }
        }

        println("Available Business ID based on nearest location : $availableBusinessIdBasedPrefAndSchedule")

        selectedBusinessIdBasedOnNearestLocation.forEach { sbbnl ->
            var isAlreadyAdded = false

            availableBusinessIdBasedPrefAndSchedule.forEach { abbpas ->
                abbpas.apply {
                    if (businessId == sbbnl.businessId) {
                        selectedBusinessIdBasedOn_Pref_Avail_Sched_Nearest.forEach {prefinal ->
                            if (prefinal.businessId == businessId) {
                                isAlreadyAdded = true
                            }
                        }
                    }
                }
            }

            if (!isAlreadyAdded) {
                selectedBusinessIdBasedOn_Pref_Avail_Sched_Nearest.add(filter_data(sbbnl.businessId,"","",""))
            }
        }

        println("Available Business ID based on Filter Above : $selectedBusinessIdBasedOn_Pref_Avail_Sched_Nearest")

        businessList.forEach {bl ->
            var isAdded = false

            selectedBusinessIdBasedOn_Pref_Avail_Sched_Nearest.forEach {prefinal ->
                if (prefinal.businessId == bl.businessId) {
                    selectedBusinessIdBasedOnBudget.forEach {sbbob ->
                        if (sbbob.businessId == prefinal.businessId) {
                            if (totalAmount!! < etBudget.text.toString().toDouble()) {
                                totalAmount = totalAmount!! + bl.amount!!
                            } else {
                                isAdded = true
                            }
                        }
                    }
                }
            }

            if (!isAdded) {
                selectedBusinessIdBasedOnBudget.add(filter_data(bl.businessId,"","",""))
            }
        }

        println("Available Business ID based on Budget and other Pref: $selectedBusinessIdBasedOnBudget")

        businessList.forEach { bl ->
            if (totalAmount < etBudget.text.toString().toDouble()) {
                var isAdded = false

                selectedBusinessIdBasedOn_Pref_Avail_Sched_Nearest.forEach { prefinal ->
                    if (prefinal.businessId == bl.businessId) {
                        selectedBusinessIdBasedOnBudget.forEach { sbbob ->
                            if (sbbob.businessId == prefinal.businessId) {
                                if (totalAmount + bl.amount <= etBudget.text.toString().toDouble()) {
                                    totalAmount += bl.amount
                                } else {
                                    isAdded = true
                                }
                            }
                        }
                    }
                }

                if (!isAdded) {
                    selectedBusinessIdBasedOnBudget.add(filter_data(bl.businessId, "", "", ""))
                }
            } else {
                // Break the loop if the total amount exceeds the budget
                return@forEach
            }
        }

        println("Available Business ID based on Filter Above and Budget : $selectedBusinessIdBasedOn_Pref_Avail_Sched_Nearest")

        //Bank of all the business available based on query above
        businessList.forEach { bl ->
            var isMatch = false

            selectedBusinessIdBasedOnBudget.forEach { sbbob ->
                if (sbbob.businessId == bl.businessId) {
                    isMatch = true
                }
            }

            if (isMatch) {
                if (!iteniraryBankList.any { it.businessId == bl.businessId }) {
                    var category = "No Category"
                    var time = "No Schedule"
                    var day = "No Day"

                    selectedBusinessIdBasedOnPref.forEach {cat_ ->
                        if (cat_.businessId == bl.businessId) {
                            category = cat_.tagName
                        }
                    }

                    selectedBusinessIdBasedOnAvailability.forEach { time_ ->
                        if (time_.businessId == bl.businessId) {
                            time = "Opens at ${time_.time}"
                            day = time_.day
                        }
                    }

                    if (category != "No Category" && time != "No Schedule" && day != "No Day") {
                        iteniraryBankList.add(rv_itenirary_data(
                            bl.businessId,
                            bl.businessName,
                            "${String.format("%.2f", CalculationByDistance(LatLng(staycationLat!!,staycationLng!!), LatLng(bl.businessLat,bl.businessLng))!!)} KM",
                            category,
                            time,
                            staycationLat!!,
                            staycationLng!!,
                            bl.businessLat,
                            bl.businessLng,
                            utils.formatNumber(bl.amount!!, 2),
                            day,
                            "",
                        null
                        ))
                    }
                }
            }
        }

        //Sort nearest to far
        iteniraryBankList.sortBy { item ->
            // Calculate distance for each item
            checkDistance(item.businessLat!!, item.businessLng!!)
        }

        println("Itinerary Bank : $iteniraryBankList")
        recalibrateSettings()
        displayItenirary()
    }

    fun recalibrateSettings() {
        // Use the global variables
        startTime = utils.convertTimeFormat(etStartTime.text.toString()).toString()
        endTime = utils.convertTimeFormat(etEndTime.text.toString()).toString()
        numberOfIntervals = if (utils.countIntervals(LocalTime.parse(startTime), LocalTime.parse(endTime), 1) == 1L) {
            1
        } else (
                utils.countIntervals(LocalTime.parse(startTime), LocalTime.parse(endTime), 2).toInt()
                )

        rowCount = 0
        currentTime = ""
        arrivalTime = ""
        currentCount = 0
        iteniraryBankList_temp.clear()
        iteniraryBankList_tempInit.clear()
        rvItenirary.adapter?.notifyDataSetChanged()
    }

    fun displayItenirary() {
        iteniraryBankList.forEach {
            if (it.day == selectedDay) {
                if (rowCount < numberOfIntervals) {
                    if (rowCount == 0) {
                        arrivalTime = etStartTime.text.toString()
                    } else {
                        val rowTime = LocalTime.parse(utils.convertTimeFormat(iteniraryBankList_temp[rowCount - 1].arrivalTime))
                        val nextTime = rowTime.plusHours(2)
                        arrivalTime = nextTime.format(DateTimeFormatter.ofPattern("hh:mm a"))
                    }

                    rowCount++

                    iteniraryBankList_temp.add(
                        rv_itenirary_data(
                            it.businessId,
                            it.businessName,
                            "${String.format("%.2f", checkDistance(it.businessLat!!,it.businessLng!!)!!)} KM",
                            it.category,
                            it.time,
                            staycationLat!!,
                            staycationLng!!,
                            it.businessLat,
                            it.businessLng,
                            it.amount,
                            it.day,
                            arrivalTime,
                            null
                        )
                    )
                }
            }
        }

        hideProgress()
        rvIteniraryAdapter = rv_itenirary_adapter(this,iteniraryBankList_temp )
        rvItenirary.layoutManager = LinearLayoutManager(requireContext())
        rvItenirary.adapter = rvIteniraryAdapter
    }

    fun clearMap() {
        googleMap?.clear()
    }

    fun getInstruction(rowIndex: Int,startLatLng: LatLng,endLatLng: LatLng) {
        instructionList.clear()

        Handler(Looper.getMainLooper()).postDelayed({
            try {
                val geoApiContext = GeoApiContext.Builder()
                    .apiKey("AIzaSyDsyqgc2srz16jC--ZK1QEoqHmpGefgOxk")
                    .build()


                val directions = DirectionsApi.newRequest(geoApiContext)
                    .origin(com.google.maps.model.LatLng(startLatLng!!.latitude, startLatLng!!.longitude))
                    .mode(TravelMode.TRANSIT)
                    .transitMode(TransitMode.BUS)
                    .transitMode(TransitMode.SUBWAY)
                    .transitMode(TransitMode.TRAIN)
                    .transitMode(TransitMode.RAIL)
                    .transitMode(TransitMode.TRAM)
                    .destination(com.google.maps.model.LatLng(endLatLng!!.latitude, endLatLng!!.longitude))
                    .await()

                val steps = directions.routes[0].legs[0].steps


                for (step in steps) {
                    val plainInstruction = HtmlCompat.fromHtml(step.htmlInstructions, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()

                    if (step.travelMode == TravelMode.TRANSIT) {
                        // Transit step, parse transit instructions and mode of transportation
                        val transitDetails = step.transitDetails
                        val modeOfTransportation = transitDetails.line.vehicle.type // Get mode of transportation
                        val fare = when (modeOfTransportation) {
                            VehicleType.BUS,VehicleType.TRAM -> "P20"
                            VehicleType.SUBWAY,VehicleType.COMMUTER_TRAIN,VehicleType.RAIL -> "P30"
                            else -> {
                                "for free"
                            }
                        }
                        val transitInstruction = "Take the $modeOfTransportation ${transitDetails.line.name} towards ${transitDetails.headsign} $fare"

                        instructionList.add(instruction(transitInstruction))
                    } else if (step.travelMode == TravelMode.WALKING) {
                        val walkingInstruction = "${HtmlCompat.fromHtml(step.htmlInstructions, HtmlCompat.FROM_HTML_MODE_LEGACY)} for ${step.duration.humanReadable}"

                        instructionList.add(instruction(walkingInstruction))
                    } else {
                        // Other travel modes, parse regular instructions
                        instructionList.add(instruction(plainInstruction))
                    }
                }

                hideProgress()
                iteniraryBankList_temp[rowIndex].instructionList = instructionList
                rvItenirary.adapter?.notifyItemChanged(rowIndex)
            } catch (e: Exception) {
                Toast.makeText(requireContext(),"Something went wrong. Please try again later",Toast.LENGTH_LONG).show()
            }
        }, 1000)
    }

    fun checkDistance(userLat: Double, userLng: Double): Double? {
        val earthRadius = 6371 // Radius of the earth in kilometers
        val dLat = Math.toRadians(staycationLat!! - userLat)
        val dLng = Math.toRadians(staycationLng!! - userLng)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(userLat)) * cos(Math.toRadians(staycationLat!!)) *
                sin(dLng / 2) * sin(dLng / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadius * c
    }

    fun CalculationByDistance(StartP: LatLng, EndP: LatLng): Double {
        val Radius = 6371 // radius of earth in Km
        val lat1 = StartP.latitude
        val lat2 = EndP.latitude
        val lon1 = StartP.longitude
        val lon2 = EndP.longitude
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = (sin(dLat / 2) * sin(dLat / 2)
                + (cos(Math.toRadians(lat1))
                * cos(Math.toRadians(lat2)) * sin(dLon / 2)
                * sin(dLon / 2)))
        val c = 2 * Math.asin(sqrt(a))
        val valueResult = Radius * c
        val km = valueResult / 1
        val newFormat = DecimalFormat("####")
        val kmInDec: Int = Integer.valueOf(newFormat.format(km))
        val meter = valueResult % 1000
        val meterInDec: Int = Integer.valueOf(newFormat.format(meter))
        Log.i(
            "Radius Value", "" + valueResult + "   KM  " + kmInDec
                    + " Meter   " + meterInDec
        )
        return kmInDec.toDouble()
    }

    fun loadMap(lat:Double,lng:Double) {
        googleMap?.mapType = GoogleMap.MAP_TYPE_TERRAIN

        val location = LatLng(lat, lng)
        val cameraPosition = CameraPosition.Builder()
            .target(location)
            .zoom(16f)
            .bearing(45.0f)
            .build()
        googleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

        val markerOptions = MarkerOptions()
            .position(LatLng(lat,lng))
            .title("$tempSelectedHotelName")
            .snippet("$tempSelectedHotelName")

        val marker = googleMap?.addMarker(markerOptions)
        markerList.add(marker!!)
    }

    fun updateLocation(startLatLng: LatLng,endLatLng: LatLng) {
        val paths: MutableList<LatLng> = ArrayList()

        Handler(Looper.getMainLooper()).postDelayed({
            googleMap.clear()

            map.onCreate(null)
            map.onResume()
            map.getMapAsync { googleMap ->
                googleMap?.mapType = GoogleMap.MAP_TYPE_TERRAIN

                val markerOptionsStay = MarkerOptions()
                    .position(LatLng(startLatLng.latitude!!, startLatLng.longitude!!))

                val markerOptionsBusiness = MarkerOptions()
                    .position(LatLng(endLatLng.latitude!!, endLatLng.longitude!!))

                val markerStay = googleMap?.addMarker(markerOptionsStay)
                val markerBusiness = googleMap?.addMarker(markerOptionsBusiness)

                // Create bounds builder to encompass both markers
                val boundsBuilder = LatLngBounds.Builder()
                markerStay?.let { boundsBuilder.include(it.position) }
                markerBusiness?.let { boundsBuilder.include(it.position) }


                val bounds = boundsBuilder.build()
                val padding = 60 // Padding in pixels
                val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
                googleMap?.animateCamera(cameraUpdate)

                val geoApiContext = GeoApiContext.Builder()
                    .apiKey("AIzaSyDsyqgc2srz16jC--ZK1QEoqHmpGefgOxk")
                    .build()

                val directions = DirectionsApi.newRequest(geoApiContext)
                    .origin(com.google.maps.model.LatLng(markerStay!!.position.latitude, markerStay!!.position.longitude))
                    .mode(TravelMode.TRANSIT)
                    .transitMode(TransitMode.BUS)
                    .transitMode(TransitMode.SUBWAY)
                    .transitMode(TransitMode.TRAIN)
                    .transitMode(TransitMode.RAIL)
                    .transitMode(TransitMode.TRAM)
                    .destination(com.google.maps.model.LatLng(markerBusiness!!.position.latitude, markerBusiness!!.position.longitude))
                    .await()

                val steps = directions.routes[0].legs[0].steps


                for (step in steps) {
                    step.polyline?.let { points ->
                        val coordinates = points.decodePath()
                        for (coordinate in coordinates) {
                            paths.add(LatLng(coordinate.lat, coordinate.lng))
                        }
                    }

                    if (paths.isNotEmpty()) {
                        val opts = PolylineOptions().addAll(paths).color(Color.BLUE).width(5f)
                        googleMap?.addPolyline(opts)
                    }
                }

                hideProgress()
            }
        }, 1000 )
    }

    fun loadStaycation() {
        staycationList.clear()
        rvStaycation?.adapter?.notifyDataSetChanged()

        db.collection("staycation_booking")
            .whereEqualTo("touristId", touristId)
            .whereIn("bookingStatus", listOf("Ongoing", "Pending"))
            .get()
            .addOnSuccessListener { staycationDocuments ->
                for (document in staycationDocuments) {
                    val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
                    val documentId = document.id
                    val staycationId = document.getString("staycationId")
                    val checkInDate = document.getTimestamp("checkInDate")?.toDate()
                    val checkOutDate = document.getTimestamp("checkOutDate")?.toDate()
                    val formattedCheckInDate = dateFormat.format(checkInDate)
                    val formattedCheckOutDate = dateFormat.format(checkOutDate)
                    val displayDate = "$formattedCheckInDate - $formattedCheckOutDate"

                    staycationId?.let { id ->
                        db.collection("staycation").document(id).get()
                            .addOnSuccessListener { staycationDocument ->
                                val staycationTitle = staycationDocument.getString("staycationTitle")
                                val hostId = staycationDocument.getString("hostId")
                                val staycationLocation = staycationDocument.getString("staycationLocation")
                                val staycationLat = staycationDocument.getDouble("staycationLat")
                                val staycationLong = staycationDocument.getDouble("staycationLng")

                                hostId?.let { id ->
                                    db.collection("tourist").document(id.replace("HOST-","")).get()
                                        .addOnSuccessListener { tourstDocument ->
                                            val fullNameMap = tourstDocument.get("fullName") as? Map<*, *>
                                            val firstName = fullNameMap?.get("firstName") as? String

                                            staycationList.add(rv_staycation_data(
                                                staycationTitle!!,
                                                displayDate,
                                                hostId!!,
                                                checkInDate!!,
                                                checkOutDate!!,
                                                firstName!!,
                                                staycationLocation!!,
                                                staycationId,
                                                staycationLat!!,
                                                staycationLong!!
                                            ))

                                            if (staycationList.size == staycationDocuments.size()) {
                                                rvStaycationAdapter = rv_staycation_adapter(this,staycationList)
                                                rvStaycation.layoutManager = LinearLayoutManager(requireContext())
                                                rvStaycation.adapter = rvStaycationAdapter
                                            }
                                        }
                                        .addOnFailureListener { exception ->
                                            // Handle failure to fetch staycation details
                                            println("Error fetching staycation details: $exception")
                                        }
                                }
                            }
                            .addOnFailureListener { exception ->
                                // Handle failure to fetch staycation details
                                println("Error fetching staycation details: $exception")
                            }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(),"Unable to retrieve data",Toast.LENGTH_LONG).show()
            }
    }

    fun changeColorOfAMPM() {
        lnAM.backgroundTintList = ColorStateList.valueOf(if (isAM) selectedAMPM else unSelectedAMPM)
        lnPM.backgroundTintList = ColorStateList.valueOf(if (!isAM) selectedAMPM else unSelectedAMPM)
    }

    fun displayTimeDialog() {
        isAM = true
        etHour.setText("")
        etHour.requestFocus()
        lnAM.backgroundTintList = ColorStateList.valueOf(selectedAMPM)
        lnPM.backgroundTintList = ColorStateList.valueOf(unSelectedAMPM)
        rlEnterTime.visibility = View.VISIBLE
    }

    fun hideShowDialog(show: Boolean) {
        if (show) {
            utils.showProgress(requireContext())
        } else {
            utils.hideProgress()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = IteniraryFragment()
    }

    override fun onResume() {
        map.onResume()
        super.onResume()
    }

    override fun onStart() {
        super.onStart()
        map.onStart()
    }

    override fun onStop() {
        super.onStop()
        map.onStop()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        map.onDestroy()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        map.onLowMemory()
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
    }
}