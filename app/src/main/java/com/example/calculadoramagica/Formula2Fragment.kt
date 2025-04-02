package com.example.calculadoramagica

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

class Formula2Fragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_formula2, container, false)
        val tvResultado = view.findViewById<TextView>(R.id.tv_resultado)
        val tvValores = view.findViewById<TextView>(R.id.tv_valores_ingresados)

        val radio = arguments?.getDouble("valor1") ?: 0.0
        val altura = arguments?.getDouble("valor2") ?: 0.0

        val resultadoTexto: String

        if (radio <= 0.0 || altura <= 0.0) {
            resultadoTexto = getString(R.string.el_radio_y_la_altura_deben_ser_mayores_que_cero)
        } else {
            val volumen = Math.PI * radio * radio * altura
            resultadoTexto = getString(R.string.el_volumen_del_cilindro_es_v_2f).format(volumen)
        }

        getString(R.string.radio_altura, radio, altura).also { tvValores.text = it }
        tvResultado.text = resultadoTexto


        // Ahora puedes usar esos valores en tu lógica
        // Ejemplo: mostrarlo con un Toast
        //Toast.makeText(requireContext(), "a=$radio, b=$altura,", Toast.LENGTH_SHORT).show()

        val btnRecalcular = view.findViewById<Button>(R.id.btn_calcular_de_nuevo)

        btnRecalcular.setOnClickListener {
            // Regresar al fragmento anterior (MainFragment)
            requireActivity().supportFragmentManager.popBackStack()
        }

        return view
    }


}