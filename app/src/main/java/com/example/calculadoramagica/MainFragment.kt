package com.example.calculadoramagica

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import com.example.calculadoramagica.databinding.FragmentMainBinding
import android.text.TextWatcher
import android.text.Editable

//Función para pasar variables por los fragments
fun <T : Fragment> T.withArguments(vararg pairs: Pair<String, Any?>): T {
    arguments = Bundle().apply {
        pairs.forEach { (key, value) ->
            when (value) {
                is Int -> putInt(key, value)
                is Double -> putDouble(key, value)
                is String -> putString(key, value)
            }
        }
    }
    return this
}

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.formulas_array,
            R.layout.spinner
        )
        adapter.setDropDownViewResource(R.layout.spinner)
        binding.spOpciones.adapter = adapter

        // Deshabilitar botón por defecto
        binding.btCalcular.isEnabled = false
        binding.btCalcular.setBackgroundTintList(
            ContextCompat.getColorStateList(requireContext(), android.R.color.darker_gray)
        )

        val opciones = resources.getStringArray(R.array.formulas_array)

        val imageMap = mapOf(
            opciones[0] to R.drawable.formula1,
            opciones[1] to R.drawable.formula2,
            opciones[2] to R.drawable.formula3,
            opciones[3] to R.drawable.formula4,
        )

        val fragments = listOf(
            Formula1Fragment(),
            Formula2Fragment(),
            Formula3Fragment(),
            Formula4Fragment(),
        )

        data class FormulaConfig(
            val nombre: String,
            val hints: List<String>
        )

        val configuraciones = listOf(
            FormulaConfig(opciones[0], listOf(getString(R.string.coeficiente_a),
                getString(R.string.coeficiente_b), getString(R.string.coeficiente_c))),
            FormulaConfig(opciones[1], listOf(getString(R.string.radio), getString(R.string.altura))),
            FormulaConfig(opciones[2], listOf(getString(R.string.base), getString(R.string.altura))),
            FormulaConfig(opciones[3], listOf(getString(R.string.velocidad_final),
                getString(R.string.velocidad_inicial), getString(R.string.tiempo)))
        )

        val fragmentMap = opciones.zip(fragments).toMap()

        // TextWatcher para habilitar o deshabilitar el botón
        val watcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = validarCamposYHabilitarBoton()
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        binding.input1.addTextChangedListener(watcher)
        binding.input2.addTextChangedListener(watcher)
        binding.input3.addTextChangedListener(watcher)

        // Limitar a 10 caracteres por entrada
        val filtro10 = arrayOf(android.text.InputFilter.LengthFilter(10))
        binding.input1.filters = filtro10
        binding.input2.filters = filtro10
        binding.input3.filters = filtro10



        binding.spOpciones.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val opcionSeleccionada = opciones[position]

                imageMap[opcionSeleccionada]?.let { imageRes ->
                    binding.ivFormula.setImageResource(imageRes)
                }

                // Limpiar campos y ocultar inputs
                binding.input1.text.clear()
                binding.input2.text.clear()
                binding.input3.text.clear()
                binding.btCalcular.isEnabled = false
                binding.btCalcular.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), android.R.color.darker_gray)
                )

                binding.input1.visibility = View.GONE
                binding.input2.visibility = View.GONE
                binding.input3.visibility = View.GONE

                val esEcuacionSegundoGrado = opcionSeleccionada == configuraciones[0].nombre
                val inputTypeCorrecto = if (esEcuacionSegundoGrado)
                    android.text.InputType.TYPE_CLASS_NUMBER or
                            android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL or
                            android.text.InputType.TYPE_NUMBER_FLAG_SIGNED
                else
                    android.text.InputType.TYPE_CLASS_NUMBER or
                            android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL

                configuraciones.find { it.nombre == opcionSeleccionada }?.hints?.let { hints ->
                    if (hints.isNotEmpty()) {
                        binding.input1.apply {
                            hint = hints[0]
                            inputType = inputTypeCorrecto
                            visibility = View.VISIBLE
                        }
                    }
                    if (hints.size >= 2) {
                        binding.input2.apply {
                            hint = hints[1]
                            inputType = inputTypeCorrecto
                            visibility = View.VISIBLE
                        }
                    }
                    if (hints.size >= 3) {
                        binding.input3.apply {
                            hint = hints[2]
                            inputType = inputTypeCorrecto
                            visibility = View.VISIBLE
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.btCalcular.setOnClickListener {
            val opcionSeleccionada = binding.spOpciones.selectedItem.toString()
            val valor1 = if (binding.input1.visibility == View.VISIBLE)
                binding.input1.text.toString().toDoubleOrNull() else null

            val valor2 = if (binding.input2.visibility == View.VISIBLE)
                binding.input2.text.toString().toDoubleOrNull() else null

            val valor3 = if (binding.input3.visibility == View.VISIBLE)
                binding.input3.text.toString().toDoubleOrNull() else null

            val fragment = fragmentMap[opcionSeleccionada]?.withArguments(
                "valor1" to valor1!!,
                "valor2" to valor2,
                "valor3" to valor3
            )

            if (fragment != null) {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fcv_main, fragment)
                    .addToBackStack(null)
                    .commit()
            }

            // Limpiar después de calcular
            binding.input1.text.clear()
            binding.input2.text.clear()
            binding.input3.text.clear()
            binding.btCalcular.isEnabled = false
            binding.btCalcular.setBackgroundTintList(
                ContextCompat.getColorStateList(requireContext(), android.R.color.darker_gray)
            )
        }
    }

    private fun validarCamposYHabilitarBoton() {
        val inputs = listOf(binding.input1, binding.input2, binding.input3)
            .filter { it.visibility == View.VISIBLE }

        val todosLlenos = inputs.all { it.text.toString().toDoubleOrNull() != null }
        binding.btCalcular.isEnabled = todosLlenos

        val color = if (todosLlenos) R.color.bt_calcular else android.R.color.darker_gray
        binding.btCalcular.setBackgroundTintList(
            ContextCompat.getColorStateList(requireContext(), color)
        )
    }
}
