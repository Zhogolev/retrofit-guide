package com.zhogolev.ui.weather.future.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import com.zhogolev.R
import com.zhogolev.data.db.unitalized.future.UnitSpecificSimpleFutureWeatherEntry
import com.zhogolev.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.future_list_weather_list_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class FutureWeatherListFragment : ScopedFragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: FutureListViewModelFactory by instance()

    private lateinit var viewModel: FutureWeatherListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.future_list_weather_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(FutureWeatherListViewModel::class.java)
        bindUi()
    }

    private fun bindUi() = launch(Dispatchers.Main) {
        val futureWeatherEntries = viewModel.weatherEntries.await()
        val weatherLocation = viewModel.weatherLocation.await()

        weatherLocation.observe(this@FutureWeatherListFragment, Observer { location ->
            if (location == null) return@Observer
            updateLocation(location.name)
        })

        futureWeatherEntries.observe(this@FutureWeatherListFragment, Observer { weatherEnties ->
            if(weatherEnties == null) return@Observer
            group_loading.visibility = View.GONE
            updateSubtitle("Next Week")
            initRecyclerView(weatherEnties.toFutureWeatherItems())
        })
    }

    private fun updateLocation(name: String) {
        (activity as AppCompatActivity).supportActionBar?.title = name
    }


    private fun updateSubtitle(subtitle: String) {
        (activity as AppCompatActivity).supportActionBar?.subtitle = subtitle
    }

    private fun List<UnitSpecificSimpleFutureWeatherEntry>.toFutureWeatherItems(): List<FutureWeatherItem>{
        return this.map {
            FutureWeatherItem(it)
        }
    }

    private fun initRecyclerView(items: List<FutureWeatherItem>){
        val groupAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(items)
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@FutureWeatherListFragment.context)
            adapter = groupAdapter
        }

        groupAdapter.setOnItemClickListener { item, view ->
            Toast.makeText(this@FutureWeatherListFragment.context, "Clicked", Toast.LENGTH_SHORT).show()
        }
    }
}
