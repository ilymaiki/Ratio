# Ratio — Guía Android + Estructura Kotlin (android.md)

> Stack decidido (07/10/2026): **Kotlin + Jetpack Compose**, 100% local.
> Base: `neto.md`. Pantalla base 360x640, retrato, una mano. Package: `app.ratio`.
> Repo actual: `https://github.com/ilymaiki/ratio.git` (rama `main`).

---

## 0. Estado actual del proyecto

`C:\Users\Dani\Documents\Ratio` **YA es un proyecto Android creado desde Android Studio** (verificado 07/10/2026):

- Plugin `com.android.application` + `org.jetbrains.kotlin.android` + compose
- `namespace = "app.ratio"`, `minSdk = 26`, `compose = true`
- `app/src/main/java/app/ratio/MainActivity.kt` (plantilla "Hello Android") + `ui/theme/` + `AndroidManifest.xml`

No hay nada de Spring aquí. No hay que crear `Ratio-android` aparte ni migrar nada: se desarrolla directamente en esta carpeta.

---

## 1. Tutorial paso a paso (Windows)

### 1.1 Instalar Android Studio
1. Descarga desde `https://developer.android.com/studio` → botón verde Download.
2. Instalador: deja marcado **Android SDK + Android Virtual Device (emulador)**. Siguiente, siguiente, Finish.
3. Primera apertura → wizard de setup → elige **Standard** → deja que descargue el SDK (tarda 5-10 min). Ruta típica: `C:\Users\Dani\AppData\Local\Android\Sdk`.
4. Comprueba: abre `Settings → Appearance → System Settings → Android SDK`. Debe aparecer una plataforma instalada (ej: Android 14 / API 34).

### 1.2 Crear el proyecto Android nuevo (YA HECHO — no repetir)
Proyecto ya creado en `C:\Users\Dani\Documents\Ratio` con:
- Name `Ratio`, package `app.ratio`, Kotlin, Kotlin DSL, minSdk 26.
- Si reinstalas, repite estos valores en un proyecto `Empty Activity` nuevo.

### 1.3 Crear el emulador (tus "pruebas en ordenador")
1. `Tools → Device Manager → Create Device`.
2. Elige **Pixel 5** (pantalla cercana a 360x640 lógicos, tu base de diseño).
3. Imagen del sistema: **API 34, x86_64, Google APIs** (no Play Store para ir más ligero).
4. `Finish → Run (triángulo verde)`. Se abre el móvil virtual. Aquí probarás con ratón, pero diseña pensando en dedo.

### 1.4 Pasar la estructura de este fichero al proyecto
1. En la vista `Android` de Studio, crea los paquetes con botón derecho → `New → Package`:
   `app.ratio.data.db`, `app.ratio.data.model`, `app.ratio.data.repo`, `app.ratio.ui.home`, etc. (ver §3).
2. Empieza por el orden de §5 (primero `model + db + Seed`, no por la UI).
3. Copia `info/bruto.md`, `neto.md`, `android.md` a `Ratio-android/info/` para que la doc viaje con el código.

---

## 2. GitHub: qué hay que hacer (explícito)

Tu repo **ya existe**: `origin → https://github.com/ilymaiki/ratio.git`. Hoy tienes sin subir: `M info/neto.md` y `?? info/android.md`. El código Spring subido no sirve como app.

**Decisión que tienes que tomar (solo 1 vez):**

- **Opción 1 — Reemplazar el repo (recomendada):** el repo `ratio` pasa a ser la app Android. El Spring desaparece del historial visible.
- **Opción 2 — Repo nuevo:** dejas `ratio` como archivo y creas `ratio-android`. Más limpio si quieres conservar el experimento Spring.

**Si eliges Opción 1 (pasos concretos en PowerShell):**

```powershell
# 1. Entrar a la carpeta vieja y guardar lo pendiente
Set-Location "C:\Users\Dani\Documents\Visual\App\Ratio"
git status --short --branch
git add info/neto.md info/android.md
git commit -m "docs(info): cierra decisiones neto + añade android.md"
git push origin main

# 2. Archivar el Spring viejo en una rama por si acaso (1 minuto, te salva)
git branch legacy-spring
git push origin legacy-spring

# 3. Vaciar main para recibir el proyecto Android (desde GitHub web o local).
#    Lo más simple: borra todo menos .git, luego copia encima el contenido de Ratio-android.
#    En local:
New-Item -ItemType Directory -Path "C:\Users\Dani\Documents\Visual\App\Ratio-android-tmp"
# (copia aquí el proyecto creado por Android Studio, luego lo mueves a Ratio/ y haces commit)
```

Secuencia real cuando ya tengas `Ratio-android` funcionando:

```powershell
Set-Location "C:\Users\Dani\Documents\Visual\App\Ratio"
# Borra el scaffold Spring (conserva .git e info/)
Get-ChildItem -Exclude ".git","info" | Remove-Item -Recurse -Force
# Copia el contenido de Ratio-android dentro ( hazlo desde el Explorador )
git add -A
git commit -m "feat(android): esqueleto Kotlin+Compose v0.1, reemplaza scaffold Spring"
git push origin main
```

**`.gitignore` Android (sustituye el actual de Spring):** cuando crees el proyecto, Studio genera uno. Debe incluir como mínimo `build/`, `.gradle/`, `local.properties`, `.idea/`, `*.apk`. No subas nunca `local.properties` (contiene tu ruta del SDK) ni la carpeta `build/`.

**README en GitHub:** deja 5 líneas: qué es, 100% local, cómo abrir (`Open → Ratio-android`), cómo compilar (`Run`), y enlace a `info/neto.md`. Sin eso la página del repo se ve vacía.

---

## 3. Estructura de carpetas (proyecto Android Studio)

