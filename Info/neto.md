# Ratio — Especificación Neta (neto.md)

> Derivado de `bruto.md` + aclaraciones del autor (06/10/2026).
> Objetivo: convertir la idea bruta en una especificación clara, priorizada y lista para diseñar / programar el MVP.

---

## 1. Visión en una frase

**App Android 100% local y privada para que adolescentes y estudiantes que viven fuera de casa sepan en qué se les va el dinero cada mes, de forma rápida e informativa.**

Lo que NO es:
- No es un banco, no mueve dinero real.
- No bloquea gastos ni impone castigos, solo **informa** (aviso en rojo si te pasas).
- No requiere cuenta, ni internet, ni nube.

## 2. Usuario objetivo

- **Principal:** adolescentes / estudiantes (16-26 años) viviendo fuera del círculo familiar por primera vez (piso compartido, residencia).
- **Perfil:** poco hábito de presupuestar, necesita algo en <10 segundos, visual, sin tecnicismos.
- **Dolor:** "a día 20 ya no sé en qué me gasté 300€".
- **Uso típico:** abre la app justo después de pagar, elige etiqueta, pone cantidad, cierra.

## 3. Plataforma y estrategia de pruebas

- **Producto final: Android nativo (100% móvil).**
- **Desarrollo/pruebas: en ordenador** (emulador Android / Android Studio) porque el autor no puede probar directamente en móvil.
  - Implicación: UI debe diseñarse mobile-first (pantalla 360x640 base, táctil, una mano), aunque se pruebe con ratón.
  - No se hará versión desktop ni web responsive en esta fase. Todo lo "desktop" es solo entorno de test.

Decisión técnica cerrada (07/10/2026): **Kotlin + Jetpack Compose** (nativo Android). El scaffold Spring Boot actual se descarta: era plantilla de servidor, incompatible con 100% local sin internet.

## 4. Principios de diseño

1. **Registro en 3 toques:** Etiqueta > Cantidad > Guardar.
2. **Local-first:** todos los datos en el dispositivo. Privacidad total estilo "vault de Obsidian": una carpeta/archivos propios que el usuario puede copiar, mover o borrar.
3. **Informativa, no punitiva:** el rojo es un indicativo visual, no un bloqueo.
4. **Opcionalidad:** tags, fotos, notas y desgloses son siempre opcionales. El flujo mínimo nunca los exige.

## 5. Categorías / Tags

### 5.1 Tags predefinidos (propuesta cerrada para MVP)

Basados en `bruto.md`, normalizados:

1. **Piso** (con sub-opción plegable: Alquiler / Luz / Agua / Internet — opcional, no obliga a desglosar)
2. **Comida** (compra supermercado / tienda alimentación)
3. **Comidas fuera** (bar, restaurante, delivery, cafetería)
4. **Transporte** (bus, metro, gasolina, bici, taxi)
5. **Ocio** (cine, juegos, suscripciones, salidas)
6. **Caprichos** (compras impulsivas, antojos)
7. **Calidad de vida / Casa** (detergentes, limpieza, menaje, higiene, cosas que aumentan comodidad)
8. **Personalizado** (slot único gratuito — ver 5.2)

> Nota: `bruto.md` separaba Ocio / Caprichos / Comidas fuera. Se mantienen separados porque para un estudiante significan comportamientos distintos.

### 5.2 Tags personalizados

- **MVP gratis:** 3 tags personalizados creados por el usuario (nombre + color + icono).
- **Futuro premium (no implementar ahora):** pack de +2 tags adicionales de pago (total 5 custom).
- Reglas:
  - Los tags predefinidos siempre existen, pero usarlos es opcional (puedes registrar todo en un solo tag si quieres).
  - No se puede borrar un tag predefinido, solo ocultar.
  - Eliminar un tag personalizado no borra sus gastos: se reasignan a "Sin categoría" o se pide reasignación.

## 6. Registro de un gasto (flujo core)

**Flujo MVP confirmado por el autor:**

