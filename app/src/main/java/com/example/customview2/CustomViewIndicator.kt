package com.example.customview2

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class CustomViewIndicator @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {
    // Количество страниц, для которых создается индикатор
    private var pageCount: Int = 0

    // Текущая страница, которая будет отображаться как активная
    private var currentPage: Int = 0

    // Кисть для рисования активного индикатора (синий цвет)
    private val paintActive = Paint().apply {
        color = 0xFF0000FF.toInt()    // Устанавливаем синий цвет (#0000FF)
    }

    // Кисть для рисования неактивных индикаторов (серый цвет)
    private val paintInactive = Paint().apply {
        color = 0xFF888888.toInt()    // Устанавливаем серый цвет (#888888)
    }

    // Метод для установки количества страниц (точек индикатора)
    fun setup(pageCount: Int) {
        this.pageCount = pageCount    // Сохраняем количество страниц
        invalidate()                  // Просим систему перерисовать View
    }

    // Метод для обновления текущей страницы
    fun setCurrentPage(page: Int) {
        currentPage = page            // Устанавливаем текущую страницу
        invalidate()                  // Перерисовываем View с обновленным состоянием
    }

    // Переопределение метода для рисования индикатора
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)          // Вызываем метод суперкласса для стандартного поведения View

        // Если холст пустой (canvas == null) или нет страниц, выходим из метода
        if (pageCount == 0) return

        // Радиус кружков индикатора
        val circleRadius = 50f

        // Расстояние между центрами соседних кружков
        val spacing = 50f

        // Вычисляем начальную координату X для рисования, чтобы центрировать индикатор
        val startX = (width - ((circleRadius * 2 + spacing) * pageCount - spacing)) / 2

        // Координата Y для всех кружков (по центру по высоте View)
        val y = height / 2f

        // Рисуем кружки
        for (i in 0 until pageCount) {
            // Координата X для текущего кружка
            val x = startX + i * (circleRadius * 2 + spacing)

            // Выбираем кисть: активная (синий) для текущей страницы, неактивная (серый) для остальных
            val paint = if (i == currentPage) paintActive else paintInactive

            // Рисуем кружок на холсте (x, y - координаты центра; circleRadius - радиус)
            canvas.drawCircle(x, y, circleRadius, paint)
        }
    }
}