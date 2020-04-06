package com.example.kotlintodoapp.ui.main

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.example.kotlintodoapp.R
import com.example.kotlintodoapp.adapter.TodoItemAdapter
import com.example.kotlintodoapp.helper.PermissonHelper
import com.example.kotlintodoapp.helper.TodoLogger
import com.example.kotlintodoapp.model.LoadingState
import com.example.kotlintodoapp.model.TodoItem
import com.example.kotlintodoapp.viewModels.TodoViewModel
import com.example.kotlintodoapp.viewModels.WeatherViewModel
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.main_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class MainFragment : Fragment() {
    private val todoViewModel by viewModel<TodoViewModel>()
    private lateinit var weatherViewModel: WeatherViewModel
    private var adapter: TodoItemAdapter? = null
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    private val onCheckBoxClicked: (isChecked: Boolean, todoItem: TodoItem) -> Unit =
        { isChecked: Boolean, todoItem: TodoItem ->
            todoViewModel.setTodoItemIsFinished(isChecked, todoItem)
        }

    private val onLongPressedItem: (position: Int, todoItem: TodoItem) -> Unit =
        { _: Int, todoItem ->
            todoViewModel.removeTodo(todoItem)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        setTodoItemData()
        setWeatherData()
        setupTodoList()
        setNavigationToAddTodo()

        context?.let {
            swipeToRefresh.setProgressBackgroundColorSchemeColor(
                ContextCompat.getColor(
                    it,
                    R.color.colorPrimary
                )
            )
            swipeToRefresh.setColorSchemeColors(Color.WHITE)
            swipeToRefresh.setOnRefreshListener {
                todoViewModel.fetchData()
                swipeToRefresh.isRefreshing = false
            }
        }

        todoViewModel.loadingState.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LoadingState.Status.FAILED -> swipeToRefresh.isRefreshing = false
                LoadingState.Status.RUNNING -> swipeToRefresh.isRefreshing = true
                LoadingState.Status.SUCCESS -> swipeToRefresh.isRefreshing = false
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
        getLastLocation()
    }


    private fun setTodoItemData() {
        todoViewModel.data.observe(viewLifecycleOwner, Observer {
            when (it.count()) {
                0 -> {
                    todoList.visibility = View.GONE
                    txtNoTodo.visibility = View.VISIBLE
                    txtNoTodo.text = getString(R.string.no_item)
                }
                else -> {
                    todoList.visibility = View.VISIBLE
                    txtNoTodo.visibility = View.GONE
                    adapter?.todoItems = it
                    adapter?.notifyDataSetChanged()
                }
            }
        })
    }

    private fun setWeatherData() {
        weatherViewModel.fetchWeather(dataType = "rhrread", lang = "tc")
        weatherViewModel.weatherLiveData.observe(viewLifecycleOwner, Observer { weather ->
            val icon = weather.icon[0]
            context?.let {
                Glide.with(it)
                    .load("https://www.hko.gov.hk/images/wxicon/pic${icon}.png")
                    .override(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    .into(DrawableImageViewTarget(ivWeather))
                // TODO: add gps to check current location weather
                txtLocation.text = getString(R.string.hko,weather.temperature.data[0].place)
                txtWeather.text = weather.temperature.data[0].getValueWithUnit()
                txtLastUpdated.text =
                    String.format(getString(R.string.last_updated_at), weather.updateTime)
            }
        })
    }

    private fun setupTodoList() {
        adapter = TodoItemAdapter(onCheckBoxClicked, onLongPressedItem);
        todoList.apply {
            layoutManager = LinearLayoutManager(
                context
            )
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    (layoutManager as LinearLayoutManager).orientation
                )
            )
        }
        todoList.adapter = adapter
    }

    private fun setNavigationToAddTodo() {
        val navController = findNavController()
        btnAddTodo.setOnClickListener {
            navController.navigate(R.id.addTodoFragment2, null, navOptions {
                popUpTo = R.id.addTodoFragment2
                launchSingleTop = true
            })
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun getLastLocation() {
        if (PermissonHelper.checkPermission(context!!)) {
            if (isLocationEnabled()) {

                mFusedLocationClient.lastLocation.addOnCompleteListener(activity!!) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        setGPSLocationUI(location)
                    }
                }
            } else {
                Toast.makeText(context!!, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            PermissonHelper.requestPermission(activity!!)
        }
    }

    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
            setGPSLocationUI(mLastLocation)
        }
    }

    private fun setGPSLocationUI(location: Location){
        val geoCoder = Geocoder(context!!, Locale.getDefault())
        val addresses =
            geoCoder.getFromLocation(location.latitude, location.longitude, 1)
        txtLat.text = getString(R.string.lat,location.latitude.toString())
        txtLng.text = getString(R.string.lat,location.longitude.toString())
        txtGps.text = getString(R.string.gps_location,addresses[0].getAddressLine(0))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PermissonHelper.PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }
}