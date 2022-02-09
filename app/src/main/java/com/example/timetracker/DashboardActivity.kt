package com.example.timetracker

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class DashboardActivity : AppCompatActivity() {
    lateinit var categoriesPieChart: PieChart
    lateinit var tasksPieChart: PieChart
    var allCategories: ArrayList<CategoryDataModel> = ArrayList()
    var allTasks: ArrayList<TaskDataModel> = ArrayList()
    var allTimeEvents: ArrayList<TimeEventDataModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        supportActionBar?.title = "TimeTracker - Dashboard"

        categoriesPieChart = findViewById(R.id.pieChart_view)
        tasksPieChart = findViewById(R.id.pieChart_view2)
        categoriesPieChart.setOnChartValueSelectedListener(PieChartOnChartValueSelected(allTasks, allTimeEvents, object: MyFunctionCallback {
            override fun onCallCallback(value: MutableMap<String, Float?>) {
                showPieChart(tasksPieChart, value, false)
            }
        }))

        categoriesPieChart.visibility = View.GONE
        tasksPieChart.visibility = View.GONE

        CategoriesDAL.getAllCategories(object: MyGetCallback {
            override fun onGetCallback(value: ArrayList<CategoryDataModel>) {
                allCategories.addAll(value)

                TasksDAL.getAllTasks(object: MyTasksGetCallback {
                    override fun onGetCallback(value: ArrayList<TaskDataModel>) {
                        allTasks.addAll(value)

                        TimeTaskDAL.getAllTimeEvents(object: MyTimeEventsGetCallback {
                            override fun onGetCallback(value: ArrayList<TimeEventDataModel>) {
                                allTimeEvents.addAll(value)

                                calculateCharts()
                            }
                        })
                    }
                })
            }
        })
    }

    private fun calculateCharts() {
        val progressBar: ProgressBar = findViewById(R.id.progress_Bar)
        val progressBarText: TextView = findViewById(R.id.progressBarText)
        val categoriesMapping: MutableMap<String, CategoryDataModel> = HashMap()
        val tasksMapping: MutableMap<String, TaskDataModel> = HashMap()
        val categoriesChartEntries: MutableMap<String, Float?> = HashMap()

        for (item in allCategories) {
            categoriesMapping[item.id] = item
            categoriesChartEntries[item.id] = 0f
        }
        for (item in allTasks) { tasksMapping[item.id] = item }
        val totalTimeRange = (allTimeEvents.first().startedAt.toDate().time / 1000f / 3600f / 24f) - (allTimeEvents.last().startedAt.toDate().time / 1000f / 3600f / 24f)

        for (i in 0 until allTimeEvents.size - 1) {
            val firstEvent = allTimeEvents[i]
            val secondEvent = allTimeEvents[i + 1]
            val eventCategoryId = categoriesMapping[tasksMapping[secondEvent.taskId]!!.categoryId]!!.id
            categoriesChartEntries[eventCategoryId] =
                categoriesChartEntries[eventCategoryId]?.plus(((
                        ((firstEvent.startedAt.toDate().time / 1000f / 3600f / 24f) - (secondEvent.startedAt.toDate().time / 1000f / 3600f / 24f)) / totalTimeRange) * 100f)
                )
        }

        showPieChart(categoriesPieChart, categoriesChartEntries, true)
        progressBar.visibility = View.GONE
        progressBarText.visibility = View.GONE
        categoriesPieChart.visibility = View.VISIBLE
        tasksPieChart.visibility = View.VISIBLE
        tasksPieChart.setNoDataText("Click on a category above to load more data")
        tasksPieChart.setNoDataTextColor(Color.BLACK)
    }

    private fun showPieChart(pieChart: PieChart, givenMap: MutableMap<String, Float?>, isChartDataCategories: Boolean) {
        val pieEntries: ArrayList<PieEntry> = ArrayList()
        val label = "Categories"
        val colors: ArrayList<Int> = ArrayList()
        val typeAmountMap: MutableMap<String, Bundle> = HashMap()

        for (element in ColorTemplate.PASTEL_COLORS) { colors.add(element) }
        for (id in givenMap.keys) {
            if (givenMap[id]!! >= 1f) {
                if (isChartDataCategories) {
                    val elem = allCategories.find { it.id == id }
                    val b = Bundle()
                    b.putFloat("value", givenMap[id]!!)
                    b.putString("id", elem!!.id)
                    typeAmountMap[elem.name] = b
                } else {
                    val elem = allTasks.find { it.id == id }
                    val b = Bundle()
                    b.putFloat("value", givenMap[id]!!)
                    b.putString("id", elem!!.id)
                    typeAmountMap[elem.name] = b
                }
            }
        }
        for (type in typeAmountMap.keys) {
            if (typeAmountMap[type]!!.getFloat("value") >= 1f) {
                pieEntries.add(PieEntry(typeAmountMap[type]!!.getFloat("value"), type, typeAmountMap[type]!!))
            }
        }

        val pieDataSet = PieDataSet(pieEntries, label)
        pieDataSet.valueTextSize = 18f
        pieDataSet.colors = colors
        val pieData = PieData(pieDataSet)
        pieData.setDrawValues(true)
        pieData.setValueFormatter(PercentFormatter())

        pieChart.description.isEnabled = false
        val l: Legend = pieChart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.textSize = 14f
        l.setDrawInside(false)
        val entries: ArrayList<LegendEntry> = ArrayList()
        for (i in 0 until typeAmountMap.size) {
            val entry = LegendEntry()
            entry.formColor = ColorTemplate.PASTEL_COLORS[i]
            entry.label = pieEntries[i].label
            entries.add(entry)
        }
        l.setCustom(entries)

        pieChart.data = pieData
        pieChart.invalidate()
    }
}

private class PieChartOnChartValueSelected(
    private val allTasks: ArrayList<TaskDataModel>,
    private val allTimeEvents: ArrayList<TimeEventDataModel>,
    private val callback: MyFunctionCallback
) : OnChartValueSelectedListener {
    override fun onValueSelected(e: Entry?, h: Highlight?) {
        if (e == null) {
            return
        }
        val categoryId = (e.data as Bundle).getString("id")
        val validTasks = allTasks.filter { it.categoryId == categoryId }
        val validTasksIds = validTasks.map { it.id }
        val tasksChartEntries: MutableMap<String, Float?> = HashMap()

        for (item in validTasks) {
            tasksChartEntries[item.id] = 0f
        }

        val validTimeEvents = allTimeEvents.filter { validTasksIds.contains(it.taskId) }
        val totalTimeRange = (validTimeEvents.first().startedAt.toDate().time / 1000f / 3600f / 24f) - (validTimeEvents.last().startedAt.toDate().time / 1000f / 3600f / 24f)
        for (i in 0 until validTimeEvents.size - 1) {
            val firstEvent = validTimeEvents[i]
            val secondEvent = validTimeEvents[i + 1]
            val eventTaskId = secondEvent.taskId
            tasksChartEntries[eventTaskId] =
                tasksChartEntries[eventTaskId]?.plus(((
                        ((firstEvent.startedAt.toDate().time / 1000f / 3600f / 24f) - (secondEvent.startedAt.toDate().time / 1000f / 3600f / 24f)) / totalTimeRange) * 100f)
                )
        }
        callback.onCallCallback(tasksChartEntries)
    }

    override fun onNothingSelected() {
        TODO("Not yet implemented")
    }

}