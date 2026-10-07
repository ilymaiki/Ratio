package app.ratio.domain

import app.ratio.data.model.Category

/**
 * Los 7 tags predefinidos (neto.md §5.1). Se insertan en la primera
 * apertura de la app (RatioApp, paso futuro) con INSERT IGNORE:
 * si ya existen, no se duplican.
 */
object Seed {

    const val MAX_CUSTOM_FREE = 3

    val predefined: List<Category> = listOf(
        Category("piso", "Piso", 0xFF5C6BC0, "home"),
        Category("comida", "Comida", 0xFF66BB6A, "food"),
        Category("fuera", "Comidas fuera", 0xFFFFA726, "restaurant"),
        Category("transporte", "Transporte", 0xFF29B6F6, "bus"),
        Category("ocio", "Ocio", 0xFFAB47BC, "leisure"),
        Category("caprichos", "Caprichos", 0xFFEC407A, "gift"),
        Category("casa", "Calidad de vida", 0xFF8D6E63, "home_comfort")
    )
}
