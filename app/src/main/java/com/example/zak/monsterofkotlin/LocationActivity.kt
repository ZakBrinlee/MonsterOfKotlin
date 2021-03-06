package com.example.zak.monsterofkotlin

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class LocationActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    override fun onMarkerClick(p0: Marker?): Boolean {
        return false
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val REQUEST_CHECK_SETTINGS = 2
        private val MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 9
    }

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location

    //location updates
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private var locationUpdateState = false

    private var trafficCams: MutableList<TrafficCamera> = mutableListOf<TrafficCamera>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        setSupportActionBar(findViewById(R.id.my_location_toolbar))

        // Get a support ActionBar corresponding to this toolbar and enable the Up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)

                lastLocation = p0.lastLocation
                placeMarkerOnMap(LatLng(lastLocation.latitude, lastLocation.longitude))
            }
        }//end of locationCallback declare

//        createLocationRequest()
//
//        getLocation()

    }//end of onCreate


//    //location updates method
//    private fun startLocationUpdates() {
//        //1
//        if (ActivityCompat.checkSelfPermission(this,
//                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this,
//                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
//                    LOCATION_PERMISSION_REQUEST_CODE)
//            return
//        }
//        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null /* Looper */)
//    }

    /*
    1) You create an instance of LocationRequest, add it to an instance of LocationSettingsRequest.
    Builder and retrieve and handle any changes to be made based on the current state of the user’s
    location settings.

    2) interval specifies the rate at which your app will like to receive updates.

    3) fastest Interval specifies the fastest rate at which the app can handle updates.
    Setting the fastestInterval rate places a limit on how fast updates will be sent to your app.
    Before you start requesting for location updates, you need to check the state of the user’s
    location settings.

    4) You create a settings client and a task to check location settings.

    5) A task success means all is well and you can go ahead and initiate a location request.

    6) A task failure means the location settings have some issues which can be fixed. This could be as a result of the user’s location settings turned off. You fix this by showing the user a dialog as shown below:
     */


