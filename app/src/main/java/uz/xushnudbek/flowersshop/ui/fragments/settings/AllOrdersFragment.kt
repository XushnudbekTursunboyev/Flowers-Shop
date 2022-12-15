package uz.xushnudbek.flowersshop.ui.fragments.settings

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import uz.xushnudbek.flowersshop.R
import uz.xushnudbek.flowersshop.adapters.recyclerview.AllOrdersAdapter
import uz.xushnudbek.flowersshop.databinding.FragmentAllOrdersBinding
import uz.xushnudbek.flowersshop.resource.Resource
import uz.xushnudbek.flowersshop.viewmodel.shopping.ShoppingViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import uz.xushnudbek.flowersshop.ui.activity.MainActivity


class AllOrdersFragment : Fragment() {

    val TAG = "AllOrdersFragment"
    private lateinit var viewModel: ShoppingViewModel
    private lateinit var binding: FragmentAllOrdersBinding
    private lateinit var allOrdersAdapter: AllOrdersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        viewModel.getUserOrders()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAllOrdersBinding.inflate(inflater)
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility = View.GONE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeAllOrders()
        onCloseClick()
        onItemClick()
        binding.imgCloseOrders.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun onItemClick() {
        allOrdersAdapter.onItemClick = {order ->
            val bundle = Bundle()
            bundle.putSerializable("order",order)
            findNavController().navigate(R.id.action_allOrdersFragment_to_orderDetails,bundle)

        }
    }

    private fun onCloseClick() {
        binding.imgCloseOrders.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun observeAllOrders() {
        viewModel.userOrders.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    showLoading()
                    return@observe
                }

                is Resource.Success -> {
                    hideLoading()
                    val orders = response.data
                    if (orders!!.isEmpty())
                        binding.apply {
                            imgEmptyBox.visibility = View.VISIBLE
                            imgEmptyBoxTexture.visibility = View.VISIBLE
                            tvEmptyOrders.visibility = View.VISIBLE
                            return@observe
                        }
                    binding.apply {
                        imgEmptyBox.visibility = View.GONE
                        imgEmptyBoxTexture.visibility = View.GONE
                        tvEmptyOrders.visibility = View.GONE
                    }
                    allOrdersAdapter.differ.submitList(orders)
                    return@observe
                }

                is Resource.Error -> {
                    hideLoading()
                    Toast.makeText(
                        activity,
                        resources.getText(R.string.error_occurred),
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e(TAG, response.message.toString())
                    return@observe
                }
            }
        }
    }

    private fun hideLoading() {
        binding.progressbarAllOrders.visibility = View.GONE

    }

    private fun showLoading() {
        binding.progressbarAllOrders.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        allOrdersAdapter = AllOrdersAdapter()
        binding.rvAllOrders.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = allOrdersAdapter
        }
    }
}