package com.example.ukids

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ukids.databinding.FragmentPlaceListBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PlaceListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlaceListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var myData = mutableListOf<myRow>()
    private lateinit var binding: FragmentPlaceListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        // XML 데이터 가져오기
        val call1: Call<responseInfo1> = MyApplication.networkServiceXml.getXmlList(
            "pYVi5WRhkWtEwEK/I4kgN7b4rNT0ilJBAD0ZrcvBAAFFgV3kqfOSQ9cQn5eEczFo+9O1Q1g5b0UiGp10XfJcOA==",
            "xml",
            2,
            30
        )

        call1?.enqueue(object : Callback<responseInfo1> {
            override fun onResponse(call: Call<responseInfo1>, response: Response<responseInfo1>) {
                if(response.isSuccessful){
                    Log.d("mobileApp", "$response")
                    val resdatas = response.body()!!.row
                    for (i in 0 until resdatas.size) {
                        myData.add(myRow(resdatas[i]))
                    }
                    binding.PlaceListRecyclerView.layoutManager = LinearLayoutManager(activity)
                    binding.PlaceListRecyclerView.adapter = XmlAdapter(requireActivity(), myData)
                }
            }

            override fun onFailure(call: Call<responseInfo1>, t: Throwable) {
                Log.d("mobileApp", "onFailure")
            }
        })

        // XML 데이터 가져오기2
        val call2: Call<responseInfo2> = MyApplication.networkServiceXml.getXmlList2(
            "pYVi5WRhkWtEwEK/I4kgN7b4rNT0ilJBAD0ZrcvBAAFFgV3kqfOSQ9cQn5eEczFo+9O1Q1g5b0UiGp10XfJcOA==",
            "xml",
            2,
            30
        )

        call2?.enqueue(object : Callback<responseInfo2> {
            override fun onResponse(call: Call<responseInfo2>, response: Response<responseInfo2>) {
                if(response.isSuccessful){
                    Log.d("mobileApp", "$response")
                    val resdatas = response.body()!!.row
                    for (i in 0 until resdatas.size) {
                        myData.add(myRow(resdatas[i]))
                    }
                    binding.PlaceListRecyclerView.layoutManager = LinearLayoutManager(activity)
                    binding.PlaceListRecyclerView.adapter = XmlAdapter(requireActivity(), myData)

                }
            }

            override fun onFailure(call: Call<responseInfo2>, t: Throwable) {
                Log.d("mobileApp", "onFailure")
            }
        })

        // XML 데이터 가져오기3
        val call3: Call<responseInfo3> = MyApplication.networkServiceXml.getXmlList3(
            "pYVi5WRhkWtEwEK/I4kgN7b4rNT0ilJBAD0ZrcvBAAFFgV3kqfOSQ9cQn5eEczFo+9O1Q1g5b0UiGp10XfJcOA==",
            "xml",
            2,
            30
        )

        call3?.enqueue(object : Callback<responseInfo3> {
            override fun onResponse(call: Call<responseInfo3>, response: Response<responseInfo3>) {
                if(response.isSuccessful){
                    Log.d("mobileApp", "$response")
                    val resdatas = response.body()!!.row
                    for (i in 0 until resdatas.size) {
                        myData.add(myRow(resdatas[i]))
                    }
                    binding.PlaceListRecyclerView.layoutManager = LinearLayoutManager(activity)
                    binding.PlaceListRecyclerView.adapter = XmlAdapter(requireActivity(), myData)

                }
            }

            override fun onFailure(call: Call<responseInfo3>, t: Throwable) {
                Log.d("mobileApp", "onFailure")
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentPlaceListBinding.inflate(inflater, container, false)

        //val returnType = arguments?.getString("returnType")

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PlaceListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PlaceListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}