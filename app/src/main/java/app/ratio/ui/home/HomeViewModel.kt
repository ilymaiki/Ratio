package app.ratio.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.ratio.data.repo.CategoryRepo
import app.ratio.data.repo.ExpenseRepo
import app.ratio.domain.Month
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

data class CategoryRow(val id: String, val name: String, val totalCents: Long)

data class HomeUiState(
    val month: String = Month.current(),
    val totalCents: Long = 0,
    val categories: List<CategoryRow> = emptyList()
)

/**
 * Tu "service" con estado: combina el total del mes + etiquetas visibles
 * + total de cada etiqueta en un solo estado que la pantalla observa.
 * Equivale a un controller que devolviera el DTO ya montado.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel(
    expenseRepo: ExpenseRepo,
    categoryRepo: CategoryRepo,
    private val month: String = Month.current()
) : ViewModel() {

    val uiState = categoryRepo.observeVisible().flatMapLatest { cats ->
        val perCategory = cats.map { cat ->
            expenseRepo.observeTotalByCategory(cat.id, month)
        }
        combine(
            expenseRepo.observeTotalByMonth(month),
            combine(perCategory) { it.toList() }
        ) { total, totals ->
            HomeUiState(
                month = month,
                totalCents = total,
                categories = cats.mapIndexed { i, cat ->
                    CategoryRow(cat.id, cat.name, totals.getOrElse(i) { 0L })
                }
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeUiState(month))
}