//    private fun createLocationRequest() {
//        // 1
//        locationRequest = LocationRequest()
//        // 2
//        locationRequest.interval = 10000
//        // 3
//        locationRequest.fastestInterval = 5000
//        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//
//        val builder = LocationSettingsRequest.Builder()
//                .addLocationRequest(locationRequest)
//
//        // 4
//        val client = LocationServices.getSettingsClient(this)
//        val task = client.checkLocationSettings(builder.build())
//
//        // 5
//        task.addOnSuccessListener {
//            locationUpdateState = true
//            startLocationUpdates()
//        }
//        task.addOnFailureListener { e ->
//            // 6
//            if (e is ResolvableApiException) {
//                // Location settings are not satisfied, but this can be fixed
//                // by showing the user a dialog.
//                try {
//                    // Show the dialog by calling startResolutionForResult(),
//                    // and check the result in onActivityResult().
//                    e.startResolutionForResult(this@LocationActivity,
//                            REQUEST_CHECK_SETTINGS)
//                } catch (sendEx: IntentSender.SendIntentException) {
//                    // Ignore the error.
//                }
//            }
//        }
//    }//end of createLocationRequest
//
//    private fun getLocation() {
//
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
//                ==PackageManager.PERMISSION_GRANTED){
//
//            fusedLocationClient.getLastLocation()
//                    .addOnSuccessListener(this, OnSuccessListener<Location>() {
//                        @Override
//                        fun onSuccess(location: Location) {
//
//                            // Got last known location. In some rare situations this can be null.
//                            if (location != null) {
//                                // Logic to handle location object
//                                lastLocation = location;
//                                startLocationUpdates()
//                            }
//                        }
//                    });
//        } else {
//
//            // Should we show an explanation?
//            // No explanation needed, we can request the permission.
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                            android.Manifest.permission.ACCESS_FINE_LOCATION)) {
//
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//
//            } else // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//            {
//                ActivityCompat.requestPermissions(this,
//                        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
//                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
//            }
//        }
//
//    }

    //Override AppCompatActivity’s onActivityResult() method and start the update
    // request if it has a RESULT_OK result for a REQUEST_CHECK_SETTINGS request.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                locationUpdateState = true
//                startLocationUpdates()
            }
        }//end of if

    }//end of onActivityResult

    //Override onPause() to stop location update request
    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    //Override onResume() to restart the location update request.
    public override fun onResume() {
        super.onResume()
        if (!locationUpdateState) {
//            startLocationUpdates()
        }
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap


        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMarkerClickListener(this)
        map.setMinZoomPreference(10.0F)
        map.setInfoWindowAdapter(CustomInfoWindow(this));
        setUpMap()
    }//end of onMapReady

    //places location marker
    private fun placeMarkerOnMap(location: LatLng) {
        val markerOptions = MarkerOptions().position(location)

        //gets a location address from getAddress method and added to markerOptions
        val titleStr = getAddress(location)
        markerOptions.title(titleStr)

        map.addMarker(markerOptions)
    }//end of placeMarkerOnMap for main location

    //places camera markers
    private fun placeMarkerOnMap(location: LatLng, camera: TrafficCamera) {
        map.addMarker(MarkerOptions().position(location).title(camera.name)).tag = camera
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        map.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                placeMarkerOnMap(currentLatLng)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
            }
        }

        // Call for Camera json method
        GetJSON(this).execute("https://web6.seattle.gov/Travelers/api/Map/Data?zoomId=13&type=2")
    }//end of setUpMap

    fun getAddress(latLng: LatLng): String {
        // 1
        val geocoder = Geocoder(this)
        val addresses: List<Address>?
        var addressText = "Address not found"

        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (null != addresses && !addresses.isEmpty()) {
                return addresses[0].getAddressLine(0)
            }
        } catch (e: IOException) {
            Log.e("LocationActivity", e.localizedMessage)
        }

        return addressText
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_location, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                Toast.makeText(this@LocationActivity, "Settings Menu Option Selected", Toast.LENGTH_LONG).show()
                true
            }
            R.id.action_share -> {
                Toast.makeText(this@LocationActivity, "You have selected to share your location", Toast.LENGTH_LONG).show()
                true
            }
            R.id.action_about -> {
                val intent = Intent(this@LocationActivity, AboutActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_search -> {
                true
            }
            else -> {super.onOptionsItemSelected(item)}
        }
    }//end of onOptionsItemSelected

    //reuse of GetJSON from previous assignment
    private inner class GetJSON(var mContext: LocationActivity) : AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            Toast.makeText(this@LocationActivity, "JSON data is downloading", Toast.LENGTH_LONG).show()
        }

        override fun doInBackground(vararg params: String): String {

            //create connection and reader
            var connection: HttpURLConnection? = null
            var reader: BufferedReader? = null

            try {
                val url = URL(params[0])
                connection = url.openConnection() as HttpURLConnection
                connection.connect()

                val inputStream = connection.getInputStream()

                reader = BufferedReader(InputStreamReader(inputStream))

                val buffer = StringBuffer()
                var jsonLine = reader.readLine()

                while ((jsonLine) != null) {
                    buffer.append(jsonLine + "\n")
                    jsonLine = reader.readLine()
                }//end of while
                return buffer.toString()

            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                if (connection != null) {
                    connection.disconnect()
                }
                try {
                    if (reader != null) {
                        reader.close()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }//end of finally

            return "" //return empty string with no connection or data

        }//end of doInBackground

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)

            //JSON object from url
            var jObj: JSONObject = JSONObject(result)
            // gets the array of features which is full list
            var coords: JSONArray = jObj.getJSONArray("Features")

            for(i in 0..(coords.length()-1)) {//Number of JSON objects
                val tempJason = JSONObject(coords[i].toString())

                val cameraNumber = JSONArray(tempJason.getString("Cameras")).length() - 1

                for(j in 0..cameraNumber) {

                    val camera = TrafficCamera()//a camera

                    camera.coords = JSONObject(coords[i].toString()).getString("PointCoordinate")

                    camera.latitude = camera.coords.substring(1,camera.coords.indexOf(",")).toDouble()
                    camera.longitude = camera.coords.substring(camera.coords.indexOf(",")+1,camera.coords.length-1).toDouble()

                    camera.name = JSONArray(tempJason.getString("Cameras")).getJSONObject(j).getString("Description")
                    camera.imageURL = JSONArray(tempJason.getString("Cameras")).getJSONObject(j).getString("ImageUrl")
                    camera.type = JSONArray(tempJason.getString("Cameras")).getJSONObject(j).getString("Type")

                    //add each camera to list of cameras

                    trafficCams.add(camera)

                    val camCoord = LatLng(camera.latitude, camera.longitude)
                    val markerOptions = MarkerOptions()

                    markerOptions.position(camCoord).title(camera.name)

                    val customInfoWindow = CustomInfoWindow(this@LocationActivity)
                    map.setInfoWindowAdapter(customInfoWindow)

                    //add map markers
                    val m:Marker = map.addMarker(markerOptions)
                    m.tag = camera.imageURL
                    m.showInfoWindow()

                }//end of for loop
            }
        }//end of onPostExecute
    }//end of GetJSON

}//end of class
