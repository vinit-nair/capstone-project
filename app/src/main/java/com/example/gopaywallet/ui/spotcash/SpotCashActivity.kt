package com.example.gopaywallet.ui.spotcash;

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.gopaywallet.R

class SpotCashActivity : AppCompatActivity() {

    private lateinit var spinnerForex: Spinner
    private lateinit var cbMedicine: CheckBox
    private lateinit var cbCinema: CheckBox
    private lateinit var cbMall: CheckBox
    private lateinit var cbFoodAndBeverage: CheckBox
    private lateinit var tvRewardPoints: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spot_cash)

        spinnerForex = findViewById(R.id.spinnerForex)
        cbMedicine = findViewById(R.id.cbMedicine)
        cbCinema = findViewById(R.id.cbCinema)
        cbMall = findViewById(R.id.cbMall)
        cbFoodAndBeverage = findViewById(R.id.cbFoodAndBeverage)
        val btnApply: Button = findViewById(R.id.btnApply)
        tvRewardPoints = findViewById(R.id.tvRewardPoints)

        btnApply.setOnClickListener {
            applyForSpotCash()
        }
    }

    private fun applyForSpotCash() {
        // Get selected forex
        val selectedForex = spinnerForex.selectedItem.toString()

        // Get selected services
        val isMedicineSelected = cbMedicine.isChecked
        val isCinemaSelected = cbCinema.isChecked
        val isMallSelected = cbMall.isChecked
        val isFoodAndBeverageSelected = cbFoodAndBeverage.isChecked

        // Implement logic to apply for Spot Cash and update rewards
        val rewardPoints = calculateRewardPoints(isMedicineSelected, isCinemaSelected, isMallSelected, isFoodAndBeverageSelected)
        tvRewardPoints.text = "Points: $rewardPoints"

        // Display custom Toast
        showCustomToast("Spot Cash Applied Successfully")
    }

    private fun calculateRewardPoints(vararg services: Boolean): Int {
        var points = 0
        for (service in services) {
            if (service) points += 10 // Example reward points logic
        }
        return points
    }

    private fun showCustomToast(message: String) {
        val inflater = layoutInflater
        val layout: View = inflater.inflate(
            R.layout.custom_toast,
            findViewById(R.id.custom_toast_container)
        )

        val text: TextView = layout.findViewById(R.id.toast_text)
        text.text = message

        val toast = Toast(applicationContext)
        toast.duration = Toast.LENGTH_LONG
        toast.view = layout
        toast.show()
    }
}