```
Home > [+ Nuevo gasto] > Seleccionar etiqueta > Introducir cantidad (€) >
[opcional: añadir foto] > [opcional: nota corta / fecha] > Guardar
```

Campos:
- **Obligatorios:** `etiqueta`, `cantidad`, `fecha` (por defecto hoy, editable).
- **Opcionales:** `foto` (ticket o cualquier cosa relevante para el usuario), `nota` (texto libre 140 car., ej: "mercadona semanal").
- **No hay en MVP:** desglose ítem a ítem (leche 1,20€ + pan 0,80€...), OCR de tickets. Quedan como idea futura descartada para MVP por complejidad.

Detalles UX:
- Teclado numérico directo al poner cantidad.
- Última etiqueta usada preseleccionada para acelerar.
- Confirmación visual inmediata (<300ms) y vuelta a Home.
- Editar / borrar gasto desde el historial con swipe.

### 6.1 Gasto complejo (futuro, fuera de MVP)

Idea original de `bruto.md` (§23): poder añadir singularmente cada producto dentro de un mismo ticket.
- Se aparca: añade fricción y choca con el principio "3 toques".
- Si se retoma: sería un modo "lista" dentro de un gasto (gasto padre con N líneas hijas que suman el total).

## 7. Presupuestos / Limitadores

Concepto aclarado: **solo indicativo visual rojo.**

- El usuario define un **presupuesto mensual global** (ej: 600€/mes) y/o **presupuesto mensual por categoría** (ej: Transporte 50€).
- La app calcula `% gastado` y `% restante`.
  - Ejemplo: Presupuesto 100€, gastado 90€ → muestra "90% usado · 10% libre (10€)".
- Estados visuales:
  - `<80%`: normal (verde/neutro)
  - `80-99%`: aviso (ámbar: "te queda poco")
  - `>=100%`: sobrepasado (rojo + etiqueta "en rojo")
- Comportamiento:
  - Nunca bloquea ni impide registrar más gastos.
  - Notificación local opcional al superar el 80% y 100% (sin servidor, solo alarma local Android).
  - Los presupuestos se reinician cada mes natural (1-31): el **gastado se pone a 0** pero el **límite se conserva** (ej: 90€/100€ en enero → 0€/100€ el 1 de febrero). Sin prórrogas ni acumulados en MVP.
  - Si no define presupuesto en una categoría, solo se muestra el total gastado, sin porcentaje.

Pantalla propuesta: lista de categorías con barra de progreso + cifra "45€ / 50€ (90%)".

## 8. Métricas y estadísticas (MVP)

**MVP imprescindible (confirmado):**

1. **Total mensual** gastado.
2. **Total mensual por categoría** (lista + gráfico circular o barras).
3. **% presupuesto usado / libre por categoría** (y global si existe).

Pantallas MVP:
- **Home / Este mes:** cifra grande total + barra global + top 3 categorías + botón [+].
- **Detalle categoría:** total, presupuesto, % libre, historial de movimientos de esa categoría ese mes.
- **Historial:** lista cronológica filtrable por mes y por etiqueta.

**Futuro premium (no MVP):**
- Comparativa entre meses, media diaria/semanal, tendencia.
- Predicción ("a este ritmo acabarás en X€").
- Detección de anomalías, informes avanzados, gráficos heatmap, etc.

## 9. Almacenamiento local y privacidad

Confirmado: **100% local, sin cuenta.**

- Base de datos local en el teléfono (recomendado: Room / SQLite).
- Fotos guardadas en carpeta privada de la app (`app_data/`), referenciadas por URI, no en la galería pública salvo que el usuario exporte.
- Modelo "vault": posibilidad de ver/exportar la carpeta de datos como un conjunto autocontenido para copiar a otro dispositivo vía archivo.
- Sin analytics, sin login, sin ads con tracking en MVP.
- Borrado total desde Ajustes ("borrar todos mis datos").

Implicaciones:
- Si pierde el móvil sin backup, pierde los datos. Por eso la exportación es crítica (ver §10).
- No hay sincronización multi-dispositivo en MVP.

