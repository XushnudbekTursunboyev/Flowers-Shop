package uz.xushnudbek.flowersshop.ui.fragments.settings

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kleine.SpacingDecorator.VerticalSpacingItemDecorator
import uz.xushnudbek.flowersshop.R
import uz.xushnudbek.flowersshop.adapters.recyclerview.CartRecyclerAdapter
import uz.xushnudbek.flowersshop.databinding.FragmentOrderDetailsBinding
import uz.xushnudbek.flowersshop.model.Order
import uz.xushnudbek.flowersshop.resource.Resource
import uz.xushnudbek.flowersshop.ui.activity.MainActivity
import uz.xushnudbek.flowersshop.util.Constants.Companion.ORDER_CONFIRM_STATE
import uz.xushnudbek.flowersshop.util.Constants.Companion.ORDER_Delivered_STATE
import uz.xushnudbek.flowersshop.util.Constants.Companion.ORDER_PLACED_STATE
import uz.xushnudbek.flowersshop.util.Constants.Companion.ORDER_SHIPPED_STATE
import uz.xushnudbek.flowersshop.viewmodel.shopping.ShoppingViewModel

class OrderDetails : Fragment() {
    val TAG = "OrderDetails"
    private lateinit var order: Order
    private lateinit var binding: FragmentOrderDetailsBinding
    private lateinit var viewModel: ShoppingViewModel
    private lateinit var productsAdapter: CartRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        arguments?.apply {
            order = getSerializable("order") as Order
        }
        viewModel.getOrderAddressAndProducts(order)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvOrderId.text = resources.getText(R.string.g_order)
            .toString().plus("# ${order.id}")
        setupRecyclerview()
        observeOrderAddress()

        observeProducts()
        onCloseImageClick()
        setupStepView()

    }

    private fun onCloseImageClick() {
        binding.imgCloseOrder.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun observeProducts() {
        viewModel.orderProducts.observe(viewLifecycleOwner, Observer { response ->
            when (response) {

                is Resource.Loading -> {
                    showProductsLoading()
                    return@Observer
                }

                is Resource.Success -> {
                    hideProductsLoading()
                    productsAdapter.differ.submitList(response.data)
                    binding.tvTotalprice.text = order.totalPrice
                    return@Observer
                }

                is Resource.Error -> {
                    hideAddressLoading()
                    Toast.makeText(
                        activity,
                        resources.getText(R.string.error_occurred),
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e(TAG, response.message.toString())
                    return@Observer
                }
            }

        })
    }

    private fun hideProductsLoading() {
        binding.apply {
            progressbarOrder.visibility = View.GONE
            rvProducts.visibility = View.VISIBLE
            tvProducts.visibility = View.VISIBLE
            linear.visibility = View.VISIBLE
            line1.visibility = View.VISIBLE
        }
    }

    private fun showProductsLoading() {
        binding.apply {
            progressbarOrder.visibility = View.VISIBLE
            rvProducts.visibility = View.INVISIBLE
            tvProducts.visibility = View.INVISIBLE
            linear.visibility = View.INVISIBLE
            line1.visibility = View.INVISIBLE
        }
    }

    private fun setupRecyclerview() {
        productsAdapter = CartRecyclerAdapter("From Order Detail")
        binding.rvProducts.apply {
            adapter = productsAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(VerticalSpacingItemDecorator(23))
        }
    }

    private fun observeOrderAddress() {
        viewModel.orderAddress.observe(viewLifecycleOwner, Observer { response ->
            when (response) {

                is Resource.Loading -> {
                    showAddressLoading()
                    return@Observer
                }

                is Resource.Success -> {
                    hideAddressLoading()
                    val address = response.data
                    binding.apply {
                        tvFullName.text = address?.fullName
                        tvAddress.text = address?.street
                            .plus(", ${address?.city}")
                            .plus(", ${address?.state}")
                        tvPhoneNumber.text = address?.phone
                    }

                    return@Observer
                }

                is Resource.Error -> {
                    hideAddressLoading()
                    Toast.makeText(
                        activity,
                        resources.getText(R.string.error_occurred),
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e(TAG, response.message.toString())
                    return@Observer
                }
            }

        })
    }

    private fun hideAddressLoading() {
        binding.apply {
            progressbarOrder.visibility = View.GONE
            stepView.visibility = View.VISIBLE
            tvShoppingAddresses.visibility = View.VISIBLE
            linearAddress.visibility = View.VISIBLE
        }
    }

    private fun showAddressLoading() {
        binding.apply {
            binding.apply {
                progressbarOrder.visibility = View.VISIBLE
                stepView.visibility = View.INVISIBLE
                tvShoppingAddresses.visibility = View.INVISIBLE
                linearAddress.visibility = View.INVISIBLE
            }
        }
    }

    private fun setupStepView() {
        val state = when (order.state) {
            ORDER_PLACED_STATE -> 1
            ORDER_CONFIRM_STATE -> 2
            ORDER_SHIPPED_STATE -> 3
            ORDER_Delivered_STATE -> 4
            else -> {
                2
            }
        }

        Log.d("test2", order.state)
        Log.d("test2", state.toString())
        val steps = arrayOf<String>(
            resources.getText(R.string.g_order_placed).toString(),
            resources.getText(R.string.g_confirm).toString(),
            resources.getText(R.string.g_shipped).toString(),
            resources.getText(R.string.g_delivered).toString()
        )

        binding.stepView.apply {
            getState().stepsNumber(4)
                .steps(steps.toMutableList())
                .commit()
            if (state == 4) {
                go(3,false)
                done(true)
            }else{
                go(state, false)
            }

        }
    }
}