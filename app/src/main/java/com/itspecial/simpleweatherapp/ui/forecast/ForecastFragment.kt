package com.itspecial.simpleweatherapp.ui.forecast

import android.annotation.SuppressLint
import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import com.itspecial.simpleweatherapp.R
import com.itspecial.simpleweatherapp.common.SwipeDetector
import com.itspecial.simpleweatherapp.common.SwipeDetector.SwipeTypeEnum
import com.itspecial.simpleweatherapp.common.getCurrentDateString
import com.itspecial.simpleweatherapp.common.scaleView
import com.itspecial.simpleweatherapp.data.Current
import com.itspecial.simpleweatherapp.data.Daily
import com.itspecial.simpleweatherapp.data.Status.*
import com.itspecial.simpleweatherapp.ui.adapters.CurrentAdapter
import com.itspecial.simpleweatherapp.ui.adapters.DailyAdapter
import com.itspecial.simpleweatherapp.ui.adapters.ForecastAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_forecast.*
import www.sanju.zoomrecyclerlayout.ZoomRecyclerLayout
import java.util.*
import javax.inject.Inject

class ForecastFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<ForecastViewModel> { viewModelFactory }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var currentLocation: Location
    private var isNight = false
    private var scaled = false
    private var index = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_forecast, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        initUI()
    }

    fun backPressed() {
        if (bottomSheetBehavior.state == STATE_EXPANDED)
            bottomSheetBehavior.state = STATE_COLLAPSED
        else
            requireActivity().finish()
    }

    @SuppressLint("MissingPermission")
    private fun initUI() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                currentLocation = it
                startObservers(it)
            } ?: showLocationError()
        }
        val nightModeFlags = requireContext().resources.configuration.uiMode and
                UI_MODE_NIGHT_MASK
        isNight = nightModeFlags == UI_MODE_NIGHT_YES
        initBottomSheet()
    }

    private fun initBottomSheet() {
        bottomSheetBehavior = from(bottom_sheet).apply {
            state = STATE_COLLAPSED
            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {}

                @SuppressLint("SwitchIntDef")
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        STATE_COLLAPSED -> {
                            scaleView(weather_rv, .82f, 1f)
                            scaled = false
                        }
                        STATE_EXPANDED -> {
                            if (!scaled) {
                                scaleView(weather_rv, 1f, .82f)
                                scaled = true
                            }
                        }
                    }
                }
            })
        }
    }

    private fun startObservers(location: Location) {
        viewModel.getDailyWeather(location.latitude, location.longitude)
            .observe(viewLifecycleOwner, { data ->
                data?.let { resource ->
                    when (resource.status) {
                        SUCCESS -> resource.data?.let { response ->
                            response.daily?.let {
                                setRecycler(it)
                            }
                        }
                        ERROR -> Toast.makeText(
                            requireContext(),
                            resource.message,
                            Toast.LENGTH_LONG
                        ).show()
                        LOADING -> loading.visibility = VISIBLE
                    }
                }
            })

        viewModel.getHourlyWeather(location.latitude, location.longitude)
            .observe(viewLifecycleOwner, { data ->
                data?.let { resource ->
                    when (resource.status) {
                        SUCCESS -> resource.data?.let { response ->
                            response.hourly?.let {
                                val list: MutableList<Current> = mutableListOf()
                                    for (i in it.indices) {
                                        if (getCurrentDateString(it[i].dt!!) != "01:00")
                                            list.add(it[i])
                                        else
                                            break
                                    }
                                setUpBottomSheet(info_rv, temp_rv, list, listOf())
                            }
                        }
                        ERROR -> Toast.makeText(
                            requireContext(),
                            resource.message,
                            Toast.LENGTH_LONG
                        )
                            .show()
                        LOADING -> {
                        }
                    }
                }
            })
    }

    private fun setRecycler(list: List<Daily>) {
        weather_rv.apply {
            val mLayoutManager = ZoomRecyclerLayout(requireContext()).apply {
                orientation = LinearLayoutManager.HORIZONTAL
            }
            LinearSnapHelper().attachToRecyclerView(this)
            isNestedScrollingEnabled = false
            layoutManager = mLayoutManager
            adapter = ForecastAdapter(
                requireContext(), list, viewModel.getCityName(
                    Geocoder(requireContext(), Locale.getDefault()), currentLocation
                ), isNight
            )

            SwipeDetector(this).setOnSwipeListener(object : SwipeDetector.OnSwipeEvent {
                override fun swipeEventDetected(v: View?, swipeType: SwipeTypeEnum?) {
                    if (swipeType === SwipeTypeEnum.BOTTOM_TO_TOP) {
                        smoothScrollToPosition(index)
                        bottomSheetBehavior.state = STATE_EXPANDED
                    }
                    if (swipeType === SwipeTypeEnum.TOP_TO_BOTTOM) {
                        smoothScrollToPosition(index)
                        bottomSheetBehavior.state = STATE_COLLAPSED
                    }
                }
            })

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        index = mLayoutManager.findFirstVisibleItemPosition()
                        performUserAction(index, list[index])
                    }
                }
            })
        }
        loading.visibility = GONE
    }

    private fun setUpBottomSheet(
        visible: RecyclerView,
        gone: RecyclerView,
        currentList: List<Current>,
        dailyList: List<Double>
    ) {
        bottom_sheet.apply {
            val mAdapter = if (visible == temp_rv)
                DailyAdapter(requireContext(), dailyList)
            else
                CurrentAdapter(requireContext(), currentList)
            gone.visibility = GONE
            visible.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext()).apply {
                    orientation = LinearLayoutManager.HORIZONTAL
                }
                adapter = mAdapter
                visibility = VISIBLE
            }
        }
    }

    private fun performUserAction(position: Int, day: Daily) {
        if (position == 0) {
            viewModel.getHourlyWeather(currentLocation.latitude, currentLocation.longitude)
        } else {
            val list = listOf(day.temp?.morn, day.temp?.day, day.temp?.eve, day.temp?.night)
            setUpBottomSheet(temp_rv, info_rv, listOf(), list as List<Double>)
        }
    }

    private fun showLocationError() {
        val builder = AlertDialog.Builder(requireContext())
        builder.apply {
            setMessage("Невозможно определить местоположение. Убедитесь, что функция определения местоположения включена в настройках устройства.")
            setCancelable(false)
            setPositiveButton(resources.getString(R.string.ok)) { dialog, _ ->
                dialog.dismiss()
            }
            create().show()
        }
    }
}