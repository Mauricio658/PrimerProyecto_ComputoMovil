package com.example.calculadoramagica

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.calculadoramagica.databinding.FragmentMainBinding

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
            R.layout.spinner // Aquí se usa el layout personalizado
        )
        adapter.setDropDownViewResource(R.layout.spinner) // También para el desplegable

        binding.spOpciones.adapter = adapter


        super.onViewCreated(view, savedInstanceState)

        // Cargar las opciones del Spinner desde strings.xml
        val opciones = resources.getStringArray(R.array.formulas_array)


        // Asociar opciones con imágenes (deben estar en res/drawable)
        val imageMap = mapOf(
            opciones[0] to R.drawable.formula1,   // Imagen para "Ecuación de segundo grado"
            opciones[1] to R.drawable.formula2,             // Imagen para "Volumen de un cilindro"
            opciones[2] to R.drawable.formula3,     // Imagen para "Perímetro de un rectángulo"
            opciones[3] to R.drawable.formula4,        // Imagen para "Formula de acelración"
        )

        // Crear instancias de los Fragments dinámicamente
        val fragments = listOf(
            Formula1Fragment(),
            Formula2Fragment(),
            Formula3Fragment(),
            Formula4Fragment(),
        )

        data class FormulaConfig(
            val nombre: String,
            val hints: List<String> // etiquetas para los EditText visibles
        )

        val configuraciones = listOf(
            FormulaConfig(opciones[0], listOf(getString(R.string.coeficiente_a),
                getString(R.string.coeficiente_b), getString(R.string.coeficiente_c))),
            FormulaConfig(opciones[1], listOf(getString(R.string.radio), getString(R.string.altura))),
            FormulaConfig(opciones[2], listOf(getString(R.string.base), getString(R.string.altura))),
            FormulaConfig(opciones[3], listOf(getString(R.string.velocidad_final),
                getString(R.string.velocidad_inicial), getString(R.string.tiempo)))
        )


        // Asociar cada opción con su Fragment usando zip()
        val fragmentMap = opciones.zip(fragments).toMap()

        // Cambiar la aspectos claves  cuando se seleccione una opción en el Spinner
        binding.spOpciones.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val opcionSeleccionada = opciones[position]

                // Cambiar imagen
                imageMap[opcionSeleccionada]?.let { imageRes ->
                    binding.ivFormula.setImageResource(imageRes)
                }

                // Ocultar todos los inputs
                binding.input1.visibility = View.GONE
                binding.input2.visibility = View.GONE
                binding.input3.visibility = View.GONE

                // Verificar si es la ecuación de segundo grado
                val esEcuacionSegundoGrado = opcionSeleccionada == configuraciones[0].nombre

                // Determinar el tipo de entrada permitido
                val inputTypeCorrecto = if (esEcuacionSegundoGrado)
                    android.text.InputType.TYPE_CLASS_NUMBER or
                            android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL or
                            android.text.InputType.TYPE_NUMBER_FLAG_SIGNED
                else
                    android.text.InputType.TYPE_CLASS_NUMBER or
                            android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL

                // Mostrar los inputs necesarios con hints y tipo correcto
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
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        // Evento del botón Calcular
        binding.btCalcular.setOnClickListener {

            val opcionSeleccionada = binding.spOpciones.selectedItem.toString()

            val config = configuraciones.find { it.nombre == opcionSeleccionada }

            // Leer los valores solo si los campos están visibles
            val valor1 = if (binding.input1.visibility == View.VISIBLE)
                binding.input1.text.toString().toDoubleOrNull() else null

            val valor2 = if (binding.input2.visibility == View.VISIBLE)
                binding.input2.text.toString().toDoubleOrNull() else null

            val valor3 = if (binding.input3.visibility == View.VISIBLE)
                binding.input3.text.toString().toDoubleOrNull() else null

            // Validar si todos los necesarios están completos
            val camposNecesarios = config?.hints?.size ?: 0
            val valoresList = listOf(valor1, valor2, valor3).take(camposNecesarios)

            if (valoresList.any { it == null }) {
                Toast.makeText(requireContext(),
                    getString(R.string.completa_todos_los_valores_requeridos), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

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

            // Limpiar los campos después del cálculo
            binding.input1.text.clear()
            binding.input2.text.clear()
            binding.input3.text.clear()
        }
    }

}
