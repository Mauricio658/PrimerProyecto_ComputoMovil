package com.example.calculadoramagica

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

class Formula3Fragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_formula3, container, false)
        val tvResultado = view.findViewById<TextView>(R.id.tv_resultado)
        val tvValores = view.findViewById<TextView>(R.id.tv_valores_ingresados)

        val base = arguments?.getDouble("valor1") ?: 0.0
        val altura = arguments?.getDouble("valor2") ?: 0.0

        val resultadoTexto = if (base <= 0.0 || altura <= 0.0) {
            getString(R.string.la_base_y_la_altura_deben_ser_mayores_que_cero)
        } else {
            val perimetro = 2 * (base + altura)
            getString(R.string.el_per_metro_del_rect_ngulo_es_p_2f).format(perimetro)
        }

        "Base = $base\nAltura = $altura".also { tvValores.text = it }
        tvResultado.text = resultadoTexto

        val btnRecalcular = view.findViewById<Button>(R.id.btn_calcular_de_nuevo)

        btnRecalcular.setOnClickListener {
            // Regresar al fragmento anterior (MainFragment)
            requireActivity().supportFragmentManager.popBackStack()
        }
        return view
    }



}