package com.example.teamsuite.fragments.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.teamsuite.abstractclass.BaseFragment
import com.example.teamsuite.data.model.company.CompanyData
import com.example.teamsuite.databinding.FragmentSettingsBinding
import com.example.teamsuite.repository.company.GetCompanyDataRepository
import com.example.teamsuite.utils.Constant
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.teamsuite.R
import com.example.teamsuite.viewmodel.company.GetCompanyDataViewModel
import com.example.teamsuite.viewmodelfactory.company.GetCompanyDataViewModelFactory

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SettingsFragment : BaseFragment() {
    private var _binding: FragmentSettingsBinding?= null
    private val binding get() = _binding!!
    private var companyid: String? = null
    private lateinit var getCompanyDataViewModel: GetCompanyDataViewModel
    private lateinit var data: CompanyData
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            companyid = arguments?.getString(Constant.companyid)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupGetCompanyDataViewModel()
        companyid?.let {
            getCompanyDataViewModel.getCompanyData(it)
        }
        observeGetCompanyDataResponse()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*
    Function for fill the fields
     */
    private fun fillAllFields(){
        val image = data.logo.url
        Glide.with(requireContext())
            .load(image)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.team_suite_logo) // while loading
                    .error(R.drawable.team_suite_logo)       // if failed
            )
            .into(binding.imgCompanyLogo)
        binding.txtCompanyName.setText(data.companyName)
        binding.txtDomain.setText(data.domain)
        binding.txtWebsite.setText(data.website)
        binding.txtEmail.setText(data.email)
        binding.txtAbout.setText(data.about)
        binding.txtStreet.setText(data.address.street)
        binding.txtCity.setText(data.address.city)
        binding.txtState.setText(data.address.state)
        binding.txtStartTime.setText(data.startTime)
        binding.txtEndTime.setText(data.endTime)
    }

    /*
    Function for setup get company data view model
     */
    private fun setupGetCompanyDataViewModel(){
        getCompanyDataViewModel = ViewModelProvider(
            this,
            GetCompanyDataViewModelFactory(GetCompanyDataRepository())
        )[GetCompanyDataViewModel::class.java]
    }

    /*
    Function for observe the response of get company data
     */
    private fun observeGetCompanyDataResponse(){
        getCompanyDataViewModel.getCompanyDataResult.observe(viewLifecycleOwner){ response ->
            if (response.success){
                hideLoader()
                data = response.data
                fillAllFields()
            }
            else{
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            }
        }
        getCompanyDataViewModel.error.observe(viewLifecycleOwner){ error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
        getCompanyDataViewModel.isLoading.observe(viewLifecycleOwner){
            if (it) showLoader() else hideLoader()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SettingsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}