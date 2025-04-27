package com.example.urtisi

import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class FloorMapActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private var webViewState: Bundle? = null
    private var currentFloor = 1 // Текущий этаж (1-4)
    private val totalFloors = 4 // Всего этажей

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_floor_map)

        webView = findViewById(R.id.webViewMap)

        // Восстановление состояния
        if (savedInstanceState != null) {
            webViewState = savedInstanceState.getBundle("webViewState")
            currentFloor = savedInstanceState.getInt("currentFloor", 1)
        }

        setupWebView()
        setupBottomNavigation()
        setupFloorControls()
        loadFloorMap(currentFloor)
    }

    private fun setupWebView() {
        with(webView.settings) {
            javaScriptEnabled = true
            builtInZoomControls = true
            displayZoomControls = false
            useWideViewPort = true
            loadWithOverviewMode = true
        }
        webView.webViewClient = WebViewClient()
    }

    private fun setupFloorControls() {
        val floorUp = findViewById<ImageView>(R.id.floorUp)
        val floorDown = findViewById<ImageView>(R.id.floorDown)
        val floorText = findViewById<TextView>(R.id.floorText)

        // Установка иконок стрелок
        floorUp.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_arrow_up))
        floorDown.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_arrow_down))

        floorUp.setOnClickListener {
            if (currentFloor < totalFloors) {
                currentFloor++
                loadFloorMap(currentFloor)
                updateFloorIndicator(floorText, floorUp, floorDown)
            }
        }

        floorDown.setOnClickListener {
            if (currentFloor > 1) {
                currentFloor--
                loadFloorMap(currentFloor)
                updateFloorIndicator(floorText, floorUp, floorDown)
            }
        }

        updateFloorIndicator(floorText, floorUp, floorDown)
    }

    private fun loadFloorMap(floor: Int) {
        // Сохраняем текущее состояние масштабирования
        webViewState = Bundle().apply { webView.saveState(this) }

        // Загружаем SVG соответствующего этажа
        webView.loadUrl("file:///android_asset/floor${floor}_map.svg")
    }

    private fun updateFloorIndicator(
        textView: TextView,
        upButton: ImageView,
        downButton: ImageView
    ) {
        textView.text = "Этаж $currentFloor"

        // Обновляем состояние кнопок
        upButton.alpha = if (currentFloor == totalFloors) 0.5f else 1.0f
        downButton.alpha = if (currentFloor == 1) 0.5f else 1.0f
        upButton.isEnabled = currentFloor < totalFloors
        downButton.isEnabled = currentFloor > 1
    }

    private fun setupBottomNavigation() {
        findViewById<ImageView>(R.id.icon1).setOnClickListener {
            navigateTo(MainActivity::class.java)
        }

        findViewById<ImageView>(R.id.icon4).setOnClickListener {
            navigateTo(SettingsActivity::class.java)
        }
    }

    private fun navigateTo(target: Class<*>) {
        val intent = Intent(this, target).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        startActivity(intent)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBundle("webViewState", Bundle().apply { webView.saveState(this) })
        outState.putInt("currentFloor", currentFloor)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        webViewState = savedInstanceState.getBundle("webViewState")
        currentFloor = savedInstanceState.getInt("currentFloor", 1)
    }

    override fun onResume() {
        super.onResume()
        webViewState?.let { webView.restoreState(it) }
    }
}