## 10. Exportación / Importación

Confirmado: **CSV + PDF.**

- **CSV (datos):** una fila por gasto: `id, fecha, etiqueta, cantidad, nota, tiene_foto (sí/no), presupuesto_categoria`.
  - Uso: abrir en Excel/Sheets, importar a otro dispositivo con la misma app (Importar CSV).
  - Incluir cabecera en español + UTF-8 con BOM para Excel.
- **PDF (informe):** resumen mensual legible: portada mes, total, tabla por categoría (presupuesto / gastado / % libre / estado color), lista de movimientos, gráfico simple.
  - Uso: guardar, imprimir o compartir por WhatsApp/email.
- **Backup completo (recomendado añadir):** ZIP con DB + fotos para migración total entre móviles. No estaba en `bruto.md` pero es necesario porque solo CSV pierde las fotos. Propuesta: `.ratio-backup.zip`.
- Ubicación: Android Sharesheet + guardado en `Descargas/Ratio/`.

## 11. Pantallas MVP (alcance cerrado)

Mínimo viable para considerar la app usable:

1. `Home / Resumen mes` (total, % global, lista categorías con barras)
2. `Nuevo gasto` (selector etiqueta + cantidad + foto opcional + guardar)
3. `Historial` (lista + filtro mes/etiqueta + editar/borrar)
4. `Presupuestos` (definir límites por categoría + global)
5. `Ajustes` (crear hasta 3 tags propios, exportar CSV/PDF, importar CSV, borrar datos, acerca de)
6. `Detalle categoría` (puede ser modal, no pantalla completa)

Fuera de MVP: login, nube, OCR, widgets, recordatorios inteligentes, multi-moneda (solo € en MVP), modo oscuro avanzado (seguir Material You por defecto).

## 12. Modelo de negocio (a futuro, no implementar)

- Gratis: todo el MVP descrito aquí (incluidos 3 tags custom y estadísticas básicas).
- Futuro premium (diseño, no código aún):
  - Slots extra de tags (+2, hasta 5 custom).
  - Estadísticas avanzadas / comparativas / predicción.
- Formato pendiente: pago único vs suscripción 1-3€/mes. No decidir ahora, validar primero que la gente usa la app gratis.

## 13. Datos — esquema mínimo sugerido

```kotlin
Expense(id, amountCents: Long, categoryId: String, date: Long, note: String?, photoPath: String?)
Category(id, name, color, icon, isPredefined: Boolean, isHidden: Boolean)
Budget(categoryId | "GLOBAL", month: "YYYY-MM", limitCents: Long)
```

Reglas: `amountCents` en céntimos (evita errores float), `date` en epoch millis, mes en formato `YYYY-MM` para agrupar.

## 14. Casos borde y decisiones tomadas

- Sin presupuesto definido → no mostrar %, solo total.
- Gasto sin foto → funciona igual, foto 100% opcional.
- Cambio de mes → nuevo periodo vacío en gastado, el límite se conserva automáticamente del mes anterior (editable).
- Moneda: solo EUR (€) en MVP, símbolo fijo, formato `es-ES`.
- Editar gasto pasado no recalcula mal: totales siempre derivados de la tabla, no cacheados.
- Foto borrada del sistema → mostrar placeholder "imagen no disponible", no romper el gasto.
- Historial: para revisar antiguos presupuestos.

## 15. Roadmap sugerido

- **v0.1 MVP:** puntos §11 + §8 básico + CSV/PDF simple + 100% local.
- **v0.2:** backup ZIP, importar CSV robusto, notificaciones locales 80/100%, pulido UI.
- **v1.0:** pulido tienda Play, icono, onboarding ("¿cuál es tu presupuesto mensual?").
- **v2.0-futuro:** premium (tags extra + stats avanzadas), OCR opcional, desglose complejo, sync cifrada opcional.

---

*Fin de neto.md — listo para usar como base de diseño y desarrollo. Si cambia alguna decisión, actualizar aquí antes que en código.*
