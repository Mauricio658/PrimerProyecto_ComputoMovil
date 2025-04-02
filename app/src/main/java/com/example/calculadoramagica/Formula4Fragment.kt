package com.example.calculadoramagica

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView


class Formula4Fragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_formula4, container, false)
        val tvResultado = view.findViewById<TextView>(R.id.tv_resultado)
        val tvValores = view.findViewById<TextView>(R.id.tv_valores_ingresados)

        val vf = arguments?.getDouble("valor1") ?: 0.0
        val vi = arguments?.getDouble("valor2") ?: 0.0
        val tiempo = arguments?.getDouble("valor3") ?: 0.0

        val resultadoTexto = if (tiempo <= 0.0) {
            getString(R.string.el_tiempo_debe_ser_mayor_que_cero_para_calcular_la_aceleraci_n)
        } else {
            val aceleracion = (vf - vi) / tiempo
            getString(R.string.la_aceleraci_n_es_a_2f_m_s).format(aceleracion)
        }

        getString(R.string.velocidad_final_velocidad_inicial_tiempo, vf, vi, tiempo).also { tvValores.text = it }
        tvResultado.text = resultadoTexto

        val btnRecalcular = view.findViewById<Button>(R.id.btn_calcular_de_nuevo)

        btnRecalcular.setOnClickListener {
            // Regresar al fragmento anterior (MainFragment)
            requireActivity().supportFragmentManager.popBackStack()
        }
        return view
    }



}