package com.example.calculadoramagica

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import kotlin.math.sqrt

class Formula1Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_formula1, container, false)
        val tvResultado = view.findViewById<TextView>(R.id.tv_resultado)
        val tvValores = view.findViewById<TextView>(R.id.tv_valores_ingresados)


        val a = arguments?.getDouble("valor1") ?: 0.0
        val b = arguments?.getDouble("valor2") ?: 0.0
        val c = arguments?.getDouble("valor3") ?: 0.0

        val discriminante = b * b - 4 * a * c
        val resultado: String

        if (a == 0.0) {
            resultado = getString(R.string.esto_no_es_una_ecuaci_n_de_segundo_grado_a_0)
        } else if (discriminante < 0) {
            resultado = getString(R.string.lo_siento_tu_ecuaci_n_no_tiene_soluciones_reales)
        } else {
            val x1 = (-b + sqrt(discriminante)) / (2 * a)
            val x2 = (-b - sqrt(discriminante)) / (2 * a)
            resultado =
                getString(R.string.felicidades_tu_ecuaci_n_tiene_soluciones_reales_las_soluciones_son_x1_2f_x2_2f).format(x1, x2)
        }
        getString(R.string.a_b_c, a, b, c).also { tvValores.text = it }
        // Mostrar en el TextView
        tvResultado.text = resultado

        // Ejemplo: mostrarlo con un Toast
        //Toast.makeText(requireContext(), "a=$a, b=$b, c=$c", Toast.LENGTH_SHORT).show()

        val btnRecalcular = view.findViewById<Button>(R.id.btn_calcular_de_nuevo)

        btnRecalcular.setOnClickListener {
            // Regresar al fragmento anterior (MainFragment)
            requireActivity().supportFragmentManager.popBackStack()
        }


        return view
    }
}