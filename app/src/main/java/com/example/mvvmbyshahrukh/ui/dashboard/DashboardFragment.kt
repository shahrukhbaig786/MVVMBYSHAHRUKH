package com.example.mvvmbyshahrukh.ui.dashboard

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.example.mvvmbyshahrukh.R
import com.example.mvvmbyshahrukh.databinding.FragmentDashboardBinding
import com.example.mvvmbyshahrukh.repository.resource.Resource
import com.example.mvvmbyshahrukh.ui.dashboard.adapter.DashBoardListAdapter
import com.example.mvvmbyshahrukh.ui.model.MaterialMaster
import com.example.mvvmbyshahrukh.ui.response.BaseResponse
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException

class DashboardFragment : Fragment(), OnRefreshListener {

    private lateinit var dashboardAdapter: DashBoardListAdapter
    var responseList = ArrayList<MaterialMaster>()


    private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dashboardViewModel =
            ViewModelProvider(this)[DashboardViewModel::class.java]

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setListAdapter()
        val mSwipeRefreshLayout = _binding!!.swipeLayout
        mSwipeRefreshLayout.setOnRefreshListener(this)
        mSwipeRefreshLayout.setColorSchemeResources(
            R.color.purple_200,
            R.color.purple_200,
            R.color.black,
            R.color.purple_200
        )

        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })
        return root
    }

    private fun setListAdapter() {
        val rvList: RecyclerView = binding.rvSelection
        rvList.layoutManager = LinearLayoutManager(requireActivity())
        dashboardAdapter = DashBoardListAdapter(requireActivity(), responseList)
        rvList.adapter = dashboardAdapter


        dashboardViewModel.response.observe(viewLifecycleOwner, {
            if (it != null) {
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        showProgress(true)
                    }
                    Resource.Status.ERROR -> {
                        showProgress(false)
                    }
                    Resource.Status.SUCCESS -> {
                        showProgress(false)
                        if (it?.data != null) {
                            Log.e("response", "" + it.data.toString())
                            try {
                                val materialMasterList = ArrayList<MaterialMaster>()
                                val jsonArray = JSONArray(it.data!!.toString())
                                for (i in 0 until jsonArray.length()) {
                                    val gson = Gson()
                                    val materialMaster: MaterialMaster = gson.fromJson(
                                        jsonArray.getJSONObject(i).toString(),
                                        MaterialMaster::class.java
                                    )
                                    materialMasterList.add(materialMaster)
                                }
                                responseList.addAll(materialMasterList)
                                dashboardAdapter!!.notifyDataSetChanged()
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            }
        })
    }

    private var pDialog: Dialog? = null

    private fun showProgress(show: Boolean) {
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onRefresh() {
        binding.swipeLayout.isRefreshing = false
        responseList = ArrayList()
        dashboardViewModel.callMaterialAPIList()
        setListAdapter()
    }
}