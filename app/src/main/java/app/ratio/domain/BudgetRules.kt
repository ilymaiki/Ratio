package app.ratio.domain

/** Estado visual del presupuesto (neto.md §7). Solo informativo, nunca bloquea. */
enum class BudgetStatus { OK, WARNING, OVER }

object BudgetRules {

    fun status(spentCents: Long, limitCents: Long?): BudgetStatus {
        if (limitCents == null || limitCents <= 0) return BudgetStatus.OK
        val pct = spentCents.toDouble() / limitCents.toDouble()
        return when {
            pct >= 1.0 -> BudgetStatus.OVER
            pct >= 0.8 -> BudgetStatus.WARNING
            else -> BudgetStatus.OK
        }
    }

    fun percent(spentCents: Long, limitCents: Long?): Int {
        if (limitCents == null || limitCents <= 0) return 0
        return ((spentCents.toDouble() / limitCents.toDouble()) * 100).toInt()
    }

    fun freeCents(spentCents: Long, limitCents: Long?): Long {
        if (limitCents == null) return 0
        return (limitCents - spentCents).coerceAtLeast(0)
    }
}
