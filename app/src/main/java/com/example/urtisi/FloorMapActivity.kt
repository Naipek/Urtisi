package com.example.urtisi

import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class FloorMapActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private var currentFloor = 1
    private val totalFloors = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_floor_map)

        webView = findViewById(R.id.webViewMap)
        setupWebView()
        setupFloorControls()
        loadFloorMap(currentFloor)
        setupBottomNavigation()
    }

    private fun setupWebView() {
        with(webView.settings) {
            javaScriptEnabled = true
            builtInZoomControls = true
            displayZoomControls = false
            useWideViewPort = true
            loadWithOverviewMode = true
        }

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                // Масштабируем SVG после загрузки
                webView.evaluateJavascript(
                    "document.querySelector('svg').style.width='100%';" +
                            "document.querySelector('svg').style.height='auto';", null)
            }
        }
    }

    private fun setupFloorControls() {
        val floorUp: ImageButton = findViewById(R.id.floorUp)
        val floorDown: ImageButton = findViewById(R.id.floorDown)
        val floorText: TextView = findViewById(R.id.floorText)

        // Установка иконок
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
        webView.loadUrl("file:///android_asset/floor${floor}_map.svg")
    }

    private fun updateFloorIndicator(
        textView: TextView,
        upButton: ImageButton,
        downButton: ImageButton
    ) {
        textView.text = currentFloor.toString()

        // Обновляем состояние кнопок
        upButton.isEnabled = currentFloor < totalFloors
        downButton.isEnabled = currentFloor > 1
        upButton.alpha = if (upButton.isEnabled) 1f else 0.5f
        downButton.alpha = if (downButton.isEnabled) 1f else 0.5f
    }

    private fun setupBottomNavigation() {
        findViewById<ImageView>(R.id.icon1).setOnClickListener {
            navigateTo(MainActivity::class.java)
        }
        findViewById<ImageView>(R.id.icon3).setOnClickListener {
            // Уже на этой странице
        }
        findViewById<ImageView>(R.id.icon4).setOnClickListener {
            navigateTo(SettingsActivity::class.java)
        }
        findViewById<ImageView>(R.id.icon2).setOnClickListener {
            navigateTo(ScheduleActivity::class.java)
        }
    }

    private fun navigateTo(target: Class<*>) {
        val intent = Intent(this, target).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}