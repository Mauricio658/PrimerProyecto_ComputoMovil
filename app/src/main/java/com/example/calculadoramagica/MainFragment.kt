package com.example.calculadoramagica

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import com.example.calculadoramagica.databinding.FragmentMainBinding

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
        super.onViewCreated(view, savedInstanceState)

        // Cargar las opciones del Spinner desde strings.xml
        val opciones = resources.getStringArray(R.array.formulas_array)


        // Asociar opciones con imágenes (deben estar en res/drawable)
        val imageMap = mapOf(
            opciones[0] to R.drawable.formula1,   // Imagen para "Ecuación de segundo grado"
            opciones[1] to R.drawable.formula2,             // Imagen para "Área de un círculo"
            opciones[2] to R.drawable.formula3,     // Imagen para "Perímetro de un rectángulo"
            opciones[3] to R.drawable.formula4,        // Imagen para "Teorema de Pitágoras"
        )

        // Crear instancias de los Fragments dinámicamente
        val fragments = listOf(
            Formula1Fragment(),
            Formula2Fragment(),
            Formula3Fragment(),
            Formula4Fragment(),
        )

        // Asociar cada opción con su Fragment usando zip()
        val fragmentMap = opciones.zip(fragments).toMap()

        // Cambiar la imagen cuando se seleccione una opción en el Spinner
        binding.spOpciones.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val opcionSeleccionada = opciones[position]

                // Cambiar la imagen en el ImageView
                imageMap[opcionSeleccionada]?.let { imageRes ->
                    binding.ivFormula.setImageResource(imageRes)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

            // Evento del botón Calcular
        binding.btCalcular.setOnClickListener {
            val opcionSeleccionada = binding.spOpciones.selectedItem.toString()

            // Buscar el fragmento correspondiente y navegar
            fragmentMap[opcionSeleccionada]?.let { fragment ->
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fcv_main, fragment)
                    .addToBackStack(null)
                    .commit()
            } ?: run {
                Toast.makeText(requireContext(), "Opción no válida", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
