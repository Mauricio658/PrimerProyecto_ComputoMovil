package com.example.calculadoramagica

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Formula1Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
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
            resultado = "Esto no es una ecuación de segundo grado (a = 0)"
        } else if (discriminante < 0) {
            resultado = "La ecuación no tiene soluciones reales"
        } else {
            val x1 = (-b + Math.sqrt(discriminante)) / (2 * a)
            val x2 = (-b - Math.sqrt(discriminante)) / (2 * a)
            resultado = "Las soluciones son:\nx1 = %.2f\nx2 = %.2f".format(x1, x2)
        }
        tvValores.text = "a = $a\nb = $b\nc = $c"
// ✅ Mostrar en el TextView
        tvResultado.text = resultado

        // Ejemplo: mostrarlo con un Toast
        Toast.makeText(requireContext(), "a=$a, b=$b, c=$c", Toast.LENGTH_SHORT).show()

        return view
    }
}