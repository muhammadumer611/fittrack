package com.example.fittrack

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager

import com.example.fittrack.databinding.FragmentDashboardfragmentBinding
import com.github.anastr.speedviewlib.TubeSpeedometer
import kotlin.random.Random

class dashboardfragment : Fragment(), CardAdapter.CardInterfaceListener {

    private var _binding: FragmentDashboardfragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPref: SharedPreferences
    private lateinit var mySharedPref: MySharedPref
    private lateinit var adapter: CardAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardfragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Shared Pref init
        sharedPref = requireActivity().getSharedPreferences("MyPreference", AppCompatActivity.MODE_PRIVATE)
        mySharedPref = MySharedPref(requireContext())

        val name = sharedPref.getString("username", "")
        binding.hello.text = "Hello, $name"
        binding.fab.setOnClickListener {
            startActivity(Intent(requireContext(), ActivityCountExpense::class.java))
        }

        // Card ka color
        val myCard = binding.card
        myCard.setCardBackgroundColor(ContextCompat.getColor(requireContext(), com.google.android.material.R.color.design_default_color_background))

        // Speedometer setup
        val tubeSpeedometer: TubeSpeedometer = binding.tubeSpeedometer
        val totalExpenses = mySharedPref.calculateTotalAmount().toFloat()
        val budget = mySharedPref.calculateTotalIncome().toFloat()
        val expenseRatio = if (budget > 0) (totalExpenses / budget) * 100f else 0f
        val gaugeValue = expenseRatio.coerceAtMost(100f)

        tubeSpeedometer.post {
            tubeSpeedometer.speedTo(gaugeValue, 1000)
            Log.d("budget", "Gauge Value Set in onCreate: $gaugeValue")
        }

        val randomColor = Color.rgb(
            Random.nextInt(256),
            Random.nextInt(256),
            Random.nextInt(256)
        )
        tubeSpeedometer.speedometerBackColor = randomColor
        tubeSpeedometer.unit = "%"
        tubeSpeedometer.maxSpeed = 100f
        tubeSpeedometer.minSpeed = 0f

        // RecyclerView setup
        val cardsList = arrayListOf(
            carditem(R.drawable.food, "Food"),
            carditem(R.drawable.buss, "Transport"),
            carditem(R.drawable.bnous, "Bonus"),
            carditem(R.drawable.alounce, "Allowance"),
            carditem(R.drawable.shopping, "Shopping"),
            carditem(R.drawable.salary, "Salary"),
            carditem(R.drawable.health, "Health"),
            carditem(R.drawable.utilities, "Utilities"),
            carditem(R.drawable.fee_receipt__1_, "Fees"),
            carditem(R.drawable.personal, "Fees")
        )

        adapter = CardAdapter(cardsList, this@dashboardfragment)
        binding.recyclerview.layoutManager = GridLayoutManager(requireContext(), 4)
        binding.recyclerview.adapter = adapter
    }

    override fun onItemclick(type: String) {
        startActivity(
            Intent(requireContext(), ActivityCountExpense::class.java)
                .putExtra("type", type)
        )
    }

    override fun onResume() {
        super.onResume()

        val tubeSpeedometer = binding.tubeSpeedometer

        // ✅ Agar history delete hui thi to gauge 0 pe reset kar do


        // ✅ Normal gauge update
        val totalExpenses = mySharedPref.calculateTotalAmount().toFloat()
        val budget = mySharedPref.calculateTotalIncome().toFloat()
        val gaugeValue = if (budget > 0) (totalExpenses / budget) * 100f else 0f

        tubeSpeedometer.post {
            tubeSpeedometer.speedTo(gaugeValue.coerceAtMost(100f), 1000)
            Log.d("budget", "Gauge Value: $gaugeValue")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