```
Ratio-android/
├── app/
│   ├── build.gradle.kts          # Compose BOM, Room, Navigation, Coroutines
│   ├── src/main/
│   │   ├── AndroidManifest.xml    # SIN permiso INTERNET. Solo POST_NOTIFICATIONS (v0.2)
│   │   ├── java/app/ratio/
│   │   │   ├── MainActivity.kt            # NavHost + Scaffold + botón [+]
│   │   │   ├── RatioApp.kt                # Application: crea Room + Seed
│   │   │   ├── data/
│   │   │   │   ├── db/
│   │   │   │   │   ├── AppDatabase.kt     # @Database(entities=[Expense,Category,Budget], v1)
│   │   │   │   │   ├── ExpenseDao.kt      # SUM por mes/categoría, lista filtrada
│   │   │   │   │   ├── CategoryDao.kt
│   │   │   │   │   └── BudgetDao.kt
│   │   │   │   ├── model/
│   │   │   │   │   ├── Expense.kt         # id, amountCents: Long, categoryId, date, note?, photoPath?
│   │   │   │   │   ├── Category.kt        # id, name, color, icon, isPredefined, isHidden
│   │   │   │   │   └── Budget.kt          # categoryId | "GLOBAL", month "YYYY-MM", limitCents
│   │   │   │   ├── repo/
│   │   │   │   │   ├── ExpenseRepo.kt     # Totales derivados por query, nunca cacheados
│   │   │   │   │   ├── CategoryRepo.kt    # Seed 7 predefinidas + 3 custom gratis
│   │   │   │   │   └── BudgetRepo.kt      # Día 1: gastado→0, límite se conserva (editable)
│   │   │   │   └── prefs/
│   │   │   │       └── Prefs.kt           # DataStore: global, onboarding visto
│   │   │   ├── ui/
│   │   │   │   ├── nav/Routes.kt          # HOME, NEW, HISTORY, BUDGETS, SETTINGS, DETAIL/{id}
│   │   │   │   ├── home/HomeScreen.kt + HomeViewModel.kt
│   │   │   │   │   # Total grande + barra global + top 3 + [+]
│   │   │   │   ├── expense/NewExpenseScreen.kt + NewExpenseViewModel.kt
│   │   │   │   │   # Etiqueta > Cantidad > Foto? > Guardar (<300ms, última etiqueta preseleccionada)
│   │   │   │   ├── history/HistoryScreen.kt + HistoryViewModel.kt
│   │   │   │   │   # Lista + filtro mes/etiqueta + swipe editar/borrar
│   │   │   │   ├── budgets/BudgetsScreen.kt + BudgetsViewModel.kt
│   │   │   │   │   # "45€ / 50€ (90%)" con BudgetBar
│   │   │   │   ├── detail/CategoryDetail.kt (modal)
│   │   │   │   ├── settings/SettingsScreen.kt (3 tags, export/import, borrar todo)
│   │   │   │   ├── components/AmountInput.kt, TagChip.kt, BudgetBar.kt, EmptyStates.kt
│   │   │   │   └── theme/Theme.kt (Material You), Tags.kt (colores/iconos 7 tags)
│   │   │   ├── domain/Month.kt (YYYY-MM), BudgetStatus.kt (OK/WARNING/OVER), Seed.kt
│   │   │   ├── export/CsvExport.kt (BOM UTF-8), PdfReport.kt, BackupZip.kt (v0.2)
│   │   │   └── notify/BudgetAlarm.kt (v0.2, WorkManager, 80% y 100%)
│   │   └── res/ (strings es-ES, iconos)
│   ├── build.gradle.kts (root: android.application, kotlin.android, ksp)
│   ├── settings.gradle.kts
│   └── gradle.properties
└── info/ (copia de bruto.md, neto.md, android.md)
```

Cada fichero tiene **1 responsabilidad**. Si un `Screen` pasa de ~200 líneas, extrae componentes a `components/`. Si un `ViewModel` hace SQL directo, muévelo al `Repo`.

## 4. Dependencias mínimas

- `androidx.compose.bom` + `material3` + `material-icons-extended`
- `navigation-compose`, `lifecycle-viewmodel-compose`, `runtime-compose`
- `room-runtime + room-ktx`, `ksp(room-compiler)`
- `datastore-preferences`, `coroutines-android`
- v0.2: `workmanager` (alarmas), `documentfile` (guardar en `Descargas/Ratio/`)

## 5. Reglas de neto.md (no romper) + orden

1. `amountCents: Long`, formato `es-ES`, € fijo. Teclado numérico directo.
2. Totales por query `SUM WHERE month`, nunca cacheados.
3. Presupuesto: gastado→0 día 1, límite se conserva.
4. Foto opcional en `app_data/`, placeholder si falta.
5. 7 predefinidas (ocultables, no borrables) + 3 custom gratis + 2 premium futuro.
6. CSV con BOM + PDF + ZIP (el ZIP conserva fotos, el CSV no).
7. Sin `INTERNET`, sin login, sin analytics en MVP.

Orden: ① `model+db+Seed` → ② `NewExpense+Home` → ③ `History+Budgets+Detail` → ④ `Settings+CSV+PDF` → ⑤ v0.2 `ZIP+alarmas+import`.

## 6. Checklist "está bien hecho"

- [ ] `Run` en Pixel 5 abre Home sin crash.
- [ ] Registrar gasto = 3 toques, vuelve a Home en <300ms.
- [ ] Día 1 simulado: gastado 0, límite intacto.
- [ ] CSV abre en Excel con tildes bien (BOM).
- [ ] `git push` sube a `ilymaiki/ratio` y la web de GitHub muestra README + `info/`.

*Fin de android.md — actualizar aquí antes que en código si cambia algo.*
