package uz.xushnudbek.flowersshop.ui.fragments.shopping.address

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import uz.xushnudbek.flowersshop.R
import uz.xushnudbek.flowersshop.databinding.FragmentAddressBinding
import uz.xushnudbek.flowersshop.model.Address
import uz.xushnudbek.flowersshop.resource.Resource
import uz.xushnudbek.flowersshop.viewmodel.shopping.ShoppingViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import uz.xushnudbek.flowersshop.ui.activity.MainActivity


class AddressFragment : Fragment() {
    private lateinit var address:Address
    val TAG = "AddressFragment"
    private lateinit var binding: FragmentAddressBinding
    private lateinit var viewModel: ShoppingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        arguments?.apply {
           // address = getSerializable("address") as Address
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddressBinding.inflate(inflater)
        val bottomNavigation = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigation?.visibility = View.INVISIBLE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //setInformation(address)
        updateAddress()
        observeUpdateAddress()
        //onDeleteClick(address)
        //observeDeleteAddress()

        onImgCloseClick()

    }

    private fun observeDeleteAddress() {
        viewModel.deleteAddress.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Loading -> {
                    showLoading()
                    return@Observer
                }

                is Resource.Success -> {
                    hideLoading()
                    findNavController().navigateUp()
                    viewModel.deleteAddress.postValue(null)
                    return@Observer
                }

                is Resource.Error -> {
                    hideLoading()
                    Log.e(TAG, response.message.toString())
                    Toast.makeText(activity, "Error occurred", Toast.LENGTH_SHORT).show()
                    return@Observer
                }
            }
        })    }

    private fun onDeleteClick(address: Address) {
        binding.btnDelete.setOnClickListener {
            viewModel.deleteAddress(address)
        }
    }

    private fun observeUpdateAddress() {
        viewModel.updateAddress.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Loading -> {
                    showLoading()
                    return@Observer
                }

                is Resource.Success -> {
                    hideLoading()
                    findNavController().navigateUp()
                    viewModel.updateAddress.postValue(null)
                    return@Observer
                }

                is Resource.Error -> {
                    hideLoading()
                    Log.e(TAG, response.message.toString())
                    Toast.makeText(activity, "Error occurred", Toast.LENGTH_SHORT).show()
                    return@Observer
                }
            }
        })
    }

    private fun updateAddress() {
        binding.btnAddNewAddress.setOnClickListener {
            binding.apply {
                val title = edAddressTitle.text.toString()
                val fullName = edFullName.text.toString()
                val street = edStreet.text.toString()
                val phone = edPhone.text.toString()
                val city = edCity.text.toString()
                val state = edState.text.toString()

                val newAddress = Address(title, fullName, street, phone, city, state)
                viewModel.updateAddress(newAddress,newAddress)
            }
        }
    }

    private fun setInformation(address: Address) {
        binding.apply {
            edAddressTitle.setText(address.addressTitle)
            edFullName.setText(address.fullName)
            edPhone.setText(address.phone)
            edCity.setText(address.city)
            edState.setText(address.state)
            edStreet.setText(address.street)

            btnAddNewAddress.text = resources.getText(R.string.g_update)
        }
    }

    private fun observeAddAddress() {
        viewModel.addAddress.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Loading -> {
                    showLoading()
                    return@Observer
                }

                is Resource.Success -> {
                    hideLoading()
                    findNavController().navigateUp()
                    viewModel.addAddress.postValue(null)
                    return@Observer
                }

                is Resource.Error -> {
                    hideLoading()
                    Log.e(TAG, response.message.toString())
                    Toast.makeText(activity, "Error occurred", Toast.LENGTH_SHORT).show()
                    return@Observer
                }
            }
        })
    }

    private fun hideLoading() {
        binding.apply {
            btnAddNewAddress.visibility = View.VISIBLE
            btnDelete.visibility = View.VISIBLE
            progressbarAddress.visibility = View.INVISIBLE
        }
    }

    private fun showLoading() {
        binding.apply {
            btnAddNewAddress.visibility = View.INVISIBLE
            btnDelete.visibility = View.INVISIBLE
            progressbarAddress.visibility = View.VISIBLE
        }
    }

    private fun onSaveClick() {
        binding.apply {
            btnAddNewAddress.setOnClickListener {
                val title = edAddressTitle.text.toString()
                val fullName = edFullName.text.toString()
                val street = edStreet.text.toString()
                val phone = edPhone.text.toString()
                val city = edCity.text.toString()
                val state = edState.text.toString()

                if (title.isEmpty() || fullName.isEmpty() || street.isEmpty() ||
                    phone.isEmpty() || city.isEmpty() || state.isEmpty()
                ) {
                    Toast.makeText(
                        activity,
                        "Make sure you filled all requirements",
                        Toast.LENGTH_LONG
                    ).show()
                    return@setOnClickListener
                }

                val address = Address(title, fullName, street, phone, city, state)
                viewModel.saveAddress(address)
            }
        }
    }

    private fun onImgCloseClick() {
        binding.imgAddressClose.setOnClickListener {
            findNavController().navigateUp()
        }
    }

}